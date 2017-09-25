package femmy.me.sticar01.ui.main;

import android.content.Context;

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

    @Inject
    public MainPresenter(MainContract.View view) {
        this.view = view;

    }
}
