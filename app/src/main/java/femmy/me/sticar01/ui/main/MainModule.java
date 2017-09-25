package femmy.me.sticar01.ui.main;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 9/25/2017.
 */
@Module
public class MainModule {

    private final MainContract.View view;

    public MainModule(MainContract.View v) {
        view = v;
    }

    @Provides
    MainContract.View provideView(){
        return view;
    }
}
