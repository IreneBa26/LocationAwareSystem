package com.sm.app.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sm.app.utils.Constant;
import com.sm.app.utils.Controller;

/**
 *  Service that implements the Polling Strategy.
 *  It check location every 5 sec, with PRIORITY_HIGH_ACCURACY.
 */
public class PollingStrategyService extends Service implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected static final String TAG = "[DebApp]PollingSService";

    /* indicates how to behave if the service is killed */
    int mStartMode;

    /* GoogleApiClient for Google Maps Android APIs */
    GoogleApiClient mGoogleApiClient;

    /* interface for clients that bind */
    IBinder mBinder;

    /* indicates whether onRebind should be used */
    boolean mAllowRebind;

    /* Called when the service is being created. */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mGoogleApiClient = this.buildGoogleApiClient();
        mGoogleApiClient.connect();
        this.init();
    }

    /* The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand" + mGoogleApiClient.toString());
        return mStartMode;
    }

    /* A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /* Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /* Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /* Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Toast.makeText(this, Constant.TOAST_TEXT_POLLING_SERVICE_STOP, Toast.LENGTH_LONG).show();
    }

    protected synchronized GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /* When Google APIs are ready, requestLocationUpdates is strated! */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connection G APIs OK!");
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(Constant.POLLING_UPDATE_REQUEST_MILLIS); // 5 sec
            mLocationRequest.setFastestInterval(Constant.POLLING_UPDATE_REQUEST_MILLIS);  // 5 sec
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, "Invalid location permission. " + "You don't have permission for FINE LOCATION ", securityException);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection G APIs fail!");
    }

    /* called every 5 sec for location update*/
    @Override
    public void onLocationChanged(Location location) {
        this.getMatchedFencesEvents(location);
    }

    /* Method that call a Controller method (status monitoring between fence and current location) */
    private void getMatchedFencesEvents(Location currentLocation) {
        for (int i=0;i<Controller.fences.size();i++) {
            Controller.processFenceEvent(Controller.fences.get(i), currentLocation, getApplicationContext());
        }
    }

    /* initialization */
    private void init() {
        Controller.getInstance();
        Controller.setDbManager(getApplicationContext());
        Controller.resumeFencesFromDb();
    }


}