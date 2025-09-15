package com.ascentya.AsgriV2.Forum;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Forum_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Delete_Post;
import com.ascentya.AsgriV2.Models.Forum_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Search_Forum extends AppCompatActivity {
    RecyclerView searchforum_recycler;
    ImageButton search;
    EditText forum_search;
    Dialog dialog_c;

    List<Forum_Model> Data;
    Forum_Adapter adapter;
    SessionManager sm;
    TextView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_forum);
        searchforum_recycler = findViewById(R.id.searchforum_recycler);
        search = findViewById(R.id.search);
        forum_search = findViewById(R.id.forum_search);
        noresult = findViewById(R.id.noresult);
        dialog_c = new Dialog(this);
        sm = new SessionManager(this);
        searchforum_recycler.setLayoutManager(new LinearLayoutManager(this));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (forum_search.getText().length() > 0) {
                    add_income(forum_search.getText().toString());

                } else {
                    Toast.makeText(Search_Forum.this, "Please write something to search", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void add_income(String term) {
        dialog_c.show();
        Data = new ArrayList<>();


        AndroidNetworking.post(Webservice.get_searchedforum)
                .addBodyParameter("search_term", term)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_c.hide();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Forum_Model obj = new Forum_Model();

                            obj.setForum_id(jsonArray.getJSONObject(i).optString("forum_id"));
                            obj.setForum_posterid(jsonArray.getJSONObject(i).optString("user_id"));
                            obj.setForum_title(jsonArray.getJSONObject(i).optString("forum_title"));
                            obj.setForum_description(jsonArray.getJSONObject(i).optString("forum_description"));
                            obj.setCategory(jsonArray.getJSONObject(i).optString("category"));
                            obj.setForum_attachment(jsonArray.getJSONObject(i).optString("forum_attachment"));
                            obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                            Data.add(obj);
                        }

                    } else {


//                        Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                    if (Data.size() > 0) {
                        noresult.setVisibility(View.GONE);
                        searchforum_recycler.setVisibility(View.VISIBLE);
                        adapter = new Forum_Adapter(Search_Forum.this, Data, sm.getUser().getId(), dialog_c, new Delete_Post() {
                            @Override
                            public void reset_adapter() {
                                Data.clear();
                                add_income(forum_search.getText().toString());
                            }
                        });
                        searchforum_recycler.setAdapter(adapter);
                    } else {

                        noresult.setVisibility(View.VISIBLE);
                        searchforum_recycler.setVisibility(View.GONE);
                        Toast.makeText(Search_Forum.this, "No Post available", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    dialog_c.hide();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

                dialog_c.hide();
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