package fr.upem.geoplan.core.radar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import co.geeksters.radar.Radar;
import co.geeksters.radar.RadarPoint;
import upem.fr.geoplan.R;

public class Radar_activity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * Map markers
     */
    private final ConcurrentHashMap<Integer, Marker> mapMarkers = new ConcurrentHashMap<>();

    /**
     * Radar markers
     */
    private final ArrayList<RadarPoint> radarMarkers = new ArrayList<>();

    private final LatLng eventPosition = new LatLng(10.00000f, 22.0000f);

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_activity);

        initializeRadar();
        initializeMap();
    }

    private void initializeRadar() {
        Radar radar = (Radar) findViewById(R.id.radar);

        //And here set the reference Point (or for example your GPS location)
        radar.setReferencePoint(new RadarPoint(getString(R.string.reference_point_label), 10f, 22f));

        radar.setPoints(radarMarkers);
        radar.setOnTouchListener(new RadarEvent(getBaseContext(), radar));
    }

    private void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Update a user position on all views (map and radar).
     *
     * @param userId   - Id of the user.
     * @param position - New position of the user.
     */
    public void updatePosition(Integer userId, LatLng position) {
        updatePositionMap(userId, position);
        updatePositionRadar(userId, position);
    }

    private void updatePositionMap(Integer userId, LatLng position) {
        Marker previousMarker = mapMarkers.get(userId);
        if (previousMarker != null) {
            previousMarker.remove();
        }
        mapMarkers.put(userId, mMap.addMarker(new MarkerOptions().position(position)));
    }

    private void updatePositionRadar(Integer userId, LatLng position) {
        RadarPoint marker = new RadarPoint("User " + userId, (float) position.latitude, (float) position.longitude);
        radarMarkers.add(marker);
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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));

        updatePosition(1, new LatLng(10.00220f, 22.0000f));
        updatePosition(2, new LatLng(10.00420f, 22.0010f));
        updatePosition(3, new LatLng(100, 200));
    }
}
