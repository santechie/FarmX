package com.ascentya.AsgriV2.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class CommonDialog {

   private Action action;
   private AlertDialog alertDialog;

   public CommonDialog(Context context,
                       @Nullable String title,
                       @Nullable String message,
                       @Nullable String actionOne,
                       @Nullable String actionTwo, Action action){
      this.action = action;

      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      if (title != null) builder.setTitle(title);
      if (message != null) builder.setMessage(message);

      if (actionOne != null){
         builder.setPositiveButton(actionOne, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               alertDialog.dismiss();
               action.actionOne();
            }
         });
      }

      if (actionTwo != null){
         builder.setNegativeButton(actionTwo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               alertDialog.dismiss();
               action.actionTwo();
            }
         });
      }

      alertDialog = builder.create();

      alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
         @Override
         public void onCancel(DialogInterface dialogInterface) {
            action.onCancelled();
         }
      });

      alertDialog.show();
   }

   public interface Action{
      void actionOne();
      void actionTwo();
      void onCancelled();
   }
}
