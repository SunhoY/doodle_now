package io.harry.doodlenow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        setSupportActionBar(toolbar);

        doodlePagerAdapter = doodlePagerAdapterWrapper.getDoodlePagerAdapter(this);
        doodleViewPager.setAdapter(doodlePagerAdapter);
        doodleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(doodleTabs));

        doodleTabs.addTab(doodleTabs.newTab().setText("Today"));
        doodleTabs.addTab(doodleTabs.newTab().setText("Archive"));
        doodleTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doodleViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.create_doodle)
    public void onCreateDoodleClick() {
        startActivity(new Intent(this, CreateDoodleActivity.class));
    }
}
