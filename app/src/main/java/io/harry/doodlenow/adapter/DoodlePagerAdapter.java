package io.harry.doodlenow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;

public class DoodlePagerAdapter extends FragmentStatePagerAdapter {
    @Inject
    DoodleListFragmentWrapper doodleListFragmentWrapper;

    public DoodlePagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        ((DoodleApplication) activity.getApplication()).getDoodleComponent().inject(this);
    }

    @Override
    public Fragment getItem(int position) {
        long start;
        long end;

        switch (position) {
            case 0:
                end = new DateTime().withTime(9, 0, 0, 0).getMillis();
                start = new DateTime().minusDays(1).withTime(9, 0, 0, 0).getMillis();

                return doodleListFragmentWrapper.getDoodleListFragment(start, end);
            case 1:
            default:
                end = Long.MAX_VALUE;
                start = new DateTime().minusDays(7).withTimeAtStartOfDay().getMillis();

                return doodleListFragmentWrapper.getDoodleListFragment(start, end);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
