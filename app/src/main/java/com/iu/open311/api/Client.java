package com.iu.open311.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.iu.open311.BuildConfig;
import com.iu.open311.R;
import com.iu.open311.database.Database;
import com.iu.open311.database.model.ServiceCategory;
import com.iu.open311.ui.newissue.NewIssueViewModel;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Client {
    private static Client instance;
    private static Context context;

    public synchronized static Client getInstance(Context context) {
        Client.context = context;
        if (null == instance) {
            instance = new Client();
            AndroidNetworking.initialize(context);
            AndroidNetworking.setParserFactory(new JacksonParserFactory());
        }
        return instance;
    }

    public void postNewIssue(NewIssueViewModel viewModel) {
        String requestUrl = createApiRequestUrl("requests");

        // https://github.com/bfpi/klarschiff-citysdk
        StringJoiner urlJoiner = new StringJoiner("&");
        urlJoiner.add(requestUrl);

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("api_key", "foobar");
        apiParams.put("email", viewModel.getEmail());
        apiParams.put("service_code", String.valueOf(viewModel.getSelectedServiceCategory().first));
        apiParams.put("description", viewModel.getDescription());

        if (null != viewModel.getPosition()) {
            apiParams.put("lat", String.valueOf(viewModel.getPosition().latitude));
            apiParams.put("long", String.valueOf(viewModel.getPosition().longitude));
        } else {
            apiParams.put("address_string", viewModel.getAddress());
        }

        apiParams.forEach((key, value) -> urlJoiner.add(key + "=" + value));

        Log.d(Client.class.getSimpleName(), "Sending request to: " + urlJoiner.toString());

        AndroidNetworking.post(urlJoiner.toString())
                         .build()
                         .getAsObject(PostRequestResponse.class, new ParsedRequestListener() {
                             @Override
                             public void onResponse(Object response) {
                                 Log.i(Client.class.getSimpleName(), response.toString());
                             }

                             @Override
                             public void onError(ANError error) {
                                 Log.e(Client.class.getSimpleName(), error.getErrorDetail());
                                 Toast.makeText(context, R.string.error_add_new_issue,
                                         Toast.LENGTH_SHORT
                                 ).show();
                                 Toast.makeText(context, error.getErrorDetail(), Toast.LENGTH_LONG).show();
                             }
                         });
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
                                         Log.e(Client.class.getSimpleName(), error.getErrorDetail());
                                     }
                                 }
                         );
    }

    private String createApiRequestUrl(String service) {
        return BuildConfig.API_BASE_URL + service + ".json?jurisdiction_id=" +
                BuildConfig.JURISDICTION_ID;
    }
}
