package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.Memberamount_Adapter;
import com.ascentya.AsgriV2.Adapters.Others_Adapter;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.Models.others_model;
import com.ascentya.AsgriV2.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Activity_Details_DIalog {

    String user_id;
    TextView activity_type, title_dialog, activity_done, activity_start, contract_type, activity_end, activity_total, machine_type, activity_vendarname;
    Activity_Cat_Model Data_R;
    Dialog dialog;
    List<AddMemberamount_Model> addmemberamount;
    List<others_model> addmachineamount;
    RecyclerView member_machine;
    Memberamount_Adapter member_adapter;
    Others_Adapter other_adapter;

    public void dialog(final Context context, String title, final Activity_Cat_Model data) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.user_id = user_id;
        this.Data_R = data;
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.activity_details_dialog, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        activity_type = dialog.findViewById(R.id.activity_type);
        activity_done = dialog.findViewById(R.id.activity_done);
        activity_start = dialog.findViewById(R.id.activity_start);
        activity_end = dialog.findViewById(R.id.activity_end);
        activity_total = dialog.findViewById(R.id.activity_total);
        machine_type = dialog.findViewById(R.id.machine_type);
        title_dialog = dialog.findViewById(R.id.title);
        activity_vendarname = dialog.findViewById(R.id.activity_vendarname);
        contract_type = dialog.findViewById(R.id.contract_type);
        member_machine = dialog.findViewById(R.id.member_machine);
        member_machine.setLayoutManager(new LinearLayoutManager(context));
        member_machine.setHasFixedSize(true);
        title_dialog.setText(title);


//

        if (Data_R.getContract_type().trim().equalsIgnoreCase("Labour")) {
            try {
                JSONArray mainObject = new JSONArray(Data_R.getMember_id());
                addmemberamount = new ArrayList<>();

                for (int i = 0; i < mainObject.length(); i++) {

                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setId(mainObject.getJSONObject(i).optString("id"));
                    obj.setName(mainObject.getJSONObject(i).optString("name"));
                    obj.setAmount(mainObject.getJSONObject(i).optString("amount"));
                    obj.setBillingtype(mainObject.getJSONObject(i).optString("billingtype"));
                    obj.setHours(mainObject.getJSONObject(i).optString("hours"));
                    addmemberamount.add(obj);

                }

                checkHeader(alertDialogView, mainObject.length() == 0);

                if (mainObject.length() != 0) {
                    member_adapter = new Memberamount_Adapter(context, addmemberamount, true);
                    member_machine.setAdapter(member_adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (Data_R.getContract_type().trim().equalsIgnoreCase("Material")) {
            try {
                JSONArray mainObject = new JSONArray(Data_R.getMaterial_name());
                addmachineamount = new ArrayList<>();

                for (int i = 0; i < mainObject.length(); i++) {

                    others_model obj = new others_model();
                    obj.setTitle(mainObject.getJSONObject(i).optString("title"));
                    obj.setAmount(mainObject.getJSONObject(i).optString("amount"));
                    addmachineamount.add(obj);

                }

                checkHeader(alertDialogView, mainObject.length() == 0);

                if (mainObject.length() != 0) {
                    other_adapter = new Others_Adapter(context, addmachineamount, true);
                    member_machine.setAdapter(other_adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            checkHeader(alertDialogView, true);
        }


        activity_type.setText(Data_R.getPrepare_type());
        activity_done.setText(Data_R.getCrop_type());

        if (Data_R.getStart_date().equalsIgnoreCase("")) {
            activity_start.setText("NA");

        } else {
            activity_start.setText(Data_R.getStart_date());

        }
        if (Data_R.getVendor_name().equalsIgnoreCase("")) {
            activity_vendarname.setText("NA");

        } else {
            activity_vendarname.setText(Data_R.getVendor_name());

        }

        activity_end.setText(Data_R.getEnd_date());


        activity_total.setText(Data_R.getTotal_amount());
        machine_type.setText(Data_R.getEquipment_type());
        contract_type.setText(Data_R.getContract_type());

        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private void checkHeader(View view, boolean isEmpty){
        System.out.println("Check Header: " + isEmpty);
        view.findViewById(R.id.header_others).setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

}
