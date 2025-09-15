package com.ascentya.AsgriV2.Forum;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Forum_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Delete_Post;
import com.ascentya.AsgriV2.Models.Forum_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.EndlessRecyclerViewScroll;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Forum_DynamicFragment extends BaseFragment {
    View view;
    SessionManager sm;

    @Override
    public void onResume() {
        super.onResume();
        val = getArguments().getString("title");
        key = getArguments().getString("value");
        dialog_c = new Dialog(getActivity());
        dialog_c.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog_c.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog_c.setContentView(R.layout.custom_loader);
        dialog_c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //...initialize the imageView form infalted layout

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        forum_recyclder = view.findViewById(R.id.forum_recyclder);
        sm = new SessionManager(getActivity());
        Data = new ArrayList<>();
        add_income(val, "0", true);

        forum_recyclder.setLayoutManager(new LinearLayoutManager(getActivity()));
        forum_recyclder.setHasFixedSize(true);
        forum_recyclder.addOnScrollListener(new EndlessRecyclerViewScroll() {
            @Override
            public void onLoadMore(int current_page) {

                add_income(val, String.valueOf(Data.size()), false);
            }

        });

    }

    public static Forum_DynamicFragment newInstance(String title, String value) {
        Forum_DynamicFragment fragment = new Forum_DynamicFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString("value", value);
        fragment.setArguments(args);
        return fragment;
    }

    String val, key;
    RecyclerView forum_recyclder;
    Forum_Adapter adapter;
    List<Forum_Model> Data;
    Dialog dialog_c;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forumfragment_layout, container, false);
        return view;
    }

    public void add_income(final String dialog, String count, final Boolean isfirst) {
        dialog_c.show();


        AndroidNetworking.post(Webservice.get_forum)
                .addBodyParameter("category", dialog)
                .addBodyParameter("count", count)
                .addBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                dialog_c.hide();
                System.out.println("Forum Response:\n" + jsonObject);
                forum_recyclder.setVisibility(View.VISIBLE);
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Forum_Model obj = new Forum_Model();

                            obj.setForum_id(jsonArray.getJSONObject(i).optString("forum_id"));
                            obj.setForum_posterid(jsonArray.getJSONObject(i).optString("user_id"));
                            obj.setForum_title(StringEscapeUtils.unescapeJava(jsonArray.getJSONObject(i).optString("forum_title")));
                            obj.setForum_description(StringEscapeUtils.unescapeJava(jsonArray.getJSONObject(i).optString("forum_description")));
                            obj.setCategory(jsonArray.getJSONObject(i).optString("category"));
                            obj.setForum_attachment(jsonArray.getJSONObject(i).optString("forum_attachment"));
                            obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                            Data.add(obj);
                        }

                    } else {


                    }

                    if (isfirst) {

                        if (Data.size() > 0) {
                            adapter = new Forum_Adapter(getActivity(), Data, sm.getUser().getId(), dialog_c, new Delete_Post() {
                                @Override
                                public void reset_adapter() {
                                    Data.clear();
                                    add_income(val, "0", true);
                                }
                            });
                            forum_recyclder.setAdapter(adapter);
                        } else {
                            forum_recyclder.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No Post available", Toast.LENGTH_SHORT).show();
                        }

                    } else {


                        adapter.notifyDataSetChanged();
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