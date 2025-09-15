package com.ascentya.AsgriV2.Models;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class FarmXSell_Adapter extends RecyclerView.Adapter<FarmXSell_Adapter.ViewHolder> {

    private List<FarmXSell_Model> items = new ArrayList<>();
    private Context ctx;
    String lang_id;
    Dialog dialog;
    EditText product_title, product_disc, product_cost;
    NoDefaultSpinner product_category;
    CircleImageView attachment;
    String attachment_path;

    List<String> cat_data = new ArrayList<>();
    List<SellCat_Model> sellcat_data = new ArrayList<>();
    String user_id;
    ViewDialog viewDialog;
    String cat_id;

    private Action action;


    public FarmXSell_Adapter(Context context, List<FarmXSell_Model> items, List<String> cat_Data, List<SellCat_Model> sellcat_data, String user_id, ViewDialog viewDialog) {
        this.items = items;
        this.ctx = context;
        this.cat_data = cat_Data;
        this.sellcat_data = sellcat_data;
        this.user_id = user_id;
        this.viewDialog = viewDialog;


    }

    public FarmXSell_Adapter(Context context, Action action, List<FarmXSell_Model> items, List<String> cat_Data, List<SellCat_Model> sellcat_data, String user_id, ViewDialog viewDialog) {
        this.items = items;
        this.ctx = context;
        this.cat_data = cat_Data;
        this.sellcat_data = sellcat_data;
        this.user_id = user_id;
        this.viewDialog = viewDialog;
        this.action = action;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView title, disc;
        public ImageView icons;
        ElasticFloatingActionButton delete, edit;


        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            disc = (TextView) v.findViewById(R.id.desc);
            disc = (TextView) v.findViewById(R.id.desc);
//            date = (TextView) v.findViewById(R.id.date);
            icons = (ImageView) v.findViewById(R.id.picture);
            edit = (ElasticFloatingActionButton) v.findViewById(R.id.edit);
            delete = (ElasticFloatingActionButton) v.findViewById(R.id.delete);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmxsell_row, parent, false);


        vh = new ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final FarmXSell_Model Position_Object = items.get(position);


            view.title.setText(Position_Object.getProduct_name());
//            view.date.setText(Position_Object.getDate());
            view.disc.setText(Html.fromHtml(Position_Object.getProduct_desc()));

            if (Position_Object.getProduct_image().startsWith("http")){
                Glide.with(ctx).load(Position_Object.getProduct_image())
                        .error(ctx.getResources().getDrawable(R.drawable.news)).into(view.icons);
            }else {
                Glide.with(ctx).load(Webservice.buysellimagebase_path + Position_Object.getProduct_image())
                        .error(ctx.getResources().getDrawable(R.drawable.news)).into(view.icons);
            }

            ((TextView) view.itemView.findViewById(R.id.price))
                    .setText(ProductUtils.getPrice(Position_Object.getProduct_price()));

            if (!action.showDelete()) view.delete.setVisibility(View.INVISIBLE);
            if (!action.showEdit()) view.edit.setVisibility(View.INVISIBLE);

            view.edit.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {

                   // if (!action.canEdit()) return;

                    LayoutInflater factory = LayoutInflater.from(ctx);
                    final View alertDialogView = factory.inflate(R.layout.farmxedit_product, null);
                    dialog = new Dialog(ctx);
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


                    product_title.setText(items.get(position).getProduct_name());
                    product_disc.setText(items.get(position).getProduct_desc());
                    product_cost.setText(items.get(position).getProduct_price());

                    System.out.println("Image URL: " + Position_Object.getProduct_image().replace("http", "----"));

                    if (Position_Object.getProduct_image().startsWith("http")){
                        Glide.with(ctx).load(Position_Object.getProduct_image())
                                .error(ctx.getResources().getDrawable(R.drawable.news)).into(attachment);
                    }else {
                        Glide.with(ctx).load(Webservice.buysellimagebase_path + Position_Object.getProduct_image())
                                .error(ctx.getResources().getDrawable(R.drawable.news)).into(attachment);
                    }

                    ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(ctx, R.layout.spinner_item,
                            cat_data);

                    product_category.setAdapter(soiltype_adpter);

                    product_category.setSelection(getposition(items.get(position).getCat_id()));

//                ArrayAdapter<String> irrigation_adpter = new ArrayAdapter(Forum_Activity.this, R.layout.spinner_item,
//                        cat_Data);
//
//                forum_category.setAdapter(irrigation_adpter);


                    product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            cat_id = sellcat_data.get(i).getCat_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    attachment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    attachment_path = ImageUtils
                                            .setImage(ctx, r, attachment, true);
                                }
                            }).show((FragmentActivity) ctx);
                        }
                    });

                    addpost.setOnClickListener(new DebouncedOnClickListener(1500) {
                        @Override
                        public void onDebouncedClick(View view) {

                            if (validatesignForm()) {

                                if (product_category.getSelectedItemPosition() > -1) {
//                                    add_income(dialog);
                                    update_product(cat_id, product_title.getText().toString(), product_disc.getText().toString(), product_cost.getText().toString(), items.get(position).getProduct_id());

                                } else {
                                    Toast.makeText(ctx, "Kindly select Category", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                    });


                }
            });
            
            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {
                    //if (!action.canDelete()) return;
                    add_mypost(position, user_id);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<FarmXSell_Model> list) {
        items = list;
        notifyDataSetChanged();
    }

    private boolean validatesignForm() {

        if (!ValidateInputs.isValidInput(product_title.getText().toString().trim())) {
            product_title.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(product_disc.getText().toString().trim())) {
            product_disc.setError(ctx.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(product_cost.getText().toString().trim())) {
            product_cost.setError(ctx.getString(R.string.required_date));
            return false;
        } else {
            return true;
        }

    }


    public int getposition(String id) {

        Integer pos = -1;
        for (int i = 0; i < sellcat_data.size(); i++) {
            String userListName = sellcat_data.get(i).getCat_id();

            if (userListName.equals(id)) {
                //Do something here
                pos = i;
            } else {
                //Nthng to do
                pos = -1;
            }
        }
        return pos;
    }

    public void add_mypost(final Integer por_id, String userId) {


        AndroidNetworking.post(Webservice.delete_sellproduct)
                .addUrlEncodeFormBodyParameter("prod_id", items.get(por_id).getProduct_id())
                .addUrlEncodeFormBodyParameter("user_id", userId)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(ctx, jsonObject)){
                    return;
                }


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        deleteItem(por_id);

                        Toasty.success(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                        if (action != null){
                            action.refresh();
                        }

                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

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

    private void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void update_product(String cat_id, String product_name, String product_desc, String product_price, String pro_id) {

        viewDialog.showDialog();
        File pan_f = null;

        if (attachment_path != null && attachment_path.length() > 0) {

            pan_f = new File(attachment_path);


        }

        AndroidNetworking.upload(Webservice.update_sellproduct)
                .addMultipartParameter("user_id", user_id)
                .addMultipartParameter("cat_id", cat_id)
                .addMultipartParameter("product_name", product_name)
                .addMultipartParameter("product_desc", product_desc)
                .addMultipartParameter("product_price", product_price)
                .addMultipartParameter("prod_id", pro_id)
                .addMultipartFile("product_image", pan_f)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                if (UserHelper.checkResponse(ctx, jsonObject)){
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

                        if (action != null){
                            action.refresh();
                        }
                    } else {
                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

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

    public interface Action{
        boolean showEdit();
        boolean showDelete();
        boolean canEdit();
        boolean canDelete();
        void delete(int position);
        void edit(int position);
        void refresh();
    }
}
