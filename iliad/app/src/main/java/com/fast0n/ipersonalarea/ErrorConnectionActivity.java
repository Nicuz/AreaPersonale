package com.fast0n.ipersonalarea;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ErrorConnectionActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_connection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView1);
        textView1 = findViewById(R.id.textView);

        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            String errorAPI = extras.getString("errorAPI", null);

            if (errorAPI.equals("true")) {
                textView.setText(R.string.old_version_title);
                textView1.setText(R.string.old_version);
                button.setText(R.string.update);

                button.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID))));
            } else {
                button.setOnClickListener(v -> {

                    if (isOnline()) {

                        Intent mStartActivity = new Intent(ErrorConnectionActivity.this, LoginActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(ErrorConnectionActivity.this,
                                mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) ErrorConnectionActivity.this
                                .getSystemService(Context.ALARM_SERVICE);
                        assert mgr != null;
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);

                    } else {
                        Toasty.info(ErrorConnectionActivity.this, textView.getText(), Toast.LENGTH_SHORT, true)
                                .show();
                        Toasty.info(ErrorConnectionActivity.this, textView1.getText(), Toast.LENGTH_SHORT, true)
                                .show();
                    }

                });
            }
        } catch (Exception ignored) {
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
