package com.iu.open311.database.repository;

import androidx.annotation.NonNull;

import com.iu.open311.api.dto.ServiceRequest;
import com.iu.open311.database.Database;
import com.iu.open311.database.Result;
import com.iu.open311.database.model.MyServiceRequest;
import com.iu.open311.database.model.ServiceCategory;

import java.io.IOException;
import java.util.List;

public class MyServiceRequestRepository {
    private Database database;

    private static volatile MyServiceRequestRepository instance;

    public static MyServiceRequestRepository getInstance(@NonNull Database database
    ) {
        if (instance == null) {
            instance = new MyServiceRequestRepository(database);
        }

        return instance;
    }

    public MyServiceRequestRepository(@NonNull Database database) {
        this.database = database;
    }

    public Result<Boolean> insert(Integer requestId)
    {
        try {
            MyServiceRequest myServiceRequest = new MyServiceRequest();
            myServiceRequest.serviceRequestId = requestId;
            database.myServiceRequestDao().insert(myServiceRequest);
            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Could not save to database", e));
        }

    }
}
