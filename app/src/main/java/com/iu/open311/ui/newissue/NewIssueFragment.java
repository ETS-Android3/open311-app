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
import com.iu.open311.ui.newissue.step1.Step1Fragment;
import com.iu.open311.ui.newissue.step2.Step2Fragment;
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
                    return new Step1Fragment();
                case 1:
                    return new Step2Fragment();
                case 2:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_3);
                case 3:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_4);
                case 4:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_5);
                case 5:
                    return NewIssueFragment.newInstance(R.layout.fragment_new_issue_6);
                default:
                    throw new IllegalArgumentException("Unsupported position: " + position);
            }
        }

        @Override
        public int getCount() {
            return 7;
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            int titleStringId = R.string.new_issue;
            return new StepViewModel.Builder(context).setTitle(titleStringId).create();
        }
    }

    private static String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";

    public static NewIssueFragment newInstance(int layoutResId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        NewIssueFragment fragment = new NewIssueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        int resourceId = (int) getArguments().get(LAYOUT_RESOURCE_ID_ARG_KEY);
        View inflate = inflater.inflate(resourceId, container, false);

        return inflate;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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