package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Activitys.Activity_Farmx_Modified;
import com.ascentya.AsgriV2.Activitys.DeviceDataActivity;
import com.ascentya.AsgriV2.Activitys.Finance_Dummy;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Add_Income;
import com.ascentya.AsgriV2.Utils.Add_ZonesCrop;
import com.ascentya.AsgriV2.Utils.ConvertUtils;
import com.ascentya.AsgriV2.Utils.CropUtils;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.farmx.my_lands.Cropwise_Zones;
import com.ascentya.AsgriV2.farmx.postharvest_diseas.PestsDisease_Activity;
import com.ascentya.AsgriV2.my_land.activities.MyLandActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyLands_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Maincrops_Model> items = new ArrayList<>();
    private Context ctx;
    private String user_id;
    private ViewDialog viewDialog;
    public MyLands_Adapter(Context context, List<Maincrops_Model> items, Boolean mi, String user_id, ViewDialog viewDialog) {
        this.items = items;
        ctx = context;
        this.user_id = user_id;
        this.viewDialog = viewDialog;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView crop_name, acre, landid, finance, intercrop_name;
        CircleImageView crop_icon, intercrop_icon;
        ImageView soiltype, zones, addzones;
        FloatingActionButton activity, finance_, pests, dideases, myincome;


        public ViewHolder(View v) {
            super(v);
            soiltype = (ImageView) v.findViewById(R.id.soiltype);
            finance_ = (FloatingActionButton) v.findViewById(R.id.finance_);
            activity = (FloatingActionButton) v.findViewById(R.id.activity);
            crop_icon = (CircleImageView) v.findViewById(R.id.crop_icon);
            pests = (FloatingActionButton) v.findViewById(R.id.pests);
            dideases = (FloatingActionButton) v.findViewById(R.id.diseases);
            myincome = (FloatingActionButton) v.findViewById(R.id.myincome);
            zones = (ImageView) v.findViewById(R.id.zones);
            addzones = (ImageView) v.findViewById(R.id.addzones);
            intercrop_name = (TextView) v.findViewById(R.id.intercrop_name);
            crop_name = (TextView) v.findViewById(R.id.crop_name);
            acre = (TextView) v.findViewById(R.id.acre);
            finance = (TextView) v.findViewById(R.id.finance);
            landid = (TextView) v.findViewById(R.id.landid);
            crop_icon = (CircleImageView) v.findViewById(R.id.crop_icon);
            intercrop_icon = (CircleImageView) v.findViewById(R.id.intercrop_icon);

//            landid.setMovementMethod(new ScrollingMovementMethod());

            int padding = ConvertUtils.dpToPx(15f, ctx);
            TextView textView = new TextView(ctx);
            textView.setTextSize(16);
            textView.setPadding(padding, padding/2, padding, padding/2);
            textView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
            PopupWindow popupWindow = new PopupWindow(ctx);
            popupWindow.setContentView(textView);

            landid.setOnClickListener(view ->{
                if (!popupWindow.isShowing()) {
                    textView.setText(((TextView) view).getText());
                    popupWindow.showAsDropDown(view);
                    new Handler().postDelayed(popupWindow::dismiss, 1000);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylands_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Maincrops_Model Position_Object = items.get(position);
            System.out.println("Position Object: " + GsonUtils.getGson().toJson(Position_Object));
            System.out.println("Position Object: " + Position_Object.getMaincrop());

            view.activity.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                   /* Intent i = new Intent(ctx, Activity_Farmx_Modified.class);
                    i.putExtra("usercrop", Position_Object.getId());
                    i.putExtra("acre", Position_Object.getAcre_count());

                    i.putExtra("crop_id", Position_Object.getMaincrop());
                    i.putExtra("crop_type", "main_crop");


                    ctx.startActivity(i);*/

                    Activity_Farmx_Modified.open(ctx,
                            Position_Object,
                            Position_Object.getId(),
                            Position_Object.getLand_name(),
                            true,
                            CropUtils.getLandCropModelList(Position_Object.getMainCropsString()),
                            CropUtils.getLandCropModelList(Position_Object.getInterCropsString()),
                            Position_Object.getMaincrop(),
                            Position_Object.getAcre_count(),
                            "main_crop");
                }
            });

            view.activity.setOnLongClickListener(v -> {
                MyLandActivity.open(v.getContext(), Position_Object);
                return false;
            });

           /* view.activity.setOnLongClickListener(v -> {
                Activity_Farmx_Modified.open(ctx,
                    "2",
                    Position_Object.getMaincrop(),
                    Position_Object.getAcre_count(),
                    "main_crop");
                return true;
            });*/

            view.pests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent i = new Intent(ctx, PestsDisease_Activity.class);
                    i.putExtra("title", "Pests");
                    i.putExtra("type", true);
                    i.putExtra("crop_id", Position_Object.getLand_name());
                    ctx.startActivity(i);*/
                    openPestDisease(true, Position_Object);
                }
            });

            view.dideases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Intent i = new Intent(ctx, PestsDisease_Activity.class);
                    i.putExtra("title", "Diseases");
                    i.putExtra("type", true);
                    i.putExtra("crop_id", Position_Object.getLand_name());
                    ctx.startActivity(i);*/
                    openPestDisease(true, Position_Object);
                }
            });


            view.addzones.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Add_ZonesCrop obj = new Add_ZonesCrop();
                    obj.dialog(ctx, "Add zone",
                            Position_Object.getLand_name(), Position_Object.getId(),
                            Position_Object.getMaincrop(), user_id,
                            viewDialog, view.crop_name.getText().toString());

                }
            });

            view.myincome.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    //Toasty.normal(ctx, Position_Object.getMaincrop()).show();
                    Add_Income obj = new Add_Income();
                    obj.dialog(ctx, "Add Income", Position_Object.getId(),
                            null, user_id, viewDialog,
                            view.crop_name.getText().toString(),
                            Position_Object, null);
                }
            });

            view.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {
                        Activity_Farmx_Modified.open(view.getContext(),
                                Position_Object,
                                Position_Object.getId(),
                                Position_Object.getLand_name(),
                                true,
                                CropUtils.getLandCropModelList(Position_Object.getMainCropsString()),
                                CropUtils.getLandCropModelList(Position_Object.getInterCropsString()),
                                Position_Object.getMaincrop(),
                                Position_Object.getAcre_count(),
                                "main_crop");
                      //Todo Dummy Land
                    } else {
                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            view.finance_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {


                        Intent i = new Intent(ctx, Finance_Dummy.class);
                        i.putExtra("usercrop", Position_Object.getId());
                        i.putExtra("is_land", true);
                        i.putExtra("crop_id", Position_Object.getMaincrop());
                        i.putExtra("crop_type", "main_crop");

                        ctx.startActivity(i);
                    } else {
                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            try {
                view.crop_name.setText(Webservice.Data_crops.get(searchFor(Position_Object.getMaincrop())).getName());
                System.out.println("dssdfsdfsdf"+Webservice.Data_crops.get(searchFor(Position_Object.getMaincrop())).getName());
                Glide.with(ctx).load(Webservice.Data_crops.get(searchFor(Position_Object.getMaincrop())).getIcon()).into(view.crop_icon);
                if (!Position_Object.getIntercrop().equalsIgnoreCase("")) {
                    Glide.with(ctx).load(Webservice.Data_crops.get(searchFor(Position_Object.getIntercrop())).getIcon()).into(view.intercrop_icon);

                    view.intercrop_name.setText(Webservice.Data_crops.get(searchFor(Position_Object.getIntercrop())).getName());

                } else {
                    view.intercrop_name.setText("NA");
                    Glide.with(ctx).load("").error(R.drawable.agripedia).into(view.intercrop_icon);

                }

            } catch (Exception e) {
            }

            view.landid.setText(StringUtils.capitalize(Position_Object.getLand_name()));

            System.out.println("dsflksdjfkldsjflkdsjf"+StringUtils.capitalize(Position_Object.getLand_name()));

            view.zones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ctx, Cropwise_Zones.class);
                    i.putExtra("land", items.get(position).getId());
                    ctx.startActivity(i);

                }
            });
            view.soiltype.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
