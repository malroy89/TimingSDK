package de.dzmitry_lamaka.timingsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.dzmitry_lamaka.timingsdk.sdk.TimingSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.send_seconds)
            .setOnClickListener(v -> TimingSdk.getInstance().onTrackSeconds(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimingSdk.getInstance().onResume(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimingSdk.getInstance().onDestroy();
    }
}
