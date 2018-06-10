package com.fast0n.iliad.fragments.InfoFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.ChangeEmailActivity;
import com.fast0n.iliad.ChangePasswordActivity;
import com.fast0n.iliad.LoginActivity;
import com.fast0n.iliad.R;
import com.fast0n.iliad.java.RecyclerItemListener;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.github.ybq.android.spinkit.style.CubeGrid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class InfoFragments extends Fragment {

    public InfoFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);

        final ProgressBar loading;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        CardView cardView;

        // java adresses
        loading = view.findViewById(R.id.progressBar);
        CubeGrid cubeGrid = new CubeGrid();
        loading.setIndeterminateDrawable(cubeGrid);
        cubeGrid.setColor(getResources().getColor(R.color.colorPrimary));
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

        getObject(site_url, url, context, view, token, password);

        return view;
    }

    private void getObject(String site_url,String url, final Context context, View view, final String token, final String password) {

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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemListener(context, recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View arg1, int position) {

                        switch (position) {
                        case 1:
                            break;
                        case 2:
                            Intent intent2 = new Intent(context, ChangeEmailActivity.class);
                            intent2.putExtra("password", password);
                            intent2.putExtra("token", token);
                            startActivity(intent2);
                            break;
                        case 3:
                            Intent intent = new Intent(context, ChangePasswordActivity.class);
                            intent.putExtra("password", password);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        case 4:
                                RequestQueue queue = Volley.newRequestQueue(context);
                                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, site_url + "?puk=true&token="+ token, null,
                                        response -> {
                                            try {

                                                JSONObject json_raw = new JSONObject(response.toString());
                                                String iliad = json_raw.getString("iliad");

                                                JSONObject json = new JSONObject(iliad);
                                                String string = json.getString(String.valueOf(0));


                                                new MaterialStyledDialog.Builder(getContext())
                                                        .setStyle(Style.HEADER_WITH_TITLE)
                                                        .setTitle(string)
                                                        .show();



                                            } catch (JSONException e) {
                                                startActivity(new Intent(context, LoginActivity.class));
                                            }
                                        }, error -> startActivity(new Intent(context, LoginActivity.class)));

                                // add it to the RequestQueue
                                queue.add(getRequest);
                                break;
                        default:
                            Toasty.warning(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT, true).show();
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

                            for (int i = 0; i < json.length(); i++) {

                                String string = json.getString(String.valueOf(i));
                                JSONObject json_strings = new JSONObject(string);

                                try {
                                    String a = json_strings.getString("0");
                                    String b = json_strings.getString("1");
                                    String c = json_strings.getString("2");
                                    String d = json_strings.getString("3");
                                    String e = json_strings.getString("4");
                                    infoList.add(new DataInfoFragments(d, a, b, c, e));

                                } catch (Exception e) {
                                    String a = json_strings.getString("0");
                                    String b = json_strings.getString("1");
                                    String c = json_strings.getString("2");
                                    String d = json_strings.getString("3");

                                    infoList.add(new DataInfoFragments(c, a, b, "", d));

                                }

                            }

                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(llm);

                            CustomAdapterInfo ca = new CustomAdapterInfo(context, infoList);
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
                        try {

                        } catch (Exception e) {
                            startActivity(new Intent(context, LoginActivity.class));

                        }

                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);

    }

}
