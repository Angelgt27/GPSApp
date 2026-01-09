package com.example.gpsapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gpsapp.model.LocationData;
import com.example.gpsapp.repository.LocationRepository;

public class LocationViewModel extends AndroidViewModel {
    private final LocationRepository repository;
    private final MutableLiveData<LocationData> locationData = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationRepository(application);
    }

    public LiveData<LocationData> getLocation() {
        return locationData;
    }
    
    public void fetchCurrentLocation() {
        repository.getCurrentLocation(locationData);
    }
}
