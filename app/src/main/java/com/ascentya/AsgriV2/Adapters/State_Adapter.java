package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.HomeScreen_Activity;
import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Models.State_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class State_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<State_Model> items = new ArrayList<>();
    private Context ctx;
    Locale myLocale;
    String currentLanguage = "en_GB", currentLang;
    Dialog dialog;


    public State_Adapter(Context context, List<State_Model> items, Dialog dialog) {
        this.items = items;
        this.ctx = context;
        this.dialog = dialog;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView c_name;


        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.disc);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.langselection_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.c_name.setText(items.get(position).getName());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Webservice.state_id = items.get(position).getName();
                    if (!Webservice.Search_id.equalsIgnoreCase("none")) {
                        DeleteTask run = new DeleteTask();
                        run.execute();


                    }

                    dialog.dismiss();
                    Intent i = new Intent(ctx, HomeScreen_Activity.class);
                    i.putExtra("lang", ctx.getResources().getConfiguration().locale.toString());
                    ctx.startActivity(i);

//                    setLocale(items.get(position).getId());
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class DeleteTask extends AsyncTask<Void, Void, Void> {
        String name, s_name, family;
        List<String> desc;
        String id;

        public DeleteTask() {
            super();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            //creating a task

            if (ctx == null) return null;

            Info_Model task = new Info_Model();
            task.setBasic_id(Integer.valueOf(Webservice.Search_id));


            //adding to database
            DatabaseClient.getInstance(ctx.getApplicationContext()).getAppDatabase()
                    .taskDao()
                    .delete(task);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }
}
