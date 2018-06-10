package com.fast0n.iliad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_newpassword, edt_password;
    Button btn_password;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.change_password_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // set row icon in the toolbar
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String password = extras.getString("password", null);
        final String token = extras.getString("token", null);
        final String site_url = getString(R.string.site_url);

        // java adresses
        edt_newpassword = findViewById(R.id.edt_newpassword);
        edt_password = findViewById(R.id.edt_oldpassword);
        btn_password = findViewById(R.id.btn_password);

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                byte[] decodeValue1 = Base64.decode(password, Base64.DEFAULT);
                String ppassword = new String(decodeValue1);


                if (edt_password.getText().toString().equals(ppassword.replaceAll("\\s+",""))
                        && edt_password.getText().toString().length() != 0
                        && edt_newpassword.getText().toString().length() != 0) {

                    byte[] decodeValue = Base64.encode( edt_password.getText().toString().getBytes(), Base64.DEFAULT);
                    String oldpassword = new String(decodeValue);


                    byte[] encodeValue1 = Base64.encode( edt_newpassword.getText().toString().getBytes(), Base64.DEFAULT);
                    String newpassword = new String(encodeValue1);


                    String url = site_url + "?new_password=" +newpassword.replaceAll("\\s+","")
                            + "&new_password_confirm=" +newpassword.replaceAll("\\s+","") + "&password=" + oldpassword.replaceAll("\\s+","")
                            + "&token=" + token;


                    changePassword(url, password);
                    btn_password.setEnabled(false);
                } else
                    btn_password.setEnabled(true);
                    Toasty.warning(ChangePasswordActivity.this, getString(R.string.wrong_password), Toast.LENGTH_LONG,
                            true).show();

            }
        });

    }

    private void changePassword(String url, final String password) {

        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        JSONObject json_raw = new JSONObject(response.toString());
                        String iliad = json_raw.getString("iliad");

                        JSONObject json = new JSONObject(iliad);
                        String string_response = json.getString("0");

                        if (string_response.equals("true")) {

                            settings = getSharedPreferences("sharedPreferences", 0);
                            editor = settings.edit();
                            editor.putString("password", password);
                            editor.apply();

                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toasty.success(ChangePasswordActivity.this, getString(R.string.password_change_success),
                                    Toast.LENGTH_LONG, true).show();

                        }

                    } catch (JSONException ignored) {
                    }

                }, error -> startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class)));

        queue.add(getRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case android.R.id.home:

            super.onBackPressed();

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
