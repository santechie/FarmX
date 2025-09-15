package com.ascentya.AsgriV2.Activitys;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.CustomListAdapter;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.State_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.CustomAutoCompleteTextView;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.State_DialogMaster;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments.Cultivation_Fragment;
import com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments.Diseases_Fragment;
import com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments.Info_Fragment;
import com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments.Pests_Fragment;
import com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments.Phd_Fragment;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class HomeScreen_Activity extends BaseActivity implements SensorEventListener {
    private DrawerLayout mDrawer;
    BottomNavigationView navigation;
    private Toolbar toolbar;
    private ActionBar actionBar;
    AutoCompleteTextView search_bar;
    CustomAutoCompleteTextView autocompleteitem;
    List<String> Data;
    ActionBarDrawerToggle toggle;
    SessionManager sm;
    ImageView logo;
    Locale myLocale;
    Lang_Token tk;
    LinearLayout state_layout;
    TextView lang;
    private static final float SHAKE_THRESHOLD = 3.25f; // m/S*3*2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Boolean voice_alret;
    private long lastUpdate;
    boolean doubleBackToExitPressedOnce = false;
    boolean addition;
    List<String> meg_crops = new ArrayList<>();

    public void onResume() {
        super.onResume();
        DeleteBus.getInstance().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        DeleteBus.getInstance().unregister(this);
    }


    @Subscribe
    public void onTeamClicked(DeleteEvent event) {

        if (event.getFlag().equalsIgnoreCase("load_main")) {

            if (Webservice.Searchvalue != null) {
                autocompleteitem.setText(Webservice.Searchvalue);
            }
            loadFragment(new Info_Fragment());
            navigation.setSelectedItemId(R.id.navigation_network);

        } else {
            search_bar.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    search_bar.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint({"NonConstantResourceId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tk = new Lang_Token(HomeScreen_Activity.this);
        voice_alret = true;

        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        state_layout = findViewById(R.id.state_layout);
        meg_crops.add("Rice");
        meg_crops.add("Maize");
        meg_crops.add("Pineapple");
        meg_crops.add("Ginger");
        meg_crops.add("Turmeric");
        lang = findViewById(R.id.lang);
        logo = findViewById(R.id.logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addition = getIntent().getExtras().getBoolean("crop", false);

//        toast("Back Press: " + addition);


//        if (getIntent().getStringExtra("lang") != null) {
//            getIntent().getStringExtra("lang");
//
//            setLocale(getIntent().getStringExtra("lang"));
//
//
//            if (getIntent().getStringExtra("lang").equalsIgnoreCase("ta")) {
//                setLocale("ta");
//                lang.setText("Ta");
//            } else if (getIntent().getStringExtra("lang").equalsIgnoreCase("hi")) {
//                setLocale("hi");
//                lang.setText("Hi");
//            } else {
//                lang.setText("En");
//                setLocale("en_GB");
//            }
//
//        } else {
//
//        }


        //"#$%%#$%^#$%^$%^&%^&%^&*              "+
//if (gpsTracker.getLongitude() != 0){
//    if (Webservice.state.equalsIgnoreCase("0")){
//        Webservice.state = getstates(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//    }
//
//    if (Webservice.state.equalsIgnoreCase("Tamil nadu")){
//        setLocale("ta");
//    }else if (Webservice.state.equalsIgnoreCase("Meghalaya")){
//        setLocale("hi");
//    }else  {
//        setLocale("en_GB");
//    }
//}


//        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        int permissionCheck = ContextCompat.checkSelfPermission(HomeScreen_Activity.this,Manifest.permission.ACCESS_FINE_LOCATION);
//        int permissioncoarse = ContextCompat.checkSelfPermission(HomeScreen_Activity.this,Manifest.permission.ACCESS_COARSE_LOCATION);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED && permissioncoarse != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//
//            return;
//        }
//        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location != null ) {
//            // Do something with the recent location fix
//            //  otherwise wait for the update below
//        }
//        else {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) HomeScreen_Activity.this);
//        }

//        gpsTracker = new GPSTracker(HomeScreen_Activity.this);
//
//
//        Toast.makeText(HomeScreen_Activity.this, gpsTracker.getLatitude()+"    $%^&$%^&$%^&*%^&%^&   "+gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
//
//
//
//        if (gpsTracker.getLatitude() != 0){
//
//
//
//
//            getaddress(gpsTracker.getLatitude(),gpsTracker.getLongitude());
//        }


        state_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<State_Model> Data = new ArrayList();
                State_Model obj = new State_Model();
                obj.setName("Salem");
                obj.setId("0");

                Data.add(obj);

                obj = new State_Model();
                obj.setName("Meghalaya");
                obj.setId("2");

                Data.add(obj);


                State_DialogMaster obj1 = new State_DialogMaster();
                obj1.dialog(HomeScreen_Activity.this, Data, getString(R.string.state));
            }
        });

// Listen for shakes
        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        sm = new SessionManager(HomeScreen_Activity.this);

        search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);

        if (Webservice.crops.size() > 0) {
            getcrops();
        } else {
            getcrops();
        }
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (search_bar.getText().toString().length() > 2) {

                        if (NetworkDetector.isNetworkStatusAvialable(HomeScreen_Activity.this)) {

                            if (!searchFor(search_bar.getText().toString()).equals(-1)) {
                                hideKeyboard(HomeScreen_Activity.this);

                                autocompleteitem.dismissDropDown();

                                String cropId = Webservice.Data_crops.get(searchFor(search_bar.getText().toString())).getCrop_id();

                                if(canShowCrop(cropId)) {

                                    Webservice.Searchvalue = Webservice.Data_crops.get(searchFor(search_bar.getText().toString())).getName();
                                    Webservice.Searchicon = Webservice.Data_crops.get(searchFor(search_bar.getText().toString())).getIcon();
                                    Webservice.Search_id = cropId;

                                    Toasty.normal(HomeScreen_Activity.this, "Crop Id: " + Webservice.Search_id).show();

//                                Webservice.lang_id = getlang(search_bar.getText().toString());
                                    if (NetworkDetector.isNetworkStatusAvialable(HomeScreen_Activity.this)) {

                                        loadFragment(new Info_Fragment());
                                        navigation.setSelectedItemId(R.id.navigation_network);

                                    } else {
                                        Toast.makeText(HomeScreen_Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } else {
                                Toast.makeText(HomeScreen_Activity.this, "No crops found for this query.", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Toast.makeText(HomeScreen_Activity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(HomeScreen_Activity.this, "Please write something to Search", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });


        search_bar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {


                    if (actionBar != null) {
                        actionBar.setHomeButtonEnabled(false); // disable the button
                        actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
                        actionBar.setDisplayShowHomeEnabled(false); // remove the icon
                    }


                } else {

                    if (actionBar != null) {
                        actionBar.setHomeButtonEnabled(true); // disable the button
                        actionBar.setDisplayHomeAsUpEnabled(true); // remove the left caret
                        actionBar.setDisplayShowHomeEnabled(true); // remove the icon
                        mDrawer.setDrawerListener(toggle);
                        toggle.syncState();

                    }

                }

            }
        });


        navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_network:
                    fragment = new Info_Fragment();
                    return loadFragment(fragment);
                case R.id.navigation_job:
                    fragment = new Cultivation_Fragment();
                    return loadFragment(fragment);
                case R.id.navigation_recruit:
                    fragment = new Pests_Fragment();
                    return loadFragment(fragment);

                case R.id.navigation_buy:
                    fragment = new Diseases_Fragment();
                    return loadFragment(fragment);

                case R.id.navigation_sell:
                    fragment = new Phd_Fragment();
                    return loadFragment(fragment);
            }
            return false;
        });

        Fragment fragobj = null;

        if(getModuleManager().canView(Components.Agripedia.INFO)) {
            fragobj = new Info_Fragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("edttext", addition);
            fragobj.setArguments(bundle);
        } else if (getModuleManager().canView(Components.Agripedia.CULTIVATION)){
            fragobj = new Cultivation_Fragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("edttext", addition);
            fragobj.setArguments(bundle);
        }


        if (fragobj != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein,
                            R.anim.fadeout)
                    .replace(R.id.fragment_container, fragobj)
                    .commit();

        }


        CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocompleteitem, Webservice.Data_crops);
        autocompleteitem = (CustomAutoCompleteTextView)
                findViewById(R.id.search_bar);

        autocompleteitem.setAdapter(adapter);
        autocompleteitem.setOnItemClickListener(onItemClickListener);
        autocompleteitem.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);

        autocompleteitem.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autocompleteitem.getRight() - autocompleteitem.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        return true;
                    }
                }
                return false;
            }
        });

        autocompleteitem.setDropDownVerticalOffset(0);

        if (!getModuleManager().canView(Components.Agripedia.INFO))
            navigation.getMenu().removeItem(R.id.navigation_network);
        if (!getModuleManager().canView(Components.Agripedia.CULTIVATION))
            navigation.getMenu().removeItem(R.id.navigation_job);
        if (!getModuleManager().canView(Components.Agripedia.PEST))
            navigation.getMenu().removeItem(R.id.navigation_recruit);
        if (!getModuleManager().canView(Components.Agripedia.DISEASE))
            navigation.getMenu().removeItem(R.id.navigation_buy);
        if (!getModuleManager().canView(Components.Agripedia.PHD))
            navigation.getMenu().removeItem(R.id.navigation_sell);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    hideKeyboard(HomeScreen_Activity.this);

                    String cropId = Webservice.Data_crops.get(getCategoryPos(adapterView.getItemAtPosition(i).toString()))
                            .getCrop_id();

                    if (canShowCrop(cropId)) {

                        Webservice.Searchvalue = Webservice.Data_crops.get(getCategoryPos(adapterView.getItemAtPosition(i).toString())).getName();
                        Webservice.Searchicon = Webservice.Data_crops.get(getCategoryPos(adapterView.getItemAtPosition(i).toString())).getIcon();
                        Webservice.Search_id = cropId;
//                      Webservice.lang_id = getlang(adapterView.getItemAtPosition(i).toString());


//                    if (Webservice.lang_id.equals(2)) {
//                        setLocale("hi");
////                        Webservice.lang_tamilhindi=2;
//                    } else {
//                        setLocale("en_GB");
////                        Webservice.lang_tamilhindi=1;
//                    }

                        if (NetworkDetector.isNetworkStatusAvialable(HomeScreen_Activity.this)) {

                            loadFragment(new Info_Fragment());
                            navigation.setSelectedItemId(R.id.navigation_network);
                        } else {
                            Toast.makeText(HomeScreen_Activity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

    public boolean canShowCrop(String cropId){
        return checkSubscription(Components.MyFarm.CROP, cropId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


            getAccelerometer(event);
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;


                if (acceleration > 20) {


                    if (voice_alret) {
                        voice_alret = false;
//                        promptSpeechInput();
                    }


                }
            }
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /*public class SideMenuAdapter extends BaseAdapter {

        Context context;
        List<SideMenuObject> items;


        public SideMenuAdapter(Context context,
                               List<SideMenuObject> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        *//*private view holder class*//*
        private class ViewHolder {
            //            SelectableRoundedImageView list_img;
            TextView title, username;
            ImageView icon;
            LinearLayout bg;


        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            final SideMenuObject PO = items.get(position);

            int listViewItemType = getItemViewType(position);


            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.sidemenu_row, null);


                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.desc);

                holder.icon = (ImageView) convertView.findViewById(R.id.icon);


                convertView.setTag(holder);


            } else
                holder = (ViewHolder) convertView.getTag();


            holder.title.setText(PO.getTitle());
            holder.icon.setBackgroundResource(PO.getImg());


            return convertView;
        }
    }*/

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
        }
        if (fragment != null) {
            getSupportFragmentManager()

                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein,
                            R.anim.fadeout)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private int getCategoryPos(String category) {
        return Webservice.crops.indexOf(category);
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getName().toLowerCase().trim();
            String C_name = Webservice.Data_crops.get(i).getS_name().toLowerCase().trim();
            if (unitString.equals(data.toLowerCase().trim()) || C_name.equals(data.toLowerCase().trim())) {
                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }

    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "speech");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

        } catch (ActivityNotFoundException a) {
            voice_alret = true;
            Toast.makeText(getApplicationContext(),
                    "Speech not for this",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));

                    if (result.get(0).equalsIgnoreCase("hindi")) {
//                        setLocale("hi");

                        tk.setusename("0");
                        voice_alret = true;
                    } else if (result.get(0).equalsIgnoreCase("english")) {
//                        setLocale("en_GB");
                        tk.setusename("0");
                        voice_alret = true;
                    } else {
                        tk.setusename("0");
                        Toast.makeText(this, "Currently this application is supporting only for english and tamil", Toast.LENGTH_SHORT).show();
                        voice_alret = true;
                    }
                } else {
                    voice_alret = true;
                }
                break;
            }
        }
    }

