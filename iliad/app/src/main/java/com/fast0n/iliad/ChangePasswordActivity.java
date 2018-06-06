package com.fast0n.iliad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.change_password_title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        final String password = extras.getString("password");
        final String token = extras.getString("token");
        final String site_url = getString(R.string.site_url);

        // java adresses
        edt_newpassword = findViewById(R.id.edt_newpassword);
        edt_password = findViewById(R.id.edt_password);
        btn_password = findViewById(R.id.btn_password);

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_password.getText().toString().equals(password)
                        && edt_password.getText().toString().length() != 0
                        && edt_newpassword.getText().toString().length() != 0) {
                    String url = site_url + "?new_password=" + edt_newpassword.getText().toString()
                            + "&new_password_confirm=" + edt_newpassword.getText().toString() + "&password=" + password
                            + "&token=" + token;
                    changePassword(url, password);
                } else
                    Toasty.warning(ChangePasswordActivity.this, getString(R.string.wrong_password), Toast.LENGTH_LONG,
                            true).show();

            }
        });

    }

    private void changePassword(String url, final String password) {

        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");

                            JSONObject json = new JSONObject(iliad);
                            String string_response = json.getString("0");

                            if (string_response.equals("true")) {

                                settings = getSharedPreferences("sharedPreferences", 0);
                                editor = settings.edit();
                                editor.putString("password", password.replace(" ", ""));
                                editor.apply();

                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toasty.success(ChangePasswordActivity.this, getString(R.string.password_change_success),
                                        Toast.LENGTH_LONG, true).show();

                            }

                        } catch (JSONException ignored) {
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
