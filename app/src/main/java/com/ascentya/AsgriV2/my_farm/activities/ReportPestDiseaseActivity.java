package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.CommonDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.NewReportItemAdapter;
import com.ascentya.AsgriV2.my_farm.dialogs.AddNewReportItemDialog;
import com.ascentya.AsgriV2.my_farm.model.NewReportItem;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReportPestDiseaseActivity
        extends BaseActivity
        implements NewReportItemAdapter.Action,
        AddNewReportItemDialog.Action{

    private TextView landTv, cropTv, zoneTv, locationTv;
    private ImageView myLocationBtn;
    private FloatingActionButton addBtn;
    private MaterialButton submitBtn;

    private RecyclerView recyclerView;
    private NewReportItemAdapter adapter;

    private ArrayList<NewReportItem> reportItems = new ArrayList<>();

    private Maincrops_Model land;
    private Crops_Main crop;
    private Zone_Model zone;

    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;

    private boolean askPermission;
    private boolean submitNow;

    private Random

            random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pest_disease);

        setToolbarTitle("New Report", true);

        land = getFromIntent("land", Maincrops_Model.class);
        crop = getFromIntent("crop", Crops_Main.class);
        zone = getFromIntent("zone", Zone_Model.class);

        landTv = findViewById(R.id.land);
        cropTv = findViewById(R.id.crop);
        zoneTv = findViewById(R.id.zone);
        locationTv = findViewById(R.id.location);

        landTv.setText(land.getLand_name());
        cropTv.setText(crop.getName());
        zoneTv.setText(zone.getZone_name());

        myLocationBtn = findViewById(R.id.locationBtn);
        addBtn = findViewById(R.id.add);
        submitBtn = findViewById(R.id.submit);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new NewReportItemAdapter(this);

        recyclerView.setAdapter(adapter);

        myLocationBtn.setOnClickListener(v -> getLastLocation(true));

