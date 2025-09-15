package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Land_Edit;
import com.ascentya.AsgriV2.Models.Mycultivation_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Mycultivation_Adapter extends RecyclerView.Adapter<Mycultivation_Adapter.ViewHolder> {

    private List<Mycultivation_Model> items = new ArrayList<>();
    private Context ctx;

    private Land_Edit refresh_callback;
    ViewDialog dialog_loader;
    ElasticButton add_field;
    Dialog dialog;
    Calendar cal;

    Boolean cultivation_check;
    AppCompatAutoCompleteTextView soil_type, land_unit, power_source, water_source, water_type, govt_scheme, soil_card, organic_former;
    List<String> Soiltype_data, landdime_data, powersource_data, watersource_data, watertype_data, govtscheme_data, soilcard_data;
    TextView applyeb, applysolar, applyborewell;
    TextInputEditText current_location, pincode, panchayat, landdime, land_name, years_soilreport, years_scheme, certification_no;
    Boolean ispermium;
    TextInputLayout inputlayout_schemes, inputlayout_card, inputlayout_certificate;
    TextView applysceme, applysoiltest, applycertificate;
    String user_id;


    public Mycultivation_Adapter(Context context, List<Mycultivation_Model> items, Boolean premium, ViewDialog dialog, Land_Edit refresh_callback, String userid) {
        this.items = items;
        this.dialog_loader = dialog;
        this.refresh_callback = refresh_callback;
        ctx = context;
        cultivation_check = false;
        this.ispermium = premium;
        this.user_id = userid;
        cal = Calendar.getInstance();


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

            view.field_name.setText(items.get(position).getLand_name());
            view.date_time.setText(items.get(position).getCreated_at());
            view.field_dimon.setText(items.get(position).getDimension() + " " + items.get(position).getUnit());
//            view.field_address.setText(ctx.getString(R.string.farmlocated) +" @ - " + items.get(position).getCurrent_location());

//if (ispermium){
//
//
//}else {
//    if (position >1){
//        view.itemView.setVisibility(View.GONE);
//
//    }
//}

            view.edit.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {


                    dialog = new Dialog(ctx, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.editland_layout);

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();


                    land_name = dialog.findViewById(R.id.land_name);
                    landdime = dialog.findViewById(R.id.landdime);
                    current_location = dialog.findViewById(R.id.current_location);
                    pincode = dialog.findViewById(R.id.pincode);
                    panchayat = dialog.findViewById(R.id.panchayat);
                    soil_type = dialog.findViewById(R.id.soil_type);
                    land_unit = dialog.findViewById(R.id.land_unit);
                    power_source = dialog.findViewById(R.id.power_source);
                    water_source = dialog.findViewById(R.id.water_source);
                    water_type = dialog.findViewById(R.id.water_type);
                    add_field = dialog.findViewById(R.id.add_field);
                    govt_scheme = dialog.findViewById(R.id.govt_scheme);
                    soil_card = dialog.findViewById(R.id.soil_card);
                    applysceme = dialog.findViewById(R.id.applysceme);
                    inputlayout_schemes = dialog.findViewById(R.id.inputlayout_schemes);
                    inputlayout_card = dialog.findViewById(R.id.inputlayout_card);
                    inputlayout_card = dialog.findViewById(R.id.inputlayout_card);
                    applysoiltest = dialog.findViewById(R.id.applysoiltest);
                    organic_former = dialog.findViewById(R.id.organic_farmer);
                    inputlayout_certificate = dialog.findViewById(R.id.inputlayout_certificate);
                    applycertificate = dialog.findViewById(R.id.applycertificate);
                    years_soilreport = dialog.findViewById(R.id.years_soilreport);
                    years_scheme = dialog.findViewById(R.id.years_scheme);
                    certification_no = dialog.findViewById(R.id.certification_no);

                    applyeb = dialog.findViewById(R.id.applyeb);
                    applysolar = dialog.findViewById(R.id.applysolar);
                    applyborewell = dialog.findViewById(R.id.applyborewell);


                    govt_scheme.setText(items.get(position).getGovernment_scheme());
                    soil_card.setText(items.get(position).getSoil_healthcard());
                    organic_former.setText(items.get(position).getCertified_organic_former());
                    years_scheme.setText(items.get(position).getScheme_year());
                    certification_no.setText(items.get(position).getCertificate());
                    years_soilreport.setText(items.get(position).getHealthcard_date());

                    land_name.setText(items.get(position).getLand_name());
                    landdime.setText(items.get(position).getDimension());
                    current_location.setText(items.get(position).getCurrent_location());
                    pincode.setText(items.get(position).getPincode());
                    panchayat.setText(items.get(position).getPanchayat_office());
                    soil_type.setText(items.get(position).getSoil_type());
                    land_unit.setText(items.get(position).getUnit());
                    power_source.setText(items.get(position).getPower_source());
                    water_source.setText(items.get(position).getWater_source());
                    water_type.setText(items.get(position).getWater_type());

                    years_soilreport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                                    years_soilreport.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            dpd.show();
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

                    ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            Soiltype_data);
                    soil_type.setAdapter(soiltype_adpter);


                    govtscheme_data = new ArrayList<>();
                    govtscheme_data.add("Yes");
                    govtscheme_data.add("No");
                    ArrayAdapter<String> scheme_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
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


                    ArrayAdapter<String> organic_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
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
                    ArrayAdapter<String> soilcard_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
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


                    Soiltype_data = new ArrayList<>();
                    Soiltype_data.add("Red loam");
                    Soiltype_data.add("Laterite");
                    Soiltype_data.add("Black");
                    Soiltype_data.add("Alluvial");
                    Soiltype_data.add("saline soil");
                    ArrayAdapter<String> adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            Soiltype_data);

                    watersource_data = new ArrayList<>();
                    watersource_data.add("Borewell");
                    watersource_data.add("Tubewell");
                    watersource_data.add("Openwell");
                    watersource_data.add("Reserve");
                    watersource_data.add("Channel");
                    ArrayAdapter<String> watersource_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            watersource_data);

                    water_source.setAdapter(watersource_adpter);

                    watertype_data = new ArrayList<>();
                    watertype_data.add("Saline");
                    watertype_data.add("Normal");
                    ArrayAdapter<String> watertype_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
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

                    ArrayAdapter<String> landdime_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            landdime_data);

                    land_unit.setAdapter(landdime_adpter);

                    powersource_data = new ArrayList<>();
                    powersource_data.add("None");
                    powersource_data.add("EB");
                    powersource_data.add("Solar");
                    powersource_data.add("Both");

                    ArrayAdapter<String> powerype_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
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
                    add_field.setOnClickListener(new DebouncedOnClickListener(1500) {
                        @Override
                        public void onDebouncedClick(View v) {
                            if (validatesignForm()) {

                                add_land(items.get(position).getLand_id());
//    Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });


            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    delete_land(items.get(position).getLand_id(), position);

                }
            });
        }


    }

    public void delete_land(String id, final Integer position) {
        dialog_loader.showDialog();
        AndroidNetworking.post(Webservice.deleteland)
                .addUrlEncodeFormBodyParameter("land_id", id)
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    private boolean validatesignForm() {
        if (!(land_name.getText().toString().length() > 2)) {
            land_name.setError(ctx.getString(R.string.required));
            return false;
        } else if (!(landdime.getText().toString().length() > 0)) {
            landdime.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!(land_unit.getText().toString().length() > 2)) {
            land_unit.setError(ctx.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public void add_land(String id) {


        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.updateland)
                .addUrlEncodeFormBodyParameter("land_id", id)
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
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        refresh_callback.update_land();
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

    public void apply_power(String apply) {


        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.apply)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("apply_for", apply)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

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
