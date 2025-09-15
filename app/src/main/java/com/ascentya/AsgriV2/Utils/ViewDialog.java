package com.ascentya.AsgriV2.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.ascentya.AsgriV2.R;

public class ViewDialog {
    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Activity activity) {
        this.activity = activity;

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showDialog() {

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */


        //...now load that gif which we put inside the drawble folder here with the help of Glide

        if (!dialog.isShowing() && !activity.isFinishing())
            dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


}
