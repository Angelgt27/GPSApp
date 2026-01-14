package com.example.gpsapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.gpsapp.R;
import com.example.gpsapp.viewmodel.LocationViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_CODE = 100;
    private TextView txtLocation;
    private LocationViewModel viewModel;
    private MapView mapView;
    private Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration.getInstance().setUserAgentValue(getPackageName());

        txtLocation = findViewById(R.id.txtLocation);
        Button btnGetLocation = findViewById(R.id.btnGetLocation);
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);

        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        MirarViewModel();

        btnGetLocation.setOnClickListener(v -> {
            if (checkPermissions()) {
                viewModel.fetchCurrentLocation();
            }
        });

        viewModel.getLocation().observe(this, location -> {
            updateMap(location.getLatitude(), location.getLongitude());
        });
    }

    private void MirarViewModel() {
        viewModel.getLocation().observe(this, data -> {
            if (data != null) {
                txtLocation.setText("Latitud: " + data.getLatitude() + "\nLongitud: " + data.getLongitude());
            }
        });
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchCurrentLocation();
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMap(double lat, double lon) {
        GeoPoint point = new GeoPoint(lat, lon);
        mapView.getController().setCenter(point);
        if (userMarker == null) {
            userMarker = new Marker(mapView);
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(userMarker);
        }
        userMarker.setPosition(point);
        userMarker.setTitle("Mi ubicación");
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}