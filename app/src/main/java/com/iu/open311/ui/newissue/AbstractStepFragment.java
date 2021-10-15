package com.iu.open311.ui.newissue;

import androidx.fragment.app.Fragment;

import com.iu.open311.NewIssueActivity;
import com.stepstone.stepper.Step;

public abstract class AbstractStepFragment extends Fragment implements Step {

    protected NewIssueViewModel getViewModel() {
        return ((NewIssueActivity) getActivity()).getViewModel();
    }
}
