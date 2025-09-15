package com.ascentya.AsgriV2.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Crops_Edit;
import com.ascentya.AsgriV2.Models.Mycrops_List_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.MyAlarm;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.tabs.TabLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class MyCrops_Adapter extends RecyclerView.Adapter<MyCrops_Adapter.ViewHolder> {

    private List<Mycrops_List_Model> items = new ArrayList<>();
    private Context ctx;
    ViewDialog dialog_loader;
    Dialog dialog;
    Spinner land, member;
    EditText plougheddate, last_chemical_spray_date, next_chemical_spray_date, fertilizer_used, chemicals_used, wateravailability, last_irrigated_date, next_irrigated_date, last_fertilized_date, next_fertilized_date;
    ElasticButton addmember;
    AutoCompleteTextView crop;
    Crops_Edit crop_listner;
    List<String> member_data;
    List<String> land_data;
    SessionManager sm;
    Calendar cal;
    CheckBox remainder;

    public MyCrops_Adapter(Context context, List<Mycrops_List_Model> items, ViewDialog dialog_loader, Crops_Edit crop_listner, SessionManager sm) {
        this.items = items;
        this.dialog_loader = dialog_loader;
        this.crop_listner = crop_listner;
        this.sm = sm;
        ctx = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView crop_name, ploughed_date, land, date;
        ElasticButton edit, delete;

        public ViewHolder(View v) {
            super(v);
            crop_name = (TextView) v.findViewById(R.id.crop_name);
            ploughed_date = (TextView) v.findViewById(R.id.ploughed_date);
            land = (TextView) v.findViewById(R.id.land);
            date = (TextView) v.findViewById(R.id.date);
//            member_address = (TextView) v.findViewById(R.id.member_address);
            edit = (ElasticButton) v.findViewById(R.id.edit);
            delete = (ElasticButton) v.findViewById(R.id.delete);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycrops_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
//
            view.crop_name.setText(items.get(position).getCrop_name());

            if (items.get(position).getCrop_land().equalsIgnoreCase("select land")) {
                view.land.setText("NA");
            } else {
                view.land.setText(items.get(position).getCrop_land());
            }

            view.date.setText(items.get(position).getCrop_ploughed_date());

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

            view.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(ctx);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.delete_layout);
                    dialog.setCancelable(true);


                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());


                    ((TextView) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    ((Button) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            add_land(items.get(position));


                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);


                }
            });
            view.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(ctx, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.editcrop_layout);

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();


                    crop = dialog.findViewById(R.id.crop);
                    land = dialog.findViewById(R.id.land);
                    member = dialog.findViewById(R.id.member);
                    plougheddate = dialog.findViewById(R.id.plougheddate);
                    wateravailability = dialog.findViewById(R.id.wateravailability);
                    last_irrigated_date = dialog.findViewById(R.id.last_irrigated_date);
                    next_irrigated_date = dialog.findViewById(R.id.next_irrigated_date);
                    last_fertilized_date = dialog.findViewById(R.id.last_fertilized_date);
                    next_fertilized_date = dialog.findViewById(R.id.next_fertilized_date);
                    fertilizer_used = dialog.findViewById(R.id.fertilizer_used);
                    chemicals_used = dialog.findViewById(R.id.chemicals_used);
                    last_chemical_spray_date = dialog.findViewById(R.id.last_chemical_spray_date);
                    next_chemical_spray_date = dialog.findViewById(R.id.next_chemical_spray_date);
                    addmember = dialog.findViewById(R.id.addcrop);
                    remainder = dialog.findViewById(R.id.remainder);
                    cal = Calendar.getInstance();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
                            R.layout.dropdown_alert, Webservice.crops);

                    crop.setAdapter(adapter);

//                    getlands(position);
                    next_chemical_spray_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    next_chemical_spray_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });

                    last_chemical_spray_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    last_chemical_spray_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });
                    next_fertilized_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    next_fertilized_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });
                    last_fertilized_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    last_fertilized_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });

                    next_irrigated_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    next_irrigated_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });
                    last_irrigated_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    last_irrigated_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });

                    plougheddate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                                    plougheddate.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.show();
                        }
                    });

                    crop.setText(items.get(position).getCrop_name());
