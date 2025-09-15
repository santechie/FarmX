package com.ascentya.AsgriV2.Adapters;

import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Farmx_Myfarm;
import com.ascentya.AsgriV2.Activitys.Forum_Activity;
import com.ascentya.AsgriV2.Activitys.HomeScreen_Activity;
import com.ascentya.AsgriV2.Activitys.Market_News;
import com.ascentya.AsgriV2.Activitys.Scheme_Activity;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.DashBoard_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.buysell.Buy_Sell;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.activities.EMarketActivity;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;
import com.ascentya.AsgriV2.my_farm.activities.MyFarmActivity;
import com.ascentya.AsgriV2.utility.activity.UtilityActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.AGRIPEDIA;

public class DashBoard_Adapter extends RecyclerView.Adapter<DashBoard_Adapter.ViewHolder> {

    private List<DashBoard_Model> items = new ArrayList<>();
    private BaseActivity ctx;
    private SessionManager sm;
    private ViewDialog dialog;


    public DashBoard_Adapter(BaseActivity context, List<DashBoard_Model> items, SessionManager sm, ViewDialog dialog) {
        this.items = items;
        this.sm = sm;
        this.dialog = dialog;
        this.ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView title;
        public ImageView icons;
        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            icons = (ImageView) v.findViewById(R.id.icons);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_row, parent, false);
//        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
//        layoutParams.height = (int) (parent.getHeight() * 0.3);
//        v.setLayoutParams(layoutParams);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // if (holder instanceof ViewHolder) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final DashBoard_Model Position_Object = items.get(holder.getAdapterPosition());

            view.icons.setImageResource(Position_Object.getIcon());
            view.title.setText(Position_Object.getName());

