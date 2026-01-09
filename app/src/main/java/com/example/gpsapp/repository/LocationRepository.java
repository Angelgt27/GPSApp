package com.example.gpsapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.gpsapp.model.LocationData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationRepository {
    private final FusedLocationProviderClient fusedLocationClient;

    public LocationRepository(Context context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(MutableLiveData<LocationData> locationLiveData) {
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            locationLiveData.setValue(new LocationData(
                                    location.getLatitude(),
                                    location.getLongitude()
                            ));
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
