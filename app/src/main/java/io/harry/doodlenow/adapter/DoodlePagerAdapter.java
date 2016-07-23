package io.harry.doodlenow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.fragment.DoodleListFragment;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;

import static io.harry.doodlenow.fragment.DoodleListType.*;

public class DoodlePagerAdapter extends FragmentStatePagerAdapter {
    @Inject
    DoodleListFragmentWrapper doodleListFragmentWrapper;

    List<DoodleListFragment> doodleListFragments;

    public DoodlePagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        ((DoodleApplication) activity.getApplication()).getDoodleComponent().inject(this);

        initDoodleListFragments();
    }

    private void initDoodleListFragments() {
        doodleListFragments = new ArrayList<>();

        doodleListFragments.add(
                doodleListFragmentWrapper.getDoodleListFragment(Today));
        doodleListFragments.add(
                doodleListFragmentWrapper.getDoodleListFragment(Archive));
    }

    @Override
    public Fragment getItem(int position) {
        return doodleListFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
