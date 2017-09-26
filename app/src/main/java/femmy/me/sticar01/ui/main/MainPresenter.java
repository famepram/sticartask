package femmy.me.sticar01.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Femmy on 9/25/2017.
 */

public class MainPresenter implements MainContract.Presenter{

    @Named("AppContext")
    @Inject
    Context context;

    MainContract.View view;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Inject
    public MainPresenter(MainContract.View view) {
        this.view = view;

    }

    @Override
    public void requestPermission(Activity act) {
        final List<String> requiredSDKPermissions = new ArrayList<String>();
        requiredSDKPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredSDKPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredSDKPermissions.add(Manifest.permission.INTERNET);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        requiredSDKPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);

        ActivityCompat.requestPermissions(act,
                requiredSDKPermissions.toArray(new String[requiredSDKPermissions.size()]),
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void initLocationManager(LocationManager lm) {
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = lm.getLastKnownLocation(provider);
        view.locationIniated(provider,location);
    }
}
