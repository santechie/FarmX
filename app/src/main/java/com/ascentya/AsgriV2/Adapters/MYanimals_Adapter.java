package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Crops_Edit;
import com.ascentya.AsgriV2.Models.Myanimals_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class MYanimals_Adapter extends RecyclerView.Adapter<MYanimals_Adapter.ViewHolder> {

    private List<Myanimals_Model> items = new ArrayList<>();
    private Context ctx;
    ViewDialog dialog_loader;
    Dialog dialog;
    AppCompatAutoCompleteTextView breed, animal, gender;
    CheckBox prediseases;
    TextInputEditText animal_name, animal_age, diseasdisc;

    List<String> animaldata, breeddata, genderdata;
    ElasticButton addanimal;
    TextInputLayout diseas_layout;


    SessionManager sm;

    List<String> member_data;
    List<String> land_data;
    List<String> animal_data;
    List<String> predisease_data;
    Crops_Edit crop_listner;

    Calendar cal;
    CheckBox remainder;

    public MYanimals_Adapter(Context context, List<Myanimals_Model> items, ViewDialog dialog_loader, Crops_Edit crop_listner, SessionManager sm) {
        this.items = items;
        this.dialog_loader = dialog_loader;
        this.crop_listner = crop_listner;
        this.sm = sm;
        ctx = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView field_name, field_dimon, date_time;
        ElasticFloatingActionButton delete, edit;

        public ViewHolder(View v) {
            super(v);
            field_name = (TextView) v.findViewById(R.id.field_title);
            field_dimon = (TextView) v.findViewById(R.id.field_dimon);
            date_time = (TextView) v.findViewById(R.id.date_time);

            delete = (ElasticFloatingActionButton) v.findViewById(R.id.delete);
            edit = (ElasticFloatingActionButton) v.findViewById(R.id.edit);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycultivation_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
//
            view.field_name.setText(items.get(position).getAnimal_petname());
            view.field_dimon.setText(items.get(position).getAnimal_name());
            view.date_time.setText(items.get(position).getCreated_at());
//            view.field_address.setText(items.get(position).getAnimal_age());


//            view.member_address.setText(items.get(position).getRelation() + "," + items.get(position).getMember_age() + "," + items.get(position).getMember_gender());
//            view.field_address.setText("This farm is located @ - "+items.get(position).getLand_location());
//            view.delete_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    delete_land(items.get(position).getMember_id(), position);
//
//                }
//            });
//

            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {

                    delete_animal(items.get(position).getAnimal_id());
                }
            });
//            view.edit.setOnClickListener(new DebouncedOnClickListener(1500) {
//                @Override
//                public void onDebouncedClick(View v) {
//                    LayoutInflater factory = LayoutInflater.from(ctx);
//                    final View alertDialogView = factory.inflate(R.layout.editanimals_layout, null);
//                    dialog = new Dialog(ctx, R.style.DialogSlideAnim);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    dialog.setContentView(alertDialogView);
//
//                    dialog.setCanceledOnTouchOutside(true);
//                    dialog.setCancelable(true);
//                    dialog.show();
//
//                    animal_age = dialog.findViewById(R.id.animal_age);
//                    animal_name = dialog.findViewById(R.id.animal_name);
//                    breed = dialog.findViewById(R.id.breed);
//                    animal = dialog.findViewById(R.id.animal);
//                    diseasdisc = dialog.findViewById(R.id.diseasdisc);
//                    prediseases = dialog.findViewById(R.id.prediseases);
//                    gender = dialog.findViewById(R.id.gender);
//                    diseas_layout = dialog.findViewById(R.id.diseas_layout);
//
//
//                    animal_age.setText(items.get(position).getAnimal_age());
//                    animal_name.setText(items.get(position).getAnimal_petname());
//                    breed.setText(items.get(position).getAnimal_breed());
//                    animal.setText(items.get(position).getAnimal_name());
//                    if (items.get(position).getAnimal_prediseases().equalsIgnoreCase("yes")) {
//                        prediseases.setChecked(true);
//                        diseas_layout.setVisibility(View.VISIBLE);
//                        diseasdisc.setText(items.get(position).getDiseases_disc());
//
//                    } else {
//                        diseas_layout.setVisibility(View.GONE);
//                        prediseases.setChecked(false);
//
//                    }
//
//                    prediseases.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                            if (b) {
//                                diseas_layout.setVisibility(View.VISIBLE);
//                            } else {
//                                diseas_layout.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                    gender.setText(items.get(position).getAnimal_gender());
//
//
//                    animaldata = new ArrayList<>();
//                    breeddata = new ArrayList<>();
//                    genderdata = new ArrayList<>();
//                    genderdata.add("Male");
//                    genderdata.add("Female");
//                    genderdata.add("Others");
//
//                    ArrayAdapter<String> gender_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
//                            genderdata);
//
//                    gender.setAdapter(gender_adpter);
//
//                    animaldata.add("Dog");
//                    animaldata.add("Cat");
//                    animaldata.add("Cow");
//
//                    ArrayAdapter<String> animal_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
//                            animaldata);
//
//                    animal.setAdapter(animal_adpter);
//
//                    breeddata = new ArrayList<>();
//                    breeddata.add("Hybrid");
//                    breeddata.add("Normal");
//
//                    ArrayAdapter<String> breed_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
//                            breeddata);
//
//                    breed.setAdapter(breed_adpter);
//
//                    addanimal = dialog.findViewById(R.id.addanimal);
//                    addanimal.setOnClickListener(new DebouncedOnClickListener(1500) {
//                        @Override
//                        public void onDebouncedClick(View v) {
//                            add_animal(items.get(position).getAnimal_id());
//                        }
//                    });
//
//
//                }
//            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void add_animal(String id) {
        dialog_loader.showDialog();
        String prediseased_text;
        if (prediseases.isChecked()) {
            prediseased_text = "Yes";
        } else {
            prediseased_text = "No";
        }


        AndroidNetworking.post(Webservice.updateanimal)
                .addUrlEncodeFormBodyParameter("animal_id", id)
                .addUrlEncodeFormBodyParameter("animal_name", animal.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_count", animal_name.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_gender", gender.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_age", animal_age.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_prediseases", prediseased_text)
                .addUrlEncodeFormBodyParameter("diseases_disc", diseasdisc.getText().toString())
                .addUrlEncodeFormBodyParameter("animal_breed", breed.getText().toString())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        dialog.dismiss();
                        crop_listner.update_crop();
                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


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

    public void delete_animal(String id) {
        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.delete_animal)
                .addUrlEncodeFormBodyParameter("animal_id", id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        crop_listner.update_crop();
                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


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


}