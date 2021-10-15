package com.iu.open311.ui.newissue.step1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.iu.open311.NewIssueActivity;
import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.stream.Collectors;

public class Step1Fragment extends AbstractStepFragment {
    private GridView listView;
    private Step1EntryAdapter entryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        getActivity().setTitle(R.string.new_issue_step1);
        View view = inflater.inflate(R.layout.fragment_new_issue_1, container, false);
        listView = view.findViewById(R.id.listView);

        loadServiceCategoryGroups();
        handleCategoryClick();

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == ((NewIssueActivity) getActivity()).getViewModel()
                                                      .getSelectedServiceCategoryGroup()) {
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
            getViewModel().setSelectedServiceCategoryGroup(entryAdapter.getByPosition(position));
            entryAdapter.notifyDataSetChanged();
        });
    }

    private void loadServiceCategoryGroups() {
        getViewModel().loadServiceCategories();

        getViewModel().getServiceCategories()
                      .observe(getViewLifecycleOwner(), serviceCategories -> {
                          if (null == serviceCategories) {
                              return;
                          }

                          if (serviceCategories.isEmpty()) {
                              Snackbar.make(listView,
                                      getResources().getString(R.string.error_load_categories),
                                      Snackbar.LENGTH_LONG
                              ).show();
                          } else {

                              List<String> groups = serviceCategories.stream()
                                                                     .map(serviceCategory -> serviceCategory.group)
                                                                     .distinct()
                                                                     .sorted()
                                                                     .collect(Collectors.toList());
                              entryAdapter =
                                      new Step1EntryAdapter(getContext(), groups, getViewModel(),
                                              getResources()
                                      );
                              listView.setAdapter(entryAdapter);
                          }
                      });
    }
}
