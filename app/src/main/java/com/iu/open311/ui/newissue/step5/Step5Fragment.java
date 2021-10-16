package com.iu.open311.ui.newissue.step5;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

public class Step5Fragment extends AbstractStepFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.fragment_new_issue_5, container, false);
        return view;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        getActivity().setTitle(R.string.new_issue_step5);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