//                    land.setText(items.get(position).getCrop_land());
//                    member.setText(items.get(position).getCrop_member());
                    plougheddate.setText(items.get(position).getCrop_ploughed_date());
                    wateravailability.setText(items.get(position).getCrop_water_facility());
                    last_irrigated_date.setText(items.get(position).getLast_irrigated_date());
                    next_irrigated_date.setText(items.get(position).getNext_irrigation_date());
                    last_fertilized_date.setText(items.get(position).getLast_fertilized_date());
                    next_fertilized_date.setText(items.get(position).getNext_fertilized_date());
                    fertilizer_used.setText(items.get(position).getFertilizer_used());
                    chemicals_used.setText(items.get(position).getChemicals_used());
                    last_chemical_spray_date.setText(items.get(position).getChemical_spray_date());
                    next_chemical_spray_date.setText(items.get(position).getNext_chemical_spray_date());
                    if (items.get(position).getRemainder().equalsIgnoreCase("1")) {
                        remainder.setChecked(true);

                    } else {
                        remainder.setChecked(false);
                    }

                    addmember.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (validateForm()) {
                                if (remainder.isChecked()) {

                                    if (next_fertilized_date.getText().length() > 0) {
                                        String[] separated = next_fertilized_date.getText().toString().split("-");
                                        int year = Integer.parseInt(separated[0]);
                                        int month = Integer.parseInt(separated[1]);
                                        int day = Integer.parseInt(separated[2]);

                                        setAlarm(year, month, day);

                                    }

                                    if (next_irrigated_date.getText().length() > 0) {
                                        String[] separated = next_irrigated_date.getText().toString().split("-");
                                        int year = Integer.parseInt(separated[0]);
                                        int month = Integer.parseInt(separated[1]);
                                        int day = Integer.parseInt(separated[2]);

                                        setAlarm(year, month, day);

                                    }

                                }

                                update_land(items.get(position).getCrop_id());
                            }
                        }
                    });
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void delete_land(String id, final Integer position) {
//        dialog_loader.showDialog();
//        AndroidNetworking.post(Webservice.deletemember)
//                .addUrlEncodeFormBodyParameter("member_id", id)
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//                dialog_loader.hideDialog();
//                try {
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//
//                        Toast.makeText(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
//                        removeAt(position);
//                    } else {
//                        Toast.makeText(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                dialog_loader.hideDialog();
//
//            }
//        });
//    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    private boolean validateForm() {
        if (!(crop.getText().toString().length() > 2)) {
            crop.setError(ctx.getString(R.string.required));
            return false;
        } else if (!(plougheddate.getText().toString().length() > 2)) {
            plougheddate.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!(wateravailability.getText().toString().length() > 2)) {
            wateravailability.setError(ctx.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }


    public void update_land(String id) {
        String remainder_selection = "0";
        if (remainder.isChecked()) {
            remainder_selection = "1";
        } else {
            remainder_selection = "0";
        }

        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.updatecrop)
                .addUrlEncodeFormBodyParameter("id", id)
                .addUrlEncodeFormBodyParameter("crop_name", crop.getText().toString())
                .addUrlEncodeFormBodyParameter("crop_land", land.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("crop_member", member.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("crop_ploughed_date", plougheddate.getText().toString())
                .addUrlEncodeFormBodyParameter("crop_water_facility", wateravailability.getText().toString())
                .addUrlEncodeFormBodyParameter("Next_chemical_spray_date", next_chemical_spray_date.getText().toString())
                .addUrlEncodeFormBodyParameter("chemical_spray_date", last_chemical_spray_date.getText().toString())
                .addUrlEncodeFormBodyParameter("chemicals_used", chemicals_used.getText().toString())
                .addUrlEncodeFormBodyParameter("fertilizer_used", fertilizer_used.getText().toString())
                .addUrlEncodeFormBodyParameter("next_fertilized_date", next_fertilized_date.getText().toString())
                .addUrlEncodeFormBodyParameter("last_fertilized_date", last_fertilized_date.getText().toString())
                .addUrlEncodeFormBodyParameter("next_irrigation_date", next_irrigated_date.getText().toString())
                .addUrlEncodeFormBodyParameter("last_irrigated_date", last_irrigated_date.getText().toString())
                .addUrlEncodeFormBodyParameter("remainder", remainder_selection)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        crop.setText("");
                        land.setSelection(0);
                        wateravailability.setText("");
                        plougheddate.setText("");
                        member.setSelection(0);
                        next_chemical_spray_date.setText("");
                        last_chemical_spray_date.setText("");
                        chemicals_used.setText("");
                        fertilizer_used.setText("");
                        next_fertilized_date.setText("");
                        last_fertilized_date.setText("");
                        next_irrigated_date.setText("");
                        last_irrigated_date.setText("");
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


                try {
                    JSONObject error_obj = new JSONObject(anError.getErrorBody());
                    Toasty.error(ctx, error_obj.optString("message"), Toast.LENGTH_SHORT, true).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if (anError.getResponse())


            }
        });
    }


//    public void getlands(final Integer pos) {
//        dialog_loader.showDialog();
//        land_data = new ArrayList<>();
//        AndroidNetworking.get(Webservice.getregisteredland + sm.getUser().getId())
//
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                dialog_loader.hideDialog();
//                getmembers(pos);
//                try {
//                    land_data.add(ctx.getString(R.string.selectland));
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            land_data.add(jsonArray.getJSONObject(i).optString("land_name"));
//
////                            Mycultivation_Model obj = new Mycultivation_Model();
////                            obj.setLand_id(jsonArray.getJSONObject(i).optString("id"));
////                            obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
////                            obj.setCultivation_onfield(jsonArray.getJSONObject(i).optString("land_cultivation_first"));
////                            obj.setLand_dimon(jsonArray.getJSONObject(i).optString("land_dimension"));
////                            obj.setLand_location(jsonArray.getJSONObject(i).optString("land_location"));
////                            obj.setLand_cultivationdate(jsonArray.getJSONObject(i).optString("land_cultivation_date"));
////                            obj.setLast_cultivated_crop(jsonArray.getJSONObject(i).optString("land_lastcultivated_crop"));
////                            obj.setIrrigation_info(jsonArray.getJSONObject(i).optString("land_irrigation"));
//
//                        }
//
//
//                    }
//
//
//                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                            ctx, R.layout.spinner_item, land_data) {
//                        @Override
//                        public boolean isEnabled(int position) {
//                            if (position == 0) {
//                                // Disable the first item from Spinner
//                                // First item will be use for hint
//                                return false;
//                            } else {
//                                return true;
//                            }
//                        }
//
//                        @Override
//                        public View getDropDownView(int position, View convertView,
//                                                    ViewGroup parent) {
//                            View view = super.getDropDownView(position, convertView, parent);
//                            TextView tv = (TextView) view;
//                            if (position == 0) {
//                                // Set the hint text color gray
//                                tv.setTextColor(Color.GRAY);
//                            } else {
//                                tv.setTextColor(Color.BLACK);
//                            }
//                            return view;
//                        }
//                    };
//                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//                    land.setAdapter(spinnerArrayAdapter);
//
//                    int selectionPosition = spinnerArrayAdapter.getPosition(items.get(pos).getCrop_land());
//                    land.setSelection(selectionPosition);
//
//                    land.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String selectedItemText = (String) parent.getItemAtPosition(position);
//                            // If user change the default selection
//                            // First item is disable and it is used for hint
//
//                            if (selectedItemText.equalsIgnoreCase("Add Land")) {
//                                Toast.makeText(ctx, "click", Toast.LENGTH_SHORT).show();
////                                ((BottomNavigationView) ctx.findViewById(R.id.navigation_mycrop)).setSelectedItemId(R.id.navigation_land);
//                            } else {
//                                if (position > 0) {
//                                    // Notify the selected item text
//
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                dialog_loader.hideDialog();
//
//            }
//        });
//    }


    public void getmembers(final Integer pos) {
        member_data = new ArrayList<>();
        AndroidNetworking.get(Webservice.getmemberlist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    member_data.add(ctx.getString(R.string.selectmem));

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            member_data.add(jsonArray.getJSONObject(i).optString("member_name"));
//
//                            Members_Model obj = new Members_Model();
//                            obj.setMember_id(jsonArray.getJSONObject(i).optString("id"));
//                            obj.setMember_name(jsonArray.getJSONObject(i).optString("member_name"));
//                            obj.setMember_age(jsonArray.getJSONObject(i).optString("member_age"));
//                            obj.setMember_gender(jsonArray.getJSONObject(i).optString("member_gender"));
//                            obj.setFarming_exp(jsonArray.getJSONObject(i).optString("farming_experience"));
//                            obj.setRelation(jsonArray.getJSONObject(i).optString("member_relation"));
//
                        }


                    }


                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            ctx, R.layout.spinner_item, member_data) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                    member.setAdapter(spinnerArrayAdapter);

                    int selectionPosition = spinnerArrayAdapter.getPosition(items.get(pos).getCrop_member());
                    member.setSelection(selectionPosition);
                    member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItemText = (String) parent.getItemAtPosition(position);

                            if (selectedItemText.equalsIgnoreCase("Add member")) {
                                TabLayout tabs = (TabLayout) ((Activity) ctx).findViewById(R.id.tab_layout);
                                tabs.getTabAt(1).select();
                            } else {
                                if (position > 0) {

                                    if (parent.getCount() == position) {
//                                        Toast.makeText(getActivity(), "member", Toast.LENGTH_SHORT).show();
                                    }
                                    // Notify the selected item text

                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    public void setAlarm(Integer year, Integer month, Integer day) {


        Calendar alarmCalendar = Calendar.getInstance();

        Intent intent1 = new Intent(ctx.getApplicationContext(),
                MyAlarm.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(ctx.getApplicationContext(),
                1,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        // Add this day of the week line to your existing code
        alarmCalendar.set(Calendar.MONTH, month);
        alarmCalendar.set(Calendar.YEAR, year);
        alarmCalendar.set(Calendar.DATE, day);
        alarmCalendar.set(Calendar.HOUR, 8);
        alarmCalendar.set(Calendar.MINUTE, 0);
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.AM_PM, Calendar.AM);

        Long alarmTime = alarmCalendar.getTimeInMillis();
        //Also change the time to 24 hours.
        am.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent1);

    }

    public void add_land(Mycrops_List_Model item) {
        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.addcrop)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("crop_name", item.getCrop_name())
                .addUrlEncodeFormBodyParameter("crop_land", item.getCrop_land())
                .addUrlEncodeFormBodyParameter("crop_member", item.getCrop_member())
                .addUrlEncodeFormBodyParameter("crop_ploughed_date", item.getCrop_ploughed_date())
                .addUrlEncodeFormBodyParameter("crop_water_facility", item.getCrop_water_facility())
                .addUrlEncodeFormBodyParameter("Next_chemical_spray_date", item.getNext_chemical_spray_date())
                .addUrlEncodeFormBodyParameter("chemical_spray_date", item.getChemical_spray_date())
                .addUrlEncodeFormBodyParameter("chemicals_used", item.getChemicals_used())
                .addUrlEncodeFormBodyParameter("fertilizer_used", item.getFertilizer_used())
                .addUrlEncodeFormBodyParameter("next_fertilized_date", item.getNext_fertilized_date())
                .addUrlEncodeFormBodyParameter("last_fertilized_date", item.getLast_fertilized_date())
                .addUrlEncodeFormBodyParameter("next_irrigation_date", item.getNext_irrigation_date())
                .addUrlEncodeFormBodyParameter("last_irrigated_date", item.getLast_irrigated_date())
                .addUrlEncodeFormBodyParameter("remainder", item.getRemainder())

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
                dialog_loader.showDialog();


            }
        });
    }

}
