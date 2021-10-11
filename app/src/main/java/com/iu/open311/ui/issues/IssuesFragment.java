package com.iu.open311.ui.issues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iu.open311.databinding.FragmentIssuesBinding;

public class IssuesFragment extends Fragment {

    private IssuessViewModel issuessViewModel;
    private FragmentIssuesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        issuessViewModel = new ViewModelProvider(this).get(IssuessViewModel.class);

        binding = FragmentIssuesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIssues;
        issuessViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}