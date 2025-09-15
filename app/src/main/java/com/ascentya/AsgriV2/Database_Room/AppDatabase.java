package com.ascentya.AsgriV2.Database_Room;

import com.ascentya.AsgriV2.Database_Room.daos.CartItemDao;
import com.ascentya.AsgriV2.Database_Room.daos.Info_Dao;
import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Info_Model.class, CartItemEntity.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Info_Dao taskDao();
    public abstract CartItemDao cartItemDao();
}
