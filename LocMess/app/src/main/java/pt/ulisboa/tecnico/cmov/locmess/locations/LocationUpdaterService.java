package pt.ulisboa.tecnico.cmov.locmess.locations;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.locmess.messages.service.MessagePollingService;

/**
 * Service responsible for retrieving location updates.
 */

public class LocationUpdaterService extends Service {

    private static final String LOG_TAG = LocationUpdaterService.class.getName();
    private LocationManager locationManager = null;
    private static final int UPDATE_INTERVAL = 1000; // TODO: use a larger interval
    private static final float UPDATE_DISTANCE = 10f;

    private class LocMessLocationListener implements LocationListener {
        Location lastLocation = null;

        public LocMessLocationListener(String provider) {
            Log.d(LOG_TAG, "provider: " + provider);
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(LOG_TAG, "onLocationChanged: " + location);
            lastLocation.set(location);

            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            Intent i = new Intent(getApplicationContext(), MessagePollingService.class);
            i.putExtra(MessagePollingService.LOCATION_TYPE_EXTRA, MessagePollingService.LocationType.GPS);
            i.putExtra(MessagePollingService.LATITUDE_EXTRA, latitude);
            i.putExtra(MessagePollingService.LONGITUDE_EXTRA, longitude);

            startService(i);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(LOG_TAG, "onStatusChanged: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(LOG_TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(LOG_TAG, "onProviderDisabled: " + provider);
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new LocMessLocationListener(LocationManager.GPS_PROVIDER),
            new LocMessLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate");
        initLocationManager();
        try {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                throw new RuntimeException("No location permissions");
                // TODO: Handle error gracefully
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL,
                    UPDATE_DISTANCE, locationListeners[0]);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL,
                    UPDATE_DISTANCE, locationListeners[1]);
        } catch (SecurityException ex) {
            Log.d(LOG_TAG, "Failed to request locaiton update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_TAG, "Provider does not exit " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
        if (locationManager != null) {
            for (LocationListener listner : locationListeners) {
                locationManager.removeUpdates(listner);
            }
        }
    }

    private void initLocationManager() {
        Log.d(LOG_TAG, "initLocaitonManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
