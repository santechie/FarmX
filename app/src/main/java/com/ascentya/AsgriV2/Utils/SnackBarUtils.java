package com.ascentya.AsgriV2.Utils;

import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.snackbar.Snackbar;

public class SnackBarUtils {

   public static void show(BaseActivity activity, String message, String button, Action action){
      if (activity == null) return;
      final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
              .findViewById(android.R.id.content)).getChildAt(0);
      if (viewGroup == null) return;
      Snackbar snackbar = Snackbar.make(viewGroup, message, Snackbar.LENGTH_INDEFINITE);
      snackbar.setAction(button, new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (action != null) action.onClicked();
         }
      });
      snackbar.show();
   }

   public interface Action{
      void onClicked();
   }
}
