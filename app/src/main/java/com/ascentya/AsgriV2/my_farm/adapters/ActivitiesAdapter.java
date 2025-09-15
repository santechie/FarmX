package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {

    private Action action;

    public ActivitiesAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_calendar_activity_item, parent, false);
        ActivityViewHolder activityViewHolder = new ActivityViewHolder(view, action);
        return activityViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.update(action.getActivityList().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getActivityList().size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {

        private Action action;
        private ImageView activityIcon;
        private TextView activityNameTv, activityTypeTv, dateTv;
        private Switch completedSt;

        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (action != null) action.onUpdate(getAdapterPosition(), b);
                    }
                };

        public ActivityViewHolder(@NonNull View itemView, Action action) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    action.onActivityClicked(getAdapterPosition());
                }
            });

            activityIcon = itemView.findViewById(R.id.activityImage);
            activityNameTv = itemView.findViewById(R.id.activityName);
            activityTypeTv = itemView.findViewById(R.id.activityType);
            dateTv = itemView.findViewById(R.id.dateTv);
            completedSt = itemView.findViewById(R.id.completedSt);

            completedSt.setOnCheckedChangeListener(onCheckedChangeListener);

            this.action = action;
        }

        public void update(Activity_Cat_Model activity){
            activityIcon.setImageResource(action.getActivityIcon(activity.getService_id()));
            activityNameTv.setText(activity.getPrepare_type());
            activityTypeTv.setText(action.getActivityName(activity.getService_id()));
            if (DateUtils.isSameDay(com.ascentya.AsgriV2.e_market.utils.DateUtils.getDate(activity.getStart_date()),
                    com.ascentya.AsgriV2.e_market.utils.DateUtils.getDate(activity.getEnd_date()))){
                dateTv.setText(activity.getStart_date());
            }else {
                dateTv.setText(activity.getStart_date() + " to " + activity.getEnd_date());
            }
            completedSt.setOnCheckedChangeListener(null);
            completedSt.setChecked(Constants.ActivityStatus.COMPLETED.equals(activity.getStatus()));
            completedSt.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public interface Action{
        ArrayList<Activity_Cat_Model> getActivityList();
        int getActivityIcon(String serviceId);
        String getActivityName(String serviceId);
        void onActivityClicked(int position);
        void onUpdate(int position, boolean checked);
    }
}
