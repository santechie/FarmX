package com.ascentya.AsgriV2.Forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ForumCommends_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Delete_Comments;
import com.ascentya.AsgriV2.Models.ForumCommends_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.EndlessRecyclerViewScroll;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Detailed_Forum extends BaseActivity {
    TextView title, date, desc;
    EditText commends;
    Button post;
    RecyclerView commends_recycler;
    SessionManager sm;
    String forum_id;
    ViewDialog viewDialog;
    ForumCommends_Adapter adapter;
    List<ForumCommends_Model> Data;
    Boolean Adapter_check;
    ImageView goback;
    Integer count_;
    private boolean loading = true;
    NestedScrollView nested_view;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    ImageView forum_attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__forum);
        title = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        forum_attachment = findViewById(R.id.forum_attachment);
        nested_view = findViewById(R.id.nested_view);
        commends_recycler = findViewById(R.id.commends_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        commends_recycler.setLayoutManager(mLayoutManager);

        commends_recycler.setHasFixedSize(true);
        commends_recycler.setItemViewCacheSize(20);
        commends_recycler.setDrawingCacheEnabled(true);
        commends_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Adapter_check = false;

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewDialog = new ViewDialog(this);
        sm = new SessionManager(this);
        date = findViewById(R.id.date);
        desc = findViewById(R.id.desc);
        commends = findViewById(R.id.commends);
        post = findViewById(R.id.post);


        nested_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {


                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {


                            if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {

                                loading = false;
                                get_commends(String.valueOf(totalItemCount), false);

                            }
                        }
                    }
                }
            }
        });


        commends_recycler.addOnScrollListener(new EndlessRecyclerViewScroll() {
            @Override
            public void onLoadMore(int current_page) {

//
//                Toast.makeText(Detailed_Forum.this, "clicked", Toast.LENGTH_SHORT).show();
//                get_commends(String.valueOf(Data.size()));

//                getPlanList(productsList.size());
            }
        });


        post.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View view) {

                if (commends.getText().length() > 0) {
                    add_commends(commends.getText().toString());
                } else {
                    Toast.makeText(Detailed_Forum.this, "kindly write something to reply", Toast.LENGTH_SHORT).show();
                }

            }
        });
        forum_id = getIntent().getStringExtra("forum_id");
        title.setText(StringUtils.capitalize(getIntent().getStringExtra("forum_title")));
        desc.setText(getIntent().getStringExtra("forum_desc"));
        date.setText(getIntent().getStringExtra("forum_date"));
        Glide.with(this).load(Webservice.forumimagebase_path + getIntent().getStringExtra("forum_attachment")).into(forum_attachment);

        Data = new ArrayList<>();
        get_commends("0", true);

        forum_attachment.setOnClickListener(view ->
                DIalogwith_Image.showImageViewer(this, title.getText().toString(), Webservice.forumimagebase_path + getIntent().getStringExtra("forum_attachment")));
    }

    public void add_commends(String commend) {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.get_addcommends)
                .addBodyParameter("user_id", sm.getUser().getId())
                .addBodyParameter("forum_id", forum_id)
                .addBodyParameter("comments", StringEscapeUtils.escapeJava(commend))

                .addBodyParameter("user_name", sm.getUser().getFirstname())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                if (UserHelper.checkResponse(Detailed_Forum.this, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        commends.setText("");
                        Data = new ArrayList<>();
                        get_commends("0", true);
                        Toasty.success(Detailed_Forum.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
//                        Toasty.error(Detailed_Forum.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
            }
        });
    }

    public void get_commends(final String count, final Boolean check) {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.get_commends)
                .addBodyParameter("user_id", sm.getUser().getId())
                .addBodyParameter("forum_id", forum_id)
                .addBodyParameter("count", count)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(Detailed_Forum.this, jsonObject)){
                    return;
                }
                viewDialog.hideDialog();

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        count_ = Data.size();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            ForumCommends_Model obj = new ForumCommends_Model();
                            obj.setComments_id(jsonArray.getJSONObject(i).optString("comments_id"));
                            obj.setCommends(StringEscapeUtils.unescapeJava(jsonArray.getJSONObject(i).optString("comments")));
                            obj.setDate(jsonArray.getJSONObject(i).optString("created_at"));
                            obj.setName(jsonArray.getJSONObject(i).optString("user_name"));
                            obj.setCommend_userid(jsonArray.getJSONObject(i).optString("user_id"));

                            Data.add(obj);
                        }


                        if (Data.size() > 0) {
                            commends_recycler.setVisibility(View.VISIBLE);
                            loading = true;
                            if (check) {

                                adapter = new ForumCommends_Adapter(Detailed_Forum.this, Data, sm.getUser().getId(), viewDialog, new Delete_Comments() {
                                    @Override
                                    public void reset_adapter() {
                                        Data = new ArrayList<>();
                                        get_commends("0", true);
                                    }
                                });

                                commends_recycler.setAdapter(adapter);
                            } else {


                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            commends_recycler.setVisibility(View.GONE);
                        }

//                        Toasty.success(Detailed_Forum.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {

//                        adapter.notifyDataSetChanged();
//                        Toasty.error(Detailed_Forum.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
            }
        });
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}