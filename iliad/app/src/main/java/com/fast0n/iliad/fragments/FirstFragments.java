package com.fast0n.iliad.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.MainActivity;
import com.fast0n.iliad.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class FirstFragments extends Fragment {

    public FirstFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_first, container, false);

        final ProgressBar loading;
        final CardView cardView, cardView1, cardView2, cardView3;
        final Button btn_activatesim;
        final EditText edt_iccid;
        final AdView mAdView;
        final InterstitialAd mInterstitialAd;
        final Context context;
        InputMethodManager keyboard;
        Unregistrar mUnregistrar;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        mAdView = view.findViewById(R.id.adView);
        loading = view.findViewById(R.id.progressBar);
        btn_activatesim = view.findViewById(R.id.btn_activatesim);
        edt_iccid = view.findViewById(R.id.edt_iccid);
        cardView = view.findViewById(R.id.cardView);
        cardView1 = view.findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView3 = view.findViewById(R.id.cardView3);

        cardView.setVisibility(View.GONE);
        cardView1.setVisibility(View.GONE);
        cardView2.setVisibility(View.GONE);
        cardView3.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        // banner && interstitialAd
        MobileAds.initialize(context, "ca-app-pub-9646303341923759~5575848020");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-9646303341923759/8529314423");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        final Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;
        final String userid = extras.getString("userid");
        final String password = extras.getString("password");
        final String token = extras.getString("token");

        final String site_url = getString(R.string.site_url);
        String url = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;

        getObject(url, context, view);

        settings = context.getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password.replace(" ", ""));
        editor.apply();

        btn_activatesim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_iccid.getText().toString().length() == 19)
                    activateSim(site_url + "?iccid=" + edt_iccid.getText().toString() + "&token=" + token, context);
                else
                    Toasty.error(context, getString(R.string.error_iccid), Toast.LENGTH_SHORT).show();
            }
        });

        mUnregistrar = KeyboardVisibilityEvent.registerEventListener(getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        updateKeyboardStatusText(isOpen, view);
                    }
                });
        updateKeyboardStatusText(KeyboardVisibilityEvent.isKeyboardVisible(getActivity()), view);



        return view;
    }

    private void changeMail(String url) {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            System.out.println(iliad);

                        } catch (JSONException ignored) {
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(getRequest);

    }

    private void activateSim(String url, final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);
                            String stringSim = json.getString("sim");

                            JSONObject json_sim = new JSONObject(stringSim);
                            String error = json_sim.getString("0");

                            Toasty.warning(context, error, Toast.LENGTH_LONG, true).show();

                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int error_code = error.networkResponse.statusCode;

                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    private void getObject(String url, final Context context, View view) {

        final ProgressBar loading;
        final CardView cardView, cardView1, cardView2, cardView3;
        final TextView tvvalidation, tvorder_date, tvdate, tvtracking, tvshipping, tvorder_shipped, tvactivation,
                tvtitle_activation, tvoffer;
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        final EditText edt_iccid;
        final Button btn_activatesim;

        tvvalidation = view.findViewById(R.id.validation);
        tvorder_date = view.findViewById(R.id.order_date);
        tvdate = view.findViewById(R.id.date);
        tvshipping = view.findViewById(R.id.shipping);
        tvtracking = view.findViewById(R.id.tracking);
        tvorder_shipped = view.findViewById(R.id.order_shipped);
        tvactivation = view.findViewById(R.id.activation);
        tvtitle_activation = view.findViewById(R.id.title_activation);
        tvoffer = view.findViewById(R.id.offer);

        edt_iccid = view.findViewById(R.id.edt_iccid);
        btn_activatesim = view.findViewById(R.id.btn_activatesim);

        cardView = view.findViewById(R.id.cardView);
        cardView1 = view.findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView3 = view.findViewById(R.id.cardView3);

        loading = view.findViewById(R.id.progressBar);
        loading.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);
                            String stringValidation = json.getString("validation");
                            String stringShipping = json.getString("shipping");
                            String stringSim = json.getString("sim");

                            JSONObject json_validation = new JSONObject(stringValidation);
                            String validation = json_validation.getString("0");
                            String order_date = json_validation.getString("1");
                            String date = json_validation.getString("2");

                            tvvalidation.setText(validation);
                            tvorder_date.setText(order_date);
                            tvdate.setText(date);

                            JSONObject json_shipping = new JSONObject(stringShipping);
                            String shipping = json_shipping.getString("0");
                            String order_shipped = json_shipping.getString("1");
                            String tracking = json_shipping.getString("2");
                            final String tracking_url = json_shipping.getString("3");

                            tvshipping.setText(shipping);
                            tvorder_shipped.setText(order_shipped);
                            tvtracking.setText(tracking);

                            JSONObject json_sim = new JSONObject(stringSim);
                            String activation = json_sim.getString("0");
                            String title_activation = json_sim.getString("1");
                            String response_sim = json_sim.getString("2");
                            String offer = json_sim.getString("3");

                            tvoffer.setText(offer);
                            tvactivation.setText(activation);
                            tvtitle_activation.setText(title_activation);

                            if (response_sim.equals("true")) {
                                tvtitle_activation.setTextColor(getResources().getColor(R.color.colorPrimary));
                                edt_iccid.setVisibility(View.GONE);
                                btn_activatesim.setVisibility(View.GONE);
                            }

                            cardView.setVisibility(View.VISIBLE);
                            cardView1.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);

                            cardView3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = tracking_url;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });

                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int error_code = error.networkResponse.statusCode;
                        if (error_code == 503) {
                            settings[0] = context.getApplicationContext().getSharedPreferences("sharedPreferences", 0);
                            editor[0] = settings[0].edit();
                            editor[0].putString("userid", null);
                            editor[0].putString("password", null);
                            editor[0].apply();

                            Toasty.warning(context, getString(R.string.error_login), Toast.LENGTH_LONG, true).show();
                            Intent mainActivity = new Intent(context, MainActivity.class);
                            startActivity(mainActivity);
                        }

                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);

    }


    private void updateKeyboardStatusText(boolean isOpen, View view) {

        final CardView cardView3;
        LinearLayout layout;
        cardView3 = view.findViewById(R.id.cardView3);
        layout = view.findViewById(R.id.layout);

        if (isOpen) {
            cardView3.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.GONE);

        } else {
            cardView3.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        }
    }

}
