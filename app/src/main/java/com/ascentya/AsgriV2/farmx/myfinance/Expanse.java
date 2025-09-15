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
import com.ascentya.AsgriV2.Models.ExpenseModel;
import com.ascentya.AsgriV2.Models.Farmx_Finance;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.ConvertUtils;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.lwb.piechart.PieChartView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Expanse extends BaseFragment {
    View root_view;

    boolean isLand = true;
    List<Farmx_Finance> Data = new ArrayList<>();
    String usercrop_id, crop_id, crop_type, lcId;
    int[] rainbow;
    PieChartView pieChartView;
    RecyclerView activity_recycler;
    Activitypie_Adapter adapter;
    TextView total_amount;
    SessionManager sm;

    HashMap<String, ArrayList<ExpenseModel>> financeGroups = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.activity_finance_dummy, container, false);
        total_amount = root_view.findViewById(R.id.total_amount);
        sm = new SessionManager(getActivity());
        activity_recycler = root_view.findViewById(R.id.activity_recycler);
        activity_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        pieChartView = root_view.findViewById(R.id.ring_chart);
        rainbow = getResources().getIntArray(R.array.rainbow);

        isLand = getArguments().getBoolean("is_land");
        usercrop_id = getArguments().getString("usercrop");
        crop_id = getArguments().getString("crop_id");
        crop_type = getArguments().getString("crop_type");
        lcId = getArguments().getString("lc_id");

        pieChartView.setInnerRadius(.4f);
        pieChartView.setItemTextSize(ConvertUtils.spToPx(12f, getContext()));
        pieChartView.setTextPadding(5);
        pieChartView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pieChartView.setTextDirection(View.TEXT_DIRECTION_RTL);

        load();
        //getmembers();

        return root_view;
    }

    public void load() {

        Data.clear();
        financeGroups.clear();

        System.out.println("User Id: " + sm.getUser().getId());
        System.out.println("Crop Id: " + usercrop_id);
        System.out.println("Land Id: " + usercrop_id);
        System.out.println("Crop Type: " + crop_type);
        System.out.println("Lc Id: " + lcId);

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(Webservice.finance_getall);
        builder.addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId());
        if (!isLand)
            builder.addUrlEncodeFormBodyParameter("crop_id", crop_id);
        builder.addUrlEncodeFormBodyParameter("land_id", usercrop_id);
        builder.addUrlEncodeFormBodyParameter("crop_type", crop_type);
        builder.addUrlEncodeFormBodyParameter("lc_id", lcId);
        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                if (UserHelper.checkResponse(getContext(), response)){
                    return;
                }

                System.out.println("Expense Response: " + response);
                if (isDetached() || getView() == null) return;
                try {
                    if (response.getBoolean("status")){
                        JSONArray jsonArray = response.optJSONArray("data");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ExpenseModel expenseModel = GsonUtils.getGson().fromJson(jsonObject.toString(),
                                    ExpenseModel.class);
                            String serviceId = expenseModel.getServiceId();
                            if (!financeGroups.containsKey(serviceId)){
                                financeGroups.put(serviceId, new ArrayList<>());
                            }
                            financeGroups.get(serviceId).add(expenseModel);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("Expense Group: " + GsonUtils.getGson().toJson(financeGroups));
                updateChart();
            }

            @Override
            public void onError(ANError anError) {
                if (getContext() != null)
                    Toasty.error(getContext(), "Expense Load Error!").show();
                anError.printStackTrace();
                System.out.println("Expense Error: " + GsonUtils.toJson(anError));
            }
        });
    }

    public void updateChart(){
        float total = 0f;
        int colorIndex = -1;
        for (String serviceId: financeGroups.keySet()){
            ArrayList<ExpenseModel> expenseModels = financeGroups.get(serviceId);
            Constants.ActivityTypes.ActivityType activityType =
                    Constants.ActivityTypes.getActivityType(serviceId);
            String name = activityType.getName();
            float itemTotal = 0f;
            for (ExpenseModel expenseModel: expenseModels){
                try {
                    itemTotal += Float.parseFloat(expenseModel.getTotalAmount());
                }catch (Exception e){e.printStackTrace();}
            }

            colorIndex = colorIndex+1< rainbow.length? colorIndex+1 : 0;

            total += itemTotal;

            Farmx_Finance fin_obj = new Farmx_Finance();
            fin_obj.setCrop_id(serviceId);
            fin_obj.setName(name);
            fin_obj.setCost(itemTotal);
            fin_obj.setColour(rainbow[colorIndex]);

            Data.add(fin_obj);

            PieChartView.ItemType itemType = new PieChartView.ItemType(name, Math.round(itemTotal),
                    rainbow[colorIndex]);
            addPieChartItem(itemType);
        }

        total_amount.setText(getString(R.string.total_expenses) + " " + String.format("%.2f", total));
        adapter = new Activitypie_Adapter(getActivity(), Data);
        activity_recycler.setAdapter(adapter);
    }

    private void addPieChartItem(PieChartView.ItemType itemType){
        pieChartView.addItemType(itemType);
    }

    public void getmembers() {

        Data = new ArrayList<>();

        System.out.println("User Id: " + sm.getUser().getId() + "Land Id: " + usercrop_id + " Crop Id: " + crop_id);

        ANRequest.PostRequestBuilder requestBuilder = AndroidNetworking.post(Webservice.finance_getall);
        requestBuilder.addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId());
        if (!isLand)
            requestBuilder.addUrlEncodeFormBodyParameter("crop_id", crop_id);
        requestBuilder.addUrlEncodeFormBodyParameter("land_id", usercrop_id);
        requestBuilder.addUrlEncodeFormBodyParameter("crop_type", crop_type);
        requestBuilder.addUrlEncodeFormBodyParameter("lc_id", lcId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                System.out.println("Expense: \n" + jsonObject);

                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//                        Userobject obj = new Userobject();
//                        obj.setId(jsonObject.getJSONObject("data").optString("id"));
//                        obj.setFirstname(jsonObject.getJSONObject("data").optString("username"));
//                        obj.setPhno(jsonObject.getJSONObject("data").optString("phone"));
//                        obj.setEmail(jsonObject.getJSONObject("data").optString("email"));
//                        obj.setIspremium(jsonObject.getJSONObject("data").optString("is_premium"));
//                        obj.setSearch_name("none");
//                        sm.setUser(obj);
//                        Intent i = new Intent(Payment_Activity.this, Mycrops_Main.class);
//                        i.putExtra("page", "crop");
//                        startActivity(i);
//                        finishAffinity();

//                        if (jsonObject.getJSONObject("data").optString("id"))

                        JSONArray finance_array = jsonObject.getJSONArray("data");

                        for (int i = 0; i < finance_array.length(); i++) {

                            Farmx_Finance fin_obj = new Farmx_Finance();
                            fin_obj.setCrop_id(finance_array.getJSONObject(i).optString("crop_id"));
                            fin_obj.setName(finance_array.getJSONObject(i).optString("prepare_type"));
                            fin_obj.setCost(Float.parseFloat(finance_array.getJSONObject(i).optString("total_amount")));
                            fin_obj.setColour(rainbow[i]);

                            Data.add(fin_obj);
                        }

//                        if (!jsonObject.getJSONObject("data").optString("ploughing_cost").equalsIgnoreCase("")) {
//
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("sowing_cost").equalsIgnoreCase("")) {
//                            Farmx_Finance fin_obj = new Farmx_Finance();
//                            fin_obj.setName(getString(R.string.sowing));
//                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("sowing_cost")));
//                            fin_obj.setColour(rainbow[1]);
//                            Data.add(fin_obj);
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("irrigation_cost").equalsIgnoreCase("")) {
//                            Farmx_Finance fin_obj = new Farmx_Finance();
//                            fin_obj.setName(getString(R.string.irrigation));
//                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("irrigation_cost")));
//                            Data.add(fin_obj);
//                            fin_obj.setColour(rainbow[2]);
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("fertilizer_cost").equalsIgnoreCase("")) {
//                            Farmx_Finance fin_obj = new Farmx_Finance();
//                            fin_obj.setName(getString(R.string.fertilizer));
//                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("fertilizer_cost")));
//                            fin_obj.setColour(rainbow[3]);
//                            Data.add(fin_obj);
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("intercultual_cost").equalsIgnoreCase("")) {
//                            Farmx_Finance fin_obj = new Farmx_Finance();
//                            fin_obj.setName(getString(R.string.intercultivation));
//                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("intercultual_cost")));
//                            fin_obj.setColour(rainbow[4]);
//                            Data.add(fin_obj);
//                        }
//                        if (!jsonObject.getJSONObject("data").optString("harvest_cost").equalsIgnoreCase("")) {
//                            Farmx_Finance fin_obj = new Farmx_Finance();
//                            fin_obj.setName(getString(R.string.harvest));
//                            fin_obj.setCost(Float.parseFloat(jsonObject.getJSONObject("data").optString("harvest_cost")));
//                            fin_obj.setColour(rainbow[5]);
//                            Data.add(fin_obj);
//                        }
                        float sum_finance = 0;
//

                        for (int i = 0; i < Data.size(); i++) {

                            sum_finance += Data.get(i).getCost();

                            String name = Data.get(i).getName();
                            if (name.length() > 10 && name.split(" ").length > 1)
                                name = name.replace(" ", "\n");

                            pieChartView.addItemType(new PieChartView.ItemType(name, (int) Math.round(Data.get(i).getCost()), rainbow[i]));
//                            pieChartView.addItemType(new PieChartView.ItemType());
//                            data_chart.add(new ChartData("Fourth", 10, Color.DKGRAY, Color.parseColor("#FFD600")));
                        }

                        pieChartView.setInnerRadius(.4f);
                        pieChartView.setItemTextSize(ConvertUtils.spToPx(12f, getContext()));
                        pieChartView.setTextPadding(5);
                        pieChartView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        pieChartView.setTextDirection(View.TEXT_DIRECTION_RTL);

                        total_amount.setText(getString(R.string.total_expenses) + " " + String.format("%.2f", sum_finance));


                    } else {

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

    public static Expanse newInstance(String name, String landId, String crop_id, String lcId){
        Expanse expanse = new Expanse();
        Bundle args = new Bundle();
        args.putString(NAME, "My Expenses");
        args.putBoolean("is_land", false);
        args.putString("usercrop", landId);
        args.putString("crop_id",  crop_id);
        args.putString("crop_type", "main");
        args.putString("lc_id", lcId);
        expanse.setArguments(args);
        expanse.setName(name);
        return expanse;
    }
}
