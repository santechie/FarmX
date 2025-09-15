package com.ascentya.AsgriV2.buysell.fragmens;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.FarmXSell_Adapter;
import com.ascentya.AsgriV2.Models.FarmXSell_Model;
import com.ascentya.AsgriV2.Models.SellCat_Model;
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
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.elasticviews.ElasticButton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Sell_Fragment extends BaseFragment implements FarmXSell_Adapter.Action {


    @Override
    public void onResume() {
        super.onResume();
        getcat_buysell();
    }

    RecyclerView buy_recycler;
    TextView nodata;
    FarmXSell_Adapter farmXSellAdapter;
    List<FarmXSell_Model> Data;
    View view;
    FloatingActionButton add_fab;
    EditText product_title, product_disc, product_cost;
    NoDefaultSpinner product_category;
    CircleImageView attachment;
    String attachment_path;
    ViewDialog viewDialog;
    List<String> cat_data;
    List<SellCat_Model> sellcat_data;
    Dialog dialog;

    SessionManager sm;
    String cat_id;

    boolean editAccess, deleteAccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.farmxsell_layout, container, false);
        add_fab = view.findViewById(R.id.floatingActionButton);
        viewDialog = new ViewDialog(getActivity());
        sm = new SessionManager(getActivity());

        editAccess = getModuleManager().canUpdate(Components.BuyAndSell.SELL);
        deleteAccess = getModuleManager().canDelete(Components.BuyAndSell.SELL);

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postSellItem();
                /*if (checkSubscription(Components.BuyAndSell.SELL, ModuleManager.ACCESS.INSERT))
                    postSellItem();*/
               /* if (sm.isPaid())
                    postSellItem();
                else

                    ((BaseActivity) getActivity()).showPayDialog();*/
            }
        });

        if (!getModuleManager().canInsert(Components.BuyAndSell.SELL))
            add_fab.setVisibility(View.INVISIBLE);

        buy_recycler = view.findViewById(R.id.buy_recycler);
        nodata = view.findViewById(R.id.nodata);

        buy_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        buy_recycler.setHasFixedSize(true);


        buy_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    if (add_fab.isShown()) {
                        add_fab.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!add_fab.isShown()) {
                        add_fab.show();
                    }
                }
            }
        });


        return view;
    }

    private void postSellItem(){
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View alertDialogView = factory.inflate(R.layout.farmxedit_product, null);
            dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(alertDialogView);

            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();

            product_title = dialog.findViewById(R.id.product_title);
            product_category = dialog.findViewById(R.id.product_category);
            product_disc = dialog.findViewById(R.id.product_disc);
            product_cost = dialog.findViewById(R.id.product_cost);
            attachment = dialog.findViewById(R.id.attachment);
            ElasticButton addpost = dialog.findViewById(R.id.addpost);


            ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                    cat_data);

            product_category.setAdapter(soiltype_adpter);


            product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cat_id = sellcat_data.get(i).getCat_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


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
                                    ImageUtils.setImage(
                                            getContext(), r, attachment, true);
                        }
                    }).show((FragmentActivity) getActivity());
                }
            });

            addpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (validatesignForm()) {

                        if (product_category.getSelectedItemPosition() > -1) {
//                                    add_income(dialog);
                            add_product(cat_id, product_title.getText().toString(), product_disc.getText().toString(), product_cost.getText().toString());

                        } else {
                            Toast.makeText(getActivity(), "Kindly select Category", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });
    }

    private boolean validatesignForm() {


        if (!ValidateInputs.isValidInput(product_title.getText().toString().trim())) {
            product_title.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(product_disc.getText().toString().trim())) {
            product_disc.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(product_cost.getText().toString().trim())) {
            product_cost.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }


    }


    public void getcat_buysell() {
        sellcat_data = new ArrayList<>();

        cat_data = new ArrayList<>();


        viewDialog.showDialog();
        AndroidNetworking.get(Webservice.getbuysell_cat)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            SellCat_Model obj = new SellCat_Model();
                            obj.setCat_id(jsonArray.getJSONObject(i).optString("cat_id"));
                            obj.setCat_name(jsonArray.getJSONObject(i).optString("category_name"));

                            sellcat_data.add(obj);
                            cat_data.add(jsonArray.getJSONObject(i).optString("category_name"));
                        }
                    } else {

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

                add_mypost();

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }


    public void add_product(String cat_id, String product_name, String product_desc, String product_price) {

        viewDialog.showDialog();
        File pan_f = null;

        if (attachment_path != null && attachment_path.length() > 0) {

            pan_f = new File(attachment_path);
        }

        AndroidNetworking.upload(Webservice.add_buysellproduct)
                .addMultipartParameter("user_id", sm.getUser().getId())
                .addMultipartParameter("cat_id", cat_id)
                .addMultipartParameter("product_name", product_name)
                .addMultipartParameter("product_desc", product_desc)
                .addMultipartFile("product_image", pan_f)
                .addMultipartParameter("product_price", product_price)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        product_title.setText("");
                        product_disc.setText("");
                        product_cost.setText("");
                        product_category.setSelected(false);
                        attachment.setImageResource(0);
                        dialog.dismiss();


                        add_mypost();
                    } else {
                        Toasty.error(requireActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

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


    public void add_mypost() {

        if (!checkSubscription(Components.BuyAndSell.SELL, ModuleManager.ACCESS.VIEW)) return;

        AndroidNetworking.post(Webservice.add_sellproduct)
                .addUrlEncodeFormBodyParameter("userid", sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                Data = new ArrayList<>();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            FarmXSell_Model obj = new FarmXSell_Model();
                            obj.setProduct_name(jsonArray.getJSONObject(i).optString("product_name"));
                            obj.setProduct_desc(jsonArray.getJSONObject(i).optString("product_desc"));
                            obj.setProduct_id(jsonArray.getJSONObject(i).optString("prod_id"));
                            obj.setProduct_date(jsonArray.getJSONObject(i).optString("created_date"));
                            obj.setProduct_price(jsonArray.getJSONObject(i).optString("product_price"));
                            obj.setProduct_status(jsonArray.getJSONObject(i).optString("status"));
                            obj.setProduct_image(jsonArray.getJSONObject(i).optString("product_image"));
                            obj.setCat_id(jsonArray.getJSONObject(i).optString("cat_id"));
                            obj.setProductuser_id(jsonArray.getJSONObject(i).optString("user_id"));
                            Data.add(obj);


                        }


                    } else {

                    }


                    if (Data.size() > 0) {
                        nodata.setVisibility(View.GONE);
                        buy_recycler.setVisibility(View.VISIBLE);
                        farmXSellAdapter = new FarmXSell_Adapter(getActivity(), Sell_Fragment.this, Data, cat_data, sellcat_data, sm.getUser().getId(), viewDialog);

                        buy_recycler.setAdapter(farmXSellAdapter);

                    } else {
                        nodata.setVisibility(View.VISIBLE);
                        buy_recycler.setVisibility(View.GONE);
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


    @Override
    public boolean showEdit() {
        return editAccess;
    }

    @Override
    public boolean showDelete() {
        return deleteAccess;
    }

    @Override
    public boolean canEdit() {
        return  checkSubscription(Modules.BUY_AND_SELL, ModuleManager.ACCESS.UPDATE);
    }

    @Override
    public boolean canDelete() {
        return checkSubscription(Modules.BUY_AND_SELL, ModuleManager.ACCESS.DELETE);
    }

    @Override
    public void delete(int position) {

    }

    @Override
    public void edit(int position) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void refresh() {
        farmXSellAdapter.notifyDataSetChanged();
    }
}
