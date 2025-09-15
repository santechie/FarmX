package com.ascentya.AsgriV2.e_market.activities;

import androidx.appcompat.widget.AppCompatButton;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.e_market.data.model.Address;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class AddressActivity extends BaseActivity {

    public static final String ADDRESS = "address";
    private Address address;

    private TextInputEditText doorNoEt, landmarkEt, streetEt, areaEt, pincodeEt;
    private MaterialAutoCompleteTextView citySp, districtSp, stateSp;
    private AppCompatButton saveBtn;

    private String selectedState, selectedCity, selectedDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        address = getFromIntent(ADDRESS, Address.class);
        setToolbarTitle((address == null ? "Add" : "Edit") + " Address", true);

        doorNoEt = findViewById(R.id.doorNumber);
        landmarkEt = findViewById(R.id.landmark);
        streetEt = findViewById(R.id.street);
        areaEt = findViewById(R.id.area);
        pincodeEt = findViewById(R.id.pincode);

        citySp = findViewById(R.id.city);
        districtSp = findViewById(R.id.district);
        stateSp = findViewById(R.id.state);

        saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(view -> validateAndSave());
        setAdapters();
        setItemListeners();
        updateUI();
    }

    private void setAdapters(){
        citySp.setAdapter(getAdapter("Chennai", "Madurai", "Trichy", "Coimbatore"));
        districtSp.setAdapter(getAdapter("Ramnathapuram", "Madurai", "Theni", "Rameswaram"));
        stateSp.setAdapter(getAdapter("Tamilnadu", "Kerala", "Andra", "Telangana"));
    }

    private void setItemListeners(){
        citySp.setOnItemClickListener((adapterView, view, i, l) -> selectedCity = (String) adapterView.getItemAtPosition(i));
        stateSp.setOnItemClickListener((adapterView, view, i, l) -> selectedState = (String) adapterView.getItemAtPosition(i));
        districtSp.setOnItemClickListener((adapterView, view, i, l) -> selectedDistrict = (String) adapterView.getItemAtPosition(i));
    }

    private void updateUI(){
        if(address == null)
            return;

        doorNoEt.setText(address.getDoorNumber());
        landmarkEt.setText(address.getLandmark());
        streetEt.setText(address.getStreet());
        areaEt.setText(address.getArea());
        pincodeEt.setText(address.getPincode());
        
        citySp.setText(address.getCity());
        selectedCity = address.getCity();
//        citySp.setListSelection(getItemPosition(address.getCity(), citySp));
        districtSp.setText(address.getDistrict());
        selectedDistrict = address.getDistrict();
//        districtSp.setListSelection(getItemPosition(address.getDistrict(), districtSp));
        stateSp.setText(address.getState());
        selectedState = address.getState();
//        stateSp.setListSelection(getItemPosition(address.getState(), stateSp));

    }

    private void validateAndSave(){
        if (!ValidateInputs.isValidInput(getStringFrom(doorNoEt))){
           doorNoEt.setError("Enter valid Door / Flat Number!");
        }else if (!ValidateInputs.isValidInput(getStringFrom(landmarkEt))){
            landmarkEt.setError("Enter valid Landmark!");
        }else if (!ValidateInputs.isValidInput(getStringFrom(streetEt))){
            streetEt.setError("Enter valid Street!");
        }else if (!ValidateInputs.isValidInput(getStringFrom(areaEt))){
            areaEt.setError("Enter valid Area!");
        }else if (!ValidateInputs.isValidPincode(getStringFrom(pincodeEt))){
            pincodeEt.setError("Enter valid Pincode!");
        }else if (!ValidateInputs.isValidInput(selectedCity)){
            citySp.setError("Select City!");
        }else if (!ValidateInputs.isValidInput(selectedDistrict)){
            districtSp.setError("Select District!");
        }else if (!ValidateInputs.isValidInput(selectedState)){
            stateSp.setError("Select State!");
        }else {
            Address newAddress = address == null ? new Address() : address;
            newAddress.setDoorNumber(getStringFrom(doorNoEt));
            newAddress.setLandmark(getStringFrom(landmarkEt));
            newAddress.setStreet(getStringFrom(streetEt));
            newAddress.setArea(getStringFrom(areaEt));
            newAddress.setPincode(getStringFrom(pincodeEt));
            newAddress.setCity(selectedCity);
            newAddress.setDistrict(selectedDistrict);
            newAddress.setState(selectedState);
            saveOrUpdate(newAddress);
        }
    }

    private void saveOrUpdate(Address newAddress){
        if (address == null){
            storage.saveAddress(newAddress);
            Toasty.normal(this, "Address Added!").show();
        }else {
            storage.updateAddress(newAddress);
            Toasty.normal(this, "Address Updated!").show();
        }
        finish();
    }

    private String getStringFrom(TextInputEditText textInputEditText){
        return textInputEditText.getText().toString().trim();
    }

    private int getItemPosition(String item, MaterialAutoCompleteTextView textView){
        for (int i=0; i<textView.getAdapter().getCount(); i++){
            if (item.equals(textView.getAdapter().getItem(i))){
                return i;
            }
        }
        return 0;
    }

    private ArrayAdapter<String> getAdapter(String... data){
        return new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data);
    }
}