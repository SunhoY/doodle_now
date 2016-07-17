package io.harry.doodlenow.wrapper;

import android.support.v7.app.AppCompatActivity;

import io.harry.doodlenow.adapter.DoodlePagerAdapter;

public class DoodlePagerAdapterWrapper {
    public DoodlePagerAdapter getDoodlePagerAdapter(AppCompatActivity activity) {
        return new DoodlePagerAdapter(activity);
    }
}
