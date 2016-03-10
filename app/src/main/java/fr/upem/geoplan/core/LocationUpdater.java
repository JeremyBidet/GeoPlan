package fr.upem.geoplan.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import fr.upem.geoplan.core.server.gcm.RequestToServer;

public class LocationUpdater extends Service implements LocationListener {
    private static final String TAG = "GeoPlanLocService";
    private static final long MIN_DISTANCE = 10000;
    private static final long MIN_TIME = 5000;

    private LocationManager locationManager;

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocationUpdater getService() {
            return LocationUpdater.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        Log.w(TAG, "Listening location updates");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Location updated " + location);
       // RequestToServer.updatePosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }
}
