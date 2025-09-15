package com.ascentya.AsgriV2.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.DashBoard_Adapter;
import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Lang_token.Preferred_LangToken;
import com.ascentya.AsgriV2.Models.DashBoard_Model;
import com.ascentya.AsgriV2.Models.Lang_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Lang_DialogMaster;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.beta.NewFeaturesActivity;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.dialog.CropAndVarietySelectDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;
import com.ascentya.AsgriV2.utility.activity.UtilityActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.VIEW_LAND;

public class Main_Dashboard extends BaseActivity {
    RecyclerView dashboard_recycler;
    DashBoard_Adapter dashBoardAdapter;
    List<DashBoard_Model> Data = new ArrayList<>();
    private ActionBar actionBar;
    private Toolbar toolbar;
    public Menu toggleMenu;
    SessionManager sm;
    LinearLayout lang_layout;
    TextView lang;
    Preferred_LangToken lang_token;

    ViewDialog dialog;

    String User_Validate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__dashboard);

        dashboard_recycler = findViewById(R.id.dashboard_recycler);
        dashboard_recycler.setLayoutManager(new GridLayoutManager(Main_Dashboard.this, 2));
        dashboard_recycler.setHasFixedSize(true);

        lang_layout = findViewById(R.id.lang_layout);
        lang = findViewById(R.id.lang);
        dialog = new ViewDialog(this);
        lang_token = new Preferred_LangToken(Main_Dashboard.this);
        sm = new SessionManager(this);
        initToolbar();
        initNavigationMenu();
   //     getSubscriptionManager().showTrial();


   //    showCropSelectionDialog();

//        lang_token = new Preferred_LangToken(this);

