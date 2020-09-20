package com.c2c.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.c2c.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class NearbyHospital extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivityTest";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    boolean trakingFlag;
    Boolean mLocationPermissionsGranted = false;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double latitude, longitude;
    int proximityRadius = 10000;
    Button fndHospitalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital);

        getLocationPermission();

        fndHospitalButton = findViewById(R.id.btn_find);
        fndHospitalButton.setOnClickListener(view -> nearbyHospitals());
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(NearbyHospital.this);
    }

    private void getLocationPermission() {
        if (!trakingFlag) {
            startTrackingLocation();
        } else {
            stopTrackingLocation();
        }
    }

    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initMap();
            Toast.makeText(this, "Location Permission Already Granted", Toast.LENGTH_SHORT).show();
            trakingFlag = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    boolean somePermissionsForeverDenied = false;
                    if (!showRationale) {
                        somePermissionsForeverDenied = true;
                        Toast.makeText(this, "Forever Denied", Toast.LENGTH_SHORT).show();
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("Permission Required")
                                .setMessage("Location permission required for retrieving your current address else service can't be used.")
                                .setPositiveButton("Retry", (dialogInterface, i13) -> startTrackingLocation())
                                .setNegativeButton("Cancel", (dialogInterface, i14) -> finish())
                                .setCancelable(false)
                                .create()
                                .show();
                        Toast.makeText(this, "Explanation", Toast.LENGTH_SHORT).show();
                    }
                    if (somePermissionsForeverDenied) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Permission Required")
                                .setMessage("You have forcefully denied some of the required permissions " +
                                        "for this action. These permissions are required for using service." +
                                        "Please open settings, go to permissions and allow them.")
                                .setPositiveButton("Settings", (dialogInterface, i12) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("Cancel", (dialogInterface, i1) -> finish())
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                }
            }
        }
    }

    private void nearbyHospitals() {
    }

    private void stopTrackingLocation() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
