package com.fast0n.iliad;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.fragments.FirstFragments;
import com.fast0n.iliad.fragments.SecondFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fast0n.iliad.R.layout.activity_main2);
        Toolbar toolbar = findViewById(com.fast0n.iliad.R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(com.fast0n.iliad.R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        Fragment fragment = null;
        fragment = new FirstFragments();

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(com.fast0n.iliad.R.id.fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.fast0n.iliad.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                com.fast0n.iliad.R.string.navigation_drawer_open, com.fast0n.iliad.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.fast0n.iliad.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String userid = extras.getString("userid");
        final String password = extras.getString("password");
        final String token = extras.getString("token");

        final String site_url = getString(com.fast0n.iliad.R.string.site_url);
        String url = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;

        getObject(url);

        settings = getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        editor.putString("userid", userid);
        editor.putString("password", password.replace(" ", ""));
        editor.apply();

    }

    private void getObject(String url) {
        final TextView textView = headerView.findViewById(com.fast0n.iliad.R.id.textView);
        final TextView textView1 = headerView.findViewById(com.fast0n.iliad.R.id.textView1);
        final TextView textView2 = headerView.findViewById(com.fast0n.iliad.R.id.textView2);

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

                            JSONObject json_user = new JSONObject(stringUser);
                            String user_name = json_user.getString("user_name");
                            String user_id = json_user.getString("user_id");
                            String user_numtell = json_user.getString("user_numtell");

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

                            Toasty.warning(Main2Activity.this, getString(com.fast0n.iliad.R.string.error_login),
                                    Toast.LENGTH_LONG, true).show();
                            Intent mainActivity = new Intent(Main2Activity.this, MainActivity.class);
                            startActivity(mainActivity);
                        }

                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.fast0n.iliad.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.fast0n.iliad.R.menu.main2, menu);
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

        if (id == R.id.nav_first) {
            fragment = new FirstFragments();
        } else if (id == R.id.nav_second) {
            fragment = new SecondFragments();

        } else if (id == R.id.nav_logout) {
            settings = getSharedPreferences("sharedPreferences", 0);
            editor = settings.edit();
            editor.putString("userid", null);
            editor.putString("password", null);
            editor.apply();

            Intent mainActivity = new Intent(Main2Activity.this, MainActivity.class);
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
