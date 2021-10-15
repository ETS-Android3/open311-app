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
    private final MutableLiveData<List<ServiceCategory>> serviceCategories =
            new MutableLiveData<>();

    private String selectedServiceCategoryGroup;

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

    public ServiceCategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public void loadServiceCategories() {
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
