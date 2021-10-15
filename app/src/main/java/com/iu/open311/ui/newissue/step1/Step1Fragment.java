package com.iu.open311.ui.newissue.step1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.stream.Collectors;

public class Step1Fragment extends AbstractStepFragment {
    private GridView gridView;
    private Step1EntryAdapter entryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_new_issue_1, container, false);
        gridView = view.findViewById(R.id.gridView);

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == getViewModel().getSelectedServiceCategoryGroup()) {
            return new VerificationError(getResources().getString(R.string.error_step1));
        }

        return null;
    }

    @Override
    public void onSelected() {
        getActivity().setTitle(R.string.new_issue_step1);
        loadServiceCategoryGroups();
        handleGroupClick();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void handleGroupClick() {
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            getViewModel().setSelectedServiceCategoryGroup(entryAdapter.getByPosition(position));
            entryAdapter.notifyDataSetChanged();
        });
    }

    private void loadServiceCategoryGroups() {
        getViewModel().loadServiceCategories();

        getViewModel().getServiceCategories()
                      .observe(getViewLifecycleOwner(), serviceCategories -> {
                          if (null == serviceCategories || serviceCategories.isEmpty()) {
                              return;
                          }

                          List<String> groups = serviceCategories.stream()
                                                                 .map(serviceCategory -> serviceCategory.group)
                                                                 .distinct()
                                                                 .sorted()
                                                                 .collect(Collectors.toList());
                          entryAdapter = new Step1EntryAdapter(getContext(), groups, getViewModel(),
                                  getResources()
                          );
                          gridView.setAdapter(entryAdapter);
                      });
    }
}
