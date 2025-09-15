package com.ascentya.AsgriV2.Mycrops_Mainfragments.Landsub_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class RegisterLand extends Fragment {
    View root_view;

    ElasticButton add_field;
    SessionManager sm;
    ViewDialog viewDialog;
    AppCompatAutoCompleteTextView soil_type, land_unit, power_source, water_source, water_type, govt_scheme, soil_card, organic_former;
    List<String> Soiltype_data, landdime_data, powersource_data, watersource_data, watertype_data, govtscheme_data, soilcard_data;
    TextView applyeb, applysolar, applyborewell;
    TextInputEditText current_location, pincode, panchayat, landdime, land_name, years_soilreport, years_scheme, certification_no;
    TextInputLayout inputlayout_schemes, inputlayout_card, inputlayout_certificate;
    TextView applysceme, applysoiltest, applycertificate;
    Calendar cal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.activity_addland_updated, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cal = Calendar.getInstance();

        land_name = root_view.findViewById(R.id.land_name);
        landdime = root_view.findViewById(R.id.landdime);
        current_location = root_view.findViewById(R.id.current_location);
        pincode = root_view.findViewById(R.id.pincode);
        panchayat = root_view.findViewById(R.id.panchayat);
        soil_type = root_view.findViewById(R.id.soil_type);
        land_unit = root_view.findViewById(R.id.land_unit);
        power_source = root_view.findViewById(R.id.power_source);
        water_source = root_view.findViewById(R.id.water_source);
        water_type = root_view.findViewById(R.id.water_type);
        applyeb = root_view.findViewById(R.id.applyeb);
        applysolar = root_view.findViewById(R.id.applysolar);
        applyborewell = root_view.findViewById(R.id.applyborewell);


        govt_scheme = root_view.findViewById(R.id.govt_scheme);
        soil_card = root_view.findViewById(R.id.soil_card);
        applysceme = root_view.findViewById(R.id.applysceme);
        inputlayout_schemes = root_view.findViewById(R.id.inputlayout_schemes);
        inputlayout_card = root_view.findViewById(R.id.inputlayout_card);
        inputlayout_card = root_view.findViewById(R.id.inputlayout_card);
        applysoiltest = root_view.findViewById(R.id.applysoiltest);
        organic_former = root_view.findViewById(R.id.organic_farmer);
        inputlayout_certificate = root_view.findViewById(R.id.inputlayout_certificate);
        applycertificate = root_view.findViewById(R.id.applycertificate);
        years_soilreport = root_view.findViewById(R.id.years_soilreport);
        years_scheme = root_view.findViewById(R.id.years_scheme);
        certification_no = root_view.findViewById(R.id.certification_no);


        years_soilreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                        years_soilreport.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        govtscheme_data = new ArrayList<>();
        govtscheme_data.add("Yes");
        govtscheme_data.add("No");
        ArrayAdapter<String> scheme_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                govtscheme_data);

        govt_scheme.setAdapter(scheme_adpter);

        govt_scheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (govtscheme_data.get(i).equalsIgnoreCase("Yes")) {
                    inputlayout_schemes.setVisibility(View.VISIBLE);
                    applysceme.setVisibility(View.GONE);
                } else {
                    inputlayout_schemes.setVisibility(View.GONE);
                    applysceme.setVisibility(View.VISIBLE);
                }
            }
        });


        ArrayAdapter<String> organic_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                govtscheme_data);

        organic_former.setAdapter(organic_adpter);

        organic_former.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (govtscheme_data.get(i).equalsIgnoreCase("Yes")) {
                    inputlayout_certificate.setVisibility(View.VISIBLE);
                    applycertificate.setVisibility(View.GONE);
                } else {
                    inputlayout_certificate.setVisibility(View.GONE);
                    applycertificate.setVisibility(View.VISIBLE);
                }
            }
        });


        soilcard_data = new ArrayList<>();
        soilcard_data.add("Yes");
        soilcard_data.add("No");
        ArrayAdapter<String> soilcard_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                soilcard_data);

        soil_card.setAdapter(soilcard_adpter);


        soil_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (soilcard_data.get(i).equalsIgnoreCase("Yes")) {
                    inputlayout_card.setVisibility(View.VISIBLE);
                    applysoiltest.setVisibility(View.GONE);
                } else {
                    inputlayout_card.setVisibility(View.GONE);
                    applysoiltest.setVisibility(View.VISIBLE);
                }
            }
        });


