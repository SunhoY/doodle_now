package io.harry.doodlenow.wrapper;

import android.os.Bundle;

import io.harry.doodlenow.fragment.DoodleListFragment;

public class DoodleListFragmentWrapper {
    public DoodleListFragment getDoodleListFragment(long start, long end) {
        DoodleListFragment doodleListFragment = new DoodleListFragment();
        Bundle arguments = new Bundle();
        arguments.putLong("start", start);
        arguments.putLong("end", end);

        doodleListFragment.setArguments(arguments);

        return doodleListFragment;
    }
}