//                    Intent i = new Intent(ctx, Myland_Master.class);
//                    i.putExtra("land", items.get(position).getId());

                    DeviceDataActivity.open(ctx, items.get(position).getId());
                    // Todo Device Data
                    //DeviceDataActivity.open(ctx, "51");

//                    ctx.startActivity(i);
                }
            });

           /* view.soiltype.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DeviceDataActivity.open(ctx, "51");
                    return false;
                }
            });*/

//            view.soiltype.setText(Position_Object.getSoiltype());
//            view.acre.setText(Position_Object.getAcre_count());

//            view.activity.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {
//
//
//
//                        Intent i = new Intent(ctx, Farmx_Activity.class);
//                        i.putExtra("usercrop", Position_Object.getId());
//                        i.putExtra("acre", Position_Object.getAcre_count());
//                        if (main_inter) {
//                            i.putExtra("crop_id", Position_Object.getMaincrop());
//                        } else {
//                            i.putExtra("crop_id", Position_Object.getIntercrop());
//                        }
//
//                        ctx.startActivity(i);
//                    } else {
//                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//            view.finance.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (NetworkDetector.isNetworkStatusAvialable(ctx)) {
//
//
//                        Intent i = new Intent(ctx, Finance_Activity.class);
//                        i.putExtra("usercrop", Position_Object.getId());
//                        if (main_inter) {
//                            i.putExtra("crop_id", Position_Object.getMaincrop());
//                        } else {
//                            i.putExtra("crop_id", Position_Object.getIntercrop());
//                        }
//                        ctx.startActivity(i);
//                    }
//                    else {
//                        Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });


        }
    }

    private void openPestDisease(boolean main_inter, Maincrops_Model Position_Object){
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

}