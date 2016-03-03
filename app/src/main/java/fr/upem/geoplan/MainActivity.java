package fr.upem.geoplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.upem.firecloud.FireCloudUser;
import fr.upem.geoplan.core.User;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.EventAdapter;
import fr.upem.geoplan.core.radar.RadarActivity;
import fr.upem.geoplan.core.server.ServerApp;
import fr.upem.geoplan.core.server.gcm.Preferences;
import fr.upem.geoplan.core.server.gcm.service.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "GeoPlan";

    ListView listEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listEvent = (ListView) findViewById(R.id.listEvent);

        List<Event> events = new ArrayList<>();
        events.add(new Event("sex party", new Date(2015, 3, 19, 23, 0), new Date(2015, 3, 20, 7, 0), "upem", Color.RED));
        events.add(new Event("fiesta", new Date(2015, 3, 20, 15, 0), new Date(2015, 3, 20, 18, 0), "chez Jeremie", Color.BLUE));
        events.add(new Event("karaoke night", new Date(2015, 3, 20, 15, 0), new Date(2015, 3, 21, 0, 0), "chez tristan", Color.GREEN));
        events.add(new Event("paintball", new Date(2015, 3, 21, 16, 0), new Date(2015, 3, 21, 19, 0), "chez Maxime", Color.GRAY));
        events.add(new Event("geek party", new Date(2015, 3, 21, 22, 0), new Date(2015, 3, 21, 2, 0), "chez Jeremy", Color.BLACK));
        events.add(new Event("seance photo", new Date(2015, 3, 28, 14, 0), new Date(2015, 3, 28, 16, 0), "chez Huy", Color.CYAN));
        events.add(new Event("Courses", new Date(2015, 3, 29, 10, 30), new Date(2015, 3, 29, 13, 30), "Aux Halles", Color.MAGENTA));
        events.add(new Event("Projet X", new Date(2015, 3, 29, 22, 0), new Date(2015, 3, 30, 10, 0), "chez Pierre", Color.YELLOW));

        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fr.upem.geoplan.core.Event event = (fr.upem.geoplan.core.Event) parent.getItemAtPosition(position);
                Log.i(LOG_TAG, "Event selected: " + event);
                startRadarActivity(event);
            }
        });
        EventAdapter adapter = new EventAdapter(MainActivity.this, events);
        listEvent.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        ServerApp server = new ServerApp("https://blazing-inferno-2418.firebaseio.com/");
        ArrayList<FireCloudUser> guests = new ArrayList<>();


        FireCloudUser userTristan = server.createUser(1, "Tristan", "Fautrel", new LatLng(48.877535, 2.59016), "0621185284");
        FireCloudUser userJeremie = server.createUser(2, "Jérémie", "Chattou", new LatLng(48.8385709, 2.561343), "0658596324");
        guests.add(userJeremie);
        guests.add(userTristan);
        server.createEvent(1, "tfautrel", "Rendez-vous Android", guests, null, null, "UPEM - Copernic", new LatLng(48.8392168, 2.5870625));

        initializeReceiver();
        registerReceiver();
        startReceiver();
    }

    private void startReceiver() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void startRadarActivity(fr.upem.geoplan.core.Event event) {
        Intent intent = new Intent(this, RadarActivity.class);
        intent.putExtra("event", event);

        startActivity(intent);
    }

    private void initializeReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Preferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.i(LOG_TAG, "Token retrieved sent. GCM is ready.");
                } else {
                    Log.e(LOG_TAG, "Error on parsing instance ID...");
                }
            }
        };
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Preferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
