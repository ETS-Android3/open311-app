package com.iu.open311;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.iu.open311.api.Client;
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
    private boolean hasNecessaryPermissions = false;

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    permissionMap -> {
                        hasNecessaryPermissions = !permissionMap.values().contains(false);
                    }
            );

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

        checkNecessaryPermissions();
    }

    private void checkNecessaryPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
            hasNecessaryPermissions = true;
        } else {
            requestPermissionLauncher.launch(new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
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
        Client apiClient = Client.getInstance(getApplicationContext());
        apiClient.postRequests(getViewModel());
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, verificationError.getErrorMessage(), Toast.LENGTH_LONG).show();
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

    public boolean hasNecessaryPermissions() {
        return hasNecessaryPermissions;
    }
}