package com.fast0n.ipersonalarea.fragments.VoicemailFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.ipersonalarea.LoginActivity;
import com.fast0n.ipersonalarea.R;
import com.github.ybq.android.spinkit.style.CubeGrid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoicemailFragment extends Fragment {


    public VoicemailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_voicemail, container, false);

        final ProgressBar loading;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CardView cardView;



        // java adresses
        loading = view.findViewById(R.id.progressBar);
        CubeGrid cubeGrid = new CubeGrid();
        cubeGrid.setColor(getResources().getColor(R.color.colorPrimary));
        loading.setIndeterminateDrawable(cubeGrid);
        cardView = view.findViewById(R.id.cardView);

        cardView.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);

        final Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;
        final String token = extras.getString("token");

        final String site_url = getString(R.string.site_url);
        String url = site_url + "?voicemail=true&token="+token;

        getObject(url, context, view, token);

        return view;
    }

    private void getObject(String url, Context context, View view, String token) {

        final ProgressBar loading;
        final RecyclerView recyclerView;
        final List<DataVoicemailFragments> infomail = new ArrayList<>();
        final CardView cardView;

        // java adresses
        recyclerView = view.findViewById(R.id.recycler_view);
        loading = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");
                            JSONObject json = new JSONObject(iliad);

                            for (int i = 0; i < json.length(); i++) {

                                String string = json.getString(String.valueOf(i));
                                JSONObject json_strings = new JSONObject(string);

                                String a = json_strings.getString("0");
                                try {
                                    String b = json_strings.getString("1");
                                    String c = json_strings.getString("2");


                                       infomail.add(new DataVoicemailFragments(a, b, c, token));
                                } catch (Exception ignored){
                                    infomail.add(new DataVoicemailFragments(a, "", "0", token));
                                }






                            }
                            CustomAdapterVoicemail ca = new CustomAdapterVoicemail( infomail, context);
                            recyclerView.setAdapter(ca);
                            cardView.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            startActivity(new Intent(context, LoginActivity.class));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(getRequest);
    }

}
