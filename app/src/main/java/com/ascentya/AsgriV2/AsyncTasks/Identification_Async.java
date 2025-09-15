package com.ascentya.AsgriV2.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;

public class Identification_Async extends AsyncTask<Void, Void, Void> {
    String Data;
    String id;
    Context cont;
    private AsyncTaskCompleteListener<String> callback;

    public Identification_Async(Context context, AsyncTaskCompleteListener<String> cb, String userid, String Data) {
        this.Data = Data;
        this.id = userid;
        this.cont = context;
        this.callback = cb;


    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (cont == null) return null;

        //adding to database
        DatabaseClient
                .getInstance(cont.getApplicationContext())
                .getAppDatabase()
                .taskDao()
                .updatepestsidentification(Data, Integer.parseInt(id));

        return null;
    }

    @Override
    protected void onPostExecute(Void tasks) {
        super.onPostExecute(tasks);


    }
}