//        if (!getModuleManager().canInsert(Components.MyFarm.REMEDY))
//            addBtn.setVisibility(View.INVISIBLE);

        addBtn.setOnClickListener(v -> showAddNewReportDialog(null));
        submitBtn.setOnClickListener(v -> submit());

        setUpLocation();
    }

    private void showAddNewReportDialog(@Nullable NewReportItem item){
//        if (checkSubscription(Components.MyFarm.REMEDY, ModuleManager.ACCESS.INSERT))
//        {
        AddNewReportItemDialog dialog = new AddNewReportItemDialog(this, this, item);
        dialog.show(item != null);
//        }
    }

    private void setUpLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation(false);
    }

    @Override
    public ArrayList<NewReportItem> getReportItems() {
        return reportItems;
    }

    @Override
    public void onEdit(int position) {
        showAddNewReportDialog(reportItems.get(position));
    }

    @Override
    public void onDelete(int position) {
        new CommonDialog(this, getString(R.string.confirm),
                "Do you want to delete `" + reportItems.get(position).getTitle() + "`?",
                getString(R.string.yes), getString(R.string.no), new CommonDialog.Action() {
            @Override
            public void actionOne() {
                reportItems.remove(position);
                adapter.notifyDataSetChanged();
                checkNoReports();
            }

            @Override
            public void actionTwo() {

            }

            @Override
            public void onCancelled() {

            }
        });
    }

    @Override
    public void onAdd(boolean isUpdate, NewReportItem item) {

        if (!isUpdate) {
            int randomId = random.nextInt();
            while (isIdFound(randomId)) { randomId = random.nextInt(); }
            item.setId(random.nextInt());
            reportItems.add(item);
        }
        adapter.notifyDataSetChanged();
        checkNoReports();
    }

    private boolean isIdFound(int id){
        for (NewReportItem i: reportItems) {
            if (i.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(NewReportItem newItem) {
       /* for (NewReportItem item: reportItems){
            if (item.hashCode() != newItem.hashCode()){
                if (item.getCropPart().getTypeValue().equals(newItem.getCropPart().getTypeValue()) &&
                        item.getReportType().getTypeValue().equals(newItem.getReportType().getTypeValue()) &&
                        item.getTitle().toLowerCase().equals(newItem.getTitle().toLowerCase())){
                    return true;
                }
            }
        }*/


        for (NewReportItem item : reportItems) {
            if (!item.equals(newItem)) {  // Use equals() instead of hashCode()
                if (item.getCropPart().getTypeValue().equals(newItem.getCropPart().getTypeValue()) &&
                        item.getReportType().getTypeValue().equals(newItem.getReportType().getTypeValue()) &&
                        item.getTitle().equalsIgnoreCase(newItem.getTitle())) {  // Use equalsIgnoreCase()

                    return true;
                }
            }
        }

        return false;
    }

    private void checkNoReports(){
        findViewById(R.id.noReports)
                .setVisibility(reportItems.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    private void submit() {

        if (!reportItems.isEmpty()) {
            submitNow = true;

            if (location == null) {
                toast("Getting Location...");
                getLastLocation(true);
            } else {
                showLoading();

                ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(Webservice.submitInfection);

                if (location == null || zone == null) {
                    toast("Location or Zone is missing!");
                    return;
                }

                builder.addHeaders("Content-Type", "application/json");
                builder.addHeaders("Accept", "application/json");

                builder.addMultipartParameter("user_id", getSessionManager().getUser().getId());
                builder.addMultipartParameter("zone_id", zone.getZone_id());
                builder.addMultipartParameter("latitude", String.valueOf(location.getLatitude()));
                builder.addMultipartParameter("longitude", String.valueOf(location.getLongitude()));

                JSONArray jsonArray = new JSONArray();

                for (NewReportItem item : reportItems) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", item.getId());
                        jsonObject.put("type", item.getReportType().getTypeValue());
                        jsonObject.put("part", item.getCropPart().getTypeValue());
                        jsonObject.put("name", item.getTitle());
                        jsonObject.put("symptom", item.getSymptom());

                        jsonArray.put(jsonObject);

                        for (ImageModel imageModel : item.getImageModels()) {
                            File imageFile = new File(imageModel.getImage());
                            if (imageFile.exists()) {
                                builder.addMultipartFile("image_" + item.getId(), imageFile);
                            } else {
                                Log.e("IMAGE_ERROR", "File not found: " + imageModel.getImage());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast("JSON Error!");
                        return;
                    }
                }

                // Add reports JSON to request
                builder.addMultipartParameter("reports", jsonArray.toString());

                builder.build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        Toast.makeText(ReportPestDiseaseActivity.this, "Report Update response!" + response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        anError.printStackTrace();

                        if (anError.getErrorBody() != null) {
                            Toast.makeText(ReportPestDiseaseActivity.this, "Error!" + anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                            Log.e("API_ERROR_BODY", anError.getErrorBody());
                        } else if (anError.getResponse() != null) {
                            Log.e("API_ERROR_RESPONSE", anError.getResponse().toString());
                        } else {
                            Log.e("API_ERROR_DETAIL", anError.getErrorDetail());
                        }

                        Toast.makeText(ReportPestDiseaseActivity.this, "Report Update Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            toast("Add Report to Submit!");
        }
    }

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

    private void setLocation(Location location){
        this.location = location;
        locationTv.setText("Lat: " + location.getLatitude() + " / Lng: " + location.getLongitude());
        if(submitNow) submit();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(boolean askPermission) {
        this.askPermission = askPermission;
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationTv.setText("Getting Location...");
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    setLocation(location);                             }
                            }
                        }
                );
            } else {

                if (!askPermission) return;

                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(ReportPestDiseaseActivity.this)
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
                                    status.startResolutionForResult(ReportPestDiseaseActivity.this, 10);
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
            if (askPermission)
                requestPermissions();
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            setLocation(mLastLocation);
        }
    };

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(askPermission);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLastLocation(askPermission);
    }
}