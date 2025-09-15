package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Activitys.HomeScreen_Activity;
import com.ascentya.AsgriV2.Adapters.AllCrops_Adapter;
import com.ascentya.AsgriV2.Adapters.CustomListAdapter;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.Dialog_Interface;
import com.ascentya.AsgriV2.Interfaces_Class.Dialog_crops;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import es.dmoral.toasty.Toasty;

public class AddCrops_Dialog {


    AllCrops_Adapter rvAdapter;

    Context contx;
    List<Crops_Main> Data, Suggested_Data;
    List<String> maincrop_list, interCropList;
    Dialog dialog;
    CustomAutoCompleteTextView autocompleteitem;
    ImageView add_maincrop;
    TagContainerLayout maincrop_tag;
    Button save;
    Dialog_crops callback_activity;
    String humidity = "0", ph = "0", temt = "0", pollution = "0", moisture = "0";
    boolean isMain = false;

    EditText area, zone_name;

    public void dialog(Context context, List<String> mainCropList, List<String> interCropList, List<Crops_Main> Data, String title, String humidity, String ph, String temt, String pollution, String moisture, Dialog_crops callback, boolean isMain) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.contx = context;
        this.Data = Data;
        this.humidity = humidity;
        this.ph = ph;
        this.temt = temt;
        this.pollution = pollution;
        this.moisture = moisture;
        this.maincrop_list = mainCropList;
        this.interCropList = interCropList;
        this.callback_activity = callback;
        this.isMain = isMain;

        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.addcrops_layout, null);
        Suggested_Data = new ArrayList<>();


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//         dialog.setContentView(R.layout.activityaddresource_layout);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        final TextView title_head = (TextView) dialog.findViewById(R.id.title);
        ImageView add_maincrop = (ImageView) dialog.findViewById(R.id.add_maincrop);
        maincrop_tag = dialog.findViewById(R.id.maincrop_tag);
        save = dialog.findViewById(R.id.save);
        area = dialog.findViewById(R.id.area);
        zone_name = dialog.findViewById(R.id.zone_name);
        title_head.setText(title);
        maincrop_tag.setEnableCross(true);
        if (isMain){
            maincrop_tag.setTags(maincrop_list);
        }else {
            maincrop_tag.setTags(interCropList);
        }
        CustomListAdapter adapter = new CustomListAdapter(context,
                R.layout.autocompleteitem, Webservice.Data_crops);
        autocompleteitem = (CustomAutoCompleteTextView)
                dialog.findViewById(R.id.search_bar);

        autocompleteitem.setAdapter(adapter);

        autocompleteitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "This crop might produce less yield", Toast.LENGTH_SHORT).show();
            }
        });

        maincrop_tag.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

                Webservice.Searchvalue = text;
                Webservice.Searchicon = Webservice.Data_crops.get(searchFor(text)).getIcon();
                Webservice.Search_id = Webservice.Data_crops.get(searchFor(text)).getCrop_id();
                Intent i = new Intent(context, HomeScreen_Activity.class);
                i.putExtra("crop", false);
                context.startActivity(i);
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("load_main"));
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                maincrop_tag.removeTag(position);
            }
        });


        add_maincrop.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (autocompleteitem.getText().toString().length() > 0) {
                    if (Webservice.crops.contains(autocompleteitem.getText().toString())) {

                        if ((mainCropList == null || !mainCropList.contains(autocompleteitem.getText().toString()))
                        && (interCropList == null || !interCropList.contains(autocompleteitem.getText().toString()))) {
                            maincrop_tag.addTag(autocompleteitem.getText().toString());
                            autocompleteitem.setText("");
                            area.setText("");
                            zone_name.setText("");
                        } else {
                            Toast.makeText(context, "This crop is already added in Main or Inter", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Kindly select from the list", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Kindly fill the all required field", Toast.LENGTH_SHORT).show();
                }
            }
        });


        RecyclerView rvTest = (RecyclerView) dialog.findViewById(R.id.dialog_recycler);
        final ImageView close = (ImageView) dialog.findViewById(R.id.close);
        EditText search = (EditText) dialog.findViewById(R.id.search);
        rvTest.setHasFixedSize(true);


        rvTest.setItemViewCacheSize(20);
        rvTest.setDrawingCacheEnabled(true);
        rvTest.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        rvTest.setLayoutManager(new LinearLayoutManager(context));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        for (int i = 0; i < Webservice.Data_crops.size(); i++) {

            String[] temdatastr = Webservice.Data_crops.get(i).getTempreture().split("-", 2);
            String[] humdatastr = Webservice.Data_crops.get(i).getHumidity().split("-", 2);
            String[] polludatastr = Webservice.Data_crops.get(i).getPollution().split("-", 2);
            String[] moisudatastr = Webservice.Data_crops.get(i).getMoisture().split("-", 2);


            if (floatbetween(ParseFlaot(temt.trim()), Float.parseFloat(temdatastr[0].trim()), Float.parseFloat(temdatastr[1].trim())) && intbetween(ParseInt(humidity.trim()), Integer.parseInt(humdatastr[0].trim()), Integer.parseInt(humdatastr[1].trim())) && intbetween(ParseInt(pollution.trim()), Integer.parseInt(polludatastr[0].trim()), Integer.parseInt(polludatastr[1].trim())) && intbetween(ParseInt(moisture.trim()), Integer.parseInt(moisudatastr[0].trim()), Integer.parseInt(moisudatastr[1].trim()))) {
//                SuggetstedCrops_Model suggest_obj = new SuggetstedCrops_Model();
//                suggest_obj.setName(Webservice.Data_crops.get(i).getName());
//                suggest_obj.setId(Webservice.Data_crops.get(i).getCrop_id());
//                suggest_obj.setDissolved_solids(Webservice.Data_crops.get(i).getDissolved_solids());
//                suggest_obj.setHumidity(Webservice.Data_crops.get(i).getHumidity());
//                suggest_obj.setIcon(Webservice.Data_crops.get(i).getIcon());
//                suggest_obj.setMoisture(Webservice.Data_crops.get(i).getMoisture());
//                suggest_obj.setPollution(Webservice.Data_crops.get(i).getPollution());
//                suggest_obj.setTempreture(Webservice.Data_crops.get(i).getTempreture());
//                suggest_obj.setWaterph(Webservice.Data_crops.get(i).getWaterph());
//
//                suggest_Data.add(suggest_obj);
                Suggested_Data.add(Webservice.Data_crops.get(i));


            } else {


            }
            if (Suggested_Data != null && Suggested_Data.size() > 0) {
                if (Suggested_Data.size() >= 10) {
                    break;
                }
            } else {
//                Suggested_Data.addAll(5, Webservice.Data_crops);
            }


        }


        rvAdapter = new AllCrops_Adapter(context, new Dialog_Interface() {
            @Override
            public void foo(String name) {
                autocompleteitem.setText(name);
            }
        }, Suggested_Data);
        rvTest.setAdapter(rvAdapter);

        save.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if(isMain){
                    if (!maincrop_tag.getTags().isEmpty()){
                        callback_activity.foo(maincrop_tag.getTags());
                        dialog.dismiss();
                    }else {
                        Toasty.error(v.getContext(), "Main Crop Should have at least one crop").show();
                    }
                }else {
                    callback_activity.foo(maincrop_tag.getTags());
                    dialog.dismiss();
                }
            }
        });
    }

    private Integer searchFor(String data) {
        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getName().toLowerCase().trim();
            String C_name = Webservice.Data_crops.get(i).getS_name().toLowerCase().trim();

            if (
                    unitString.equals(data.toLowerCase().trim())
                            || C_name.equals(data.toLowerCase().trim()) ||
                            unitString.toLowerCase().trim().contains(data.toLowerCase().trim()) ||
                            C_name.toLowerCase().trim().contains(data.toLowerCase().trim())) {
                return i;
            }
        }

        return -1;
    }


    public static boolean floatbetween(float i, float minValueInclusive, float maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    public static boolean intbetween(int i, int minValueInclusive, int maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    Float ParseFlaot(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Float.parseFloat(strNumber);
            } catch (Exception e) {
                return -1.0f;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0.00f;
    }

    Integer ParseInt(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Integer.parseInt(strNumber);
            } catch (Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0;
    }
}

