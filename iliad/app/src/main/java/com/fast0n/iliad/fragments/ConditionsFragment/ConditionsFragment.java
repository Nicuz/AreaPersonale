package com.fast0n.iliad.fragments.ConditionsFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fast0n.iliad.fragments.InfoFragments.CustomAdapterInfo;
import com.fast0n.iliad.fragments.InfoFragments.DataInfoFragments;
import com.fast0n.iliad.java.RecyclerItemListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ConditionsFragment extends Fragment {

    public ConditionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_conditions, container, false);

        final ProgressBar loading;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CardView cardView;

        // java adresses
        loading = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardView);

        cardView.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);

        final Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;
        final String password = extras.getString("password");
        final String token = extras.getString("token");

        final String site_url = getString(R.string.site_url);
        String url = site_url + "?doc=true&token=" + token;

        getObject(url, context, view, token, password);

        return view;
    }

    private void getObject(String url, final Context context, View view, final String token, final String password) {

        final ProgressBar loading;
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        final RecyclerView recyclerView;
        final List<DataConditionsFragments> conditionList = new ArrayList<>();
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
                        TextView getUrl = arg1.findViewById(R.id.url);
                        String url = getUrl.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
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

                            System.out.println(json.length());

                            for (int j = 0; j < json.length(); j++) {

                                String string = json.getString(String.valueOf(j));
                                JSONObject json_strings = new JSONObject(string);

                                String a = json_strings.getString("0");
                                String b = json_strings.getString("1");
                                String c = json_strings.getString("2");
                                conditionList.add(new DataConditionsFragments(a, b, c));
                            }

                            CustomAdapterConditions ca = new CustomAdapterConditions(conditionList);
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