//        ArrayAdapter<String> certificate_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
//                soilcard_data);
//
//        organic_former.setAdapter(certificate_adpter);
//
//
//        organic_former.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (soilcard_data.get(i).equalsIgnoreCase("Yes")) {
//
//                } else {
//
//                }
//            }
//        });

        applyeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_power("EB");
            }
        });
        applysolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_power("Solar");
            }
        });
        applyborewell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_power("Borewell");
            }
        });

        applysceme.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                apply_power("Scheme");
            }
        });


        applycertificate.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                apply_power("Organic");
            }
        });

        applysoiltest.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                apply_power("Soiltest");
            }
        });


        Soiltype_data = new ArrayList<>();
        Soiltype_data.add("Sand");
        Soiltype_data.add("Loamy sand");
        Soiltype_data.add("Sandy loam");
        Soiltype_data.add("Loam");
        Soiltype_data.add("Silt loam");
        Soiltype_data.add("Silt");
        Soiltype_data.add("Sandy clay loam");
        Soiltype_data.add("Clay loam");
        Soiltype_data.add("Silt clay loam");
        Soiltype_data.add("Sandy clay");
        Soiltype_data.add("Silty clay");
        Soiltype_data.add("Clay");
        ArrayAdapter<String> adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                Soiltype_data);

        watersource_data = new ArrayList<>();
        watersource_data.add("Borewell");
        watersource_data.add("Tubewell");
        watersource_data.add("Openwell");
        watersource_data.add("Reserve");
        watersource_data.add("Channel");
        ArrayAdapter<String> watersource_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                watersource_data);

        water_source.setAdapter(watersource_adpter);

        watertype_data = new ArrayList<>();
        watertype_data.add("Saline");
        watertype_data.add("Normal");
        ArrayAdapter<String> watertype_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                watertype_data);

        water_type.setAdapter(watertype_adpter);

        water_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//

                if (watersource_data.get(i).equalsIgnoreCase("Borewell")) {
                    applyborewell.setVisibility(View.GONE);

                } else {
                    applyborewell.setVisibility(View.VISIBLE);


                }
            }
        });


        landdime_data = new ArrayList<>();
        landdime_data.add("Hectare");
        landdime_data.add("Acre");

        ArrayAdapter<String> landdime_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                landdime_data);

        land_unit.setAdapter(landdime_adpter);

        powersource_data = new ArrayList<>();
        powersource_data.add("None");
        powersource_data.add("EB");
        powersource_data.add("Solar");
        powersource_data.add("Both");

        ArrayAdapter<String> powerype_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                powersource_data);

        power_source.setAdapter(powerype_adpter);

        power_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (powersource_data.get(i).equalsIgnoreCase("None")) {
                    applyeb.setVisibility(View.VISIBLE);
                    applysolar.setVisibility(View.VISIBLE);
                } else if (powersource_data.get(i).equalsIgnoreCase("EB")) {
                    applyeb.setVisibility(View.GONE);
                    applysolar.setVisibility(View.VISIBLE);

                } else if (powersource_data.get(i).equalsIgnoreCase("Solar")) {
                    applyeb.setVisibility(View.VISIBLE);
                    applysolar.setVisibility(View.GONE);
                } else {
                    applyeb.setVisibility(View.GONE);
                    applysolar.setVisibility(View.GONE);
                }
            }
        });

