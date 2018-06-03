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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_id, edt_password;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fast0n.iliad.R.layout.activity_main);
        Toolbar toolbar = findViewById(com.fast0n.iliad.R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(com.fast0n.iliad.R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // java adresses
        btn_login = findViewById(com.fast0n.iliad.R.id.btn_login);
        edt_id = findViewById(com.fast0n.iliad.R.id.edt_id);
        edt_password = findViewById(com.fast0n.iliad.R.id.edt_password);


        try {
            settings = getSharedPreferences("sharedPreferences", 0);
            String userid = settings.getString("userid", null);
            String password = settings.getString("password", null);
            editor = settings.edit();

            if (password.length() > 7) {
                edt_id.setText(userid);
                edt_password.setText(password);

                String token = GenerateToken.randomString(20);

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("userid", userid);
                intent.putExtra("password", password);
                intent.putExtra("token", token);

                startActivity(intent);
            }
        }
        catch (Exception ignores){}


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_id.getText().toString().length() != 0 && edt_password.getText().toString().length() != 0) {

                    String userid = edt_id.getText().toString();
                    String password = edt_password.getText().toString();
                    String token = GenerateToken.randomString(20);

                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("password", password);
                    intent.putExtra("token", token);

                    startActivity(intent);

                }

            }
        });

    }

}
