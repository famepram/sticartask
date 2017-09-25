package femmy.me.sticar01.ui.main;

import dagger.Component;
import femmy.me.sticar01.annot.PerActivity;
import femmy.me.sticar01.application.AppComponent;

/**
 * Created by Femmy on 9/25/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules=MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}