//        power_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (adapterView.getSelectedItem().toString().equalsIgnoreCase("None")){
//                    applyeb.setVisibility(View.VISIBLE);
//                    applysolar.setVisibility(View.VISIBLE);
//                } else {
//                    applyeb.setVisibility(View.GONE);
//                    applysolar.setVisibility(View.GONE);
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        sm = new SessionManager(getActivity());
        viewDialog = new ViewDialog(getActivity());
        add_field = root_view.findViewById(R.id.add_field);
        soil_type.setAdapter(adpter);
        soil_type.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        );


        add_field.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (validatesignForm()) {

                    if (NetworkDetector.isNetworkStatusAvialable(getActivity())) {
                        add_land();
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        return root_view;
    }


    private boolean validatesignForm() {
        if (!(land_name.getText().toString().length() > 2)) {
            land_name.setError(getString(R.string.required));
            return false;
        } else if (!(landdime.getText().toString().length() > 0)) {
            landdime.setError(getString(R.string.required_date));
            return false;
        } else if (!(land_unit.getText().toString().length() > 2)) {
            Toast.makeText(getActivity(), R.string.selectunit, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public void add_land() {


        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.addland)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("land_name", land_name.getText().toString())
                .addUrlEncodeFormBodyParameter("soil_type", soil_type.getText().toString())
                .addUrlEncodeFormBodyParameter("dimension", landdime.getText().toString())
                .addUrlEncodeFormBodyParameter("unit", land_unit.getText().toString())
                .addUrlEncodeFormBodyParameter("power_source", power_source.getText().toString())
                .addUrlEncodeFormBodyParameter("water_source", water_source.getText().toString())
                .addUrlEncodeFormBodyParameter("water_type", water_type.getText().toString())
                .addUrlEncodeFormBodyParameter("pincode", pincode.getText().toString())
                .addUrlEncodeFormBodyParameter("panchayat_office", panchayat.getText().toString())
                .addUrlEncodeFormBodyParameter("current_location", current_location.getText().toString())
                .addUrlEncodeFormBodyParameter("government_scheme", govt_scheme.getText().toString())
                .addUrlEncodeFormBodyParameter("soil_healthcard", soil_card.getText().toString())
                .addUrlEncodeFormBodyParameter("certified_organic_former", organic_former.getText().toString())
                .addUrlEncodeFormBodyParameter("scheme_year", years_scheme.getText().toString())
                .addUrlEncodeFormBodyParameter("healthcard_date", years_soilreport.getText().toString())
                .addUrlEncodeFormBodyParameter("certificate", certification_no.getText().toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        land_name.setText("");
                        soil_type.setText("");
                        landdime.setText("");
                        land_unit.setText("");
                        power_source.setText("");
                        water_source.setText("");
                        water_type.setText("");
                        pincode.setText("");
                        panchayat.setText("");
                        current_location.setText("");
                        govt_scheme.setText("");
                        soil_card.setText("");
                        organic_former.setText("");
                        years_scheme.setText("");
                        years_soilreport.setText("");
                        certification_no.setText("");
                        applyborewell.setVisibility(View.GONE);
                        applycertificate.setVisibility(View.GONE);
                        applyeb.setVisibility(View.GONE);
                        applysceme.setVisibility(View.GONE);
                        applysoiltest.setVisibility(View.GONE);
                        applysolar.setVisibility(View.GONE);
                        inputlayout_schemes.setVisibility(View.GONE);
                        inputlayout_card.setVisibility(View.GONE);
                        inputlayout_certificate.setVisibility(View.GONE);
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


                try {
                    JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                    Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void apply_power(String apply) {


        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.apply)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("apply_for", apply)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

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

    public void hidesoftkeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void openkeyboard(final EditText field) {
        field.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(field, 0);
            }
        }, 200);
    }

//    private void toggle() {
//        Transition transition = new Fade();
//        transition.setDuration(600);
//        transition.addTarget(R.id.image);
//
//        TransitionManager.beginDelayedTransition(parent, transition);
//        image.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
}
