package com.iu.open311.ui.newissue.step1;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.open311.database.Database;
import com.iu.open311.database.repository.ServiceCategoryRepository;

public class Step1ViewModelFactory implements ViewModelProvider.Factory {
    private Database database;

    public Step1ViewModelFactory(Database database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass
    ) {
        if (modelClass.isAssignableFrom(Step1ViewModel.class)) {
            return (T) new Step1ViewModel(ServiceCategoryRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
