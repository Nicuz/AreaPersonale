package com.fast0n.iliad;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ErrorConnectionActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_connection);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {

                    Intent mStartActivity = new Intent(ErrorConnectionActivity.this, LoginActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(ErrorConnectionActivity.this, mPendingIntentId, mStartActivity,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) ErrorConnectionActivity.this.getSystemService(Context.ALARM_SERVICE);
                    assert mgr != null;
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    System.exit(0);

                } else {
                    Toasty.info(ErrorConnectionActivity.this, getString(R.string.errorconnection),
                            Toast.LENGTH_SHORT, true).show();
                    Toasty.info(ErrorConnectionActivity.this, getString(R.string.errorconnection_one),
                            Toast.LENGTH_SHORT, true).show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
