package io.harry.doodlenow;

import android.app.Application;

import dagger.ObjectGraph;

public class DoodleApplication extends Application {

    private static ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        objectGraph = ObjectGraph.create(new ApplicationModule(this));
        super.onCreate();
    }

    public static void inject(Object object) {
        objectGraph.inject(object);
    }
}
