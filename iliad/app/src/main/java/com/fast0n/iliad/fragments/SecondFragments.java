package com.fast0n.iliad.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragments extends Fragment {

    public SecondFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_second, container, false);

        final ProgressBar loading;
        final CardView cardView, cardView1, cardView2, cardView3, cardView4, cardView5;
        final Button btn_activatesim;
        final EditText edt_iccid;
        final AdView mAdView;
        final InterstitialAd mInterstitialAd;
        final Context context;
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
        cardView4 = view.findViewById(R.id.cardView4);
        cardView5 = view.findViewById(R.id.cardView5);

        cardView.setVisibility(View.GONE);
        cardView1.setVisibility(View.GONE);
        cardView2.setVisibility(View.GONE);
        cardView3.setVisibility(View.GONE);
        cardView4.setVisibility(View.GONE);
        cardView5.setVisibility(View.GONE);

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
        String url = site_url + "?info=true&token=" + token;

        getObject(url, context, view);

        settings = context.getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password.replace(" ", ""));
        editor.apply();

        return view;
    }

    private void getObject(String url, final Context context, View view) {

        final ProgressBar loading;
        final CardView cardView, cardView1, cardView2, cardView3, cardView4, cardView5;
        final TextView tvaddress_title, tvaddress, tvcap, tvpay_title, tvpay_method, tvmail_title, tvmail,
                tvpassword_title, tvpassword, tvpuk_title, tvpuk, tvpuk_text;

        final View headerView;
        final NavigationView navigationView;
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        final EditText edt_iccid;
        final Button btn_activatesim;

        tvaddress_title = view.findViewById(R.id.address_title);
        tvaddress = view.findViewById(R.id.address);
        tvcap = view.findViewById(R.id.cap);
        tvpay_title = view.findViewById(R.id.pay_title);
        tvpay_method = view.findViewById(R.id.pay_method);
        tvmail_title = view.findViewById(R.id.mail_title);
        tvmail = view.findViewById(R.id.mail);
        tvpassword_title = view.findViewById(R.id.password_title);
        tvpassword = view.findViewById(R.id.password);
        tvpuk_title = view.findViewById(R.id.puk_title);
        tvpuk = view.findViewById(R.id.puk);
        tvpuk_text = view.findViewById(R.id.puk_text);

        cardView = view.findViewById(R.id.cardView);
        cardView1 = view.findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView3 = view.findViewById(R.id.cardView3);
        cardView4 = view.findViewById(R.id.cardView4);
        cardView5 = view.findViewById(R.id.cardView5);

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
                            String stringAddress = json.getString("address");
                            String stringPay = json.getString("pay");
                            String stringMail = json.getString("mail");
                            String stringPuk = json.getString("puk");
                            String stringPassword = json.getString("password");

                            JSONObject json_address = new JSONObject(stringAddress);
                            String address_title = json_address.getString("0");
                            String address = json_address.getString("1");
                            String cap = json_address.getString("2");
                            tvaddress_title.setText(address_title);
                            tvaddress.setText(address);
                            tvcap.setText(cap);

                            JSONObject json_pay = new JSONObject(stringPay);
                            String pay_title = json_pay.getString("0");
                            String pay_method = json_pay.getString("1");
                            tvpay_title.setText(pay_title);
                            tvpay_method.setText(pay_method);

                            JSONObject json_mail = new JSONObject(stringMail);
                            String mail_title = json_mail.getString("0");
                            String mail = json_mail.getString("1");
                            tvmail_title.setText(mail_title);
                            tvmail.setText(mail);

                            JSONObject json_password = new JSONObject(stringPassword);
                            String password_title = json_password.getString("0");
                            String password = json_password.getString("1");
                            tvpassword_title.setText(password_title);
                            tvpassword.setText(password);

                            JSONObject json_puk = new JSONObject(stringPuk);
                            String puk_title = json_puk.getString("0");
                            String puk_text = json_puk.getString("1");
                            String puk = json_puk.getString("2");
                            tvpuk_title.setText(puk_title);
                            tvpuk_text.setText(puk_text);
                            tvpuk.setText(puk);

                            cardView.setVisibility(View.VISIBLE);
                            cardView1.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.VISIBLE);

                            cardView4.setVisibility(View.VISIBLE);
                            cardView5.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);

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

}
