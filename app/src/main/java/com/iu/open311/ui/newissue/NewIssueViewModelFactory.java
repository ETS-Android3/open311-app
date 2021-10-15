package com.iu.open311.ui.newissue;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.open311.database.Database;
import com.iu.open311.database.repository.ServiceCategoryRepository;
import com.iu.open311.ui.newissue.NewIssueViewModel;

public class NewIssueViewModelFactory implements ViewModelProvider.Factory {
    private Database database;

    public NewIssueViewModelFactory(Database database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass
    ) {
        if (modelClass.isAssignableFrom(NewIssueViewModel.class)) {
            return (T) new NewIssueViewModel(ServiceCategoryRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
