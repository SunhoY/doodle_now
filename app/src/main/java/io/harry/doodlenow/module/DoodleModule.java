package io.harry.doodlenow.module;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.chrometab.ChromeTabHelper;
import io.harry.doodlenow.converter.DoodleBitmapFactory;
import io.harry.doodlenow.fragment.doodlerange.DoodleRangeCalculator;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;
import io.harry.doodlenow.wrapper.DoodlePagerAdapterWrapper;

@Module
public class DoodleModule {
    private Context context;

    public DoodleModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public DoodlePagerAdapterWrapper provideDoodlePagerAdapterWrapper() {
        return new DoodlePagerAdapterWrapper();
    }

    @Provides @Singleton
    public DoodleListFragmentWrapper provideDoodleListFragmentWrapper() {
        return new DoodleListFragmentWrapper();
    }

    @Provides
    public DoodleIcon provideDoodleIcon() {
        return new DoodleIcon(context);
    }

    @Provides
    public DoodleListAdapter provideDoodleListAdapter() {
        return new DoodleListAdapter(context, new ArrayList<Doodle>());
    }

    @Provides @Singleton
    public DoodleRangeCalculator provideDoodleRangeCalculator() {
        return new DoodleRangeCalculator();
    }

    @Provides @Singleton
    public DoodleBitmapFactory provideDoodleBitmapFactory() {
        return new DoodleBitmapFactory();
    }

    @Provides @Singleton
    public ChromeTabHelper provideChromeTabHelper() {
        return new ChromeTabHelper(context);
    }
}
