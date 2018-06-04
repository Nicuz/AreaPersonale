package com.fast0n.iliad.fragments.InfoFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.ChangeEmailActivity;
import com.fast0n.iliad.ChangePasswordActivity;
import com.fast0n.iliad.MainActivity;
import com.fast0n.iliad.R;
import com.fast0n.iliad.java.RecyclerItemListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class InfoFragments extends Fragment {

    public InfoFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);

        final ProgressBar loading;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        CardView cardView;

        // java adresses
        loading = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardView);

        cardView.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);

        final Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;
        final String userid = extras.getString("userid");
        final String password = extras.getString("password");
        final String token = extras.getString("token");

        final String site_url = getString(R.string.site_url);
        String url = site_url + "?info=true&token=" + token;

        getObject(url, context, view, token, password);

        settings = context.getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password.replace(" ", ""));
        editor.apply();

        return view;
    }

    private void getObject(String url, final Context context, View view, final String token, final String password) {

        final ProgressBar loading;
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        final RecyclerView recyclerView;
        final List<DataInfoFragments> infoList = new ArrayList<>();
        final CardView cardView;

        // java adresses
        recyclerView = view.findViewById(R.id.recycler_view);
        loading = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemListener(context, recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View arg1, int position) {

                        if (position == 2) {
                            Intent intent = new Intent(context, ChangeEmailActivity.class);
                            intent.putExtra("password", password);
                            intent.putExtra("token", token);

                            startActivity(intent);
                        }

                        if (position == 4) {
                            Intent intent = new Intent(context, ChangePasswordActivity.class);
                            intent.putExtra("password", password);
                            intent.putExtra("token", token);

                            startActivity(intent);
                        }

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);

                            String[] array = { "address", "pay", "mail", "puk", "password" };

                            for (int i = 0; i < array.length; i++) {

                                String stringArray = json.getString(array[i]);
                                JSONObject json_shipping = new JSONObject(stringArray);
                                try {
                                    String a = json_shipping.getString("0");
                                    String b = json_shipping.getString("1");
                                    String c = json_shipping.getString("2");
                                    infoList.add(new DataInfoFragments("Modifica", a, b, c, i));

                                } catch (Exception e) {
                                    String a = json_shipping.getString("0");
                                    String b = json_shipping.getString("1");
                                    infoList.add(new DataInfoFragments("Modifica", a, b, "", i));

                                }

                            }

                            CustomAdapterInfo ca = new CustomAdapterInfo(infoList);
                            recyclerView.setAdapter(ca);
                            cardView.setVisibility(View.VISIBLE);
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
