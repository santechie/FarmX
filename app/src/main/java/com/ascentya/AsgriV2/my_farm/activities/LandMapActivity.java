package com.ascentya.AsgriV2.my_farm.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LandMapActivity extends BaseActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private int selectedLand = -1;

    private GoogleMap map;

    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Polyline> polylines = new ArrayList<>();
    private ArrayList<Polygon> polygons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_map);

        setToolbarTitle("Land Area", true);

        ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);

        findViewById(R.id.complete).setOnClickListener(v -> finishMarking());

        loadLands();
    }

    private void loadLands() {
        showLoading();
        ApiHelper.loadLands(sessionManager.getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {
                hideLoading();
                if (!error) {
                    if (UserHelper.checkResponse(LandMapActivity.this, response)){
                        return;
                    }
                    LandMapActivity.this.lands.clear();
                    LandMapActivity.this.lands.addAll(lands);
                    updateLands();
                } else {
                    errorToast("Network Error!");
                }
            }
        });
    }

    private void updateLands() {
        if (lands.isEmpty()) {
            toast("No Lands");
            return;
        }

        if (selectedLand < 0) {
            showSelectLandDialog();
        }
    }

    private void showSelectLandDialog() {
        new SelectItemDialog("Lands", (List<Object>) (List<?>) lands, new SelectItemDialog.Action() {
            @Override
            public void onItemClicked(int position) {
                selectedLand = position;
                updateLands();
            }
        }).show(getSupportFragmentManager(), "land");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.9734, 78.6569) , 6.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        Marker marker = map.addMarker(markerOptions);
        markers.add(marker);
        updateDiagram();
    }

    public void updateDiagram() {
        removeAllLines();
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < markers.size(); i++) {
            polylineOptions.add(markers.get(i).getPosition());
        }
        polylines.add(map.addPolyline(polylineOptions));
    }

    private void removeAllLines() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear();
    }

    private void hideAllMarkers(){
        for (Marker marker: markers){
            marker.setVisible(false);
        }
    }

    private void finishMarking(){
        if (markers.size() < 3){
            toast("Should have atleast 3 marks");
            return;
        }

        removeAllLines();
        hideAllMarkers();

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.strokeColor(Color.RED);

        for (Marker marker: markers){
            polygonOptions.add(marker.getPosition());
        }
        polygonOptions.add(markers.get(0).getPosition());
        polygons.add(map.addPolygon(polygonOptions));
    }
}