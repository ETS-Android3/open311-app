package com.iu.open311.ui.newissue;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
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

    private LatLng position;

    private String description;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;


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

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void loadServiceCategories() {
        if (!startedLoadingServiceCategories) {
            startedLoadingServiceCategories = true;
            new Thread(() -> {
                Result<List<ServiceCategory>> result = categoryRepository.findAll();
                if (result instanceof Result.Success) {
                    this.serviceCategories.postValue(
                            (List<ServiceCategory>) ((Result.Success<?>) result).getData());
                } else {
                    Log.e(this.getClass().getSimpleName(), "Could not load service categories");
                    this.serviceCategories.postValue(new ArrayList<>());
                }
            }).start();
        }
    }
}
