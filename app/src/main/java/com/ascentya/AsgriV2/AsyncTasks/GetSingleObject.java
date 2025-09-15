package com.ascentya.AsgriV2.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;

public class GetSingleObject extends AsyncTask<Void, Void, Info_Model> {

    String id;
    private Context cont;
    private Async_Single<Info_Model> callback;

    public GetSingleObject(Context context, Async_Single<Info_Model> cb, String userid) {

        this.id = userid;
        this.cont = context;
        this.callback = cb;


    }

    @Override
    protected Info_Model doInBackground(Void... voids) {

        if (cont == null) return null;

        Info_Model taskList = DatabaseClient
                .getInstance(cont.getApplicationContext())
                .getAppDatabase()
                .taskDao()
                .findSpecificEvent(Long.parseLong(id));
        return taskList;
    }

    @Override
    protected void onPostExecute(Info_Model tasks) {
        super.onPostExecute(tasks);
        callback.onTaskComplete(tasks);

    }
}