package com.ascentya.AsgriV2.agripedia.cropcycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.Tax_Adapter;
import com.ascentya.AsgriV2.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CropCycle_Fragment extends Fragment {
    View root_view;
    TextView title;
    RecyclerView crop_content;
    Tax_Adapter cropcycle;
    List<String> Data;
    String content_data;
    ImageView pic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.activity_cropcycle, container, false);
        crop_content = root_view.findViewById(R.id.crop_content);
        title = root_view.findViewById(R.id.title);
        pic = root_view.findViewById(R.id.pic);
        content_data = getArguments().getString("disc");
        crop_content.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        crop_content.setHasFixedSize(true);

        Data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(content_data);
            for (int i = 0; i < jsonArray.length(); i++) {

                Data.add(jsonArray.get(i).toString().trim());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cropcycle = new Tax_Adapter(getActivity(), Data);

        crop_content.setAdapter(cropcycle);


        title.setText(getArguments().getString("Title"));
        Glide.with(getActivity()).load(getArguments().getString("image")).into(pic);


        return root_view;
    }
}
