package femmy.me.sticar01.util;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
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

    public MapFragmentView(Activity activity) {
        m_activity = activity;
        initMapFragment();
    }

    private void initParentUI(){
        btnZoom = (Button) m_activity.findViewById(R.id.btn_map_zoom);
        btnZoom.setVisibility(View.VISIBLE);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_map.setZoomLevel( m_map.getZoomLevel() * 1.05 );
            }
        });

        btnCenter = (Button) m_activity.findViewById(R.id.btn_map_center);
        btnCenter.setVisibility(View.VISIBLE);
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_map.setCenter(new GeoCoordinate(49.259149, -123.008555, 0.0),Map.Animation.LINEAR);
                m_map.setZoomLevel(14);
            }
        });
    }

    private void initMapFragment() {
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
                        m_map.setCenter(new GeoCoordinate(49.259149, -123.008555, 0.0),Map.Animation.LINEAR);
                        m_map.setZoomLevel(14);

                        //initParentUI();
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
        m_map_marker = new MapMarker(new GeoCoordinate(49.259149, -123.008555, 0.0), marker_img);
        // add a MapMarker to current active map.
        m_map.addMapObject(m_map_marker);
    }
}
