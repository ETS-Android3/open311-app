package com.iu.open311.ui.newissue.step1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.open311.R;
import com.iu.open311.database.Database;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.stream.Collectors;

public class Step1Fragment extends Fragment implements Step {
    private GridView listView;
    private Step1EntryAdapter entryAdapter;
    private Step1ViewModel viewModel;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        getActivity().setTitle(R.string.new_issue_step1);
        view = inflater.inflate(R.layout.fragment_new_issue_1, container, false);
        listView = view.findViewById(R.id.listView);

        viewModel = new ViewModelProvider(this,
                new Step1ViewModelFactory(Database.getInstance(getContext()))
        ).get(Step1ViewModel.class);

        loadServiceCategoryGroups();
        handleCategoryClick();

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == viewModel.getSelectedServiceCategoryGroup()) {
            return new VerificationError(getResources().getString(R.string.error_step1));
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void handleCategoryClick() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.setSelectedServiceCategoryGroup(entryAdapter.getByPosition(position));
            entryAdapter.notifyDataSetChanged();
        });
    }

    private void loadServiceCategoryGroups() {
        viewModel.loadServiceCategories();

        viewModel.getServiceCategories().observe(getViewLifecycleOwner(), serviceCategories -> {
            if (null == serviceCategories) {
                return;
            }

            if (serviceCategories.isEmpty()) {
                Snackbar.make(listView, getResources().getString(R.string.error_load_categories),
                        Snackbar.LENGTH_LONG
                ).show();
            } else {

                List<String> groups = serviceCategories.stream()
                                                       .map(serviceCategory -> serviceCategory.group)
                                                       .distinct()
                                                       .sorted()
                                                       .collect(Collectors.toList());
                entryAdapter =
                        new Step1EntryAdapter(getContext(), groups, viewModel, getResources());
                listView.setAdapter(entryAdapter);
            }
        });
    }
}
