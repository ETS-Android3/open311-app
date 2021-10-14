package com.iu.open311.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.open311.database.model.ServiceCategory;

import java.util.List;

@Dao
public interface ServiceCategoryDao {
    @Query("SELECT * FROM service_category")
    List<ServiceCategory> findAll();

    @Insert
    void insert(ServiceCategory serviceCategory);

    @Query("DELETE FROM service_category")
    void deleteAll();
}
