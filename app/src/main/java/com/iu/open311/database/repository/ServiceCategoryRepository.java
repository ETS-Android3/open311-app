package com.iu.open311.database.repository;

import androidx.annotation.NonNull;

import com.iu.open311.database.Database;
import com.iu.open311.database.Result;
import com.iu.open311.database.model.ServiceCategory;

import java.io.IOException;
import java.util.List;

public class ServiceCategoryRepository {
    private final Database database;

    private static volatile ServiceCategoryRepository instance;

    public static ServiceCategoryRepository getInstance(@NonNull Database database
    ) {
        if (instance == null) {
            instance = new ServiceCategoryRepository(database);
        }
        return instance;
    }

    public ServiceCategoryRepository(@NonNull Database database) {
        this.database = database;
    }

    public Result<List<ServiceCategory>> findAll() {
        try {
            List<ServiceCategory> categories = database.serviceCategoryDao().findAll();
            return new Result.Success<List<ServiceCategory>>(categories);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching service categories", e));
        }
    }
}
