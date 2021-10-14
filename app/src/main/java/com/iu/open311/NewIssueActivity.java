package com.iu.open311;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.iu.open311.databinding.ActivityNewIssueBinding;
import com.iu.open311.ui.newissue.NewIssueFragment;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class NewIssueActivity extends DefaultActivity implements StepperLayout.StepperListener {

    private ActivityNewIssueBinding binding;
    private StepperLayout stepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewIssueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle(R.string.new_issue);
        stepperLayout = findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(
                new NewIssueFragment.StepAdapter(getSupportFragmentManager(), this));
        stepperLayout.setListener(this);

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
        Toast.makeText(this, verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onReturn() {
        finish();
    }
}