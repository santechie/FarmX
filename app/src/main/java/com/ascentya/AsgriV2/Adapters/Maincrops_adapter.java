package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Activitys.Activity_Farmx_Modified;
import com.ascentya.AsgriV2.Activitys.Finance_Dummy;
import com.ascentya.AsgriV2.Models.Farmx_Finance;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.CropUtils;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.farmx.postharvest_diseas.PestsDisease_Activity;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Maincrops_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Maincrops_Model> items = new ArrayList<>();
    private Context ctx;
    private int layoutRes = 0;
    Boolean main_inter;
    List<Farmx_Finance> Data;


    public Maincrops_adapter(Context context, List<Maincrops_Model> items, Boolean mi) {
        this.items = items;
        ctx = context;
        this.main_inter = mi;
//

    }

    public Maincrops_adapter(Context context, List<Maincrops_Model> items,
                             Boolean mi, @LayoutRes int layoutRes) {
        this.items = items;
        ctx = context;
        this.main_inter = mi;
        this.layoutRes = layoutRes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView crop_name, acre, soiltype, id;
        CircleImageView crop_icon;
        FloatingActionButton activity, finance, pests, dideases;

        public ViewHolder(View v) {
            super(v);
            soiltype = (TextView) v.findViewById(R.id.soiltype);
            crop_name = (TextView) v.findViewById(R.id.crop_name);
            acre = (TextView) v.findViewById(R.id.acre);
            id = (TextView) v.findViewById(R.id.id);
            finance = (FloatingActionButton) v.findViewById(R.id.finance);
            activity = (FloatingActionButton) v.findViewById(R.id.activity);
            crop_icon = (CircleImageView) v.findViewById(R.id.crop_icon);
            pests = (FloatingActionButton) v.findViewById(R.id.pests);
            dideases = (FloatingActionButton) v.findViewById(R.id.diseases);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                layoutRes == 0 ? R.layout.farmx_mycrops_row : layoutRes,
                parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Maincrops_Model Position_Object = items.get(position);


            if (main_inter) {

                try {
                    view.id.setText(String.valueOf("M" + Position_Object.getId()));


                    view.crop_name.setText(Webservice.Data_crops.get(searchFor(Position_Object.getMaincrop())).getName());
                    Glide.with(ctx).load(Webservice.Data_crops.get(searchFor(Position_Object.getMaincrop())).getIcon()).into(view.crop_icon);

                } catch (Exception e) {

                }

            } else {

                try {


                    view.id.setText(String.valueOf("I" + Position_Object.getId()));

                    Glide.with(ctx).load(Webservice.Data_crops.get(searchFor(Position_Object.getIntercrop())).getIcon()).into(view.crop_icon);

                    view.crop_name.setText(Webservice.Data_crops.get(searchFor(Position_Object.getIntercrop())).getName());


                } catch (Exception e) {

                }

            }


            view.soiltype.setText(Position_Object.getLand_name());
            view.soiltype.setMovementMethod(new ScrollingMovementMethod());

            String acreText = "";
            try {
                int count = Integer.parseInt(Position_Object.getAcre_count());
                acreText = Position_Object.getAcre_count() + (count == 1 ? " acre" : " acres");
            }catch (Exception e){
                e.printStackTrace();
                acreText = Position_Object.getAcre_count() + " acre";
            }
            view.acre.setText(acreText);


            view.pests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ctx, PestsDisease_Activity.class);
                    i.putExtra("title", "Pests");
                    i.putExtra("type", main_inter);
                    i.putExtra("land", GsonUtils.getGson().toJson(Position_Object));
                    if (main_inter) {
                        i.putExtra("crop_id", Position_Object.getMaincrop());
                    } else {
                        i.putExtra("crop_id", Position_Object.getIntercrop());
                    }
                    ctx.startActivity(i);

                }
            });

            view.dideases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ctx, PestsDisease_Activity.class);
                    i.putExtra("title", "Diseases");
                    i.putExtra("type", main_inter);
                    i.putExtra("land", GsonUtils.getGson().toJson(Position_Object));
                    if (main_inter) {
                        i.putExtra("crop_id", Position_Object.getMaincrop());
                    } else {
                        i.putExtra("crop_id", Position_Object.getIntercrop());
                    }
                    ctx.startActivity(i);

                }
            });

            view.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {
                        /*Intent i = new Intent(ctx, Activity_Farmx_Modified.class);
                        i.putExtra("usercrop", Position_Object.getId());
                        i.putExtra("acre", Position_Object.getAcre_count());
                        if (main_inter) {
                            i.putExtra("crop_id", Position_Object.getMaincrop());
                            i.putExtra("crop_type", "main_crop");
                        } else {
                            i.putExtra("crop_id", Position_Object.getIntercrop());
                            i.putExtra("crop_type", "inter_crop");
                        }

                        ctx.startActivity(i);*/

                        Activity_Farmx_Modified.open(ctx,
                                Position_Object,
                                Position_Object.getId(),
                                Position_Object.getLand_name(),
                                false,
                                CropUtils.getLandCropModelList(Position_Object.getMainCropsString()),
                                CropUtils.getLandCropModelList(Position_Object.getInterCropsString()),
                                main_inter ? Position_Object.getMaincrop() : Position_Object.getIntercrop(),
                                Position_Object.getAcre_count(),
                                main_inter ? "main_crop" : "inter_crop");

                    } else {
                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            view.finance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {


                        Intent i = new Intent(ctx, Finance_Dummy.class);
                        i.putExtra("usercrop", Position_Object.getId());
                        i.putExtra("is_land", false);
                        if (main_inter) {
                            i.putExtra("crop_id", Position_Object.getMaincrop());
                            i.putExtra("crop_type", "main_crop");
                        } else {
                            i.putExtra("crop_id", Position_Object.getIntercrop());
                            i.putExtra("crop_type", "inter_crop");
                        }
                        ctx.startActivity(i);
                    } else {
                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getCrop_id();


            if (unitString.equals(data.toLowerCase())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }

//    public void getmembers() {
//
//
//        Data = new ArrayList<>();
//
//        AndroidNetworking.get(Webservice.getcrop_byid + crop_id + "/" + usercrop_id)
//
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//                try {
//
//
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//
////                        Userobject obj = new Userobject();
////                        obj.setId(jsonObject.getJSONObject("data").optString("id"));
////                        obj.setFirstname(jsonObject.getJSONObject("data").optString("username"));
////                        obj.setPhno(jsonObject.getJSONObject("data").optString("phone"));
////                        obj.setEmail(jsonObject.getJSONObject("data").optString("email"));
////                        obj.setIspremium(jsonObject.getJSONObject("data").optString("is_premium"));
////                        obj.setSearch_name("none");
////                        sm.setUser(obj);
////                        Intent i = new Intent(Payment_Activity.this, Mycrops_Main.class);
////                        i.putExtra("page", "crop");
////                        startActivity(i);
////                        finishAffinity();
//
////                        if (jsonObject.getJSONObject("data").optString("id"))
//
//                        if (!jsonObject.getJSONObject("data").optString("ploughing_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance(ctx.getString(R.string.ploughed) + " - " + jsonObject.getJSONObject("data").optString("ploughing_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("ploughing_cost")));
//
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.ploughed) + " - " + jsonObject.getJSONObject("data").optString("ploughing_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("ploughing_cost")));
// );
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("sowing_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance();
////                            fin_obj.setName(ctx.getString(R.string.sowing) + " - " + jsonObject.getJSONObject("data").optString("sowing_cost"));
////                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("sowing_cost")));
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.sowing) + " - " + jsonObject.getJSONObject("data").optString("sowing_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("sowing_cost"))));
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("irrigation_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance();
////                            fin_obj.setName(ctx.getString(R.string.irrigation) + " - " + jsonObject.getJSONObject("data").optString("irrigation_cost"));
////                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("irrigation_cost")));
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.irrigation) + " - " + jsonObject.getJSONObject("data").optString("irrigation_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("irrigation_cost"))));
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("fertilizer_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance();
////                            fin_obj.setName(ctx.getString(R.string.fertilizer) + " - " + jsonObject.getJSONObject("data").optString("fertilizer_cost"));
////                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("fertilizer_cost")));
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.fertilizer) + " - " + jsonObject.getJSONObject("data").optString("fertilizer_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("fertilizer_cost"))));
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("intercultual_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance();
////                            fin_obj.setName(ctx.getString(R.string.intercultivation) + " - " + jsonObject.getJSONObject("data").optString("intercultual_cost"));
////                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("intercultual_cost")));
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.intercultivation) + " - " + jsonObject.getJSONObject("data").optString("intercultual_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("intercultual_cost"))));
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("harvest_cost").equalsIgnoreCase("")) {
////                            Farmx_Finance fin_obj = new Farmx_Finance();
////                            fin_obj.setName(ctx.getString(R.string.harvest) + " - " + jsonObject.getJSONObject("data").optString("harvest_cost"));
////                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("harvest_cost")));
//                            Data.add(new Farmx_Finance(ctx.getString(R.string.harvest) + " - " + jsonObject.getJSONObject("data").optString("harvest_cost"),Float.parseFloat(jsonObject.getJSONObject("data").optString("harvest_cost"))));
//                        }
//                        float sum_finance = 0;
//
//
//
//                        for (int i = 0; i < Data.size(); i++) {
//
//                            sum_finance += Data.get(i).getCost();
//
//
////                            data_chart.add(new ChartData("Fourth", 10, Color.DKGRAY, Color.parseColor("#FFD600")));
//
//                        }
//
//
//
//
////                        simpleChart.setChartData(data_chart);
//
//
//
//                    } else {
//
//                    }
//
//
//
//
//
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//
//            }
//        });
//    }

}