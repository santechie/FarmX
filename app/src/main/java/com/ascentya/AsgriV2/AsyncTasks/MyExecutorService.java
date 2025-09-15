package com.ascentya.AsgriV2.AsyncTasks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Database_Room.daos.CartItemDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutorService {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyExecutorService(Context context, MyRunnable runnable){
        if (runnable != null){
            executorService.execute(runnable);
        }
    }

    public MyExecutorService(Runnable runnable){
        if (runnable != null){
            executorService.execute(runnable);
        }
    }


    public interface Action{
        void result(Object result);
    }

    public abstract static class MyRunnable implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private CartItemDao cartItemDao;
        public Action action;

        public MyRunnable(Context context, Action action){
            cartItemDao = DatabaseClient.getInstance(context).getAppDatabase().cartItemDao();
            this.action = action;
        }

        @Override
        public void run() {
            Object object = runForResult();
            if (action != null){
                handler.post(() -> action.result(object));
            }
        }

        public abstract Object runForResult();

        public CartItemDao getCartItemDao(){ return cartItemDao; }

    }
}