//        if (getIntent().getBooleanExtra("location", false)) {
//            if (Webservice.state_id.equalsIgnoreCase("Tamil Nadu")) {
//                setLocale("ta");
//                lang.setText("Ta");
//            } else if (Webservice.state_id.equalsIgnoreCase("meghalaya")) {
//                setLocale("hi");
//                lang.setText("Hi");
//            } else {
//                lang.setText("En");
//                setLocale("en_GB");
//            }
//        } else {
//            if (lang_token != null) {
//
//
//                if (lang_token.getToken().equalsIgnoreCase("ta")) {
//                    lang.setText("Ta");
//                    setLocale("ta");
//                } else if (lang_token.getToken().equalsIgnoreCase("hi")) {
//                    lang.setText("Hi");
//                    setLocale("hi");
//                } else {
//                    lang.setText("En");
//                    setLocale("en_GB");
//                }
//            } else {
//
//                lang.setText("En");
//                setLocale("en_GB");
//            }
//        }

        dashBoardAdapter = new DashBoard_Adapter(Main_Dashboard.this, Data, sm, dialog);
        dashboard_recycler.setAdapter(dashBoardAdapter);

        lang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Lang_Model> Data = new ArrayList();
                Lang_Model obj = new Lang_Model();
                obj.setName(getString(R.string.English));
                obj.setLang_id("en_GB");

                Data.add(obj);

                obj = new Lang_Model();
                obj.setName(getString(R.string.Hindi));
                obj.setLang_id("hi");

                Data.add(obj);

                obj = new Lang_Model();
                obj.setName(getString(R.string.tamil));
                obj.setLang_id("ta");

                Data.add(obj);

                Lang_DialogMaster obj1 = new Lang_DialogMaster();
                obj1.dialog(Main_Dashboard.this, Data, getString(R.string.language));
            }
        });

        if (sm.getUser() != null) {
            reset();
        } else {
            loadDashboardMenu(null);
        }
    }

    private void showCropSelectionDialog(){
        CropAndVarietySelectDialog.Configuration  configuration =
                new CropAndVarietySelectDialog.Configuration();

        configuration.displayVarieties = true;
        configuration.multiSelection = true;

        CropAndVarietySelectDialog cropAndVarietySelectDialog =
                new CropAndVarietySelectDialog(this)
                        .setConfiguration(configuration)
                        .setAction(new CropAndVarietySelectDialog.Action() {
                            @Override
                            public void onSelected(HashMap<String, ArrayList<String>> selectedVarieties) {

                            }

                            @Override
                            public void onClose() {

                            }
                        });

        cropAndVarietySelectDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateDashboard(){
        dashBoardAdapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("ASGRI Dashboard");
    }


    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        Menu nv = nav_view.getMenu();
        MenuItem item1 = nv.findItem(R.id.navigation_logout);
        MenuItem info = nv.findItem(R.id.navigation_network);

        MenuItem pest = nv.findItem(R.id.navigation_job);
        MenuItem disease = nv.findItem(R.id.navigation_buy);

        MenuItem utility = nv.findItem(R.id.navigation_utility);

        utility.setVisible(getModuleManager().canView(Modules.UTILITY));

        userTypeAction(new UserTypeAction() {
            @Override
            public void onGuest() {
                info.setVisible(false);
                pest.setVisible(false);
                disease.setVisible(false);
            }

            @Override
            public void onRegistered() {

            }

            @Override
            public void onPaid() {

            }
        });

        info.setTitle(R.string.profile);
        pest.setTitle(R.string.notification);

        disease.setTitle(R.string.setting);

        if (sm.getUser() != null) {
            item1.setTitle(getString(R.string.logout));

        } else {
            item1.setTitle(getString(R.string.login));

        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.logout))) {
                    SessionManager sm = new SessionManager(Main_Dashboard.this);
                    sm.clearall();
                    lang_token.clearall();
                    actionBar.setTitle(item.getTitle());
                    drawer.closeDrawers();
                    Intent i = new Intent(Main_Dashboard.this, Formx_Login_Activity.class);
                    startActivity(i);
                    finish();

                } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.login))) {
                    sm.setGuestId(null);
                    Intent i = new Intent(Main_Dashboard.this, Formx_Login_Activity.class);
                    startActivity(i);
                    finish();
                } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.profile))) {
                    drawer.closeDrawers();
                    Intent i = new Intent(Main_Dashboard.this, MyProfile.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.navigation_utility) {
                    drawer.closeDrawers();
                    if (checkSubscription(Modules.UTILITY))
                        openActivity(UtilityActivity.class, true);
//                    openWithAccess(UtilityActivity.class, true, UTILITY);
                    /*Intent i = new Intent(Main_Dashboard.this, UtilityActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(i);*/
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if(item.getItemId() == R.id.navigation_buy){
                    openWithAccess(NewFeaturesActivity.class, VIEW_LAND);
                    drawer.closeDrawers();
                } /* else if(item.getItemId() == R.id.manage_staff){
                    openActivity(ManageStaffActivity.class);
                    drawer.closeDrawers();
                }*/ else {
                    actionBar.setTitle(item.getTitle());
                    drawer.closeDrawers();
                    Toast.makeText(Main_Dashboard.this,
                            getString(R.string.noupdate), Toast.LENGTH_SHORT).show();
                }


                return true;
            }
        });


    }

    public void setLocale(String localeName) {
        lang_token.setToken(localeName);

        Locale current = getResources().getConfiguration().locale;

        if (localeName.equalsIgnoreCase(current.toString())) {

        } else {

            deleteTask delete = new deleteTask();
            delete.execute();
            Locale locale;
            //Log.e("Lan",session.getLanguage());
            locale = new Locale(localeName);
            Configuration config = new Configuration(this.getResources().getConfiguration());
            Locale.setDefault(locale);
            config.setLocale(locale);

            this.getBaseContext().getResources().updateConfiguration(config,
                    this.getBaseContext().getResources().getDisplayMetrics());

        }


    }

    public class deleteTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            //creating a task
            if(getApplicationContext() == null) return null;

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .taskDao()
                    .delete();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public void reset() {


        //AndroidNetworking.post(Webservice.getuserauthenticate)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Authentication/userauthenticate")
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                System.out.println("User Authenticate: " + jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        User_Validate = jsonObject.optString("user_status");
                        if (User_Validate.equalsIgnoreCase("inactive")) {

                            Intent i = new Intent(Main_Dashboard.this, Expired_Page.class);
                            startActivity(i);
                            onBackPressed();

                        } else if (User_Validate.equalsIgnoreCase("pending")) {
                            Intent i = new Intent(Main_Dashboard.this, Random_Verification.class);
                            startActivity(i);
                            onBackPressed();
                        } else {
//                            Data = new ArrayList<>();
//
//                            DashBoard_Model obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.AGRIPEDIA));
//                            obj.setIcon(R.drawable.agripedia);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.my_crops));
//                            obj.setIcon(R.drawable.mycrops);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.chats));
//                            obj.setIcon(R.drawable.chat);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.scheme));
//                            obj.setIcon(R.drawable.schimes);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.forum));
//                            obj.setIcon(R.drawable.forum);
//                            Data.add(obj);
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.mnews));
//                            obj.setIcon(R.drawable.marketnews);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.buysell));
//                            obj.setIcon(R.drawable.buysell);
//                            Data.add(obj);
//
//                            obj = new DashBoard_Model();
//                            obj.setName(getString(R.string.history));
//                            obj.setIcon(R.drawable.history);
//                            Data.add(obj);
//
//                            dashBoardAdapter = new DashBoard_Adapter(Main_Dashboard.this, Data, sm, dialog);
//                            dashboard_recycler.setAdapter(dashBoardAdapter);
                            userprivileges();
                        }
                    } else {
                        Toasty.success(Main_Dashboard.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    //working
    public void userprivileges() {
       // AndroidNetworking.post(Webservice.userprivileges)

        System.out.println("errerer"+sm.getUser().getId());
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Agripedia/userprivileges")
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                System.out.println("Userprivileges: " + jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Data.clear();

                        DashBoard_Model obj = new DashBoard_Model();
                        obj.setName(getString(R.string.AGRIPEDIA));
                        obj.setIcon(R.drawable.agripedia);
                        Data.add(obj);

                        if (jsonObject.getJSONObject("data").optString("mycrops")
                                .equalsIgnoreCase("1")){
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.my_farm));
                            obj.setIcon(R.drawable.mycrops);
                            Data.add(obj);
                        }


                        if (jsonObject.getJSONObject("data").optString("chat")
                                .equalsIgnoreCase("1")){
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.chats));
                            obj.setIcon(R.drawable.chat);
                            Data.add(obj);
                        }
                        if (jsonObject.getJSONObject("data").optString("scheme")
                                .equalsIgnoreCase("1")){
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.scheme));
                            obj.setIcon(R.drawable.schimes);
                            Data.add(obj);
                        }
                        if (jsonObject.getJSONObject("data").optString("forum").equalsIgnoreCase("1")){
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.forum));
                            obj.setIcon(R.drawable.forum);
                            Data.add(obj);
                        }

                        if (jsonObject.getJSONObject("data").optString("marketnews").equalsIgnoreCase("1")) {
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.mnews));
                            obj.setIcon(R.drawable.marketnews);
                            Data.add(obj);
                        }

                        if (jsonObject.getJSONObject("data").optString("buysell").equalsIgnoreCase("1")) {
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.buysell));
                            obj.setIcon(R.drawable.buysell);
                            Data.add(obj);
                        }

                        if (jsonObject.getJSONObject("data").optString("history")
                                .equalsIgnoreCase("1")) {
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.history));
                            obj.setIcon(R.drawable.history);
                            Data.add(obj);

                        }

                        if (jsonObject.getJSONObject("data").optString("emarket").equalsIgnoreCase("1")) {
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.emarket));
                            obj.setIcon(R.drawable.ic_cart);
                            Data.add(obj);
                        }

                        if (jsonObject.getJSONObject("data").optString("utility").equalsIgnoreCase("1")) {
                            obj = new DashBoard_Model();
                            obj.setName(getString(R.string.utility));
                            obj.setIcon(R.drawable.ic_utility);
                            Data.add(obj);
                        }

                        updateDashboard();

                    } else {
                        loadDashboardMenu(null);
                        updateDashboard();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    private void loadDashboardMenu(@Nullable JSONObject jsonObject){
        Data.clear();

        if (getModuleManager().canView(Modules.AGRIPEDIA)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.AGRIPEDIA));
            obj.setIcon(R.drawable.agripedia);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.MY_FARM)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.my_farm));
            obj.setIcon(R.drawable.mycrops);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.CHATS)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.chats));
            obj.setIcon(R.drawable.chat);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.SCHEME)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.scheme));
            obj.setIcon(R.drawable.schimes);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.COMMUNITY)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.forum));
            obj.setIcon(R.drawable.forum);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.MARKET_NEWS)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.mnews));
            obj.setIcon(R.drawable.marketnews);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.BUY_AND_SELL)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.buysell));
            obj.setIcon(R.drawable.buysell);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.HISTORY)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.history));
            obj.setIcon(R.drawable.history);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.EMARKET)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.emarket));
            obj.setIcon(R.drawable.ic_cart);
            Data.add(obj);
        }

        if (getModuleManager().canView(Modules.UTILITY)) {
            DashBoard_Model obj = new DashBoard_Model();
            obj.setName(getString(R.string.utility));
            obj.setIcon(R.drawable.ic_utility);
            Data.add(obj);
        }

        updateDashboard();
    }

}
