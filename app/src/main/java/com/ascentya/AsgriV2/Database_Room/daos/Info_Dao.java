package com.ascentya.AsgriV2.Database_Room.daos;

import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao

public interface Info_Dao {
    @Query("SELECT * FROM Info_Model")
    List<Info_Model> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Info_Model task);

    @Delete
    void delete(Info_Model task);

    @Update
    void update(Info_Model task);

    @Query("DELETE FROM Info_Model")
    void delete();

    @Query("SELECT * FROM Info_Model WHERE basic_id = :event_id")
    Info_Model findSpecificEvent(long event_id);

    @Query("UPDATE Info_Model SET taxonomy = :description WHERE basic_id =:id")
    void updatedesc(String description, int id);

    @Query("UPDATE Info_Model SET pests_symtoms = :description WHERE basic_id =:id")
    void updatepestssymtoms(String description, int id);

    @Query("UPDATE Info_Model SET pests_identification = :description WHERE basic_id =:id")
    void updatepestsidentification(String description, int id);

    @Query("UPDATE Info_Model SET pests_controlmeasure = :description WHERE basic_id =:id")
    void updatepestscontrol(String description, int id);

    @Query("UPDATE Info_Model SET diseas_symtoms = :description WHERE basic_id =:id")
    void updatediseasssymtoms(String description, int id);

    @Query("UPDATE Info_Model SET diseas_identification = :description WHERE basic_id =:id")
    void updatediseasidentification(String description, int id);

    @Query("UPDATE Info_Model SET diseas_controlmeasure = :description WHERE basic_id =:id")
    void updatediseascontrol(String description, int id);

    @Query("UPDATE Info_Model SET phd_symtoms = :description WHERE basic_id =:id")
    void updatephdsymtoms(String description, int id);

    @Query("UPDATE Info_Model SET phd_identification = :description WHERE basic_id =:id")
    void updatephdidentification(String description, int id);

    @Query("UPDATE Info_Model SET phd_controlmeasure = :description WHERE basic_id =:id")
    void updatephdcontrol(String description, int id);


    @Query("UPDATE Info_Model SET nutrient_dificiency = :description WHERE basic_id =:id")
    void updatenutrient_dificiency(String description, int id);

    @Query("UPDATE Info_Model SET fav_condition = :description WHERE basic_id =:id")
    void updatefavcondition(String description, int id);

    @Query("UPDATE Info_Model SET varieties = :description WHERE basic_id =:id")
    void updatevarieties(String description, int id);

    @Query("UPDATE Info_Model SET nutrient_values = :description WHERE basic_id =:id")
    void updatenutrient_values(String description, int id);

    @Query("UPDATE Info_Model SET cultivation = :description WHERE basic_id =:id")
    void updatecultivation(String description, int id);

    @Query("UPDATE Info_Model SET post_cultivation = :description WHERE basic_id =:id")
    void updatepostcultivation(String description, int id);

}
