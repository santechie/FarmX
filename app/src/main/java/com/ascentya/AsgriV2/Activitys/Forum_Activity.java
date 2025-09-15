package com.ascentya.AsgriV2.Activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.PlansPagerAdapter;
import com.ascentya.AsgriV2.Forum.Search_Forum;
import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Forum_Activity extends BaseActivity {

    ViewDialog viewDialog;
    List<Cat_Model> Data;
    List<String> cat_Data;
    FloatingActionButton add_forum;
    Dialog dialog;
    EditText forum_title, forum_disc;
    NoDefaultSpinner forum_category;
    CircleImageView attachment;
    SessionManager sm;
    String attachment_path;
    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager viewPager;
    TextView empty;
    ImageView search_forum;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        sm = new SessionManager(this);
        viewPager = findViewById(R.id.viewpager);
        add_forum = findViewById(R.id.add_forum);
        search_forum = findViewById(R.id.search_forum);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forum_Activity.this, Search_Forum.class);
                startActivity(i);

            }
        });

        tab = findViewById(R.id.tabs);
        viewDialog = new ViewDialog(this);
        Data = new ArrayList<>();

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (!getModuleManager().canInsert(Modules.COMMUNITY)) {
            add_forum.setVisibility(View.INVISIBLE);
        }

        add_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUser())
                    addForumPost();
            }
        });

        getmembers();
    }

    public boolean checkUser() {
        return checkSubscription(Modules.COMMUNITY, ModuleManager.ACCESS.INSERT);
    }

    public void addForumPost() {
        LayoutInflater factory = LayoutInflater.from(Forum_Activity.this);
        final View alertDialogView = factory.inflate(R.layout.addforum_layout, null);
        dialog = new Dialog(Forum_Activity.this/*, R.style.DialogSlideAnim*/);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        forum_title = dialog.findViewById(R.id.forum_title);
        forum_category = dialog.findViewById(R.id.forum_category);
        forum_disc = dialog.findViewById(R.id.forum_disc);
        attachment = dialog.findViewById(R.id.attachment);

        ElasticButton addpost = dialog.findViewById(R.id.addpost);

//                spinner_Adapter adapter = new spinner_Adapter(Forum_Activity.this,Data);
//                forum_category.setAdapter(adapter);

        ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(Forum_Activity.this, R.layout.spinner_item,
                cat_Data);

        forum_category.setAdapter(soiltype_adpter);


//                ArrayAdapter<String> irrigation_adpter = new ArrayAdapter(Forum_Activity.this, R.layout.spinner_item,
//                        cat_Data);
//
//                forum_category.setAdapter(irrigation_adpter);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        attachment_path =
                                ImageUtils.setImage(Forum_Activity.this, r, attachment, true);
                              /*  Bitmap bitmap = (BitmapFactory.decodeFile(r.getPath()));
                                attachment.setImageBitmap(bitmap);
                                attachment_path = r.getPath();*/
                    }
                }).show(Forum_Activity.this);
            }
        });

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatesignForm()) {

                    if (forum_category.getSelectedItemPosition() > -1) {
                        add_income(dialog);

                    } else {
                        Toast.makeText(Forum_Activity.this, "Kindly select Category", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


//                Toast.makeText(Forum_Activity.this, "cool", Toast.LENGTH_SHORT).show();

    }

    public void getmembers() {
        tab.removeAllTabs();
        Data = new ArrayList<>();

        if (getModuleManager().canView(Components.Community.MY_FORUM)) {
            Cat_Model obj = new Cat_Model();
            obj.setId("0");
            obj.setName("My forum");
            obj.setValue(Components.Community.MY_FORUM.getValue());
            Data.add(obj);
            tab.addTab(tab.newTab().setText("My forum"));
            System.out.println("Adding: " + obj.getName());
        }
        if (getModuleManager().canView(Components.Community.ALL)) {
            Cat_Model obj = new Cat_Model();
            obj.setId("01");
            obj.setName("All");
            obj.setValue(Components.Community.ALL.getValue());
            Data.add(obj);
            tab.addTab(tab.newTab().setText("All"));
            System.out.println("Adding: " + obj.getName());
        }


        cat_Data = new ArrayList<>();

        viewDialog.showDialog();
        AndroidNetworking.get(Webservice.getcat)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (UserHelper.checkResponse(Forum_Activity.this, jsonObject)) {
                            return;
                        }
                        viewDialog.hideDialog();

                        try {

                            if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Components.Component component = Components
                                            .findComponent(jsonArray.getJSONObject(i).optString("category_value"),
                                                    Modules.COMMUNITY);
                                    if (getModuleManager().canView(component)) {
                                        tab.addTab(tab.newTab().setText(jsonArray.getJSONObject(i).optString("category_title")));
                                        if (getModuleManager().canInsert(component))
                                            cat_Data.add(jsonArray.getJSONObject(i).optString("category_title"));
                                        Cat_Model obj = new Cat_Model();
                                        obj.setId(jsonArray.getJSONObject(i).optString("category_id"));
                                        obj.setName(jsonArray.getJSONObject(i).optString("category_title"));
                                        obj.setValue(jsonArray.getJSONObject(i).optString("category_value"));
                                        Data.add(obj);
                                        System.out.println("Adding: " + obj.getName());
                                    } else {
                                        System.out.println("Discarding: " + jsonArray.getJSONObject(i).optString("category_title"));
                                    }
                                }
                            } else {

                            }
                        } catch (Exception e) {
                            viewDialog.hideDialog();
                            e.printStackTrace();
                        }

                        PlansPagerAdapter adapter = new PlansPagerAdapter
                                (getSupportFragmentManager(), tab.getTabCount(), Data);
//                tab.setupWithViewPager(viewPager);
                        viewPager.setAdapter(adapter);

                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));


                    }

                    @Override
                    public void onError(ANError anError) {

                        viewDialog.hideDialog();
                    }
                });
    }

    private boolean validatesignForm() {


        if (!ValidateInputs.isValidInput(forum_title.getText().toString().trim())) {
            forum_title.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(forum_disc.getText().toString().trim())) {
            forum_disc.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }


    }


    public void add_income(final Dialog dialog) {
        File pan_f = null;

        viewDialog.showDialog();

        if (attachment_path != null && attachment_path.length() > 0) {
            pan_f = new File(attachment_path);
        }

        String userId = sm.getUser().getId();
        String forumTitle = StringEscapeUtils.escapeJava(forum_title.getText().toString());
        String forumDescription = StringEscapeUtils.escapeJava(forum_disc.getText().toString());
        String category = forum_category.getSelectedItem().toString();
        File forumAttachment = pan_f;

        System.out.println("user_id: " + userId);
        System.out.println("forum_title: " + forumTitle);
        System.out.println("forum_description: " + forumDescription);
        System.out.println("category: " + category);
        System.out.println("forum_attachment: " + attachment_path);

        AndroidNetworking.upload(Webservice.post_forum)
                .addMultipartParameter("user_id", userId)
                .addMultipartParameter("forum_title", forumTitle)
                .addMultipartParameter("forum_description", forumDescription)
                .addMultipartParameter("category", category)
                .addMultipartFile("forum_attachment", forumAttachment)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (UserHelper.checkResponse(Forum_Activity.this, jsonObject)) {
                            return;
                        }

                        viewDialog.hideDialog();

                        System.out.println("Forum Response:\n" + jsonObject);
                        try {
                            if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                                attachment_path = "";

                                dialog.dismiss();
                                Toasty.success(Forum_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                                getmembers();
                            } else {
                                Toasty.error(Forum_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                            }


                        } catch (Exception e) {

                            Toasty.error(Forum_Activity.this, "Something went Wrong!", Toast.LENGTH_SHORT, true).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        viewDialog.hideDialog();
                        Toasty.error(Forum_Activity.this, "Something went Wrong! " + anError.getErrorCode(), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

}