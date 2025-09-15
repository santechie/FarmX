package com.ascentya.AsgriV2.Activitys;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

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
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
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

public class FarmX_MyStockActivity extends BaseActivity implements FarmXSell_Adapter.Action {

    private RecyclerView recyclerView;

    private ViewDialog viewDialog;
    private FarmXSell_Adapter adapter;
    private ArrayList<String> categoryList = new ArrayList<>();
    private String selectedCategoryId;
    private ArrayList<SellCat_Model> sellCatModels = new ArrayList<>();
    private ArrayList<FarmXSell_Model> sellModelList = new ArrayList<>();
    private String attachmentPath;
    private FloatingActionButton addStock;

    boolean editAccess, deleteAccess;
    private Components.Component component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_my_stock);
        setUpToolbar();

        component = getFromIntent("component", Components.Component.class);

        sessionManager = new SessionManager(this);
        viewDialog = new ViewDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FarmXSell_Adapter(this, this,
                sellModelList,
                categoryList,
                sellCatModels,
                sessionManager.getUser().getId(),
                viewDialog);
        recyclerView.setAdapter(adapter);
        addStock = findViewById(R.id.addStock);
       // findViewById(R.id.addStock).setOnClickListener(view -> addStock());
        findViewById(R.id.addStock).setOnClickListener(view ->    addStockDialog());

        if (!getModuleManager().canInsert(Components.MyFarm.MY_STOCK)){
            addStock.setVisibility(View.GONE);
        }

        editAccess = getModuleManager().canUpdate(Components.MyFarm.MY_STOCK);
        deleteAccess = getModuleManager().canDelete(Components.MyFarm.MY_STOCK);

        getCategoryList();
    }

    private void setUpToolbar(){
        findViewById(R.id.toolbarLayout)
                .findViewById(R.id.back)
                .setOnClickListener(view -> onBackPressed());
        ((TextView)findViewById(R.id.toolbarLayout)
                .findViewById(R.id.name))
            .setText(getString(R.string.my_stocks));
    }


    private void addStock(){

        if (checkSubscription(Components.MyFarm.MY_STOCK, ModuleManager.ACCESS.INSERT)){
//            if(checkSubscription(Components.MyFarm.MY_STOCK){
            addStockDialog();
        }

        /*if (sessionManager.isPaid()){
            addStockDialog();
        }else {
            showPayDialog();
        }*/
    }

    private void addStockDialog(){

        LayoutInflater factory = LayoutInflater.from(FarmX_MyStockActivity.this);
        final View alertDialogView = factory.inflate(R.layout.farmxedit_product, null);
        Dialog dialog = new Dialog(FarmX_MyStockActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(alertDialogView);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = dialog.findViewById(R.id.title);
        TextView cropname = dialog.findViewById(R.id.cropname);
        EditText product_title = dialog.findViewById(R.id.product_title);
        NoDefaultSpinner product_category = dialog.findViewById(R.id.product_category);
        EditText product_disc = dialog.findViewById(R.id.product_disc);
        EditText product_cost = dialog.findViewById(R.id.product_cost);
        CircleImageView attachment = dialog.findViewById(R.id.attachment);
        ElasticButton addpost = dialog.findViewById(R.id.addpost);
        title.setText(getString(R.string.add_stock));
        cropname.setText(getString(R.string.stock_name));

        ArrayAdapter<String> soiltype_adpter =
                new ArrayAdapter(FarmX_MyStockActivity.this, R.layout.spinner_item, categoryList);

        product_category.setAdapter(soiltype_adpter);

        product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategoryId = sellCatModels.get(i).getCat_id();
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
                        attachmentPath = ImageUtils.setImage(FarmX_MyStockActivity.this, r, attachment, true);
                                /*Bitmap bitmap = (BitmapFactory.decodeFile(r.getPath()));
                                attachment.setImageBitmap(bitmap);
                                attachment_path = r.getPath();*/
                    }
                }).show((
                        FarmX_MyStockActivity.this));
            }
        });

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatesignForm(product_title, product_disc, product_cost)) {
                    if (product_category.getSelectedItemPosition() > -1) {
                        addStock(dialog,
                                selectedCategoryId,
                                product_title.getText().toString(),
                                product_disc.getText().toString(),
                                product_cost.getText().toString());
                    } else {
                        Toast.makeText(FarmX_MyStockActivity.this, R.string.select_category, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    private boolean validatesignForm(EditText product_title, EditText product_disc, EditText product_cost) {

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

    public void getCategoryList() {
        sellCatModels.clear();
        categoryList.clear();


        viewDialog.showDialog();
        AndroidNetworking.get(Webservice.getbuysell_cat)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                if (UserHelper.checkResponse(FarmX_MyStockActivity.this, jsonObject)){
                    return;
                }

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            SellCat_Model obj = new SellCat_Model();
                            obj.setCat_id(jsonArray.getJSONObject(i).optString("cat_id"));
                            obj.setCat_name(jsonArray.getJSONObject(i).optString("category_name"));

                            sellCatModels.add(obj);
                            categoryList.add(jsonArray.getJSONObject(i).optString("category_name"));
                        }
                    } else {

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

                getSellList();

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void getSellList() {

        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.add_sellproduct)
                .addUrlEncodeFormBodyParameter("userid", sessionManager.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(FarmX_MyStockActivity.this, jsonObject)){
                    return;
                }

                sellModelList.clear();

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            System.out.println("Stock"+(i)+": \n"+jsonArray.getJSONObject(i));
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
                            sellModelList.add(obj);
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

                viewDialog.hideDialog();

                updateUI();
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
            }
        });
    }

    public void addStock(Dialog dialog,
            String cat_id, String product_name, String product_desc, String product_price) {

        viewDialog.showDialog();
        File pan_f = null;

        if (attachmentPath != null && attachmentPath.length() > 0) {

            pan_f = new File(attachmentPath);
        }

        AndroidNetworking.upload(Webservice.add_buysellproduct)
                .addMultipartParameter("user_id", sessionManager.getUser().getId())
                .addMultipartParameter("cat_id", cat_id)
                .addMultipartParameter("product_name", product_name)
                .addMultipartParameter("product_desc", product_desc)
                .addMultipartFile("product_image", pan_f)
                .addMultipartParameter("product_price", product_price)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                if (UserHelper.checkResponse(FarmX_MyStockActivity.this, jsonObject)){
                    return;
                }


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                     /*   product_title.setText("");
                        product_disc.setText("");
                        product_cost.setText("");
                        product_category.setSelected(false);
                        attachment.setImageResource(0);*/
                        dialog.dismiss();


                        getCategoryList();
                    } else {
                        Toasty.error(FarmX_MyStockActivity.this,
                                jsonObject.optString("message"),
                                Toast.LENGTH_SHORT, true).show();
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

    private void updateUI(){
        adapter.notifyDataSetChanged();
        findViewById(R.id.noData)
                .setVisibility(sellModelList.isEmpty() ? View.VISIBLE : View.GONE);
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
        return checkSubscription(Components.MyFarm.MY_STOCK , ModuleManager.ACCESS.UPDATE);
    }

    @Override
    public boolean canDelete() {
        return checkSubscription(Components.MyFarm.MY_STOCK, ModuleManager.ACCESS.DELETE);
    }

    @Override
    public void delete(int position) {
        getSellList();
    }

    @Override
    public void edit(int position) {
        getSellList();
    }

    @Override
    public void refresh() {
        getSellList();
    }
}