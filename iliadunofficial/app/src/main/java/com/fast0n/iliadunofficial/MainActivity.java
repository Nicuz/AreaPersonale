package com.fast0n.iliadunofficial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_id, edt_password;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // java adresses
        btn_login = findViewById(R.id.btn_login);
        edt_id = findViewById(R.id.edt_id);
        edt_password = findViewById(R.id.edt_password);


        try {
            settings = getSharedPreferences("sharedPreferences", 0);
            String userid = settings.getString("userid", null);
            String password = settings.getString("password", null);
            editor = settings.edit();

            if (password.length() > 7) {
                edt_id.setText(userid);
                edt_password.setText(password);

                btn_login.postDelayed(new Runnable() { // delay button
                    public void run() {
                        btn_login.performClick();
                        btn_login.setPressed(true);
                        btn_login.invalidate();
                        btn_login.setPressed(false);
                        btn_login.invalidate();
                    }
                }, 1000);

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
