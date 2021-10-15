package com.iu.open311.ui.newissue.step2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.stream.Collectors;

public class Step2Fragment extends AbstractStepFragment {
    private ListView listView;
    private Step2EntryAdapter entryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_new_issue_2, container, false);
        listView = view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == getViewModel().getSelectedServiceCategory()) {
            return new VerificationError(getResources().getString(R.string.error_step2));
        }

        return null;
    }

    @Override
    public void onSelected() {
        getActivity().setTitle(R.string.new_issue_step2);
        loadServiceCategories();
        handleCategoryClick();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void handleCategoryClick() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            getViewModel().setSelectedServiceCategory(entryAdapter.getByPosition(position));
            entryAdapter.notifyDataSetChanged();
        });
    }

    private void loadServiceCategories() {
        getViewModel().loadServiceCategories();

        getViewModel().getServiceCategories()
                      .observe(getViewLifecycleOwner(), serviceCategories -> {
                          if (null == serviceCategories || serviceCategories.isEmpty()) {
                              return;
                          }

                          List<String> serviceNames = serviceCategories.stream()
                                                                       .filter(serviceCategory -> serviceCategory.group
                                                                               .equals(getViewModel()
                                                                                       .getSelectedServiceCategoryGroup()))
                                                                       .map(serviceCategory -> serviceCategory.serviceName)
                                                                       .sorted()
                                                                       .collect(
                                                                               Collectors.toList());
                          entryAdapter =
                                  new Step2EntryAdapter(getContext(), serviceNames, getViewModel(),
                                          getResources()
                                  );
                          listView.setAdapter(entryAdapter);
                      });
    }
}
