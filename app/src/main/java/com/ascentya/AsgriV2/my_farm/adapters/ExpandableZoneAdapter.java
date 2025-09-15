package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ConvertUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandableZoneAdapter extends BaseExpandableListAdapter {

    private Action action;

    public ExpandableZoneAdapter(Action action){
        this.action = action;
    }

    @Override
    public int getGroupCount() {
        return action.getCrops().size();
    }

    @Override
    public int getChildrenCount(int i) {
        ArrayList<Zone_Model> zones = action.getZones(action.getCrops().get(i));
        if (zones == null) return 0;
        return action.getZones(action.getCrops().get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return action.getCrops().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return action.getZones(action.getCrops().get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup parent) {
        Cat_Model catModel = (Cat_Model) getGroup(groupPosition);
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_zone_head_item, parent, false);
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
        }
        ((TextView) view.findViewById(R.id.text)).setText(catModel.getName());
        ((TextView) view.findViewById(R.id.text2)).setText(catModel.getVarietyName());

        Glide.with(view.getContext())
                .load(Webservice.getCrop(catModel.getId()).getIcon())
                .into((ImageView) view.findViewById(R.id.image));
        //((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.white));
        return view;


    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View  convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_zone_child_item, parent, false);
        }

        boolean isLast = getChildrenCount(groupPosition) - 1 == childPosition;

        Cat_Model catModel = (Cat_Model) getGroup(groupPosition);
        Zone_Model Position_Object = (Zone_Model) getChild(groupPosition, childPosition);

        ((TextView) view.findViewById(R.id.title)).setText(Position_Object.getZone_name());
        ((TextView) view.findViewById(R.id.crop_name)).setText(Position_Object.getCrop_name());
        Glide.with(view).load("https://vrjaitraders.com/ard_farmx/" + Position_Object.getCrop_icons_images()).into((CircleImageView) view.findViewById(R.id.crop_icon));

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ((CardView) view.findViewById(R.id.cardView)).getLayoutParams();

        if (isLast){
            layoutParams.bottomMargin = ConvertUtils.dpToPx(5f, view.getContext());
        }else {
            layoutParams.bottomMargin = ConvertUtils.dpToPx(1f, view.getContext());
        }

        ((CardView) view.findViewById(R.id.cardView)).setLayoutParams(layoutParams);

//        view.findViewById(R.id.pests).setOnClickListener(v -> action.report(groupPosition, childPosition));
//        view.findViewById(R.id.remedy).setOnClickListener(v -> action.remedy(groupPosition, childPosition));
        view.findViewById(R.id.history).setOnClickListener(v -> action.history(groupPosition, childPosition));
        view.findViewById(R.id.history).setVisibility(action.canViewPest() ? View.VISIBLE : View.GONE);
        //setListeners(view, catModel, Position_Object);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public interface Action{
        boolean canViewPest();
        ArrayList<Cat_Model> getCrops();
        ArrayList<Zone_Model> getZones(Cat_Model catModel);
        void report(int groupPosition, int childPosition);
        void remedy(int groupPosition, int childPosition);
        void history(int groupPosition, int childPosition);
    }
}
