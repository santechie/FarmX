package com.ascentya.AsgriV2.agripedia.Info_Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Activity_Lifecycle;
import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.MyInterface;
import com.ascentya.AsgriV2.Models.BasicInfo_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.AllCrops_Master;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.nambimobile.widgets.efab.FabOption;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class BasicInfo_fragment extends BaseFragment implements MyInterface {

    private static final int MODE_PRIVATE = 100;
    private static final int REQUEST_CHECK_SETTINGS = 5;
    private View root_view;
    private Boolean expand;
    private TextView name, science_name, family, disc;
    private SessionManager sm;
    private LinearLayout basic_main, empty, search_do;
    private LinearLayout root_layout;
    private List<String> Data;
    private List<BasicInfo_Model> datas;
    private MyInterface myInterface;
    private Lang_Token tk;
    Boolean crop_load = false;
    FabOption crop_list, crop_cycle;
    AllCrops_Master allCropsDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.basicinfo_layout, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            crop_load = getArguments().getBoolean("edttext", false);
        }


        disc = root_view.findViewById(R.id.disc);
        name = root_view.findViewById(R.id.name);
        science_name = root_view.findViewById(R.id.science_name);
//        edit_movies_fab = root_view.findViewById(R.id.edit_movies_fab);
        root_layout = root_view.findViewById(R.id.root_layout);
        family = root_view.findViewById(R.id.family);
        tk = new Lang_Token(getActivity());
        basic_main = root_view.findViewById(R.id.basic_main);
        empty = root_view.findViewById(R.id.empty);
        search_do = root_view.findViewById(R.id.search_do);
        crop_list = root_view.findViewById(R.id.crop_list);
        crop_cycle = root_view.findViewById(R.id.crop_cycle);


        if (crop_load) {
            allCropsDialog = new AllCrops_Master();
            allCropsDialog.dialog(getActivity(), null, Webservice.Data_crops, getString(R.string.listcrops), true);

        }

        crop_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allCropsDialog = new AllCrops_Master();
//                allCropsDialog.AllCropsDialog(getParentFragmentManager()());
                allCropsDialog.dialog(getActivity(), null, Webservice.Data_crops, getString(R.string.listcrops), false);
            }
        });

        crop_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Lifecycle.class);
                i.putExtra("crop", Webservice.Search_id);
                startActivity(i);
            }
        });

        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });
        sm = new SessionManager(getActivity());
        expand = true;
        myInterface = new BasicInfo_fragment();

        Data = new ArrayList<>();


        disc.setOnClickListener(new DebouncedOnClickListener(500) {
            @Override
            public void onDebouncedClick(View v) {
                if (Data.size() > 0) {
                    Dialog_Master obj = new Dialog_Master();
                    obj.dialog(getActivity(), Data, getString(R.string.description));
                } else {
                    Toast.makeText(getActivity(), "Description are not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (Webservice.Searchvalue.equalsIgnoreCase("none")) {


            empty.setVisibility(View.VISIBLE);
            basic_main.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            basic_main.setVisibility(View.VISIBLE);

            GetTasks obj = new GetTasks();
            obj.execute();

        }


        return root_view;
    }


    public void getcrops(final String lang) {

        Data.clear();
        AndroidNetworking.post(Webservice.getcrops)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }

                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                    name.setText(jsonObject1.optString("Basic_Name").trim());


                    science_name.setText(jsonObject1.optString("Scientific_Name").trim());
                    family.setText(jsonObject1.optString("Family"));

                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Plant_Description");


                    datas = new ArrayList<>();
                    BasicInfo_Model obj = new BasicInfo_Model();
                    obj.setExpand(true);


                    for (int i = 0; i < jsonArray1.length(); i++) {

                        if (!jsonArray1.get(i).toString().equalsIgnoreCase("")) {
                            Data.add(jsonArray1.get(i).toString().trim());
                        }

                    }

                    SaveTask bi = new SaveTask(Webservice.Search_id, name.getText().toString(), science_name.getText().toString(), family.getText().toString(), Data);
                    bi.execute();

//                    obj.setDisc(jsonObject1.getJSONArray("Plant_Description").toString());
//                    Data.add(obj);
                    if (Data.size() > 0) {
                        root_layout.setVisibility(View.VISIBLE);
                        disc.setText(Data.get(0));
                    } else {
                        root_layout.setVisibility(View.GONE);
                        disc.setVisibility(View.GONE);
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void foo() {

    }

    class SaveTask extends AsyncTask<Void, Void, Void> {
        String name, s_name, family;
        List<String> desc;
        String id;

        public SaveTask(String id, String name, String s_name, String family, List<String> desc) {
            super();
            this.name = name;
            this.s_name = s_name;
            this.family = family;
            this.desc = desc;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //creating a task
            Info_Model task = new Info_Model();
            task.setBasic_id(Integer.parseInt(id));
            task.setName(name);
            task.setS_name(s_name);
            task.setFamily(family);
            task.setDesc(desc);

            if(getActivity() == null) return null;

            //adding to database
            DatabaseClient.getInstance(getActivity().getApplicationContext()).getAppDatabase()
                    .taskDao()
                    .insert(task);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    class GetTasks extends AsyncTask<Void, Void, Info_Model> {


        @Override
        protected Info_Model doInBackground(Void... voids) {

            if (getActivity() == null) return null;

            Info_Model taskList = DatabaseClient
                    .getInstance(getActivity().getApplicationContext())
                    .getAppDatabase()
                    .taskDao()
                    .findSpecificEvent(Long.parseLong(Webservice.Search_id));
            return taskList;
        }

        @Override
        protected void onPostExecute(Info_Model tasks) {
            super.onPostExecute(tasks);
            if (tasks != null) {
                if (tasks.getName() != null) {
                    name.setText(tasks.getName());
                    Data.clear();

                    science_name.setText(tasks.getS_name());
                    family.setText(tasks.getFamily());

                    if (tasks.getDesc().size() > 0) {
                        root_layout.setVisibility(View.VISIBLE);
                        disc.setText(tasks.getDesc().get(0));
                        Data.addAll(tasks.getDesc());
                    } else {
                        root_layout.setVisibility(View.GONE);
                        disc.setVisibility(View.GONE);
                    }

                } else {


                }
            } else {
                getcrops(tk.getusename());
            }


        }
    }
//    private void displayLocationSettingsRequest(Context context) {
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API).build();
//        googleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(10000 / 2);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//
//                         Log.i(TAG, "All location settings are satisfied.");
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
//                        Toast.makeText(getActivity(), "PendingIntent unable to execute request.", Toast.LENGTH_SHORT).show();
//
//                        try {
//                            // Show the dialog by calling startResolutionForResult(), and check the result
//                            // in onActivityResult().
//                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Toast.makeText(getActivity(), "Location settings are inadequate, and cannot be fixed here. Dialog not created.", Toast.LENGTH_SHORT).show();
//
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                        break;
//                }
//            }
//        });
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.i(TAG, "User agreed to make required location settings changes.");
//                        Toast.makeText(getActivity(), "User agreed to make required location settings changes.", Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getActivity(), "User chose not to make required location settings changes.", Toast.LENGTH_SHORT).show();
//
//                        Log.i(TAG, "User chose not to make required location settings changes.");
//                        break;
//                }
//                break;
//        }
//    }


    @Override
    public void onDestroy() {
        if (allCropsDialog != null)
            allCropsDialog.dismiss();
        super.onDestroy();
    }
}
