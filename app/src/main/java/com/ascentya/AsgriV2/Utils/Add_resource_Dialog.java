package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.add_resource;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.Models.Members_Model;
import com.ascentya.AsgriV2.R;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Add_resource_Dialog {


    List<String> genderdata;
    String memeer_id, user_id;
    LabeledSwitch memberswitch;
    EditText member_name, member_age, aadhar, hourscost, payment, hoursworked;
    NoDefaultSpinner gender;
    add_resource Data_R;
    Dialog dialog;

    public void dialog(final Context context, List<Members_Model> Data, String user_id, String title, final add_resource data) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.user_id = user_id;
        this.Data_R = data;
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.activityaddresource_layout, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//         dialog.setContentView(R.layout.activityaddresource_layout);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        genderdata = new ArrayList<>();
        TextView title_head = (TextView) dialog.findViewById(R.id.title);
        title_head.setText("Add a " + title);

        final AutoCompleteTextView member = (AutoCompleteTextView) dialog.findViewById(R.id.member);
        hourscost = (EditText) dialog.findViewById(R.id.hourscost);
        hoursworked = (EditText) dialog.findViewById(R.id.hoursworked);
        payment = (EditText) dialog.findViewById(R.id.payment);
        memberswitch = (LabeledSwitch) dialog.findViewById(R.id.memberswitch);
        member_name = dialog.findViewById(R.id.member_name);
        member_age = dialog.findViewById(R.id.member_age);
        gender = dialog.findViewById(R.id.gender);
        aadhar = dialog.findViewById(R.id.aadhar);
        member.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                member.showDropDown();
                return false;
            }
        });
        final LinearLayout nonmember_layout = (LinearLayout) dialog.findViewById(R.id.nonmember_layout);

        final View member_view = (View) dialog.findViewById(R.id.member_view);
        genderdata.add("Male");
        genderdata.add("Female");
        genderdata.add("Others");

        ArrayAdapter<String> gender_adpter = new ArrayAdapter(context, R.layout.spinner_item,
                genderdata);

        gender.setAdapter(gender_adpter);

        memberswitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    nonmember_layout.setVisibility(View.GONE);
                    member.setVisibility(View.VISIBLE);
                    member_view.setVisibility(View.VISIBLE);
                } else {
                    nonmember_layout.setVisibility(View.VISIBLE);
                    member.setVisibility(View.GONE);
                    member_view.setVisibility(View.GONE);
                }
            }
        });
//        final TextInputLayout resource_hint = (TextInputLayout) dialog.findViewById(R.id.resource_hint);
//        resource_hint.setHint("Select " + title);
        ElasticButton add = (ElasticButton) dialog.findViewById(R.id.add);
        add.setText("Save");
        hourscost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (hourscost.getText().length() > 0 && hoursworked.getText().length() > 0) {

                    Integer total = Integer.parseInt(hourscost.getText().toString()) * Integer.parseInt(hoursworked.getText().toString());
                    payment.setText(String.valueOf(total));
                } else {
                    payment.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        hoursworked.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (hourscost.getText().length() > 0 && hoursworked.getText().length() > 0) {


                    Integer total = Integer.parseInt(hourscost.getText().toString()) * Integer.parseInt(hoursworked.getText().toString());
                    payment.setText(String.valueOf(total));
                } else {
                    payment.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter<String> crop_adpter = new ArrayAdapter(context, R.layout.spinner_item,
                Data);

        member.setAdapter(crop_adpter);

        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Members_Model selected = (Members_Model) adapterView.getAdapter().getItem(i);
                memeer_id = selected.getMember_id();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hourscost.getText().length() > 0) {

                    if (hoursworked.getText().length() > 0) {
                        if (payment.getText().length() > 0) {

                            if (memberswitch.isOn()) {

                                if (member.getText().length() > 0) {
                                    AddMemberamount_Model obj = new AddMemberamount_Model();

                                    obj.setName(member.getText().toString());
                                    obj.setId(memeer_id);
                                    obj.setAmount(payment.getText().toString());
                                    obj.setBillingtype(hoursworked.getText().toString());
                                    obj.setHours(hourscost.getText().toString());

                                    Data_R.crop_suggest(obj);

                                    dialog.cancel();
                                } else {
                                    Toast.makeText(context, R.string.selectmember, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (member_name.getText().toString().length() >= 3) {
                                    if (member_age.getText().toString().length() == 2) {

                                        if (aadhar.getText().toString().length() == 12) {
                                            if (gender != null && gender.getSelectedItem() != null) {
                                                add_land(member_name.getText().toString(), gender.getSelectedItem().toString(), member_age.getText().toString(), aadhar.getText().toString());

                                            } else {
                                                Toast.makeText(context, "Select gender", Toast.LENGTH_SHORT).show();
                                            }


                                        } else {
                                            aadhar.setError(context.getString(R.string.required_date));

                                        }

                                    } else {
                                        member_age.setError(context.getString(R.string.required_date));
                                    }

                                } else {
                                    member_name.setError(context.getString(R.string.required_date));
                                }
                            }


                        } else {
                            payment.setError(context.getString(R.string.required_date));
                        }
                    } else {
                        hoursworked.setError(context.getString(R.string.required_date));
                    }

                } else {
                    hourscost.setError(context.getString(R.string.required_date));
                }


            }
        });


        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


    }

    public void add_land(String m_name, String m_gender, String m_age, String m_aadhar) {


        AndroidNetworking.post(Webservice.addmember)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("member_name", m_name)
                .addUrlEncodeFormBodyParameter("member_gender", m_gender)
                .addUrlEncodeFormBodyParameter("member_age", m_age)
                .addUrlEncodeFormBodyParameter("member_relation", "")
                .addUrlEncodeFormBodyParameter("member_experience", "")
                .addUrlEncodeFormBodyParameter("member_payment", "")
                .addUrlEncodeFormBodyParameter("member_bilingtype", "")
                .addUrlEncodeFormBodyParameter("member_aadhar", m_aadhar)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        AddMemberamount_Model obj = new AddMemberamount_Model();


                        obj.setName(member_name.getText().toString());
                        obj.setId(jsonObject.optString("data"));
                        obj.setAmount(payment.getText().toString());
                        obj.setBillingtype(hoursworked.getText().toString());
                        obj.setHours(hourscost.getText().toString());

                        Data_R.crop_suggest(obj);

                        dialog.cancel();
//                        member_name.setText("");
//
//                        member_age.setText("");
//
//                        aadhar.setText("");
//
//                        genderdata.add("Male");
//                        genderdata.add("Female");
//                        genderdata.add("Others");
//
//                        ArrayAdapter<String> gender_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
//                                genderdata);
//
//                        gender.setAdapter(gender_adpter);
//
//
//
//                        Toasty.success(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    } else {

//                        Toasty.error(, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


                try {
                    JSONObject jsonObject = new JSONObject(anError.getErrorBody());
//                    Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

}
