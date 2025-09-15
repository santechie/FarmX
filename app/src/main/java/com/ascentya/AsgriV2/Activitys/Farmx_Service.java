package com.ascentya.AsgriV2.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ascentya.AsgriV2.Adapters.Farmx_services_Adpter;
import com.ascentya.AsgriV2.Models.Farmx_Services_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Farmx_Service extends AppCompatActivity {
    RecyclerView service_recycler;

    Farmx_services_Adpter adapter;

    List<Farmx_Services_Model> Data;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    ImageView backtologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_service);
        service_recycler = findViewById(R.id.service_recycler);
        backtologin = findViewById(R.id.backtologin);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Data = new ArrayList<>();
        service_recycler.setLayoutManager(new GridLayoutManager(Farmx_Service.this, 2));

        if (Webservice.state_id.equalsIgnoreCase("0")) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            getLastLocation();
        } else {


        }
        Farmx_Services_Model obj = new Farmx_Services_Model();

        obj = new Farmx_Services_Model();
        obj.setName("Bank");
        obj.setIcon(R.drawable.bank);
        Data.add(obj);
        obj = new Farmx_Services_Model();
        obj.setName("Insurance");
        obj.setIcon(R.drawable.insurance);
        Data.add(obj);
        obj = new Farmx_Services_Model();
        obj.setName("Procurer");
        obj.setIcon(R.drawable.procurer);
        Data.add(obj);
//        obj = new Farmx_Services_Model();
//        obj.setName("Retailer");
//        obj.setIcon(R.drawable.retailer);
//        Data.add(obj);
        obj = new Farmx_Services_Model();
        obj.setName("Cold chain Warehouse");
        obj.setIcon(R.drawable.warehouse);
        Data.add(obj);


        obj = new Farmx_Services_Model();
        obj.setName("Logistics");
        obj.setIcon(R.drawable.agrilog);
        Data.add(obj);

        obj = new Farmx_Services_Model();
        obj.setName("Supply chain");
        obj.setIcon(R.drawable.suplychain);
        Data.add(obj);


        obj = new Farmx_Services_Model();
        obj.setName("VLU");
        obj.setIcon(R.drawable.villageuser);
        Data.add(obj);


        obj = new Farmx_Services_Model();
        obj.setName("Farmer Input");
        obj.setIcon(R.drawable.fetlize);
        Data.add(obj);

        obj = new Farmx_Services_Model();
        obj.setName("Institution");
        obj.setIcon(R.drawable.institute);
        Data.add(obj);


        adapter = new Farmx_services_Adpter(Farmx_Service.this, Data);
        service_recycler.setAdapter(adapter);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    Webservice.state_id = getstates(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Farmx_Service.this)
                        .addApi(LocationServices.API).build();
                googleApiClient.connect();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(10000 / 2);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);

                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:

                                Log.i(TAG, "All location settings are satisfied.");
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the result
                                    // in onActivityResult().
                                    status.startResolutionForResult(Farmx_Service.this, 10);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                                break;
                        }
                    }
                });
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            Webservice.state_id = getstates(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getLastLocation();
    }

    public String getstates(Double lat, Double lang) {

        Geocoder geocoder;
        List<Address> addresses = null;
        String state = "English";
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() > 0) {
                state = addresses.get(0).getAdminArea();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return state;
    }

}