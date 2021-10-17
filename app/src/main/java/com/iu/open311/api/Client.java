package com.iu.open311.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.iu.open311.BuildConfig;
import com.iu.open311.R;
import com.iu.open311.api.dto.DefaultResult;
import com.iu.open311.api.dto.PostRequestResponse;
import com.iu.open311.api.dto.ServiceRequest;
import com.iu.open311.common.threads.ThreadExecutorSupplier;
import com.iu.open311.database.Database;
import com.iu.open311.database.model.ServiceCategory;
import com.iu.open311.ui.newissue.NewIssueViewModel;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Client {
    private static Client instance;
    private static Context context;

    private final MutableLiveData<List<ServiceRequest>> serviceRequests = new MutableLiveData<>();
    private final MutableLiveData<List<ServiceRequest>> myServiceRequests = new MutableLiveData<>();

    public MutableLiveData<List<ServiceRequest>> getServiceRequests() {
        return serviceRequests;
    }

    public MutableLiveData<List<ServiceRequest>> getMyServiceRequests() {
        return myServiceRequests;
    }

    private Client() {
    }

    public synchronized static Client getInstance(Context context) {
        Client.context = context;
        if (null == instance) {
            instance = new Client();
            AndroidNetworking.initialize(context);
            AndroidNetworking.setParserFactory(new JacksonParserFactory());
        }
        return instance;
    }

    public MutableLiveData<DefaultResult<ServiceRequest>> loadRequest(Integer serviceRequestId) {

        MutableLiveData<DefaultResult<ServiceRequest>> serviceRequestLiveData =
                new MutableLiveData<>();
        DefaultResult<ServiceRequest> result = new DefaultResult<>();

        AndroidNetworking.get(createApiRequestUrl("requests/" + serviceRequestId))
                         .build()
                         .getAsObjectList(ServiceRequest.class,
                                 new ParsedRequestListener<List<ServiceRequest>>() {
                                     @Override
                                     public void onResponse(List<ServiceRequest> serviceRequests
                                     ) {
                                         if (serviceRequests.size() > 0) {
                                             result.setData(serviceRequests.get(0));
                                         } else {
                                             result.setError("No data found");
                                         }
                                         serviceRequestLiveData.postValue(result);
                                     }

                                     @Override
                                     public void onError(ANError error) {
                                         Log.e(Client.class.getSimpleName(),
                                                 error.getErrorDetail() + ": " + error.getMessage()
                                         );

                                         result.setError(error.getErrorDetail());
                                         serviceRequestLiveData.postValue(result);
                                     }
                                 }
                         );

        return serviceRequestLiveData;
    }

    public void loadMyRequests(Database database) {
        ThreadExecutorSupplier.getInstance().getMajorBackgroundTasks().execute(() -> {
            List<Integer> serviceRequestIds = database.myServiceRequestDao()
                                                      .findAll()
                                                      .stream()
                                                      .map(serviceRequest -> serviceRequest.serviceRequestId)
                                                      .collect(Collectors.toList());

            // todo: this is stubbed until we have an api key and can store custom service requests:
            if (serviceRequestIds.isEmpty()) {
                serviceRequestIds.add(1);
                serviceRequestIds.add(4);
                serviceRequestIds.add(6);
            }

            StringJoiner stringJoiner = new StringJoiner(",");
            serviceRequestIds.forEach(id -> stringJoiner.add(String.valueOf(id)));
            String apiRequestUrl = createApiRequestUrl("requests") + "&service_request_id=" +
                    stringJoiner.toString();

            AndroidNetworking.get(apiRequestUrl)
                             .build()
                             .getAsObjectList(ServiceRequest.class,
                                     new ParsedRequestListener<List<ServiceRequest>>() {
                                         @Override
                                         public void onResponse(List<ServiceRequest> serviceRequests
                                         ) {
                                             Client.this.myServiceRequests.postValue(
                                                     serviceRequests);
                                         }

                                         @Override
                                         public void onError(ANError error) {
                                             Log.e(Client.class.getSimpleName(),
                                                     error.getErrorDetail() + ": " +
                                                             error.getMessage()
                                             );
                                         }
                                     }
                             );
        });
    }

    public void loadRequests() {
        AndroidNetworking.get(createApiRequestUrl("requests"))
                         .build()
                         .getAsObjectList(ServiceRequest.class,
                                 new ParsedRequestListener<List<ServiceRequest>>() {
                                     @Override
                                     public void onResponse(List<ServiceRequest> serviceRequests
                                     ) {
                                         Client.this.serviceRequests.postValue(serviceRequests);
                                     }

                                     @Override
                                     public void onError(ANError error) {
                                         Log.e(Client.class.getSimpleName(),
                                                 error.getErrorDetail() + ": " + error.getMessage()
                                         );
                                     }
                                 }
                         );
    }

    public void loadServices(Database database) throws IOException {
        AndroidNetworking.get(createApiRequestUrl("services"))
                         .build()
                         .getAsObjectList(ServiceCategory.class,
                                 new ParsedRequestListener<List<ServiceCategory>>() {
                                     @Override
                                     public void onResponse(List<ServiceCategory> serviceCategories
                                     ) {
                                         ThreadExecutorSupplier.getInstance()
                                                               .getMajorBackgroundTasks()
                                                               .execute(() -> {
                                                                   database.serviceCategoryDao()
                                                                           .deleteAll();
                                                                   serviceCategories.forEach(
                                                                           serviceCategory -> database
                                                                                   .serviceCategoryDao()
                                                                                   .insert(serviceCategory));
                                                               });
                                     }

                                     @Override
                                     public void onError(ANError error) {
                                         Log.e(Client.class.getSimpleName(),
                                                 error.getErrorDetail() + ": " + error.getMessage()
                                         );
                                     }
                                 }
                         );
    }

    public void postRequests(NewIssueViewModel viewModel) {
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
                                 Log.e(Client.class.getSimpleName(),
                                         error.getErrorDetail() + ": " + error.getMessage()
                                 );
                                 Toast.makeText(context, R.string.error_add_new_issue,
                                         Toast.LENGTH_SHORT
                                 ).show();
                                 Toast.makeText(context, error.getErrorDetail(), Toast.LENGTH_LONG)
                                      .show();
                             }
                         });
    }

    private String createApiRequestUrl(String service) {
        return BuildConfig.API_BASE_URL + service + ".json?jurisdiction_id=" +
                BuildConfig.JURISDICTION_ID;
    }
}
