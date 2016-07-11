package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleServiceCloudantAPI;
import io.harry.doodlenow.service.ServiceCallback;

public class LandingActivity extends AppCompatActivity implements DoodleListAdapter.OnDoodleClickListener {

    @Inject
    DoodleServiceCloudantAPI doodleServiceCloudantAPI;
    @Inject
    DoodleListAdapter doodleListAdapter;

    @BindView(R.id.doodleList)
    RecyclerView doodleListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ButterKnife.bind(this);

        DoodleComponent doodleComponent = ((DoodleApplication) getApplicationContext()).getDoodleComponent();
        doodleComponent.inject(this);

        doodleListAdapter.setDoodleClickListener(this);

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(LandingActivity.this);
        doodleListView.setLayoutManager(contentListLayoutManager);
        doodleListView.setAdapter(doodleListAdapter);
    }

    @Override
    protected void onResume() {
        doodleServiceCloudantAPI.retrieveDoodles(new ServiceCallback<List<Doodle>>() {
            @Override
            public void onSuccess(List<Doodle> items) {
                DoodleListAdapter doodleListAdapter = (DoodleListAdapter) doodleListView.getAdapter();
                doodleListAdapter.refreshDoodles(items);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        super.onResume();
    }

    @Override
    public void onDoodleClick(Doodle doodle) {
        startActivity(DoodleActivity.getIntent(this, doodle._id));
    }
}
