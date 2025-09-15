package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Member_Edit;
import com.ascentya.AsgriV2.Models.Members_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Members_Adapter extends RecyclerView.Adapter<Members_Adapter.ViewHolder> {

    private List<Members_Model> items = new ArrayList<>();
    private Context ctx;
    private Member_Edit listener;
    ViewDialog dialog_loader;
    Dialog dialog;
    Spinner member_gender;
    String user_id;
    AutoCompleteTextView gender, relation, payment, billingtype;
    List<String> genderdata, relationdata, paymentdata, billingtypedata;
    TextInputEditText member_name, member_age, member_exp;
    ElasticButton addmember;
    Action action;


    public Members_Adapter(Context context, Action action, List<Members_Model> items, ViewDialog dialog_loader, String user_id,Member_Edit listener) {
        this.items = items;
        this.dialog_loader = dialog_loader;
        this.listener = listener;
        ctx = context;
        this.user_id = user_id;
        this.action = action;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView member_name, member_address, create_at;
        ElasticFloatingActionButton edit_layout, delete_layout;

        public ViewHolder(View v) {
            super(v);
            member_name = (TextView) v.findViewById(R.id.member_name);
            create_at = (TextView) v.findViewById(R.id.create_at);
            member_address = (TextView) v.findViewById(R.id.member_address);
            edit_layout = (ElasticFloatingActionButton) v.findViewById(R.id.edit_layout);
            delete_layout = (ElasticFloatingActionButton) v.findViewById(R.id.delete_layout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.member_name.setText(items.get(position).getMember_name());
            view.create_at.setText(items.get(position).getCreated_at());


            if (items.get(position).getRelation().trim().equalsIgnoreCase("")) {
                if (items.get(position).getMember_age().trim().equalsIgnoreCase("")) {
                    view.member_address.setText(items.get(position).getMember_gender());

                } else {
                    view.member_address.setText(items.get(position).getMember_age() + "," + items.get(position).getMember_gender());

                }

            } else if (items.get(position).getMember_age().trim().equalsIgnoreCase("")) {

                if (items.get(position).getMember_gender().trim().equalsIgnoreCase("")) {
                    view.member_address.setText(items.get(position).getRelation());

                } else {
                    view.member_address.setText(items.get(position).getRelation() + "," + items.get(position).getMember_gender());

                }


            } else {
                view.member_address.setText(items.get(position).getRelation() + "," + items.get(position).getMember_age() + "," + items.get(position).getMember_gender());

            }
//            view.member_address.setText(items.get(position).getRelation() + "," + items.get(position).getMember_age() + "," + items.get(position).getMember_gender());
//            view.field_address.setText("This farm is located @ - "+items.get(position).getLand_location());
            view.delete_layout.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    if (action.canDelete())
                        delete_land(items.get(position).getMember_id(), user_id, position );
                }
            });

            view.edit_layout.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    dialog = new Dialog(ctx, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.editmember_layout);

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();

                    genderdata = new ArrayList<>();
                    relationdata = new ArrayList<>();
                    paymentdata = new ArrayList<>();
                    billingtypedata = new ArrayList<>();


                    member_exp = dialog.findViewById(R.id.member_exp);
                    member_age = dialog.findViewById(R.id.member_age);
                    member_name = dialog.findViewById(R.id.member_name);
                    relation = dialog.findViewById(R.id.relation);
                    gender = dialog.findViewById(R.id.gender);
                    payment = dialog.findViewById(R.id.payment);
                    billingtype = dialog.findViewById(R.id.billingtype);
                    addmember = dialog.findViewById(R.id.addmember);

                    member_exp.setText(items.get(position).getFarming_exp());
                    gender.setText(items.get(position).getMember_gender());
                    member_age.setText(items.get(position).getMember_age());
                    member_name.setText(items.get(position).getMember_name());
                    relation.setText(items.get(position).getRelation());
                    payment.setText(items.get(position).getMember_payment());
                    billingtype.setText(items.get(position).getMember_bilingtype());


                    genderdata.add("Male");
                    genderdata.add("Female");
                    genderdata.add("Others");


                    ArrayAdapter<String> gender_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            genderdata);

                    gender.setAdapter(gender_adpter);

                    relationdata.add("Brother");
                    relationdata.add("Sister");
                    relationdata.add("Father");
                    relationdata.add("Mother");
                    relationdata.add("Uncle");
                    relationdata.add("Aunty");
                    relationdata.add("Wife");
                    relationdata.add("Nephew");
                    relationdata.add("Niece");

                    ArrayAdapter<String> relation_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            relationdata);

                    relation.setAdapter(relation_adpter);

                    paymentdata.add("Yes");
                    paymentdata.add("No");

                    ArrayAdapter<String> payment_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            paymentdata);

                    payment.setAdapter(payment_adpter);

                    billingtypedata.add("Hour");
                    billingtypedata.add("Day");

                    ArrayAdapter<String> billing_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            billingtypedata);

                    billingtype.setAdapter(billing_adpter);


                    addmember.setOnClickListener(new DebouncedOnClickListener(1500) {
                        @Override
                        public void onDebouncedClick(View v) {


                            if (validateForm()) {

                                update_land(items.get(position).getMember_id());
                            }

                        }
                    });


                }
            });

            if (!action.showDelete()) view.delete_layout.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void delete_land(String id, String userId, final Integer position) {
        dialog_loader.showDialog();
        AndroidNetworking.post(Webservice.deletemember)
                .addUrlEncodeFormBodyParameter("member_id", id)
                .addUrlEncodeFormBodyParameter("user_id", userId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                dialog_loader.hideDialog();
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        removeAt(position);
                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }

                    action.update();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                dialog_loader.hideDialog();

            }
        });
    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    private boolean validateForm() {
        if (!(member_name.getText().toString().length() > 2)) {
            member_name.setError(ctx.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }


    public void update_land(String id) {
        dialog_loader.showDialog();


        AndroidNetworking.post(Webservice.updatemember)
                .addUrlEncodeFormBodyParameter("member_id", id)
                .addUrlEncodeFormBodyParameter("member_name", member_name.getText().toString())
                .addUrlEncodeFormBodyParameter("member_gender", gender.getText().toString())
                .addUrlEncodeFormBodyParameter("member_age", member_age.getText().toString())
                .addUrlEncodeFormBodyParameter("member_relation", relation.getText().toString())
                .addUrlEncodeFormBodyParameter("member_experience", member_exp.getText().toString())
                .addUrlEncodeFormBodyParameter("member_payment", payment.getText().toString())
                .addUrlEncodeFormBodyParameter("member_bilingtype", billingtype.getText().toString())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        listener.update_member();
                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                        action.update();
                    } else {

                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {
                    dialog_loader.hideDialog();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                dialog_loader.hideDialog();


            }
        });
    }

    public interface Action{
        void update();
        boolean showDelete();
        boolean canDelete();
    }
}
