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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Member_Edit;
import com.ascentya.AsgriV2.Models.Expense_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Expense_Adapter extends RecyclerView.Adapter<Expense_Adapter.ViewHolder> {

    private List<Expense_Model> items = new ArrayList<>();
    private Context ctx;
    private Member_Edit listener;
    ViewDialog dialog_loader;
    Dialog dialog;
    Spinner payment_type;
    EditText payment_title, payment_date, total_amount, nextpayment_date;
    ElasticButton addpayment;
    Calendar cal;


    public Expense_Adapter(Context context, List<Expense_Model> items, ViewDialog dialog_loader, Member_Edit listener) {
        this.items = items;
        this.dialog_loader = dialog_loader;
        this.listener = listener;
        ctx = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView payment_title, date, received, balance;
        ElasticFloatingActionButton edit_layout, delete_layout;

        public ViewHolder(View v) {
            super(v);
            payment_title = (TextView) v.findViewById(R.id.payment_title);
            date = (TextView) v.findViewById(R.id.date);
            received = (TextView) v.findViewById(R.id.received);
            balance = (TextView) v.findViewById(R.id.balance);
            edit_layout = (ElasticFloatingActionButton) v.findViewById(R.id.edit_layout);
            delete_layout = (ElasticFloatingActionButton) v.findViewById(R.id.delete_layout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            view.payment_title.setText(items.get(position).getExpense_title());
            view.date.setText(items.get(position).getExpense_dated());
            view.received.setText(ctx.getString(R.string.total) + "-" + items.get(position).getExpense_total());
//            view.balance.setText("Bal-"+items.get(position).getPayment_balance_amount());
//            view.member_address.setText(items.get(position).getRelation() + "," + items.get(position).getMember_age() + "," + items.get(position).getMember_gender());
//            view.field_address.setText("This farm is located @ - "+items.get(position).getLand_location());
            view.delete_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete_land(items.get(position).getExpense_id(), position);

                }
            });

            view.edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(ctx, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.editexpanse_layout);

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();

                    payment_title = dialog.findViewById(R.id.payment_title);
                    payment_date = dialog.findViewById(R.id.payment_date);
                    total_amount = dialog.findViewById(R.id.total_amount);
                    nextpayment_date = dialog.findViewById(R.id.nextpayment_date);
                    addpayment = dialog.findViewById(R.id.addpayment);
                    payment_type = dialog.findViewById(R.id.payment_type);
                    cal = Calendar.getInstance();


                    payment_title.setText(items.get(position).getExpense_title());
                    payment_date.setText(items.get(position).getExpense_dated());
                    total_amount.setText(items.get(position).getExpense_total());
                    nextpayment_date.setText(items.get(position).getExpense_nextdate());

                    List<String> genderlist = new ArrayList<>();
                    genderlist.add(ctx.getString(R.string.selectpayment));
                    genderlist.add("Cash/Cheque");
                    genderlist.add("Online Payment");


                    nextpayment_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                                    nextpayment_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });

                    payment_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                                    payment_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                                }
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                            dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                            dpd.show();
                        }
                    });

                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            ctx, R.layout.spinner_item, genderlist) {
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
                    payment_type.setAdapter(spinnerArrayAdapter);

                    if (items.get(position).getExpense_type().equalsIgnoreCase("Online Payment")) {
                        payment_type.setSelection(2);
                    } else {
                        payment_type.setSelection(1);
                    }

                    payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItemText = (String) parent.getItemAtPosition(position);
                            // If user change the default selection
                            // First item is disable and it is used for hint
                            if (position > 0) {
                                // Notify the selected item text

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    addpayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (payment_type.getSelectedItemPosition() != 0) {
                                if (validateForm()) {
                                    update_land(items.get(position).getExpense_id());
                                }
                            } else {
                                Toast.makeText(ctx, "Kindly select Payment type", Toast.LENGTH_SHORT).show();
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

    public void delete_land(String id, final Integer position) {
        dialog_loader.showDialog();
        AndroidNetworking.post(Webservice.deleteexpense)
                .addUrlEncodeFormBodyParameter("expense_id", id)
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

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    private boolean validateForm() {
        if (!(payment_title.getText().toString().length() > 2)) {
            payment_title.setError(ctx.getString(R.string.required));
            return false;
        } else if (!(payment_date.getText().toString().length() > 1)) {
            payment_date.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!(total_amount.getText().toString().length() > 0)) {
            total_amount.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!(nextpayment_date.getText().toString().length() > 0)) {
            nextpayment_date.setError(ctx.getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }


    public void update_land(String id) {
        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.updateexpense)
                .addUrlEncodeFormBodyParameter("id", id)
                .addUrlEncodeFormBodyParameter("expenses_title", payment_title.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_type", payment_type.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("expenses_dated", payment_date.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_totlamount", total_amount.getText().toString())
                .addUrlEncodeFormBodyParameter("expenses_next_payment_data", nextpayment_date.getText().toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        listener.update_member();
                        dialog.dismiss();

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
                    Toasty.success(ctx, error_obj.optString("message"), Toast.LENGTH_SHORT, true).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
