package com.ascentya.AsgriV2.Database_Room.daos;

import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItemEntity cartItemEntity);

    @Update
    void update(CartItemEntity cartItemEntity);

    @Delete
    void delete(CartItemEntity cartItemEntity);

    @Query("SELECT * FROM CartItemEntity")
    List<CartItemEntity> getAll();

    @Query("SELECT * FROM cartitementity WHERE prod_id = :prod_id")
    CartItemEntity get(String prod_id);

    @Query("DELETE FROM CartItemEntity")
    void deleteAll();

}
