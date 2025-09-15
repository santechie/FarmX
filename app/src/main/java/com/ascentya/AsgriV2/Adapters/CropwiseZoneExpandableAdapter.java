package com.ascentya.AsgriV2.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Remedy_Dialog;
import com.ascentya.AsgriV2.Utils.Reports_Dialog;
import com.ascentya.AsgriV2.farmx.my_lands.ReportHistory_Activity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropwiseZoneExpandableAdapter extends BaseExpandableListAdapter {

    private int userId = 0;
    private int landId = 0;
    private LinkedHashMap<Integer, Cat_Model> cropNames = new LinkedHashMap();
    private LinkedHashMap<Integer, ArrayList<Zone_Model>> cropDatas = new LinkedHashMap();

    public void updateData(HashMap<Integer, Cat_Model> cropNames,
                           HashMap<Integer, ArrayList<Zone_Model>> cropDatas){
        this.cropNames.clear();
        this.cropDatas.clear();

        this.cropNames.putAll(cropNames);
        this.cropDatas.putAll(cropDatas);

        notifyDataSetChanged();
    }

    public int getLandId() {
        return landId;
    }

    public void setLandId(int landId) {
        this.landId = landId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getGroupCount() {
        return cropNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Zone_Model> zoneModels = cropDatas.get(getGroupIdFromPosition(groupPosition));
        return zoneModels != null ? zoneModels.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cropNames.values().toArray()[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Zone_Model> zoneModels = cropDatas.get(getGroupIdFromPosition(groupPosition));
        return zoneModels != null ? zoneModels.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.green_farmx));
        }
        ((TextView) view.findViewById(android.R.id.text1)).setText(((Cat_Model) getGroup(groupPosition)).getName());
        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.white));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.zone_row, parent, false);
        }

        Cat_Model catModel = (Cat_Model) getGroup(groupPosition);
        Zone_Model Position_Object = (Zone_Model) getChild(groupPosition, childPosition);

        ((TextView) view.findViewById(R.id.title)).setText(Position_Object.getZone_name());
        ((TextView) view.findViewById(R.id.crop_name)).setText(Position_Object.getCrop_name());
        Glide.with(view).load("https://vrjaitraders.com/ard_farmx/" + Position_Object.getCrop_icons_images()).into((CircleImageView) view.findViewById(R.id.crop_icon));

        setListeners(view, catModel, Position_Object);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public int getGroupIdFromPosition(int groupPosition){
        if (groupPosition < cropNames.size()){
            return Integer.parseInt(((Cat_Model) cropNames.values().toArray()[groupPosition]).getId());
        }
        return 0;
    }

    private void setListeners(View view, Cat_Model catModel, Zone_Model zoneModel){
        view.findViewById(R.id.remedy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Intent i = new Intent(ctx, Farmx_Mycro.class);
//                    ctx.startActivity(i);
                Remedy_Dialog obj = new Remedy_Dialog();
                obj.dialog(view.getContext(), "Remedy", String.valueOf(landId), zoneModel.getZone_id(), String.valueOf(userId));

            }
        });

        view.findViewById(R.id.history).setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(view.getContext(), ReportHistory_Activity.class);
                i.putExtra("zone", zoneModel.getZone_id());
                i.putExtra("land", String.valueOf(landId));
                i.putExtra("crop_name", catModel.getName());
                i.putExtra("zone_name", zoneModel.getZone_name());
                view.getContext().startActivity(i);

            }
        });

        view.findViewById(R.id.pests).setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Reports_Dialog obj = new Reports_Dialog();
                obj.dialog(view.getContext(), "Report", String.valueOf(landId), null, zoneModel.getZone_id(), String.valueOf(userId));

            }
        });
    }
}
