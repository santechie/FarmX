package com.ascentya.AsgriV2.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Mycrops_Mainfragments.Crop_Fragment;
import com.ascentya.AsgriV2.Mycrops_Mainfragments.Finance;
import com.ascentya.AsgriV2.Mycrops_Mainfragments.Land_Fragment;
import com.ascentya.AsgriV2.Mycrops_Mainfragments.Rearing_Fragment;
import com.ascentya.AsgriV2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Mycrops_Main extends AppCompatActivity {
    BottomNavigationView navigation;
    String navigation_to;
    ImageView logo;
    TextView title_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycrops_main);

        logo = findViewById(R.id.logo);
        title_tab = findViewById(R.id.title_tab);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Mycrops_Main.this, Main_Dashboard.class);
                i.putExtra("state", "live");
                startActivity(i);
                finish();
            }
        });

        navigation = (BottomNavigationView) findViewById(R.id.navigation_mycrop);

        if (getIntent().getExtras() != null) {
            navigation_to = getIntent().getStringExtra("page");

        }

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.navigation_land:
                        fragment = new Land_Fragment();
                        title_tab.setText(getString(R.string.land));
                        return loadFragment(fragment);
                    case R.id.navigation_crop:
                        fragment = new Crop_Fragment();
                        title_tab.setText(getString(R.string.members));
                        return loadFragment(fragment);
                    case R.id.navigation_rear:
                        fragment = new Rearing_Fragment();
                        title_tab.setText(getString(R.string.rearing));

                        return loadFragment(fragment);

                    case R.id.navigation_finance:
                        fragment = new Finance();
                        title_tab.setText(getString(R.string.activity));

                        return loadFragment(fragment);

                }
                return false;
            }
        });


        if (navigation_to.equalsIgnoreCase("crop")) {
            title_tab.setText(getString(R.string.land));

            Land_Fragment crops = new Land_Fragment();
            if (crops != null) {
                getSupportFragmentManager()

                        .beginTransaction()
                        .setCustomAnimations(R.anim.fadein,
                                R.anim.fadeout)
                        .replace(R.id.fragment_container, crops)
                        .commit();

            }
            navigation.getMenu().findItem(R.id.navigation_land).setChecked(true);


        } else if (navigation_to.equalsIgnoreCase("finance")) {
            title_tab.setText(getString(R.string.activity));

            Finance crops = new Finance();
            if (crops != null) {
                getSupportFragmentManager()

                        .beginTransaction()
                        .setCustomAnimations(R.anim.fadein,
                                R.anim.fadeout)
                        .replace(R.id.fragment_container, crops)
                        .commit();

            }
            navigation.getMenu().findItem(R.id.navigation_finance).setChecked(true);


        } else {
            title_tab.setText(getString(R.string.rearing));

            Rearing_Fragment fragobj = new Rearing_Fragment();
            if (fragobj != null) {
                getSupportFragmentManager()

                        .beginTransaction()
                        .setCustomAnimations(R.anim.fadein,
                                R.anim.fadeout)
                        .replace(R.id.fragment_container, fragobj)
                        .commit();

            }
            navigation.getMenu().findItem(R.id.navigation_rear).setChecked(true);


        }


    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment

        if (fragment != null) {
            getSupportFragmentManager()

                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein,
                            R.anim.fadeout)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
        return super.dispatchTouchEvent(ev);
    }


}