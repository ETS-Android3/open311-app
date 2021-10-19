package com.iu.open311;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.iu.open311.api.Client;
import com.iu.open311.api.dto.ServiceRequest;
import com.iu.open311.database.Result;
import com.iu.open311.database.model.ServiceCategory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Comparator;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ApiTest {

    private Client client;

    @Before
    public void setUp() {
        client = Client.getInstance(ApplicationProvider.getApplicationContext(), "foobar");
    }

    @Test
    public void testLoadRequest() {
        int serviceRequestId = 1;
        TestLifecycleOwner lifecycleOwner = new TestLifecycleOwner();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> client.loadRequest(serviceRequestId)
                                 .observe(lifecycleOwner, serviceRequestResult -> {
                                     assertTrue(serviceRequestResult instanceof Result.Success);
                                     ServiceRequest serviceRequest =
                                             (ServiceRequest) ((Result.Success<?>) serviceRequestResult)
                                                     .getData();
                                     assertEquals((Integer) serviceRequestId,
                                             (Integer) serviceRequest.serviceRequestId
                                     );
                                 }));
    }

    @Test
    public void loadRequests() {
        TestLifecycleOwner lifecycleOwner = new TestLifecycleOwner();
        Handler handler = new Handler(Looper.getMainLooper());
        client.loadRequests();
        handler.post(() -> client.getServiceRequests().observe(lifecycleOwner, serviceRequests -> {
            assertTrue(serviceRequests.size() > 0);
            serviceRequests.sort(
                    Comparator.comparing(serviceRequest -> serviceRequest.serviceCode));
            assertEquals((Integer) 1, (Integer) serviceRequests.get(0).serviceRequestId);
        }));
    }

    @Test
    public void loadServices() {
        TestLifecycleOwner lifecycleOwner = new TestLifecycleOwner();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> client.loadServices().observe(lifecycleOwner, serviceCategoryResult -> {
            assertTrue(serviceCategoryResult instanceof Result.Success);
            List<ServiceCategory> serviceCategories =
                    (List<ServiceCategory>) ((Result.Success<?>) serviceCategoryResult).getData();
            assertTrue(serviceCategories.size() > 0);
            serviceCategories.sort(
                    Comparator.comparing(serviceCategory -> serviceCategory.serviceName));
            assertEquals("Barrierefreiheit", serviceCategories.get(0).serviceName);
        }));
    }
}