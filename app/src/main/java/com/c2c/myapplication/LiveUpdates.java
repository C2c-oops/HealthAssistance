package com.c2c.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class LiveUpdates extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    public TextView textViewSeeMore;

    private LottieAnimationView lottieAnimationView;

    public LinearLayout linearLayoutMoreData;
    public CardView cardView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_live_updates);
        mapView = findViewById(R.id.mapViewLU);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        textViewSeeMore = findViewById(R.id.tvLUSeeMore1);
        textViewSeeMore.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    LiveUpdates.this, R.style.BottomSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.layout_bottom_sheet,
                            findViewById(R.id.relativeLayoutBottomSheetContainer)
                    );
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        lottieAnimationView = findViewById(R.id.lottieViewMore);
        linearLayoutMoreData = findViewById(R.id.linearLayoutCV1MoreData);
        cardView = findViewById(R.id.cardViewLU1);

        cardView.setOnClickListener(v -> {
            if(linearLayoutMoreData.getVisibility()==View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                linearLayoutMoreData.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(View.GONE);
            } else{
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                linearLayoutMoreData.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LiveUpdates.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, this::enableLocationComponent);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadMapStyle){
        //checking permission
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            //get instance of component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            //activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadMapStyle).build());
            //enabling for making component visible
            locationComponent.setLocationComponentEnabled(true);
            //component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            //component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explained, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
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

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
