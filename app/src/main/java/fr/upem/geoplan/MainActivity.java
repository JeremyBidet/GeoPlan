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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

import fr.upem.geoplan.core.LocationUpdater;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.EventAdapter;
import fr.upem.geoplan.core.planning.Planning;
import fr.upem.geoplan.core.radar.RadarActivity;
import fr.upem.geoplan.core.server.gcm.Preferences;
import fr.upem.geoplan.core.server.gcm.RequestToServer;
import fr.upem.geoplan.core.server.gcm.service.RegistrationIntentService;
import fr.upem.geoplan.core.session.User;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "GeoPlan";

    private User currentUser;

    //private final ArrayList<Event> events = new ArrayList<>();
    private final Planning planning = new Planning();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2015, Calendar.NOVEMBER, 19, 23, 0);
        endCalendar.set(2015, Calendar.NOVEMBER, 20, 7, 0);
        planning.addEvent(new Event(-666, "sex party", startCalendar.getTime(), endCalendar.getTime(), "upem", Color.RED));

        startCalendar.set(2015, Calendar.DECEMBER, 20, 15, 0);
        endCalendar.set(2015, Calendar.DECEMBER, 20, 18, 0);
        planning.addEvent(new Event(-667, "fiesta", startCalendar.getTime(), endCalendar.getTime(), "chez Jeremie", Color.BLUE));

        startCalendar.set(2016, Calendar.JANUARY, 20, 15, 0);
        endCalendar.set(2016, Calendar.JANUARY, 21, 0, 0);
        planning.addEvent(new Event(-668, "karaoke night", startCalendar.getTime(), endCalendar.getTime(), "chez tristan", Color.GREEN));

        startCalendar.set(2016, Calendar.MARCH, 21, 22, 0);
        endCalendar.set(2016, Calendar.MARCH, 21, 2, 0);
        planning.addEvent(new Event(-669, "geek party", startCalendar.getTime(), endCalendar.getTime(), "chez Jeremy", Color.BLACK));

        startCalendar.set(2016, Calendar.MARCH, 10, 3, 0);
        endCalendar.set(2016, Calendar.MARCH, 9, 16, 30);
        planning.addEvent(new Event(-670, "seance photo", startCalendar.getTime(), endCalendar.getTime(), "chez Huy", Color.CYAN));

        for(int i=0; i<20; i++) {
            startCalendar.set(2016, Calendar.MARCH, 28, 14, i+1);
            endCalendar.set(2016, Calendar.MARCH, 28, 16, i+20+1);
            planning.addEvent(new Event(-i-671, "seance photo"+i, startCalendar.getTime(), endCalendar.getTime(), "chez Huy", Color.CYAN));
        }

        setContent();

        initializeReceiver();
        registerReceiver();
        startReceiver();

        //currentUser = RequestToServer.createUser(this);
        startLocationUpdater();

        // Identify user
        // Maybe use https://developers.google.com/identity/sign-in/android/

        // Start correct activity
        Intent intent = getIntent();
        doAction(intent.getAction(), intent.getData());
    }

    private void startLocationUpdater() {
        assert currentUser != null;

        Intent intent = new Intent(this, LocationUpdater.class);
        intent.putExtra("user", currentUser);

        startService(intent);
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

        final ListView listEvent = (ListView) findViewById(R.id.listEvent);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        listEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);

                startRadarActivity(event);
            }
        });

        listEvent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                registerForContextMenu(listEvent);
                openContextMenu(listEvent);
                return true;
            }
        });

        EventAdapter adapter = new EventAdapter(MainActivity.this, planning.getEvents());
        listEvent.setAdapter(adapter);
        listEvent.setSelection(3);
        // TODO:
        // au démragge récupérer l'index du premier event à partir de la date actuelle
        // modifier setContent() pour permettre de modifier les events afficher
        // extraire la ListView dans l'activité et non la méthode
        // appeler setContent(...) après chaque modification de la liste (voir comment l'updateListener de la ListView ou de l'Adapter fonctionne)
    }

    private Event getEventFromId(int id) {
        Event e = planning.getEventByID(id);
        if (e == null) {
            throw new IllegalArgumentException("Invalid event id");
        }
        return e;
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
                Intent intent = new Intent(this, NewEventActivity.class);
                startActivity(intent);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Action sur l'Event");
        menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 1, Menu.NONE, "Synchronize");
        menu.add(Menu.NONE, 2, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            case 1:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
            case 2:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onContextItemSelected(item);
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