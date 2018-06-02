package com.fast0n.iliadunofficial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    TextView tvuser_name, tvuser_id, tvuser_numtell, tvvalidation, tvorder_date, tvdate, tvtracking, tvshipping,
            tvorder_shipped, tvactivation, tvtitle_activation;
    ProgressBar loading;
    CardView cardView, cardView1, cardView2, cardView3;
    ActionBar actionBar;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Button btn_activatesim;
    EditText edt_iccid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set row icon in the toolbar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvuser_name = findViewById(R.id.user_name);
        tvuser_id = findViewById(R.id.user_id);
        tvuser_numtell = findViewById(R.id.user_numtell);
        tvvalidation = findViewById(R.id.validation);
        tvorder_date = findViewById(R.id.order_date);
        tvdate = findViewById(R.id.date);
        tvshipping = findViewById(R.id.shipping);
        tvtracking = findViewById(R.id.tracking);
        tvorder_shipped = findViewById(R.id.order_shipped);
        tvactivation = findViewById(R.id.activation);
        tvtitle_activation = findViewById(R.id.title_activation);
        loading = findViewById(R.id.progressBar);

        btn_activatesim = findViewById(R.id.btn_activatesim);
        edt_iccid = findViewById(R.id.edt_iccid);

        cardView = findViewById(R.id.cardView);
        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);

        cardView.setVisibility(View.GONE);
        cardView1.setVisibility(View.GONE);
        cardView2.setVisibility(View.GONE);
        cardView3.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        String userid = extras.getString("userid");
        String password = extras.getString("password");
        final String token = extras.getString("token");

        final String site_url = "https://iliad-unofficial.glitch.me/";
        String url = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;

        getObject(url);

        settings = getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password.replace(" ", ""));
        editor.apply();

        btn_activatesim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateSim(site_url + "?iccid=" + edt_iccid.getText().toString() + "&token=" + token);
            }
        });

    }

    private void activateSim(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

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

    private void getObject(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);
                            String stringUser = json.getString("user");
                            String stringValidation = json.getString("validation");
                            String stringShipping = json.getString("shipping");
                            String stringSim = json.getString("sim");

                            JSONObject json_user = new JSONObject(stringUser);
                            String user_name = json_user.getString("user_name");
                            String user_id = json_user.getString("user_id");
                            String user_numtell = json_user.getString("user_numtell");

                            tvuser_name.setText(user_name);
                            tvuser_id.setText(getString(R.string.id_user) + " " + user_id);
                            tvuser_numtell.setText(getString(R.string.number_user) + " " + user_numtell);

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
                            final String tracking = json_shipping.getString("2");

                            tvshipping.setText(shipping);
                            tvorder_shipped.setText(order_shipped);

                            JSONObject json_sim = new JSONObject(stringSim);
                            String activation = json_sim.getString("0");
                            String title_activation = json_sim.getString("1");

                            tvactivation.setText(activation);
                            tvtitle_activation.setText(title_activation);

                            cardView3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = tracking;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });

                            cardView.setVisibility(View.VISIBLE);
                            cardView1.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case android.R.id.home:

            super.onBackPressed();
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            super.onBackPressed();

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        super.onBackPressed();
    }
}
