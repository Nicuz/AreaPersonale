package com.fast0n.iliad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.fragments.MasterCreditFragment;
import com.fast0n.iliad.fragments.SimFragments;
import com.fast0n.iliad.fragments.ConditionsFragment.ConditionsFragment;
import com.fast0n.iliad.fragments.InfoFragments.InfoFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fast0n.iliad.R.layout.activity_home);
        Toolbar toolbar = findViewById(com.fast0n.iliad.R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(com.fast0n.iliad.R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(com.fast0n.iliad.R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                com.fast0n.iliad.R.string.navigation_drawer_open, com.fast0n.iliad.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(com.fast0n.iliad.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();

        headerView = navigationView.getHeaderView(0);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String userid = extras.getString("userid", null);
        final String password = extras.getString("password", null);
        final String token = extras.getString("token", null);
        final String checkbox = extras.getString("checkbox", null);

        final String site_url = getString(com.fast0n.iliad.R.string.site_url);
        String url = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;

        if (isOnline())
            getObject(url, nav_Menu);
        else {
            finishAffinity();
            Toasty.error(HomeActivity.this, getString(R.string.wrong_internet), Toast.LENGTH_SHORT).show();
        }

        try {
            if (checkbox.equals("true")) {

                settings = getSharedPreferences("sharedPreferences", 0);
                editor = settings.edit();
                editor.putString("userid", userid);
                editor.putString("password", password.replace(" ", ""));
                editor.apply();

            }
        } catch (Exception ignored) {
        }

    }

    private void getObject(String url, final Menu nav_Menu) {

        final ProgressBar loading;

        // java adresses
        final TextView textView = headerView.findViewById(com.fast0n.iliad.R.id.textView);
        final TextView textView1 = headerView.findViewById(com.fast0n.iliad.R.id.textView1);
        final TextView textView2 = headerView.findViewById(com.fast0n.iliad.R.id.textView2);

        loading = findViewById(R.id.progressBar);

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
                            String stringSim = json.getString("sim");

                            JSONObject json_user = new JSONObject(stringUser);
                            String user_name = json_user.getString("user_name");
                            String user_id = json_user.getString("user_id");
                            String user_numtell = json_user.getString("user_numtell");

                            JSONObject json_sim = new JSONObject(stringSim);
                            String sim_state = json_sim.getString("2");

                            if (sim_state.equals("false")) {

                                Fragment fragment = null;
                                fragment = new SimFragments();

                                nav_Menu.findItem(R.id.nav_credit).setVisible(false);

                                if (fragment != null) {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(com.fast0n.iliad.R.id.fragment, fragment);
                                    ft.commit();
                                }

                            }

                else {
                                Fragment fragment = null;
                                fragment = new MasterCreditFragment();

                                nav_Menu.findItem(R.id.nav_credit).setVisible(true);
                                nav_Menu.findItem(R.id.nav_credit).setChecked(true);

                                if (fragment != null) {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(com.fast0n.iliad.R.id.fragment, fragment);
                                    ft.commit();
                                }
                            }

                            loading.setVisibility(View.INVISIBLE);
                            textView.setText(user_name);
                            textView1.setText(getString(com.fast0n.iliad.R.string.id_user) + " " + user_id);
                            textView2.setText(getString(com.fast0n.iliad.R.string.number_user) + " " + user_numtell);

                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int error_code = error.networkResponse.statusCode;
                        if (error_code == 503) {
                            settings = getSharedPreferences("sharedPreferences", 0);
                            editor = settings.edit();
                            editor.putString("userid", null);
                            editor.putString("password", null);
                            editor.apply();

                            Toasty.warning(HomeActivity.this, getString(com.fast0n.iliad.R.string.error_login),
                                    Toast.LENGTH_LONG, true).show();
                            Intent mainActivity = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(mainActivity);
                        }

                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.fast0n.iliad.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.fast0n.iliad.R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String password = extras.getString("password");
        final String token = extras.getString("token");

        if (id == R.id.nav_credit) {
            fragment = new MasterCreditFragment();
        }
        if (id == R.id.nav_info) {
            fragment = new InfoFragments();
        } else if (id == R.id.nav_sim) {
            fragment = new SimFragments();
        } else if (id == R.id.nav_conditions) {
            fragment = new ConditionsFragment();
        } else if (id == R.id.nav_logout) {
            settings = getSharedPreferences("sharedPreferences", 0);
            editor = settings.edit();
            editor.putString("userid", null);
            editor.putString("password", null);
            editor.apply();

            Intent mainActivity = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(mainActivity);

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(com.fast0n.iliad.R.id.fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.fast0n.iliad.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
