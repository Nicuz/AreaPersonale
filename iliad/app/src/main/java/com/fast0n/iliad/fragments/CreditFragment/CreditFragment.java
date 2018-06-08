package com.fast0n.iliad.fragments.CreditFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.ChangeEmailActivity;
import com.fast0n.iliad.ConsumptionDetailsActivity;
import com.fast0n.iliad.LoginActivity;
import com.fast0n.iliad.R;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreditFragment extends Fragment {

    public CreditFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_credit, container, false);

        final ProgressBar loading;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CardView cardView;
        final Button button;

        // java adresses
        loading = view.findViewById(R.id.progressBar);
        CubeGrid cubeGrid = new CubeGrid();
        loading.setIndeterminateDrawable(cubeGrid);
        cubeGrid.setColor(getResources().getColor(R.color.colorPrimary));
        cardView = view.findViewById(R.id.cardView);
        button = view.findViewById(R.id.button);


        cardView.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);




        SharedPreferences settings = context.getSharedPreferences("sharedPreferences", 0);
        String token = settings.getString("token", null);
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();

        final String site_url = getString(R.string.site_url);
        String url = site_url + "?credit=true&token=" + token;

        getObject(url, context, view);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsumptionDetailsActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getObject(String url, final Context context, View view) {

        final ProgressBar loading;
        final PullToRefreshRecyclerView recyclerView;
        final List<DataCreditFragments> creditList = new ArrayList<>();
        final CardView cardView;
        final Button button;

        // java adresses
        recyclerView = view.findViewById(R.id.recycler_view);
        loading = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardView);
        button = view.findViewById(R.id.button);
        final TextView credit = view.findViewById(R.id.creditText);

        recyclerView.setSwipeEnable(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setOnRefreshListener(() -> startActivity(new Intent(context, LoginActivity.class)));

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        JSONObject json_raw = new JSONObject(response.toString());
                        String iliad = json_raw.getString("iliad");

                        JSONObject json = new JSONObject(iliad);

                        String string1 = json.getString("0");
                        JSONObject json_strings1 = new JSONObject(string1);
                        String stringCredit = json_strings1.getString("0");
                        credit.setText(stringCredit);

                        for (int j = 1; j < json.length(); j++) {

                            String string = json.getString(String.valueOf(j));
                            JSONObject json_strings = new JSONObject(string);

                            String c = json_strings.getString("0");
                            String b = json_strings.getString("1");
                            String a = json_strings.getString("2");
                            String d = json_strings.getString("3");

                            creditList.add(new DataCreditFragments(a, b, c, d));

                        }

                        CustomAdapterCredit ca = new CustomAdapterCredit(context, creditList);
                        recyclerView.setAdapter(ca);
                        cardView.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        startActivity(new Intent(context, LoginActivity.class));
                    }
                }, error -> startActivity(new Intent(context, LoginActivity.class)));

        // add it to the RequestQueue
        queue.add(getRequest);

    }
}
