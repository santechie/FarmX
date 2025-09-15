package com.ascentya.AsgriV2.Mycrops_Mainfragments.Financesub_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Activitycompleted_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Member_Edit;
import com.ascentya.AsgriV2.Models.Income_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Myexpenses extends Fragment {
    View root_view;
    RecyclerView members_recycler;
    LinearLayout addmember_layout, memberslisting_layout;
    ElasticImageView add_members;
    Activitycompleted_Adapter adapter;
    List<Income_Model> Data;
    Spinner payment_type;
    EditText payment_title, payment_date, total_amount, nextpayment_date;
    ElasticButton addpayment;
    SessionManager sm;
    TextView memberslisting;

    ViewDialog viewDialog;
    Calendar cal;

    @Override
    public void onResume() {
        getmembers();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.expense_layout, container, false);
        members_recycler = root_view.findViewById(R.id.members_recycler);
        members_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        members_recycler.setHasFixedSize(true);
        viewDialog = new ViewDialog(getActivity());

        sm = new SessionManager(getActivity());


        return root_view;
    }

    public void getmembers() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.add_completedactivitylist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Income_Model obj = new Income_Model(jsonArray.getJSONObject(i).optString("activity_id"), jsonArray.getJSONObject(i).optString("activity_land"), jsonArray.getJSONObject(i).optString("activity_crop"), jsonArray.getJSONObject(i).optString("activity"), jsonArray.getJSONObject(i).optString("activity_resources"), jsonArray.getJSONObject(i).optString("activity_start"), jsonArray.getJSONObject(i).optString("activity_end"), jsonArray.getJSONObject(i).optString("members"), jsonArray.getJSONObject(i).optString("machines"), jsonArray.getJSONObject(i).optString("animals"), jsonArray.getJSONObject(i).optString("others"));

                            Data.add(obj);
                        }

                        adapter = new Activitycompleted_Adapter(getActivity(), Data, viewDialog, new Member_Edit() {
                            @Override
                            public void update_member() {
                                getmembers();
                            }
                        });

                        members_recycler.setAdapter(adapter);

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    private boolean validateForm() {
        if (!(payment_title.getText().toString().length() > 2)) {
            payment_title.setError(getString(R.string.required));
            return false;
        } else if (!(payment_date.getText().toString().length() > 1)) {
            payment_date.setError(getString(R.string.required_date));
            return false;
        } else if (!(total_amount.getText().toString().length() > 0)) {
            total_amount.setError(getString(R.string.required_date));
            return false;
        } else if (!(nextpayment_date.getText().toString().length() > 0)) {
            nextpayment_date.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

    public void add_income() {
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.addexpense)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("expenses_title", payment_title.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_type", payment_type.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("expenses_dated", payment_date.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_totlamount", total_amount.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_next_payment_data", nextpayment_date.getText().toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        payment_title.setText("");
                        payment_date.setText("");
                        total_amount.setText("");
                        nextpayment_date.setText("");
                        payment_type.setSelection(0);

                        Toasty.success(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        getmembers();


                        memberslisting_layout.setVisibility(View.VISIBLE);
                        addmember_layout.setVisibility(View.GONE);

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

    public void hidesoftkeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
