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

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_CODE = 100;
    private TextView txtLocation;
    private LocationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLocation = findViewById(R.id.txtLocation);
        Button btnGetLocation = findViewById(R.id.btnGetLocation);

        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        MirarViewModel();

        btnGetLocation.setOnClickListener(v -> {
            if (checkPermissions()) {
                viewModel.fetchCurrentLocation();
            }
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
}