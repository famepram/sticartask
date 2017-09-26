package femmy.me.sticar01.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import femmy.me.sticar01.R;
import femmy.me.sticar01.application.App;
import femmy.me.sticar01.util.MapFragmentView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainContract.View,
        LocationListener,
        View.OnClickListener {

    @Inject
    MainPresenter presenter;

    MainComponent actvComponent;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private MapFragmentView m_mapFragmentView;

    DrawerLayout drawer;
    FloatingActionButton fabMenu, fabRefresh;
    NavigationView navigationView;
    Button btnCloseDrawer, btnOpenBottomDrawer, btnCloseBottomDrawer;
    SlideUp slideUp;
    private View dim;

    private LocationManager locationManager;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actvComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);

        initView();
        presenter.requestPermission(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        presenter.initLocationManager(locationManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
        fabMenu.setOnClickListener(this);

        fabRefresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(this);

        btnOpenBottomDrawer = (Button) findViewById(R.id.btn_open_bottom_drawer);
        btnOpenBottomDrawer.setOnClickListener(this);
        btnCloseBottomDrawer = (Button) findViewById(R.id.btn_close_bottom_drawer);
        btnCloseBottomDrawer.setOnClickListener(this);

        // init nav & set fullwidth
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);

        setNavItemCount(R.id.nav_camera);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        btnCloseDrawer = (Button) headerView.findViewById(R.id.btn_drawer_close);
        btnCloseDrawer.setOnClickListener(this);


        dim = findViewById(R.id.dim);
        RelativeLayout bottomPanelSlide = (RelativeLayout) findViewById(R.id.panel_bottom_drawer);
        slideUp = new SlideUpBuilder(bottomPanelSlide)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                    }
                })
                .withStartState(SlideUp.State.HIDDEN)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartGravity(Gravity.BOTTOM).build();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {

                        /**
                         * If the user turned down the permission request in the past and chose the
                         * Don't ask again option in the permission request system dialog.
                         */
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[index])) {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " not granted. "
                                            + "Please go to settings and turn on for sample app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,
                                    "Required permission " + permissions[index] + " not granted",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                /**
                 * All permission requests are being handled.Create map fragment view.Please note
                 * the HERE SDK requires all permissions defined above to operate properly.
                 */
                m_mapFragmentView = new MapFragmentView(this);
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == fabMenu.getId()) {
            drawer.openDrawer(GravityCompat.START);
        } else if (view.getId() == btnCloseDrawer.getId()) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == fabRefresh.getId()) {
            Toast.makeText(this,"Reloading...",Toast.LENGTH_LONG).show();
        } else if (view.getId() == btnOpenBottomDrawer.getId()) {
            slideUp.show();
        } else if (view.getId() == btnCloseBottomDrawer.getId()) {
            slideUp.hide();
        }
    }

    private void setNavItemCount(@IdRes int itemId) {
        RelativeLayout view = (RelativeLayout) navigationView.getMenu().findItem(itemId).getActionView();
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onLocationChanged(Location location) {
        if(m_mapFragmentView != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            m_mapFragmentView.setMarker(lat,lng);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void locationIniated(String provider, Location location) {
        this.provider = provider;
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(this,"Location not available",Toast.LENGTH_SHORT).show();
        }
    }
}
