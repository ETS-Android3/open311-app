package com.iu.open311.ui.newissue;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.open311.database.Result;
import com.iu.open311.database.model.ServiceCategory;
import com.iu.open311.database.repository.ServiceCategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class NewIssueViewModel extends ViewModel {
    private boolean startedLoadingServiceCategories = false;
    private final MutableLiveData<List<ServiceCategory>> serviceCategories =
            new MutableLiveData<>();

    private String selectedServiceCategoryGroup;

    private String selectedServiceCategory;

    private String address;

    private double longitude;

    private double latitude;


    private final ServiceCategoryRepository categoryRepository;

    public NewIssueViewModel(ServiceCategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public MutableLiveData<List<ServiceCategory>> getServiceCategories() {
        return serviceCategories;
    }

    public String getSelectedServiceCategoryGroup() {
        return selectedServiceCategoryGroup;
    }

    public void setSelectedServiceCategoryGroup(String selectedServiceCategoryGroup) {
        this.selectedServiceCategoryGroup = selectedServiceCategoryGroup;
    }

    public String getSelectedServiceCategory() {
        return selectedServiceCategory;
    }

    public void setSelectedServiceCategory(String selectedServiceCategory) {
        this.selectedServiceCategory = selectedServiceCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void loadServiceCategories() {
        if (!startedLoadingServiceCategories) {
            startedLoadingServiceCategories = true;
            new Thread(() -> {
                Result<List<ServiceCategory>> result = categoryRepository.findAll();
                if (result instanceof Result.Success) {
                    this.serviceCategories.postValue((List<ServiceCategory>) ((Result.Success<?>) result).getData());
                } else {
                    Log.e(this.getClass().getSimpleName(), "Could not load service categories");
                    this.serviceCategories.postValue(new ArrayList<>());
                }
            }).start();
        }
    }
}
