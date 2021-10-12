package com.iu.open311;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public abstract class DefaultActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                switchActivity(SettingsActivity.class, null);
                return true;
            default:
                Log.e(this.getClass().getSimpleName(), "Unknown menu option");
        }

        return super.onOptionsItemSelected(item);
    }

    protected void switchActivity(Class<? extends Activity> activityClass,
            @Nullable Map<String, String> extras
    ) {
        Intent intent = new Intent(this, activityClass);

        if (null != extras) {
            extras.forEach(intent::putExtra);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
