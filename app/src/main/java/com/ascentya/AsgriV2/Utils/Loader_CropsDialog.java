package com.ascentya.AsgriV2.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.ascentya.AsgriV2.R;

public class Loader_CropsDialog {
    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public Loader_CropsDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String url) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loaderlayout);
        ImageView loading_view = dialog.findViewById(R.id.loading_view);

//        Glide.with().load(url).error(R.drawable.loder_logo).into(loading_view);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //...initialize the imageView form infalted layout


        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */


        //...now load that gif which we put inside the drawble folder here with the help of Glide


        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        dialog.dismiss();
    }
}
