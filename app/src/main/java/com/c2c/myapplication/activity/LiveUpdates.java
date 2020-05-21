package com.c2c.myapplication.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.c2c.myapplication.R;
import com.c2c.myapplication.fragment.BottomSheet;
import com.c2c.myapplication.utils.Config;
import com.google.android.material.switchmaterial.SwitchMaterial;
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
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class LiveUpdates extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, AdapterView.OnItemSelectedListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private ValueAnimator redZoneColorAnimator;
    private ValueAnimator orangeZoneColorAnimator;

    public TextView textViewSeeMore;

    private LottieAnimationView lottieAnimationView;

    public LinearLayout linearLayoutMoreData;
    public CardView cardView;
    private SwitchMaterial switchMaterial1, switchMaterial2, switchMaterial3;

    private Spinner spinner;

    TextView tvSConfirm, tvSRecover, tvSDeath, tvSActive, tvSDateTime, tvSDeltaConfirm, tvSDeltaRecover, tvSDeltaDeath;

    ArrayList<String> regionalData;
    JSONArray statewise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_live_updates);
        mapView = findViewById(R.id.mapViewLU);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        regionalData = new ArrayList<>();

        spinner = findViewById(R.id.spinnerLUState);
        spinner.setOnItemSelectedListener(this);

        tvSConfirm = findViewById(R.id.tvCV1CountA);
        tvSRecover = findViewById(R.id.tvCV1CountR);
        tvSDeath = findViewById(R.id.tvCV1CountD);
        tvSActive = findViewById(R.id.tvCV1CountAT);
        tvSDateTime = findViewById(R.id.tvLUDateTime);
        tvSDeltaConfirm = findViewById(R.id.tvDeltaAffectCountState);
        tvSDeltaRecover = findViewById(R.id.tvDeltaRecoverCountState);
        tvSDeltaDeath = findViewById(R.id.tvDeltaDeceasedCountState);

        switchMaterial1 = findViewById(R.id.switchRed);
        switchMaterial2 = findViewById(R.id.switchOrange);

        textViewSeeMore = findViewById(R.id.tvLUSeeMore1);
        textViewSeeMore.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.show(getSupportFragmentManager(),"IndiaData");
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
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                response -> {
                    JSONObject j;
                    try {
                        j = new JSONObject(response);
                        statewise = j.getJSONArray(Config.JSON_ARRAY);
                        getRegionalData(statewise);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getRegionalData(JSONArray j) {
        for (int i = 1; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                regionalData.add(json.getString(Config.TAG_STATE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(LiveUpdates.this, android.R.layout.simple_spinner_dropdown_item, regionalData));
    }

    private String getConfirmed(int position) {
        String confirmed = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            confirmed = json.getString(Config.TAG_CONFIRMED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return confirmed;
    }

    private String getRecovered(int position) {
        String recovered = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            recovered = json.getString(Config.TAG_RECOVERED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recovered;
    }

    private String getDeaths(int position) {
        String deaths = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            deaths = json.getString(Config.TAG_DEATHS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deaths;
    }

    private String getActive(int position) {
        String active = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            active = json.getString(Config.TAG_ACTIVE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return active;
    }

    private String getDateTime(int position) {
        String dateTime = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            dateTime = json.getString(Config.TAG_DATETIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private String getDeltaConfirm(int position) {
        String deltaConfirm = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            deltaConfirm = json.getString(Config.TAG_DCONFIRMED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deltaConfirm;
    }

    private String getDeltaRecover(int position) {
        String deltaRecover = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            deltaRecover = json.getString(Config.TAG_DRECOVERED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deltaRecover;
    }

    private String getDeltaDeath(int position) {
        String deltaDeath = "";
        try {
            JSONObject json = statewise.getJSONObject(position);
            deltaDeath = json.getString(Config.TAG_DDEATHS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deltaDeath;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LiveUpdates.this.mapboxMap = mapboxMap;

        //mapboxMap.setStyle(Style.MAPBOX_STREETS, this::enableLocationComponent);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            GeoJsonSource redZoneSource = null;
            try {
                redZoneSource = new GeoJsonSource("redZone", new URI("https://gist.githubusercontent.com/C2c-oops/71b63d6d64319719235042fcbf10fd57/raw/cd29cf24d2efd8d6eb82f442d60392846302fa89/redzonemap.geojson"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            assert redZoneSource != null;
            style.addSource(redZoneSource);

            CircleLayer redZoneLayer = new CircleLayer("redZone", "redZone").withProperties(
                    circleColor(Color.parseColor("#5a9fcf")),
                    visibility(Property.NONE)
            );

            style.addLayer(redZoneLayer);

            final CircleLayer redZone = (CircleLayer) style.getLayer("redZone");

            redZoneColorAnimator = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    Color.parseColor("#EC8A8A"), // Brighter shade
                    Color.parseColor("#DE3232") // Darker shade
            );
            redZoneColorAnimator.setDuration(1000);
            redZoneColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
            redZoneColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
            redZoneColorAnimator.addUpdateListener(animator -> {
                if (redZone != null) {
                    redZone.setProperties(
                            circleColor((int) animator.getAnimatedValue())
                    );
                }
            });

            GeoJsonSource orangeZoneSource = null;
            try {
                orangeZoneSource = new GeoJsonSource("orangeZone", new URI("https://gist.githubusercontent.com/C2c-oops/4933e34faa844822e8aaf5464077958e/raw/81cf17107b3ed008d8f60c8f9c59c8e1f89c6252/orangezonemap.geojson"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            assert orangeZoneSource != null;
            style.addSource(orangeZoneSource);

            CircleLayer orangeZoneLayer = new CircleLayer("orangeZone", "orangeZone").withProperties(
                    circleColor(Color.parseColor("#5a9fcf")),
                    visibility(Property.NONE)
            );

            style.addLayer(orangeZoneLayer);

            final CircleLayer orangeZone = (CircleLayer) style.getLayer("orangeZone");

            orangeZoneColorAnimator = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    Color.parseColor("#FFC680"), // Brighter shade
                    Color.parseColor("#FF8C00") // Darker shade
            );

            orangeZoneColorAnimator.setDuration(1000);
            orangeZoneColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
            orangeZoneColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
            orangeZoneColorAnimator.addUpdateListener(animator -> {
                if (orangeZone != null) {
                    orangeZone.setProperties(
                            circleColor((int) animator.getAnimatedValue())
                    );
                }
            });
            switchMaterial1.setOnCheckedChangeListener((buttonView, isChecked) -> setLayerVisible("redZone", style));
            switchMaterial2.setOnCheckedChangeListener((buttonView, isChecked) -> setLayerVisible("orangeZone", style));

            enableLocationComponent(style);
            redZoneColorAnimator.start();
            orangeZoneColorAnimator.start();
        });
    }

    private void setLayerVisible(String layerId, @NonNull Style loadedMapStyle) {
        Layer layer = loadedMapStyle.getLayer(layerId);
        if (layer == null) {
            return;
        }
        if (VISIBLE.equals(layer.getVisibility().getValue())) {
            // Layer is visible
            layer.setProperties(
                    visibility(Property.NONE)
            );
        } else {
            // Layer isn't visible
            layer.setProperties(
                    visibility(VISIBLE)
            );
        }
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
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (redZoneColorAnimator != null) {
            redZoneColorAnimator.start();
        }
        if (orangeZoneColorAnimator != null) {
            orangeZoneColorAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (redZoneColorAnimator != null) {
            redZoneColorAnimator.cancel();
        }
        if (orangeZoneColorAnimator != null) {
            orangeZoneColorAnimator.cancel();
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvSConfirm.setText(getConfirmed(position + 1));
        tvSRecover.setText(getRecovered(position + 1));
        tvSDeath.setText(getDeaths(position + 1));
        tvSActive.setText(getActive(position + 1));
        tvSDateTime.setText(getDateTime(position + 1));
        tvSDeltaConfirm.setText(getDeltaConfirm(position + 1));
        tvSDeltaRecover.setText(getDeltaRecover(position + 1));
        tvSDeltaDeath.setText(getDeltaDeath(position + 1));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tvSConfirm.setText("NA");
        tvSRecover.setText("NA");
        tvSDeath.setText("NA");
        tvSActive.setText("NA");
        tvSDateTime.setText("NA");
        tvSDeltaConfirm.setText("NA");
        tvSDeltaRecover.setText("NA");
        tvSDeltaDeath.setText("NA");
    }
}
