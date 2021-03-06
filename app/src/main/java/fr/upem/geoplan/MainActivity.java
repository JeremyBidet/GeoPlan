package fr.upem.geoplan;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.telephony.TelephonyManager;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.upem.geoplan.core.LocationUpdater;
import fr.upem.geoplan.core.NoAccountActivity;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.EventAdapter;
import fr.upem.geoplan.core.planning.Planning;
import fr.upem.geoplan.core.radar.RadarActivity;
import fr.upem.geoplan.core.server.gcm.Preferences;
import fr.upem.geoplan.core.server.gcm.service.RegistrationIntentService;
import fr.upem.geoplan.core.server.gcm.service.RequestToServer;
import fr.upem.geoplan.core.session.User;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "GeoPlan";

    public static User currentUser;
    public static Planning planning;

    public static RequestToServer requestToServer;

    private ListView listEvent;
    private EventAdapter adapter;

    private static final class NoAccountException extends IllegalAccessException {
        public NoAccountException() {
            super("No account found");
        }
    }

    private void getCurrentUser() throws NoAccountException {

        requestToServer = new RequestToServer(getApplicationContext());
        // TODO: get the datas of the current connected Google account: email, names, phone
        AccountManager manager = AccountManager.get(this);
        Account[] mail = manager.getAccountsByType("com.google");

        // If not account ask for a new one
        if (mail.length == 0) {
            Intent intent = new Intent(this, NoAccountActivity.class);
            startActivity(intent);
            throw new NoAccountException();
        }

        Account[] accounts = manager.getAccounts();
        TelephonyManager tele = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phone = tele.getLine1Number();

        for (Account account : accounts) {
            Log.i("Accounts", account.name);
        }
        // TODO: set datas of the google account here
        assert mail.length > 0;
        //String email = mail[0].name;

        assert accounts.length > 0;
        //String firstname = accounts[0].name;

        String email = "john.doe@gmail.com";
        String firstname = "John";
        String lastname = "Doe";
        currentUser = requestToServer.createUser(new User(email, email, firstname, lastname, phone, new LatLng(1.0, 1.0)));
    }

    private void initList() {
        listEvent = (ListView) findViewById(R.id.listEvent);

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
                return false;
            }
        });
    }

    private void initPlanning(Intent intent) {
        if (intent.hasExtra("planning")) {
            planning = intent.getParcelableExtra("planning");
        } else {
            //ArrayList<Event> event_guested = requestToServer.getAllEventsGuested(currentUser.getID());
            //ArrayList<Event> event_owned = requestToServer.getAllEventsOwned(currentUser.getID());
            planning = new Planning();

            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            for (int i = 0; i < 30; i++) {
                c.set(2016, 3, i + 1, i % 24, i);
                c2.set(2016, 3, i + 1, i % 24, i + 10);
                planning.addEvent(new Event(-i - 700, "Event " + i, "Description " + i, new LatLng(i + 50.342, i - 39.543), i + " Main street", c.getTime(), c2.getTime(), new ArrayList<User>(), new ArrayList<User>(), i + 1, "Type " + (i % 10), i + 10.99f, i));
            }
        }

        /*requestToServer = new RequestToServer(getBaseContext());*/

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2015, Calendar.NOVEMBER, 19, 23, 0);
        endCalendar.set(2015, Calendar.NOVEMBER, 20, 7, 0);
        final Event event = new Event(-666, "sex party", new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "upem", Color.RED);
        event.addGuest(getBaseContext(), currentUser);
        currentUser.setPosition(new LatLng(48.839, 2.5848780));
        planning.addEvent(event);

        startCalendar.set(2015, Calendar.DECEMBER, 20, 15, 0);
        endCalendar.set(2015, Calendar.DECEMBER, 20, 18, 0);
        planning.addEvent(new Event(-667, "fiesta", new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "chez Jeremie", Color.BLUE));

        startCalendar.set(2016, Calendar.JANUARY, 20, 15, 0);
        endCalendar.set(2016, Calendar.JANUARY, 21, 0, 0);
        planning.addEvent(new Event(-668, "karaoke night", new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "chez tristan", Color.GREEN));

        startCalendar.set(2016, Calendar.MARCH, 21, 22, 0);
        endCalendar.set(2016, Calendar.MARCH, 21, 2, 0);
        planning.addEvent(new Event(-669, "geek party", new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "chez Jeremy", Color.BLACK));

        startCalendar.set(2016, Calendar.MARCH, 9, 3, 0);
        endCalendar.set(2016, Calendar.MARCH, 10, 16, 30);
        planning.addEvent(new Event(-670, "seance photo", new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "chez Huy", Color.CYAN));

        for (int i = 0; i < 20; i++) {
            startCalendar.set(2016, Calendar.MARCH, 28, 14, i + 1);
            endCalendar.set(2016, Calendar.MARCH, 28, 16, i + 20 + 1);
            planning.addEvent(new Event(-i - 671, "seance photo" + i, new LatLng(48.8392203, 2.5848739), startCalendar.getTime(), endCalendar.getTime(), "chez Huy", Color.CYAN));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_planning);

        try {
            getCurrentUser();
            initPlanning(intent);
            initList();

            setListContent(planning.getEvents());
            setListPosition(planning.getPosition());

            initializeReceiver();
            registerReceiver();
            startReceiver();

            startLocationUpdater();

            // Start correct activity
            doAction(intent.getAction(), intent.getData());
        } catch (NoAccountException e) {
            Log.i(LOG_TAG, "No account found", e);
            finish();
        }
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
                        Event event = planning.getEventByID(Integer.parseInt(event_id));

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

    private void setListContent(List<Event> events) {
        // TODO: add sections
        // to add sections, two ways:
        // 1)   change row_event.xml to add a section (TextView, ImageView, etc...)
        //      add a boolean to Event to check if this event is the first of this day
        //      display the section in row_event.xml only if the boolean is true
        // 2)   Adapater can have multiple type of View inside.
        //      Add a new View to the adatper which displays a section each time we reach a new day
        adapter = new EventAdapter(MainActivity.this, events);
        listEvent.setAdapter(adapter);
    }

    private void setListPosition(int position) {
        listEvent.setSelection(position);
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
                Intent intent = new Intent(this, EventActivity.class);
                intent.putExtra("create", true);
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

        Event e = (Event) listEvent.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case 0:
                if (e.getOwners().contains(currentUser)) {
                    Toast.makeText(this, "You are not the owner of this event!", Toast.LENGTH_LONG).show();
                    return true;
                }
                Intent intent = new Intent(this, EventActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("event", e.getId());
                startActivity(intent);
                return true;
            case 1:
                // TODO: call synchronize method on this event
                //requestToServer.updateEvent(e);
                Toast.makeText(this, "Synchronizing...", Toast.LENGTH_LONG).show();
                return true;
            case 2:
                if (!e.getOwners().contains(currentUser)) {
                    Toast.makeText(this, "You are not the owner of this event!", Toast.LENGTH_LONG).show();
                    return true;
                }
                planning.removeEvent(e);
                Toast.makeText(this, "Event " + e.getName() + " removed!", Toast.LENGTH_LONG).show();
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