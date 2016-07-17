package io.harry.doodlenow.wrapper;

import android.os.Bundle;

import io.harry.doodlenow.fragment.DoodleListFragment;

public class DoodleListFragmentWrapper {
    public DoodleListFragment getDoodleListFragment(DoodleListFragment.DoodleListType doodleListType) {
        DoodleListFragment doodleListFragment = new DoodleListFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable("doodleListType", doodleListType);

        doodleListFragment.setArguments(arguments);

        return doodleListFragment;
    }
}
