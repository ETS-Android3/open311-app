package com.iu.open311;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.iu.open311.databinding.ActivityNewIssueBinding;
import com.iu.open311.ui.newissue.NewIssueFragment;
import com.iu.open311.ui.newissue.NewIssueStep;
import com.iu.open311.ui.newissue.NewIssueStep1;
import com.iu.open311.ui.newissue.NewIssueStep2;
import com.iu.open311.ui.newissue.NewIssueStep3;
import com.iu.open311.ui.newissue.NewIssueStep4;
import com.iu.open311.ui.newissue.NewIssueStep5;
import com.iu.open311.ui.newissue.NewIssueStep6;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class NewIssueActivity extends DefaultActivity implements StepperLayout.StepperListener {

    private ActivityNewIssueBinding binding;
    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewIssueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(
                new NewIssueFragment.StepAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCompleted(View completeButton) {

        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        NewIssueStep step = getStepByPosition(newStepPosition);
        if (null != step) {
            step.setupLayout();
        }
    }

    @Override
    public void onReturn() {
        finish();
    }

    private NewIssueStep getStepByPosition(int position) {
        switch (position) {
            case 0:
                return new NewIssueStep1();
            case 1:
                return new NewIssueStep2();
            case 2:
                return new NewIssueStep3();
            case 3:
                return new NewIssueStep4();
            case 4:
                return new NewIssueStep5();
            case 5:
                return new NewIssueStep6();
        }

        return null;
    }
}