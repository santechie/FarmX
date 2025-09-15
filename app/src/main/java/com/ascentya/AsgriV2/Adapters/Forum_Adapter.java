package com.ascentya.AsgriV2.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Forum.Detailed_Forum;
import com.ascentya.AsgriV2.Interfaces_Class.Delete_Post;
import com.ascentya.AsgriV2.Models.Forum_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Forum_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Forum_Model> items = new ArrayList<>();
    private Context ctx;

    String user_id;
    Dialog viewDialog;
    Delete_Post delete_comments;

    public Forum_Adapter(Context context, List<Forum_Model> items, String userid, Dialog viewDialog, Delete_Post delete_comment) {
        this.items = items;
        ctx = context;
        this.user_id = userid;
        this.viewDialog = viewDialog;
        this.delete_comments = delete_comment;

    }

//    public Forum_Adapter(FragmentActivity activity, List<Forum_Model> data, String id, Dialog dialog_c, Delete_Post delete_post) {
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, disc, date;
        ImageView report, delete;
        ImageView forum_attachment;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            disc = (TextView) v.findViewById(R.id.disc);
            date = (TextView) v.findViewById(R.id.date);
            report = (ImageView) v.findViewById(R.id.report);
            delete = (ImageView) v.findViewById(R.id.delete);
            forum_attachment = (ImageView) v.findViewById(R.id.forum_attachment);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Forum_Model Position_Object = items.get(position);

            view.title.setText(StringUtils.capitalize(Position_Object.getForum_title()));
            view.disc.setText(Position_Object.getForum_description());
            view.date.setText(Position_Object.getCreated_at());


//            if (!Position_Object.getForum_attachment().equals("")) {
//                Glide.with(ctx).load(Webservice.forumimagebase_path + Position_Object.getForum_attachment()).into(view.forum_attachment);
//
//            } else {
            Glide.with(ctx).load(Webservice.forumimagebase_path + Position_Object.getForum_attachment()).into(view.forum_attachment);

//            }

            if (Position_Object.getForum_posterid().equalsIgnoreCase(user_id)) {
                view.report.setVisibility(View.GONE);
                view.delete.setVisibility(View.VISIBLE);

            } else {

                view.report.setVisibility(View.VISIBLE);
                view.delete.setVisibility(View.GONE);

//                if (!action.showDelete()) view.delete.setVisibility(View.INVISIBLE);

            }

            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {

                    if (ctx != null && ctx instanceof BaseActivity
                            && ((BaseActivity) ctx).checkSubscription(Modules.COMMUNITY, ModuleManager.ACCESS.VIEW)){
                        Intent i = new Intent(ctx, Detailed_Forum.class);
                        i.putExtra("forum_id", Position_Object.getForum_id());
                        i.putExtra("forum_title", Position_Object.getForum_title());
                        i.putExtra("forum_desc", Position_Object.getForum_description());
                        i.putExtra("forum_date", Position_Object.getCreated_at());
                        i.putExtra("forum_attachment", Position_Object.getForum_attachment());
                        ctx.startActivity(i);
                    }

                   /* if (((BaseActivity) ctx).sessionManager.isPaid()) {

                    }else {
                        ((BaseActivity) ctx).showPayDialog();
                    }*/
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
                                    delete_comments(Position_Object.getForum_id());
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });

            view.report.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {


                    new AlertDialog.Builder(ctx, R.style.AlertDialogTheme)
                            .setTitle("Report Message")
                            .setMessage("Are you sure you want to report this message?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    add_reports(Position_Object.getForum_id());
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void delete_comments(String commendid) {
        viewDialog.show();


        AndroidNetworking.post(Webservice.delete_forum)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addBodyParameter("forum_id", commendid)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hide();

                if (UserHelper.checkResponse(ctx, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        delete_comments.reset_adapter();
                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                } catch (Exception e) {
                    viewDialog.hide();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hide();
            }
        });
    }

    public void add_reports(String forumid) {
        viewDialog.show();


        AndroidNetworking.post(Webservice.add_report)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("forum_id", forumid)
                .addBodyParameter("commments_id", "0")
                .addBodyParameter("type", "forum")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hide();

                if (UserHelper.checkResponse(ctx, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                } catch (Exception e) {
                    viewDialog.hide();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hide();
            }
        });
    }


    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

}
