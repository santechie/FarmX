package com.ascentya.AsgriV2.farmx.myfinance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Activitypie_Adapter;
import com.ascentya.AsgriV2.Models.Farmx_Finance;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.ConvertUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.lwb.piechart.PieChartView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Revenue extends BaseFragment {
    View root_view;


    List<Farmx_Finance> Data = new ArrayList<>();
    boolean isLand = true;
    String usercrop_id, crop_id, crop_type, lcId, user_id;

    int[] rainbow;
    PieChartView pieChartView;
    RecyclerView activity_recycler;
    Activitypie_Adapter adapter;
    TextView total_amount;
    SessionManager sm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.revenue_page, container, false);

        total_amount = root_view.findViewById(R.id.total_amount);
        sm = new SessionManager(getActivity());
        activity_recycler = root_view.findViewById(R.id.activity_recycler);
        activity_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        pieChartView = root_view.findViewById(R.id.ring_chart);
        rainbow = getResources().getIntArray(R.array.rainbow);

        assert getArguments() != null;
        isLand = getArguments().getBoolean("is_land");
        usercrop_id = getArguments().getString("usercrop");
        crop_id = getArguments().getString("crop_id");
        crop_type = getArguments().getString("crop_type");
        lcId = getArguments().getString("lc_id");
        user_id = sm.getUser().getId();
        getmembers();

        return root_view;
    }

    public void getmembers() {

        Data.clear();

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Agripedia/income_farmer");
        builder.addUrlEncodeFormBodyParameter("land_id", usercrop_id);
        if (!isLand)
            builder.addUrlEncodeFormBodyParameter("crop_id", crop_id);
        builder.addUrlEncodeFormBodyParameter("lc_id", lcId);
        builder.addUrlEncodeFormBodyParameter("user_id",user_id );
        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                if (UserHelper.checkResponse(getContext(), response)){
                    return;
                }
                System.out.println("Revenue: " + response);
                if (isDetached() || getView() == null) return;

                try {

                    if (response.optString("status").equalsIgnoreCase("true")) {

                        JSONArray finance_array = new JSONArray();

                        try {
                            finance_array = response.getJSONArray("data");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        int colorIndex = 0;
                        int colors = rainbow.length;

                        for (int i = 0; i < finance_array.length(); i++) {
                            colorIndex = i;

                            boolean isAdded = false;

                            Farmx_Finance fin_obj = new Farmx_Finance();
                            fin_obj.setName(finance_array.getJSONObject(i).optString("income_cat"));
                            fin_obj.setCost(Float.parseFloat(finance_array.getJSONObject(i).optString("amount_total")));
                            fin_obj.setColour(rainbow[colorIndex]);


                            for (int d = 0; d < Data.size(); d++) {
                                Farmx_Finance finance = Data.get(d);
                                if (finance.getName().equals(fin_obj.getName())) {
                                    isAdded = true;
                                    finance.setCost(finance.getCost() + fin_obj.getCost());
                                }
                            }

                            if (!isAdded) {
                                colorIndex = colorIndex + 1 < colors ? colorIndex + 1 : 0;
                                Data.add(fin_obj);
                            }
                        }

                        float sum_finance = 0;
//

                        for (int i = 0; i < Data.size(); i++) {

                            sum_finance += Data.get(i).getCost();

                            String name = Data.get(i).getName();
                            if (name.length() > 10 && name.split(" ").length > 1)
                                name = name.replace(" ", "\n");

                            pieChartView.addItemType(new PieChartView.ItemType(name, (int) Math.round(Data.get(i).getCost()), Data.get(i).getColour()));
//                            data_chart.add(new ChartData("Fourth", 10, Color.DKGRAY, Color.parseColor("#FFD600")));

                        }

                        pieChartView.setInnerRadius(.4f);
                        pieChartView.setItemTextSize(ConvertUtils.spToPx(12f, getContext()));
                        pieChartView.setTextPadding(5);
                        pieChartView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        pieChartView.setTextDirection(View.TEXT_DIRECTION_RTL);

                        total_amount.setText(getString(R.string.total_revenue) + " " + String.format("%.2f", sum_finance));


//                        simpleChart.setChartData(data_chart);


                    } else {

                    }
                    adapter = new Activitypie_Adapter(getActivity(), Data);
                    activity_recycler.setAdapter(adapter);


                } catch (Exception e) {

                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    public static Revenue newInstance(String name, String landId, String crop_id, String lcId){
        Revenue revenue = new Revenue();
        Bundle args = new Bundle();
        args.putString(NAME, "My Revenue");
        args.putBoolean("is_land", false);
        args.putString("usercrop", landId);
        args.putString("crop_id",  crop_id);
        args.putString("crop_type", "main");
        args.putString("lc_id", lcId);
        revenue.setArguments(args);
        revenue.setName(name);
        return revenue;
    }
}
