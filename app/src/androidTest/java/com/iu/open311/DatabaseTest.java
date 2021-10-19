package com.iu.open311;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.iu.open311.database.Database;
import com.iu.open311.database.model.MyServiceRequest;
import com.iu.open311.database.model.ServiceCategory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    Database database;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
    }

    @Test
    public void testServiceCategories() {
        List<ServiceCategory> serviceCategories = database.serviceCategoryDao().findAll();
        assertEquals(0, serviceCategories.size());

        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.serviceName = "foobar";
        serviceCategory.serviceCode = 42;
        serviceCategory.group = "barfoo";
        serviceCategory.groupId = 23;
        serviceCategory.keywords = "foo, bar";

        database.serviceCategoryDao().insert(serviceCategory);

        serviceCategories = database.serviceCategoryDao().findAll();
        assertEquals(1, serviceCategories.size());
        assertEquals((Integer) 42, serviceCategories.get(0).serviceCode);
        assertEquals("foobar", serviceCategories.get(0).serviceName);
        assertEquals((Integer) 23, serviceCategories.get(0).groupId);
        assertEquals("barfoo", serviceCategories.get(0).group);
        assertEquals("foo, bar", serviceCategories.get(0).keywords);
    }

    @Test
    public void testMyServiceRequests() {
        List<MyServiceRequest> myServiceRequests = database.myServiceRequestDao().findAll();
        assertEquals(0, myServiceRequests.size());

        MyServiceRequest myServiceRequest = new MyServiceRequest();
        myServiceRequest.serviceRequestId = 42;

        database.myServiceRequestDao().insert(myServiceRequest);

        myServiceRequests = database.myServiceRequestDao().findAll();
        assertEquals(1, myServiceRequests.size());
        assertEquals((Integer) 42, myServiceRequests.get(0).serviceRequestId);
    }
}