package fr.upem.geoplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import fr.upem.firecloud.FireCloudUser;
import fr.upem.firecloud.ServerApp;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.EventAdapter;
import fr.upem.geoplan.core.radar.RadarActivity;
import fr.upem.geoplan.core.server.gcm.Preferences;
import fr.upem.geoplan.core.server.gcm.service.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "GeoPlan";

    private final ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent();

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2015, Calendar.NOVEMBER, 19, 23, 0);
        endCalendar.set(2015, Calendar.NOVEMBER, 20, 7, 0);
        events.add(new Event("sex party", startCalendar.getTime(), endCalendar.getTime(), "upem", Color.RED));

        startCalendar.set(2015, Calendar.DECEMBER, 20, 15, 0);
        endCalendar.set(2015, Calendar.DECEMBER, 20, 18, 0);
        events.add(new Event("fiesta", startCalendar.getTime(), endCalendar.getTime(), "chez Jeremie", Color.BLUE));

        startCalendar.set(2016, Calendar.JANUARY, 20, 15, 0);
        endCalendar.set(2016, Calendar.JANUARY, 21, 0, 0);
        events.add(new Event("karaoke night", startCalendar.getTime(), endCalendar.getTime(), "chez tristan", Color.GREEN));

        startCalendar.set(2016, Calendar.MARCH, 21, 22, 0);
        endCalendar.set(2016, Calendar.MARCH, 21, 2, 0);
        events.add(new Event("geek party", startCalendar.getTime(), endCalendar.getTime(), "chez Jeremy", Color.BLACK));

        startCalendar.set(2016, Calendar.MARCH, 28, 14, 0);
        endCalendar.set(2016, Calendar.MARCH, 28, 16, 0);
        events.add(new Event("seance photo", startCalendar.getTime(), endCalendar.getTime(), "chez Huy", Color.CYAN));

        // test firebase en cours
        Firebase.setAndroidContext(this);
        ServerApp server = new ServerApp(getString(R.string.database_url));

        ArrayList<Long> guests = new ArrayList<>();
        //guests.add(server.createUser(2, "Jérémie", "Chattou", new LatLng(48.8385709, 2.561343), "0658596324"));
        //guests.add(server.createUser(1, "Tristan", "Fautrel", new LatLng(48.877535, 2.59016), "0621185284"));

        //server.createEvent(1, "tfautrel", "Rendez-vous Android", guests, null, null, "UPEM - Copernic", new LatLng(48.8392168, 2.5870625));

        initializeReceiver();
        registerReceiver();
        startReceiver();

        // Identify user
        // Maybe use https://developers.google.com/identity/sign-in/android/

        // Start correct activity
        Intent intent = getIntent();
        doAction(intent.getAction(), intent.getData());
    }

    private void doAction(String action, Uri data) {
        if (Intent.ACTION_VIEW.equals(action)) {
            switch (data.getHost()) {
                case "home":
                default:
                    break;
                case "radar":
                    try {
                        String event_id = data.getQueryParameter("eventid");
                        if (event_id == null) {
                            throw new IllegalArgumentException("No event found");
                        }
                        Log.i(LOG_TAG, "Starting radar for event " + event_id);
                        Event event = getEventFromId(Integer.parseInt(event_id));

                        Intent intent = new Intent(this, RadarActivity.class);
                        intent.putExtra("event", event);
                        startActivity(intent);

                        finish();
                    } catch (Exception e) {
                        Toast.makeText(this, R.string.toast_invalid_event, Toast.LENGTH_LONG).show();
                        Log.i(LOG_TAG, "Invalid event");
                    }
                    break;
            }
        }
    }

    private void setContent() {
        setContentView(R.layout.activity_planning);

        ListView listEvent = (ListView) findViewById(R.id.listEvent);

        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);

                startRadarActivity(event);
            }
        });
        EventAdapter adapter = new EventAdapter(MainActivity.this, events);
        listEvent.setAdapter(adapter);
    }

    private Event getEventFromId(int id) {
        int index = events.indexOf(new Event(id));
        if (index == -1) {
            throw new IllegalArgumentException("Invalid event id");
        }
        return events.get(index);
    }

    private void startRadarActivity(Event event) {
        Intent intent = new Intent(this, RadarActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    private void startReceiver() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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
        getMenuInflater().inflate(R.menu.menu_planning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.filter:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            case R.id.synchronize:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
