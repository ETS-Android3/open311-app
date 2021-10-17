package com.iu.open311.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.open311.database.model.MyServiceRequest;

import java.util.List;

@Dao
public interface MyServiceRequestDao {
    @Query("SELECT * FROM service_request")
    List<MyServiceRequest> findAll();

    @Insert
    void insert(MyServiceRequest myServiceRequest);
}
