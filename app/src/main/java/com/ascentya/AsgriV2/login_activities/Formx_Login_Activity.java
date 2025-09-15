package com.ascentya.AsgriV2.login_activities;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Expired_Page;
import com.ascentya.AsgriV2.Activitys.HomeScreen_Activity;
import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.Adapters.SuggestedCrops_Adapter;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.Suggest_Interface;
import com.ascentya.AsgriV2.Models.SubscriptionModel;
import com.ascentya.AsgriV2.Models.SuggetstedCrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.CustomAutoCompleteTextView;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;
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
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.otto.Bus;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Formx_Login_Activity extends BaseActivity {
    TextView forgot_password, new_user, guest_user, privacypolicy, terms;
    ViewDialog viewDialog;
    Button signin;
    EditText username;
    TextInputEditText password;
    CustomAutoCompleteTextView search_bar;
    SessionManager sm;
    Lang_Token tk;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String User_Validate;
    List<SuggetstedCrops_Model> suggest_Data;
    RecyclerView crop_suggestion;
    SuggestedCrops_Adapter suggestedCrops_adapter;

    // New
    private TextView skipBtn;
    private Location location;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formx__login);

        username = findViewById(R.id.user_name);
        search_bar = findViewById(R.id.search_bar);
        terms = findViewById(R.id.terms);

        crop_suggestion = findViewById(R.id.crop_suggestion);
        crop_suggestion.setLayoutManager(new LinearLayoutManager(Formx_Login_Activity.this,
                LinearLayoutManager.HORIZONTAL, false));
        crop_suggestion.setHasFixedSize(true);

        privacypolicy = findViewById(R.id.privacypolicy);
        password = findViewById(R.id.password);
        tk = new Lang_Token(this);
        signin = findViewById(R.id.signin);
        guest_user = findViewById(R.id.guest_user);
        forgot_password = findViewById(R.id.forgot_password);
        viewDialog = new ViewDialog(Formx_Login_Activity.this);
        new_user = findViewById(R.id.new_user);
        sm = new SessionManager(this);
        // New
        skipBtn = findViewById(R.id.skip);

        search_bar.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Formx_Login_Activity.this, HomeScreen_Activity.class);
                i.putExtra("crop", true);
                startActivity(i);
            }
        });

        if (Webservice.state_id.equalsIgnoreCase("0")) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
        } else {
        }

        guest_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tk.setusename("0");
                Webservice.Searchvalue = "none";
                Intent i = new Intent(Formx_Login_Activity.this, Main_Dashboard.class);
                startActivity(i);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatePasswordEquality()) {
                    Login();
                }
                /*if (validateEmail()) {*/

               /* }*/
            }
        });
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Formx_Login_Activity.this, Farmer_Registration.class);
                startActivity(i);

            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Formx_Login_Activity.this, Farmx_Forgot_Password.class);
                startActivity(i);*/
                openActivity(ForgotPasswordActivity.class);
            }
        });

        suggest_Data = new ArrayList<>();
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {


//                            if (floatbetween(Float.parseFloat(jsonobj.optString("crop_temperature")), Float.parseFloat(temdatastr[0]), Float.parseFloat(temdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_humidity")), Integer.parseInt(humdatastr[0]), Integer.parseInt(humdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_pollution")), Integer.parseInt(polludatastr[0]), Integer.parseInt(polludatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_moisture")), Integer.parseInt(moisudatastr[0]), Integer.parseInt(moisudatastr[1]))) {
            SuggetstedCrops_Model suggest_obj = new SuggetstedCrops_Model();
            suggest_obj.setName(Webservice.Data_crops.get(i).getName());
            suggest_obj.setId(Webservice.Data_crops.get(i).getCrop_id());
            suggest_obj.setDissolved_solids(Webservice.Data_crops.get(i).getDissolved_solids());
            suggest_obj.setHumidity(Webservice.Data_crops.get(i).getHumidity());
            suggest_obj.setIcon(Webservice.Data_crops.get(i).getIcon());
            suggest_obj.setMoisture(Webservice.Data_crops.get(i).getMoisture());
            suggest_obj.setPollution(Webservice.Data_crops.get(i).getPollution());
            suggest_obj.setTempreture(Webservice.Data_crops.get(i).getTempreture());
            suggest_obj.setWaterph(Webservice.Data_crops.get(i).getWaterph());

            suggest_Data.add(suggest_obj);


//                            }
            if (suggest_Data.size() >= 10) {
                break;
            }
        }

        suggestedCrops_adapter = new SuggestedCrops_Adapter(Formx_Login_Activity.this,
                suggest_Data, new Suggest_Interface() {
            @Override
            public void crop_suggest(SuggetstedCrops_Model name) {
                Webservice.Searchvalue = name.getName();
                Webservice.Searchicon = name.getIcon();
                Webservice.Search_id = name.getId();

                System.out.println("namammmm"+name.getName());
                System.out.println("iccccccoooo"+name.getIcon());
                Intent i = new Intent(Formx_Login_Activity.this, HomeScreen_Activity.class);
                i.putExtra("crop", false);
                startActivity(i);
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("load_main"));


            }
        });
        crop_suggestion.setAdapter(suggestedCrops_adapter);

        skipBtn.setOnClickListener(view -> guestLogin());
    }

    private void guestLogin(){
        viewDialog.showDialog();

        String brand = Build.MANUFACTURER;
        String model = Build.MODEL;
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Double latitude = location != null ? location.getLatitude() : 0;
        Double longitude = location != null ? location.getLongitude() : 0;

        System.out.println("Location: " + latitude + " / " + longitude);

        AndroidNetworking.post(Webservice.guestLogin)
                .addUrlEncodeFormBodyParameter("brand", brand)
                .addUrlEncodeFormBodyParameter("model", model)
                .addUrlEncodeFormBodyParameter("android_id", androidId)
                .addUrlEncodeFormBodyParameter("latitude", latitude + "")
                .addUrlEncodeFormBodyParameter("longitude", longitude + "")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                viewDialog.hideDialog();
                try {
                    if(response.getBoolean("status")){
                        String guestId = response.optString("data");
                        /*Toasty.normal(Formx_Login_Activity.this,
                                "Guest: " + guestId).show();*/
                        loginAsGuest(guestId);
                    }else {
                        Toasty.normal(Formx_Login_Activity.this,
                                response.optString("message", "Cant login as Guest!")).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toasty.error(Formx_Login_Activity.this, "Guest Login Error!").show();
            }
        });
    }

    public void loginAsGuest(String guestId){
        sm.setGuestId(guestId);
        goToDashboard();
    }


    public void Login() {
        viewDialog.showDialog();
       // AndroidNetworking.post(Webservice.Login)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Authentication/login")
                .addUrlEncodeFormBodyParameter("phone", username.getText().toString())
                .addUrlEncodeFormBodyParameter("password", password.getText().toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    System.out.println("Login Response: \n" + jsonObject);

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        username.setText("");
                        password.setText("");

                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.getJSONObject("data").optString("id"));
                        obj.setFirstname(jsonObject.getJSONObject("data").optString("username"));
                        obj.setPhno(jsonObject.getJSONObject("data").optString("phone"));
                        obj.setEmail(jsonObject.getJSONObject("data").optString("email"));
                        obj.setIspremium(jsonObject.getJSONObject("data").optString("is_premium"));
                        obj.setSearch_name("none");

                        sm.setUser(obj);
                        Webservice.Searchvalue = "none";

                        if (jsonObject.optJSONObject("subscription") != null)
                            sm.setSubscription(GsonUtils.getGson()
                                    .fromJson(jsonObject.getJSONObject("subscription").toString(),
                                            SubscriptionModel.class));

                        Login_farmer(jsonObject.getJSONObject("data").optString("id"),
                                jsonObject.getJSONObject("data").optString("username"),
                                jsonObject.getJSONObject("data").optString("phone"),
                                jsonObject.getJSONObject("data").optString("email"));

                        User_Validate = jsonObject.getJSONObject("data").optString("expired_status");
                        if (User_Validate.equalsIgnoreCase("0")) {
                            Intent i = new Intent(Formx_Login_Activity.this, Expired_Page.class);
                            startActivity(i);
                            finishAffinity();
                        } else {
                        Intent i = new Intent(Formx_Login_Activity.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();
                        }

                    } else {
                        Toast.makeText(Formx_Login_Activity.this,
                                jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                try {
                    System.out.println("Login Error: " + GsonUtils.getGson().toJson(anError));
                }catch (Exception e){
                    e.printStackTrace();
                }
                viewDialog.hideDialog();
            }
        });
    }

    public void Login_farmer(String user_id, String user_name, String phno, String email) {


       // AndroidNetworking.post(Webservice.get_farmer)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Authentication/getfarmerinfo")
                .addUrlEncodeFormBodyParameter("id", user_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Userobject obj = new Userobject();
                        obj.setId(user_id);
                        obj.setFirstname(user_name);
                        obj.setPhno(phno);
                        obj.setEmail(email);
                        obj.setSearch_name("none");

                        obj.setFarmer_type(jsonObject.getJSONArray("data").getJSONObject(0).optString("farming_type"));
                        obj.setLandloc(jsonObject.getJSONArray("data").getJSONObject(0).optString("farmer_location"));
                        obj.setBank_no(jsonObject.getJSONArray("data").getJSONObject(0).optString("bank_ac_no"));
                        obj.setPincode(jsonObject.getJSONArray("data").getJSONObject(0).optString("pincode"));
                        obj.setVillage(jsonObject.getJSONArray("data").getJSONObject(0).optString("village"));
                        obj.setDistrict(jsonObject.getJSONArray("data").getJSONObject(0).optString("district"));
                        obj.setState(jsonObject.getJSONArray("data").getJSONObject(0).optString("state"));
                        obj.setCity(jsonObject.getJSONArray("data").getJSONObject(0).optString("farmer_city"));
                        obj.setStreet_name(jsonObject.getJSONArray("data").getJSONObject(0).optString("farmer_street"));
                        obj.setNoofmember(jsonObject.getJSONArray("data").getJSONObject(0).optString("no_of_family_members"));
                        obj.setAge(jsonObject.getJSONArray("data").getJSONObject(0).optString("farmer_age"));
                        obj.setGender(jsonObject.getJSONArray("data").getJSONObject(0).optString("farmer_gender"));
                        obj.setMother_name(jsonObject.getJSONArray("data").getJSONObject(0).optString("mother_name"));

                        sm.setUser(obj);

                        Webservice.Searchvalue = "none";

                        User_Validate = jsonObject.getJSONObject("data").optString("expired_status");
                        if (User_Validate.equalsIgnoreCase("0")) {
                            Intent i = new Intent(Formx_Login_Activity.this, Expired_Page.class);
                            startActivity(i);
                            finishAffinity();
                        } else {
                        loadModules();
                        }
                    } else {
                        Toast.makeText(Formx_Login_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    public void loadModules(){

        if(getModuleManager().canLoad()){

            getModuleManager().load(new ModuleManager.LoaderAction() {
                @Override
                public void onLoaded(boolean error) {
                    viewDialog.hideDialog();
                    if (error){
                        toast("Modules Load Error!");
                    }else {
                        goToDashboard();
                    }
                }
            });
        }else {
            viewDialog.hideDialog();
            goToDashboard();
        }
    }

    private void goToDashboard() {

        if (getSubscriptionManager().canLoad()) {
            getSubscriptionManager().load(new SubscriptionManager.Action() {
                //        loadUserPrivileges(true, new Result() {
                @Override
                public void onLoaded(boolean error) {
                    if (error) {
                        Toasty.error(Formx_Login_Activity.this, "Network error").show();
                    } else {
                        Intent i = new Intent(Formx_Login_Activity.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();
                    }
                }
            });
        }
    }

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";
        if (ValidateInputs.isValidNumber(username.getText().toString())) {
            return true;
        } else {
            username.setError("Enter valid Number");
            return false;
        }

    }


    ///validate password
    public boolean validatePasswordEquality() {
        if (ValidateInputs.isValidPassword(password.getText().toString())) {
            return true;
        } else {
            password.setError("Enter valid password!");
            return false;
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
                                    Formx_Login_Activity.this.location = location;
                                    Webservice.state_id = getstates(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Formx_Login_Activity.this)
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
                                    status.startResolutionForResult(Formx_Login_Activity.this, 10);
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

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Formx_Login_Activity.this.location = mLastLocation;
            Webservice.state_id = getstates(mLastLocation.getLatitude(), mLastLocation.getLongitude());

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

}