//    public void setLocale(String localeName) {
//
//
//
//        if (getResources().getConfiguration().locale.toString().equalsIgnoreCase(localeName)) {
//
//        } else {
//
//
//            myLocale = new Locale(localeName);
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(this, HomeScreen_Activity.class);
//            refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//            refresh.putExtra(currentLang, localeName);
//            startActivity(refresh);
//        }
//
//
//    }

    public void getcrops() {

        //AndroidNetworking.get(Webservice.getname_icon)
        AndroidNetworking.get("https://vrjaitraders.com/ard_farmx/api/Agripedia/ciup")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(HomeScreen_Activity.this, jsonObject)){
                    return;
                }
                Webservice.Data_crops.clear();
                Webservice.crops.clear();


                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    System.out.println("dsfsdfsfdafdsf"+jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        Crops_Main obj = new Crops_Main();
                        JSONObject jobj = jsonArray.getJSONObject(i);
                        System.out.println("dfhkdsfhisfhk"+jobj);
                        obj.setName(jobj.optString("crop_name").trim());
                        obj.setIcon("https://vrjaitraders.com/ard_farmx/" + jobj.optString("crop_icons_images").trim());
                        obj.setCrop_id(jobj.optString("Basic_info_id").trim());
                        obj.setS_name(jobj.optString("scientific_name").trim());
                        obj.setTempreture(jobj.optString("temperature").trim());
                        obj.setPollution("40-50");
                        obj.setHumidity(jobj.optString("humidity").trim());
                        obj.setMoisture(jobj.optString("soil_moisture").trim());
                        obj.setWaterph(jobj.optString("soil_ph").trim());
                        obj.setVarieties(GsonUtils.fromJson(jobj.getJSONArray("varieties").toString(),
                                EMarketStorage.varietyListType));
                        Webservice.crops.add(jobj.optString("Basic_Name").split("###")[0].trim());
                        Webservice.Data_crops.add(obj);
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }


    public int getlang(String name) {
        Integer lang_id = 0;

        for (String s : meg_crops) {
            int i = s.indexOf(name);
            if (i >= 0) {
                lang_id = 2;
                break;
            } else {
                lang_id = 1;
            }
        }
        return lang_id;
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

    public void setLocale(String localeName) {


        Locale current = getResources().getConfiguration().locale;

        if (localeName.equalsIgnoreCase(current.toString())) {

        } else {

            Locale locale;
            //Log.e("Lan",session.getLanguage());
            locale = new Locale(localeName);
            Configuration config = new Configuration(this.getResources().getConfiguration());
            Locale.setDefault(locale);
            config.setLocale(locale);

            this.getBaseContext().getResources().updateConfiguration(config,
                    this.getBaseContext().getResources().getDisplayMetrics());


//            myLocale = new Locale(localeName);
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(HomeScreen_Activity.this, HomeScreen_Activity.class);
//            refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//            refresh.putExtra(currentLang, localeName);
//            startActivity(refresh);
        }


    }
}
