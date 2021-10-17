package com.iu.open311.ui.issues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.iu.open311.DefaultActivity;
import com.iu.open311.NewIssueActivity;
import com.iu.open311.database.Database;
import com.iu.open311.databinding.FragmentIssuesBinding;
import com.iu.open311.ui.search.AbstractSearchFragment;

public class IssuesFragment extends AbstractSearchFragment {

    private FragmentIssuesBinding binding;

    private FragmentIssuesBinding getBinding(@NonNull LayoutInflater inflater, ViewGroup container
    ) {
        if (null == binding) {

            binding = FragmentIssuesBinding.inflate(inflater, container, false);
        }
        return binding;
    }

    @Override
    protected View getViewRoot(@NonNull LayoutInflater inflater, ViewGroup container) {
        return getBinding(inflater, container).getRoot();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = getBinding(inflater, container);

        // loading results
        binding.recyclerList.loading.setVisibility(View.VISIBLE);
        client.loadMyRequests(Database.getInstance(getContext()));
        observeMyServiceRequests();

        // Sorting
        handleSortButton(binding.btnSort);

        // New Issue Button
        handleNewIssueButton();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void handleNewIssueButton() {
        binding.btnNewIssue.setOnClickListener(view -> {
            DefaultActivity activity = (DefaultActivity) getActivity();
            activity.switchActivity(NewIssueActivity.class);
        });
    }

    private void observeMyServiceRequests() {
        client.getMyServiceRequests().observe(getViewLifecycleOwner(), serviceRequests -> {
            if (null == serviceRequests) {
                return;
            }
            binding.recyclerList.loading.setVisibility(View.GONE);
            binding.searchCountWrapper.setVisibility(View.VISIBLE);
            binding.filterWrapper.setVisibility(View.VISIBLE);
            binding.searchCount.setText(String.valueOf(serviceRequests.size()));
            entryAdapter.setServiceRequests(serviceRequests);
        });
    }
}