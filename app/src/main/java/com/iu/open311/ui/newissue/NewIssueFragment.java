package com.iu.open311.ui.newissue;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.iu.open311.R;
import com.iu.open311.databinding.FragmentSearchBinding;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class NewIssueFragment extends Fragment implements Step {

    public static class StepAdapter extends AbstractFragmentStepAdapter {

        public StepAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager, context);
        }

        @Override
        public Step createStep(int position) {
            switch (position) {
                case 0:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_1, R.string.new_issue_step1);
                case 1:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_2, R.string.new_issue_step2);
                case 2:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_3, R.string.new_issue_step3);
                case 3:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_4, R.string.new_issue_step4);
                case 4:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_5, R.string.new_issue_step5);
                case 5:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_6, R.string.new_issue_step6);
                default:
                    throw new IllegalArgumentException("Unsupported position: " + position);
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            int titleStringId = R.string.new_issue;
            return new StepViewModel.Builder(context).setTitle(titleStringId).create();
        }
    }

    private FragmentSearchBinding binding;

    private static String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";
    private static String TITLE_STRING_ID_ARG_KEY = "titleStringId";

    public static NewIssueFragment newInstance(int layoutResId, int titleStringId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        args.putInt(TITLE_STRING_ID_ARG_KEY, titleStringId);
        NewIssueFragment fragment = new NewIssueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        int resourceId = (int) getArguments().get(LAYOUT_RESOURCE_ID_ARG_KEY);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        int titleStringId = (int) getArguments().get(TITLE_STRING_ID_ARG_KEY);
        getActivity().setTitle(titleStringId);
        return inflater.inflate(resourceId, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}