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
import com.iu.open311.ui.newissue.step3.Step3Fragment;
import com.iu.open311.ui.newissue.step4.Step4Fragment;
import com.iu.open311.ui.newissue.step5.Step5Fragment;
import com.iu.open311.ui.newissue.step6.Step6Fragment;
import com.iu.open311.ui.newissue.step7.Step7Fragment;
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
                    return new Step3Fragment();
                case 3:
                    return new Step4Fragment();
                case 4:
                    return new Step5Fragment();
                case 5:
                    return new Step6Fragment();
                case 6:
                    return new Step7Fragment();
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