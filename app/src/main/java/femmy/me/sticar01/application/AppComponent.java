package femmy.me.sticar01.application;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Femmy on 9/25/2017.
 */

@SuppressWarnings("unused")
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    void inject(AppCompatActivity activity);

    @Named("AppContext")
    Context getContext();

}
