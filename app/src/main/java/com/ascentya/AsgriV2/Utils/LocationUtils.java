package com.ascentya.AsgriV2.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocationUtils extends LocationCallback implements LocationListener {

    private Context context;
    private Action action;
    private FusedLocationProviderClient client;

    public LocationUtils(Context context, Action action) {
        this.context = context;
        this.action = action;
        client = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("NewApi")
    public void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isLocationEnabled()) {
                getLastLocation();
            } else {
                    requestLocation();
            }
        } else {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    getLastLocation();
                } else {
                    requestLocation();
                }
            } else {
                requestPermission();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        action.showLoading();
        client.getLastLocation().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        if (location == null) {
                            setUpFusedLocation();
                        } else {
                            onLocationChanged(location);
                        }
                    }else {
                        setUpFusedLocation();
                    }
                }
        );
    }

    @SuppressLint("MissingPermission")
    private void setUpFusedLocation() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        client.requestLocationUpdates(
                mLocationRequest, this, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                1000
        );
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == 1000){
            boolean permissionGranted = true;
            for (int res : grantResults){
                permissionGranted = permissionGranted && res == PackageManager.PERMISSION_GRANTED;
            }
            if (permissionGranted){
                getLastLocation();
            }else {
                action.onPermissionDenied();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        getLastLocation();
    }

    private void requestLocation() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        locationRequest.setNumUpdates(1);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(TAG, "All location settings are satisfied.");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult((Activity) context, 10);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onLocationChanged(Location location) {
        action.hideLoading();
        action.onLocationReceived(location.getLatitude(), location.getLongitude());
    }

    public interface Action {
        void onLocationReceived(Double latitude, Double longitude);
        void onPermissionDenied();
        void showLoading();
        void hideLoading();
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult != null){
            if (locationResult.getLastLocation() != null){
                onLocationChanged(locationResult.getLastLocation());
            }
        }
    }
}
