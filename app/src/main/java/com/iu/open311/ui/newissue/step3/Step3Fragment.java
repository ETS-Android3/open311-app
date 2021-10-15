package com.iu.open311.ui.newissue.step3;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.StringJoiner;

public class Step3Fragment extends AbstractStepFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_new_issue_3, container, false);
        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == getViewModel().getAddress()) {
            return new VerificationError(getResources().getString(R.string.error_step3));
        }

        return null;
    }

    @Override
    public void onSelected() {
        getActivity().setTitle(R.string.new_issue_step3);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @NonNull
    private String getAddressFromLatLong(Context context, double lat, double lng) {

        Geocoder geocoder = new Geocoder(context);

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(lat, lng, 10);
            Address address = list.get(0);
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (int i = 0, max = address.getMaxAddressLineIndex(); i < max; i++) {
                stringJoiner.add(address.getAddressLine(i));
            }

            return stringJoiner.toString();
        } catch (Throwable e) {
            Log.e(getClass().getSimpleName(), "Could not determine address", e);
        }

        return "";
    }
}
