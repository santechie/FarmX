package com.ascentya.AsgriV2.managers;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

   public static ModuleManager instance = null;
   private Context context;
   private SessionManager sessionManager;
   public static final Type accessListType = new TypeToken<List<Access>>(){}.getType();

   private ArrayList<Access> accessList = new ArrayList<>();

   private ModuleManager(Context context){
      this.context = context;
      sessionManager = new SessionManager(context);
   }

   private void prepare() {
      if (accessList.isEmpty()) {
         accessList.addAll(sessionManager.getAccesses());
      } else if (sessionManager.getAccesses().isEmpty() && !accessList.isEmpty()) {
         accessList.clear();

      }
   }

   public boolean canLoad(){
      return sessionManager.getUser() != null;
   }

   public boolean canView(Modules.Module module){
      prepare();
      return check(module, ACCESS.VIEW);
   }

   public boolean canInsert(Modules.Module module){
      prepare();
      return check(module, ACCESS.INSERT);
   }

   public boolean canDelete(Modules.Module module){
      prepare();
      return check(module, ACCESS.DELETE);
   }

   public boolean canView(Components.Component component){
      prepare();
      return check(component, ACCESS.VIEW);
   }

   public boolean canInsert(Components.Component component){
      prepare();
      return check(component, ACCESS.INSERT);
   }

   public boolean canUpdate(Components.Component component){
      prepare();
      return check(component, ACCESS.UPDATE);
   }

   public boolean canDelete(Components.Component component){
      prepare();
      return check(component, ACCESS.DELETE);
   }

   private boolean check(Modules.Module module, String operation){
      for (Access access: accessList) {
         if (access.module_value.equals(module.value)
                 && access.module_status.equals("1")){
            if (access.component_status.equals("1")) {
               if (operation != null){
                  boolean status = false;
                  switch (operation) {
                     case ACCESS.VIEW:
                        status = access.view_access != null && access.view_access.equals("1");
                        break;
                     case ACCESS.INSERT:
                        status = access.insert_access != null && access.insert_access.equals("1");
                        break;
                     case ACCESS.UPDATE:
                        status = access.update_access != null && access.update_access.equals("1");
                        break;
                     case ACCESS.DELETE:
                        status = access.delete_access != null && access.delete_access.equals("1");
                        break;
                  }
                  if (status) return status;
               }else if ((access.view_access != null && access.view_access.equals("1"))
                       || (access.insert_access != null && access.insert_access.equals("1"))
                       || (access.update_access != null && access.update_access.equals("1"))
                       || (access.delete_access != null && access.delete_access.equals("1"))) {
                     return true;
               }
            }
         }
      }
      return false;
   }

   private boolean check(Components.Component component, String access){
      boolean status = false;
      for (Access a: accessList) {
         if (a.module_value.equals(component.module.value) && a.module_status.equals("1")) {
            if (a.component_value.equals(component.value) && a.component_status.equals("1")) {
               switch (access) {
                  case ACCESS.VIEW:
                     status = a.view_access != null && a.view_access.equals("1");
                     break;
                  case ACCESS.INSERT:
                     status = a.insert_access != null && a.insert_access.equals("1");
                     break;
                  case ACCESS.UPDATE:
                     status = a.update_access != null && a.update_access.equals("1");
                     break;
                  case ACCESS.DELETE:
                     status = a.delete_access != null && a.delete_access.equals("1");
                     break;
               }
               if (status) return status;
            }
         }
      }
      return status;
   }

   public void load(LoaderAction action){
    //  AndroidNetworking.post(Webservice.getModulePrivileges)
      AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/User_Privilege/get_privileges")
              .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
              .build().getAsJSONObject(new JSONObjectRequestListener() {
         @Override
         public void onResponse(JSONObject response) {
            if ( UserHelper.checkResponse(context, response)){
               return;
            }
            try {
               JSONArray jsonArray = new JSONArray(response.optString("privileges"));
               accessList.clear();
               accessList.addAll(GsonUtils.fromJson(jsonArray.toString(), accessListType));
               sessionManager.setAccesses(accessList);
               if (action != null) action.onLoaded(false);
            }catch (Exception e){
               if (action != null) action.onLoaded(true);
               e.printStackTrace();
            }
         }

         @Override
         public void onError(ANError anError) {
            if (action != null) action.onLoaded(true);
         }
      });
   }

   public interface LoaderAction{
      void onLoaded(boolean error);
   }

   public static class ACCESS{
      public static final String VIEW = "view";
      public static final String INSERT = "insert";
      public static final String UPDATE = "update";
      public static final String DELETE = "delete";
   }

   public static ModuleManager getModuleManager(Context context){
      if (instance == null){
         instance = new ModuleManager(context);
      }
      return instance;
   }

   public class Access{

      public String module_id;
      public String module_name;
      public String module_value;
      public String module_status;
      public String component_id;
      public String component_name;
      public String component_value;
      public String component_status;
      public String view_access;
      public String insert_access;
      public String update_access;
      public String delete_access;


   }
}
