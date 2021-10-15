package com.iu.open311.ui.newissue.step3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iu.open311.R;
import com.iu.open311.ui.newissue.AbstractStepFragment;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.StringJoiner;

public class Step3Fragment extends AbstractStepFragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private Marker marker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_new_issue_3, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

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

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
//        marker = map.addMarker(
//                new MarkerOptions().title(getResources().getString(R.string.label_marker))
//                                   .icon(BitmapDescriptorFactory.defaultMarker(
//                                           BitmapDescriptorFactory.HUE_CYAN))
//                                   .draggable(true)
//                                   .visible(true));

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 42
            );
            return;
        }
        map.setMyLocationEnabled(true);
    }
}
