package femmy.me.sticar01.ui.main;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

/**
 * Created by Femmy on 9/25/2017.
 */

public class MainContract {

    interface View {
        void locationIniated(String provider, Location location);
    }

    interface Presenter {
        void requestPermission(Activity act);

        void initLocationManager(LocationManager lm);

    }
}
