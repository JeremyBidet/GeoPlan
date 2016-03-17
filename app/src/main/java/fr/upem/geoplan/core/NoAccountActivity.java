package fr.upem.geoplan.core;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fr.upem.geoplan.MainActivity;
import fr.upem.geoplan.R;

public class NoAccountActivity extends AppCompatActivity {
    private boolean firstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_account);
    }

    public void onClick(View view) {
        Intent launchIntent = new Intent(Settings.ACTION_ADD_ACCOUNT);
        startActivity(launchIntent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (firstLaunch) {
            firstLaunch = false;
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
