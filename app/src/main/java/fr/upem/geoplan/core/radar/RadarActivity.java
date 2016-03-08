package fr.upem.geoplan.core.radar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import co.geeksters.radar.Radar;
import co.geeksters.radar.RadarPoint;
import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class RadarActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Event event;

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    /**
     * Map markers
     */
    private final ConcurrentHashMap<String, Marker> mapMarkers = new ConcurrentHashMap<>();

    /**
     * The map
     */
    private GoogleMap mMap;

    /**
     * Radar markers associated by label
     */
    private final HashMap<String, RadarPoint> radarDatas = new HashMap<>();

    /**
     * Radar label associated by user
     */
    private final HashMap<String, String> radarPinsLabel = new HashMap<>();

    /**
     * Shown radar markers
     */
    private final ArrayList<RadarPoint> radarMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        event = getIntent().getParcelableExtra("event");

        setContent();

        List<User> users = event.getGuests();
        for (User user : users) {
            this.users.put(user.getID(), user);
        }

        initializeRadar();
        initializeMap();

        LatLng eventPosition = event.getPosition();
        for (User user : users) {
            updatePosition(user.getID(), new LatLng(eventPosition.latitude + Math.random() / 100., eventPosition.longitude));
        }
    }

    private void setContent() {
        setContentView(R.layout.activity_radar);

        assert event != null;
        setTitle(event.getName());

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        final ColorDrawable color = new ColorDrawable(event.getColor());
        actionBar.setBackgroundDrawable(color);
    }

    private void initializeRadar() {
        Radar radar = (Radar) findViewById(R.id.radar);

        LatLng position = event.getPosition();
        radar.setReferencePoint(new RadarPoint(getString(R.string.reference_point_label), (float) position.latitude, (float) position.longitude));

        HashMap<String, Bundle> usersBundles = new HashMap<>();

        for (User user : users.values()) {
            String userId = user.getID();
            String label = user.getFirstname() + user.getLastname();
            radarPinsLabel.put(userId, label);
            Bundle newBundle = new Bundle();
            newBundle.putParcelable("user", user);
            usersBundles.put(label, newBundle);
        }

        radar.setPoints(radarMarkers);
        radar.setOnTouchListener(new RadarEvent(this, radar, usersBundles));
    }

    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Update a user position on all views (map and radar).
     *
     * @param userId   - Id of the user.
     * @param position - New position of the user.
     */
    public void updatePosition(String userId, LatLng position) {
        System.out.println("User " + userId + "\tposition " + position);
        updatePositionMap(userId, position);
        updatePositionRadar(userId, position);
    }

    private void updatePositionMap(String userId, LatLng position) {
        if (mMap != null) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(position));
            Marker oldMarker = mapMarkers.replace(userId, marker);
            if (oldMarker != null) {
                oldMarker.remove();
            }
        }
    }

    private final Object lockRadar = new Object();

    private void updatePositionRadar(String userId, LatLng position) {
        String label = radarPinsLabel.get(userId);
        RadarPoint marker = new RadarPoint(label, (float) position.latitude, (float) position.longitude);
        synchronized (lockRadar) {
            RadarPoint oldMarker = radarDatas.get(label);
            radarDatas.put(label, marker);

            if (oldMarker != null) {
                radarMarkers.remove(oldMarker);
            }
            radarMarkers.add(marker);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param googleMap - Loaded map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng eventPosition = event.getPosition();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
        final MarkerOptions eventMarkerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.radar_marker_event))
                .position(eventPosition)
                .snippet(String.format(getString(R.string.users_number_format), users.size()))
                .title(event.getName());
        mMap.addMarker(eventMarkerOptions);
    }
}
