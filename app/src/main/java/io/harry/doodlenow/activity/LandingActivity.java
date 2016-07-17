package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodlePagerAdapter;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.wrapper.DoodlePagerAdapterWrapper;

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.doodle_pager)
    ViewPager doodleViewPager;
    @BindView(R.id.doodle_tabs)
    TabLayout doodleTabs;

    @Inject
    DoodlePagerAdapterWrapper doodlePagerAdapterWrapper;

    private DoodlePagerAdapter doodlePagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ButterKnife.bind(this);

        DoodleComponent doodleComponent = ((DoodleApplication) getApplicationContext()).getDoodleComponent();
        doodleComponent.inject(this);

        getSupportActionBar().setElevation(0);

        doodlePagerAdapter = doodlePagerAdapterWrapper.getDoodlePagerAdapter(this);
        doodleViewPager.setAdapter(doodlePagerAdapter);
        doodleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(doodleTabs));

        doodleTabs.addTab(doodleTabs.newTab().setText("Today"));
        doodleTabs.addTab(doodleTabs.newTab().setText("Archive"));
        doodleTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doodleViewPager.setCurrentItem(tab.getPosition());
                ((DoodlePagerAdapter) doodleViewPager.getAdapter()).getItem(tab.getPosition()).onResume();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
