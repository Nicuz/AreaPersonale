package com.fast0n.iliad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.fragments.AboutFragment.AboutFragment;
import com.fast0n.iliad.fragments.ConditionsFragment.ConditionsFragment;
import com.fast0n.iliad.fragments.InfoFragments.InfoFragments;
import com.fast0n.iliad.fragments.MasterCreditFragment;
import com.fast0n.iliad.fragments.OptionsFragment.OptionsFragment;
import com.fast0n.iliad.fragments.ServicesFragment.ServicesFragment;
import com.fast0n.iliad.fragments.SimFragments;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Menu nav_Menu;
    NavigationView navigationView;
    ProgressBar loading;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    View headerView;
    private boolean backPressedToExitOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // java adresses
        drawer = findViewById(R.id.drawer_layout);
        loading = findViewById(R.id.progressBar);
        CubeGrid cubeGrid = new CubeGrid();
        loading.setIndeterminateDrawable(cubeGrid);
        cubeGrid.setColor(getResources().getColor(R.color.colorPrimary));
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        nav_Menu = navigationView.getMenu();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String userid = extras.getString("userid", null);
        String password = extras.getString("password", null);
        String token = extras.getString("token", null);
        String checkbox = extras.getString("checkbox", null);

        if (isOnline()) {
            String site_url = getString(R.string.site_url);
            String url = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;
            getObject(url, nav_Menu);
            settings = getSharedPreferences("sharedPreferences", 0);
            editor = settings.edit();
            editor.putString("token", token);
            editor.apply();

        } else {
            Intent mainActivity = new Intent(HomeActivity.this, ErrorConnectionActivity.class);
            startActivity(mainActivity);
        }

        if (checkbox != null && checkbox.equals("true")) {
            settings = getSharedPreferences("sharedPreferences", 0);
            editor = settings.edit();
            editor.putString("userid", userid);
            editor.putString("password", password.replace(" ", ""));
            editor.apply();

        }

    }

    private void getObject(String url, final Menu nav_Menu) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);

                            String stringSim = json.getString("sim");
                            String stringVersion = json.getString("version");
                            String user_name = json.getString("user_name");
                            String user_id = json.getString("user_id");
                            String user_numtell = json.getString("user_numtell");

                            if (BuildConfig.VERSION_CODE < Integer.parseInt(stringVersion)) {
                                Intent intent = new Intent(HomeActivity.this, ErrorConnectionActivity.class);
                                intent.putExtra("errorAPI", "true");
                                startActivity(intent);

                            }

                            try {
                                if (stringSim.equals("false")) {

                                    Fragment fragment;
                                    fragment = new SimFragments();

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(R.id.fragment, fragment);
                                    ft.commit();

                                } else {
                                    Fragment fragment;
                                    fragment = new MasterCreditFragment();

                                    nav_Menu.findItem(R.id.nav_credit).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_credit).setChecked(true);
                                    nav_Menu.findItem(R.id.nav_options).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_services).setVisible(true);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(R.id.fragment, fragment);
                                    ft.commit();
                                }
                            } catch (IllegalStateException ignored) {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }

                            TextView textView = headerView.findViewById(R.id.textView);
                            TextView textView1 = headerView.findViewById(R.id.textView1);
                            TextView textView2 = headerView.findViewById(R.id.textView2);

                            loading.setVisibility(View.INVISIBLE);
                            textView.setText(user_name);
                            textView1.setText(user_id);
                            textView2.setText(user_numtell);

                            toggle.syncState();
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        settings = getSharedPreferences("sharedPreferences", 0);
                        editor = settings.edit();
                        editor.putString("userid", null);
                        editor.putString("password", null);
                        editor.apply();

                        Toasty.warning(HomeActivity.this, getString(R.string.error_login), Toast.LENGTH_LONG, true)
                                .show();
                        Intent mainActivity = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(mainActivity);

                    }

                });

        queue.add(getRequest);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedToExitOnce) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finishAffinity();
                    }
                }, 500);

            } else {
                this.backPressedToExitOnce = true;
                Toasty.info(HomeActivity.this, getString(R.string.press_back), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        backPressedToExitOnce = false;
                    }
                }, 1000);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_credit) {
            if (isOnline())
                fragment = new MasterCreditFragment();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_options) {

            if (isOnline())
                fragment = new OptionsFragment();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_services) {

            if (isOnline())
                fragment = new ServicesFragment();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_info) {

            if (isOnline())
                fragment = new InfoFragments();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_sim) {

            if (isOnline())
                fragment = new SimFragments();

            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_conditions) {

            if (isOnline())
                fragment = new ConditionsFragment();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_about) {

            if (isOnline())
                fragment = new AboutFragment();
            else
                startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        } else if (id == R.id.nav_logout) {

            if (isOnline()) {

                new AlertDialog.Builder(this).setMessage(R.string.dialog_exit).setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                settings = getSharedPreferences("sharedPreferences", 0);
                                editor = settings.edit();
                                editor.putString("userid", null);
                                editor.putString("password", null);
                                editor.apply();

                                Intent mainActivity = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(mainActivity);
                            }
                        }).setNegativeButton(getString(R.string.no), null).show();

            }
        } else
            startActivity(new Intent(HomeActivity.this, ErrorConnectionActivity.class));

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
