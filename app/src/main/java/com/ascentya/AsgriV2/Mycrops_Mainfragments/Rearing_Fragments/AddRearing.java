package com.ascentya.AsgriV2.Mycrops_Mainfragments.Rearing_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class AddRearing extends Fragment implements OnMapReadyCallback {
    View root_view;
    //    Spinner land, member, animal, pre_condition;
//    EditText food_cycle, animal_nick;
    ElasticButton addanimal;

    ViewDialog viewDialog;
    SessionManager sm;
    Spinner breed, animal, gender;
    TextInputLayout diseas_layout;
    TextInputEditText animal_name, animal_age, diseasdisc;
    EditText count;

    List<String> animaldata, breeddata, genderdata;
    //    List<String> member_data;
//    List<String> land_data;
//    List<String> animal_data;
//    List<String> predisease_data;
    TextView name, address, pincode;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.activity_rearingupdated, container, false);
        animal_age = root_view.findViewById(R.id.animal_age);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        name = root_view.findViewById(R.id.name);
        address = root_view.findViewById(R.id.address);
        pincode = root_view.findViewById(R.id.pincode);
        animal_name = root_view.findViewById(R.id.animal_name);
        breed = root_view.findViewById(R.id.breed);
        animal = root_view.findViewById(R.id.animal);
        diseasdisc = root_view.findViewById(R.id.diseasdisc);
        gender = root_view.findViewById(R.id.gender);
        diseas_layout = root_view.findViewById(R.id.diseas_layout);
        count = root_view.findViewById(R.id.count);
        animaldata = new ArrayList<>();
        breeddata = new ArrayList<>();
        genderdata = new ArrayList<>();
        viewDialog = new ViewDialog(getActivity());
        genderdata.add("Male");
        genderdata.add("Female");


        ArrayAdapter<String> gender_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                genderdata);

        gender.setAdapter(gender_adpter);

        animaldata.add("Cow");
        animaldata.add("Goat");
        animaldata.add("Chicken");
        animaldata.add("Duck");
        animaldata.add("Pig");
        animaldata.add("Rabbit");
        animaldata.add("Bull");
        animaldata.add("Sheep");

        ArrayAdapter<String> animal_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                animaldata);

        animal.setAdapter(animal_adpter);

        breeddata = new ArrayList<>();
        breeddata.add("Hybrid");
        breeddata.add("Normal");

        ArrayAdapter<String> breed_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                breeddata);

        breed.setAdapter(breed_adpter);

        sm = new SessionManager(getActivity());

        addanimal = root_view.findViewById(R.id.addanimal);
        addanimal.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (animal.getSelectedItem().toString().length() > 0) {
                    if (NetworkDetector.isNetworkStatusAvialable(getActivity())) {
                        add_animal();
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

//                    add_animal();
                } else {

                    Toast.makeText(getActivity(), R.string.selectanimalerror, Toast.LENGTH_SHORT).show();

                }

            }
        });

        getexpecteddata();
        return root_view;
    }

    public void add_animal() {
        viewDialog.showDialog();

        String prediseased_text;


        AndroidNetworking.post(Webservice.addanimal)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("animal_name", animal.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("animal_count", count.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_gender", gender.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("animal_age", "")
                .addUrlEncodeFormBodyParameter("animal_prediseases", "")
                .addUrlEncodeFormBodyParameter("diseases_disc", "")
                .addUrlEncodeFormBodyParameter("animal_breed", breed.getSelectedItem().toString())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        count.setText("");
//                        animal.setError(null);

                        Toasty.success(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });


    }


    public void getexpecteddata() {

        AndroidNetworking.get(Webservice.getveterinary_byplace + "chennai")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        name.setText(jsonObject.getJSONObject("data").optString("name"));
                        address.setText(jsonObject.getJSONObject("data").optString("address"));
                        pincode.setText(jsonObject.getJSONObject("data").optString("pincode"));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
