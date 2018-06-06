package com.fast0n.iliad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fast0n.iliad.java.GenerateToken;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_id, edt_password;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // java adresses
        btn_login = findViewById(R.id.btn_login);
        edt_id = findViewById(R.id.edt_id);
        edt_password = findViewById(R.id.edt_password);
        checkBox = findViewById(R.id.checkBox);

        settings = getSharedPreferences("sharedPreferences", 0);
        String userid = settings.getString("userid", null);
        String password = settings.getString("password", null);
        editor = settings.edit();
        editor.apply();

        if (password != null && password.trim().length() > 7) {
            edt_id.setText(userid);
            edt_password.setText(password);

            String token = GenerateToken.randomString(20);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            intent.putExtra("userid", userid);
            intent.putExtra("password", password);
            intent.putExtra("token", token);

            startActivity(intent);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() && edt_id.getText().toString().length() != 0
                        && edt_password.getText().toString().length() != 0) {

                    String userid = edt_id.getText().toString();
                    String password = edt_password.getText().toString();
                    String token = GenerateToken.randomString(20);

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("password", password);
                    intent.putExtra("token", token);

                    if (checkBox.isChecked()) {
                        intent.putExtra("checkbox", "true");
                    } else {
                        intent.putExtra("checkbox", "false");
                    }
                    startActivity(intent);

                } else {
                    Toasty.error(LoginActivity.this, getString(R.string.errorconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

}
