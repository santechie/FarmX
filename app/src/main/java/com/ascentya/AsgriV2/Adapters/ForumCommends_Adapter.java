package com.ascentya.AsgriV2.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Delete_Comments;
import com.ascentya.AsgriV2.Models.ForumCommends_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class ForumCommends_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ForumCommends_Model> items = new ArrayList<>();
    private Context ctx;
    String user_id;
    ViewDialog viewDialog;
    Delete_Comments delete_comments;



    public ForumCommends_Adapter(Context context, List<ForumCommends_Model> items, String userid, ViewDialog viewDialog, Delete_Comments delete_comment) {
        this.items = items;
        this.ctx = context;
        this.user_id = userid;
        this.viewDialog = viewDialog;
        this.delete_comments = delete_comment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView user_name, commmends, created_at;
        ImageView report, delete;


        public ViewHolder(View v) {
            super(v);
            user_name = (TextView) v.findViewById(R.id.user_name);
            commmends = (TextView) v.findViewById(R.id.commmends);
            created_at = (TextView) v.findViewById(R.id.created_at);
            report = (ImageView) v.findViewById(R.id.report);
            delete = (ImageView) v.findViewById(R.id.delete);
//            date = (TextView) v.findViewById(R.id.date);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commends_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final ForumCommends_Model Position_Object = items.get(position);


            if (!Position_Object.getName().equalsIgnoreCase("")) {
                view.user_name.setText(Position_Object.getName());

            } else {
                view.user_name.setText("User-" + Position_Object.getCommend_userid());

            }
            view.commmends.setText(Position_Object.getCommends());


            view.created_at.setText(parseDate(Position_Object.getDate()));

            if (Position_Object.getCommend_userid().equalsIgnoreCase(user_id)) {
                view.report.setVisibility(View.GONE);
                view.delete.setVisibility(View.VISIBLE);

            } else {

                view.report.setVisibility(View.VISIBLE);
                view.delete.setVisibility(View.GONE);

            }

//            if (!action.showDelete()) view.delete.setVisibility(View.INVISIBLE);

            view.report.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {


                    new AlertDialog.Builder(ctx)
                            .setTitle("Report Message")
                            .setMessage("Are you sure you want to report this message ?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    add_reports(Position_Object.getComments_id());

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });

            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {

                    new AlertDialog.Builder(ctx)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    removeAt(position);
                                    delete_comments(Position_Object.getComments_id());
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });


//            view.title.setText(Position_Object.getForum_title());
//            view.disc.setText(Position_Object.getForum_description());
//            view.date.setText(Position_Object.getCreated_at());
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent i = new Intent(ctx, Detailed_Forum.class);
//                    i.putExtra("forum_id", Position_Object.getForum_id());
//                    i.putExtra("forum_title", Position_Object.getForum_title());
//                    i.putExtra("forum_desc", Position_Object.getForum_description());
//                    i.putExtra("forum_date", Position_Object.getCreated_at());
//                    ctx.startActivity(i);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void add_reports(String commendid) {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.add_report)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("forum_id", "0")
                .addBodyParameter("commments_id", commendid)
                .addBodyParameter("type", "comment")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

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

    public void delete_comments(String commendid) {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.delete_comment)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addBodyParameter("comments_id", commendid)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        delete_comments.reset_adapter();
                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

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

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

//    public interface Action {
//    boolean showDelete();
//    }

}
