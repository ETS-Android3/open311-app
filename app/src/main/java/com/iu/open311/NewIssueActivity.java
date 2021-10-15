package com.iu.open311;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.iu.open311.database.Database;
import com.iu.open311.databinding.ActivityNewIssueBinding;
import com.iu.open311.ui.newissue.NewIssueFragment;
import com.iu.open311.ui.newissue.NewIssueViewModel;
import com.iu.open311.ui.newissue.NewIssueViewModelFactory;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class NewIssueActivity extends DefaultActivity implements StepperLayout.StepperListener {

    private ActivityNewIssueBinding binding;
    private StepperLayout stepperLayout;
    private NewIssueViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewIssueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this,
                new NewIssueViewModelFactory(Database.getInstance(getApplicationContext()))
        ).get(NewIssueViewModel.class);

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

    public NewIssueViewModel getViewModel() {
        return viewModel;
    }
}