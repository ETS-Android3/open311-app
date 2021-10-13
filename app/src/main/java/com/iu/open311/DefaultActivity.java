package com.iu.open311;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class DefaultActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                switchActivity(SettingsActivity.class);
                return true;
            case R.id.action_credits:
                switchActivity(CreditsActivity.class);
                return true;
            default:
                Log.e(this.getClass().getSimpleName(), "Unknown menu option");
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchActivity(Class<? extends Activity> activityClass
    ) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
