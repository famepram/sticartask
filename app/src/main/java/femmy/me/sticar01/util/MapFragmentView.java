package femmy.me.sticar01.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolygon;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolygon;
import com.here.android.mpa.mapping.MapPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import femmy.me.sticar01.R;

/**
 * Created by Femmy on 9/25/2017.
 */

public class MapFragmentView {
    private MapFragment m_mapFragment;
    private Activity m_activity;
    private Map m_map;
    private MapMarker m_map_marker;
    private Button btnZoom;
    private Button btnCenter;
    private double lat, lng, zoom;

    public MapFragmentView(Activity activity) {
        m_activity = activity;
        initMapFragment();
    }


    private void initParentUI(){
        RelativeLayout wrapMapButtons = (RelativeLayout) m_activity.findViewById(R.id.wrap_map_button);
        wrapMapButtons.setVisibility(View.VISIBLE);
        btnZoom = (Button) m_activity.findViewById(R.id.btn_map_zoom);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_map.setZoomLevel( m_map.getZoomLevel() * 1.05 );
            }
        });

        btnCenter = (Button) m_activity.findViewById(R.id.btn_map_center);
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_map.setCenter(new GeoCoordinate(lat, lng, zoom),Map.Animation.LINEAR);
                m_map.setZoomLevel(14);
            }
        });
    }

    private void initLL(){
        lat = 49.259149;
        lng = -123.008555;
        zoom = 0.0;
    }

    private void initMapFragment() {
        initLL();

        /* Locate the mapFragment UI element */
        m_mapFragment = (MapFragment) m_activity.getFragmentManager()
                .findFragmentById(R.id.mapfragment);

        if (m_mapFragment != null) {
            /* Initialize the MapFragment, results will be given via the called back. */
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                    if (error == Error.NONE) {
                        m_map = m_mapFragment.getMap();
                        m_map.setCenter(new GeoCoordinate(lat, lng, zoom),Map.Animation.LINEAR);
                        m_map.setZoomLevel(14);
                    }
                    createMapMarker();
                    initParentUI();
                }
            });
        }
    }

    private void createMapMarker() {
        Image marker_img = new Image();
        try {
            marker_img.setImageResource(R.drawable.ic_car);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a MapMarker centered at current location with png image.
        m_map_marker = new MapMarker(new GeoCoordinate(lat, lng, zoom), marker_img);
        // add a MapMarker to current active map.
        m_map.addMapObject(m_map_marker);
    }


    public void setMarker(double lat, double lng){
        GeoCoordinate geo = new GeoCoordinate(lat, lng, zoom);
        m_map.setCenter(geo,Map.Animation.LINEAR);
        m_map.setZoomLevel(17);
        m_map_marker.setCoordinate(geo);
    }

}
