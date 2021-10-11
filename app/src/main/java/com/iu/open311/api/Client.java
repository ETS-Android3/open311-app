package com.iu.open311.api;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.iu.open311.BuildConfig;
import com.iu.open311.database.Database;
import com.iu.open311.database.model.ServiceCategory;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.io.IOException;
import java.util.List;

public class Client {
    private static Client instance;

    public synchronized static Client getInstance(Context context) {
        if (null == instance) {
            instance = new Client();
            AndroidNetworking.initialize(context);
            AndroidNetworking.setParserFactory(new JacksonParserFactory());
        }
        return instance;
    }

    public void initServiceCategories(Database database) throws IOException {
        AndroidNetworking.get(createApiRequestUrl("services"))
                         .build()
                         .getAsObjectList(ServiceCategory.class,
                                 new ParsedRequestListener<List<ServiceCategory>>() {
                                     @Override
                                     public void onResponse(List<ServiceCategory> serviceCategories
                                     ) {
                                         new Thread(() -> {
                                             database.serviceCategoryDao().deleteAll();
                                             serviceCategories.forEach(
                                                     serviceCategory -> database.serviceCategoryDao()
                                                                                .insert(serviceCategory));
                                         }).start();
                                     }

                                     @Override
                                     public void onError(ANError error) {
                                         Log.e(Client.class.getSimpleName(), error.getMessage());
                                     }
                                 }
                         );
    }

    private String createApiRequestUrl(String service) {
        return BuildConfig.API_BASE_URL + service + ".json?jurisdiction_id=" +
                BuildConfig.JURISDICTION_ID;
    }
}
