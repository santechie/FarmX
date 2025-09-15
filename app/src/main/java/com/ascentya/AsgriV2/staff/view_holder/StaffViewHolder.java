package com.ascentya.AsgriV2.staff.view_holder;

import android.view.View;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.CommonRecyclerAdapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.staff.data.Staff;

import androidx.annotation.NonNull;

public class StaffViewHolder extends CommonRecyclerAdapter.CommonViewHolder {

   private Action action;
   private TextView nameTv, emailTv, mobileTv;

   public StaffViewHolder(@NonNull View itemView, Action action) {
      super(itemView);

      this.action = action;
      nameTv = itemView.findViewById(R.id.name);
      emailTv = itemView.findViewById(R.id.email);
      mobileTv = itemView.findViewById(R.id.mobileNumber);
      itemView.setOnClickListener( v -> action.onClicked(getAdapterPosition()));
   }

   @Override
   public void update() {
      super.update();
   }

   public void update(Staff staff){
      nameTv.setText(staff.getName());
      emailTv.setText(staff.getEmail());
      mobileTv.setText(staff.getMobileNumber());
   }

   public interface Action{
      void onClicked(int position);
   }
}
