package com.iu.open311.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iu.open311.database.dao.ServiceCategoryDao;
import com.iu.open311.database.model.ServiceCategory;

@androidx.room.Database(entities = {ServiceCategory.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public static String databaseName = "open311";

    public abstract ServiceCategoryDao serviceCategoryDao();

    public synchronized static Database getInstance(Context context) {
        if (null == instance) {
            instance = buildDatabase(context);
        }
        return instance;
    }

    private static Database buildDatabase(final Context context) {
        return Room.databaseBuilder(context, Database.class, databaseName).build();
    }
}