            /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent i = new Intent(ctx, Farmx_Myfarm.class);
                    ctx.startActivity(i);
                    return false;
                }
            });*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.AGRIPEDIA))) {

                        if (Webservice.crops.size() > 0) {
                            if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                                ctx.openActivity(HomeScreen_Activity.class, Pair.create("crop", true));
//                                ctx.openWithAccess(HomeScreen_Activity.class,
//                                    Pair.create("crop", true);
                                ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                               /* if (ctx.checkSubscription(Modules.AGRIPEDIA)) {

                                }*/
                               /* Intent i = new Intent(ctx, HomeScreen_Activity.class);
                                i.putExtra("crop", true);
                                ctx.startActivity(i);*/

                            } else {
                                Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                                getcrops();
                            } else {

                                Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();
                            }

                        }


                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.my_farm))) {

                        if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                            if (ctx.checkSubscription(Modules.MY_FARM)) {
                                ctx.openActivity(MyFarmActivity.class);
                               // ctx.openWithAccess(MyFarmActivity.class, MY_CROPS);
                                ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                 /* Intent i = new Intent(ctx, MyFarmActivity.class);
                            ctx.startActivity(i);*/
                            }else if (sm.getUser() != null) {
                                Intent in = new Intent(ctx, Farmx_Myfarm.class);
                                ctx.startActivity(in);
                            } else {
                                Intent ij =
                                        new Intent(ctx, Formx_Login_Activity.class);
                                ctx.startActivity(ij);
                            }

                        } else {
                            Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();
                        }


                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.forum))) {


                        if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                            if (sm.isGuest()) {
                                ((BaseActivity) ctx).showRegisterDialog();
                            } else {
//                                ctx.openWithAccess(Forum_Activity.class, COMMUNITY);
                                if (ctx.checkSubscription(Modules.COMMUNITY)) {
                                    ctx.openActivity(Forum_Activity.class);
                                    ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                               /* Intent i = new Intent(ctx, Forum_Activity.class);
                                ctx.startActivity(i);*/
                            }

                           /* if (sm.getUser() != null) {
                                Intent i = new Intent(ctx, Forum_Activity.class);
                                ctx.startActivity(i);
                            } else {
                                Intent i = new Intent(ctx, Formx_Login_Activity.class);
                                ctx.startActivity(i);
                            }*/
                        } else {
                            Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();

                        }


//                        Toast.makeText(ctx, R.string.noupdate, Toast.LENGTH_SHORT).show();

                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.mnews))) {

                        if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                            if (ctx.checkSubscription(Modules.MARKET_NEWS))
                                ctx.openActivity(Market_News.class);
                            ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


//                            ctx.openWithAccess(Market_News.class, MARKET_NEWS);

                            /*Intent i = new Intent(ctx, Market_News.class);
                            ctx.startActivity(i);*/

                            /*if (sm.getUser() != null) {
                                Intent i = new Intent(ctx, Market_News.class);
                                ctx.startActivity(i);
                            } else {
                                Intent i = new Intent(ctx, Formx_Login_Activity.class);
                                ctx.startActivity(i);
                            }*/
                        } else {
                            Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();

                        }

                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.buysell))) {
                        if (NetworkDetector.isNetworkStatusAvialable(ctx)) {

                            if (sm.isGuest()) {
                                ((BaseActivity) ctx).showRegisterDialog();
                            } else {
                                if (ctx.checkSubscription(Modules.BUY_AND_SELL))
                                    ctx.openActivity(Buy_Sell.class, true);

//                                ctx.openWithAccess(Buy_Sell.class, true, BUY_AND_SELL);
                                /*Intent i = new Intent(ctx, Buy_Sell.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(i);*/
                            }
                            /*if (sm.getUser() != null) {
                                Intent i = new Intent(ctx, Buy_Sell.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(i);
                            } else {
                                Intent i = new Intent(ctx, Formx_Login_Activity.class);
                                ctx.startActivity(i);
                            }*/
                        } else {
                            Toast.makeText(ctx, "Please check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.chats))) {
                        if (ctx.checkSubscription(Modules.CHATS))
                            Toast.makeText(ctx, "We will update chats option soon", Toast.LENGTH_SHORT).show();
                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.scheme))) {
                        if (ctx.checkSubscription(Modules.SCHEME))
                            ctx.openActivity(Scheme_Activity.class, true);
                        ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        ctx.openWithAccess(Scheme_Activity.class, SCHEME);
                        /*Intent i = new Intent(ctx, Scheme_Activity.class);
                        ctx.startActivity(i);*/
//                        Toast.makeText(ctx, "We will update scheme soon", Toast.LENGTH_SHORT).show();
                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.history))) {
//                        Intent i = new Intent(ctx, DeviceDataActivity.class);
//                        ctx.startActivity(i);
                        if (ctx.checkSubscription(Modules.HISTORY))
                            Toast.makeText(ctx, "We will update user history soon", Toast.LENGTH_SHORT).show();
                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.emarket))) {
                        // e Market
                        if (!sm.isGuest()) {
                            /*if (ctx.checkSubscription(Modules.EMARKET))
                                ctx.openActivity(EMarketActivity.class, true);
                            ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

//                            ctx.openWithAccess(EMarketActivity.class, true, EMARKET);
                            Intent i = new Intent(ctx, EMarketActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            ctx.startActivity(i);

                        } else {
                            ((BaseActivity) ctx).showRegisterDialog();
                        }
                    } else if (Position_Object.getName().equalsIgnoreCase(ctx.getString(R.string.utility))) {
                        // Utility
                        if (ctx.checkSubscription(Modules.UTILITY))
                            ctx.openActivity(UtilityActivity.class, true);
                        ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        ctx.openWithAccess(UtilityActivity.class, true, UTILITY);
                        /*Intent i = new Intent(ctx, UtilityActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        ctx.startActivity(i);*/

                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<DashBoard_Model> list) {
        items = list;
        notifyDataSetChanged();
    }

    public void getcrops() {
        dialog.showDialog();
        //AndroidNetworking.get(Webservice.getname_icon)
        AndroidNetworking.get("https://vrjaitraders.com/ard_farmx/api/Agripedia/ciup")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        dialog.hideDialog();
                        Webservice.Data_crops.clear();
                        Webservice.crops.clear();

                        try {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                Crops_Main obj = new Crops_Main();
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                obj.setName(jobj.optString("crop_name").trim());
                                obj.setIcon("https://vrjaitraders.com/ard_farmx/" + jobj.optString("crop_icons_images").trim());
                                obj.setCrop_id(jobj.optString("Basic_info_id").trim());
                                obj.setS_name(jobj.optString("scientific_name").trim());
                                obj.setTempreture(jobj.optString("temperature").trim());
                                obj.setPollution("40-50");
                                obj.setHumidity(jobj.optString("humidity").trim());
                                obj.setMoisture(jobj.optString("soil_moisture").trim());
                                obj.setWaterph(jobj.optString("soil_ph").trim());
                                Webservice.crops.add(jobj.optString("crop_name").trim());
                                obj.setVarieties(GsonUtils.fromJson(jobj.getJSONArray("varieties").toString(),
                                        EMarketStorage.varietyListType));
                                Webservice.Data_crops.add(obj);
                            }

                        } catch (Exception e) {
                            dialog.hideDialog();
                            e.printStackTrace();

                        }

                        ctx.openWithAccess(HomeScreen_Activity.class, AGRIPEDIA, Pair.create("crop", true));
                /*Intent i = new Intent(ctx, HomeScreen_Activity.class);
                i.putExtra("crop", true);
                ctx.startActivity(i);*/

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();

                    }
                });
    }

}
