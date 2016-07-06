package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

public class LandingActivity extends AppCompatActivity implements DoodleListAdapter.OnDoodleClickListener {

    DoodleService doodleService;

    @BindView(R.id.contentList)
    RecyclerView contentListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ButterKnife.bind(this);

        DoodleComponent doodleComponent = ((DoodleApplication) getApplicationContext()).getDoodleComponent();
        doodleService = doodleComponent.contentService();

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(LandingActivity.this);
        contentListView.setLayoutManager(contentListLayoutManager);
        contentListView.setAdapter(new DoodleListAdapter(this, new ArrayList<Doodle>(), this));
    }

    @Override
    protected void onResume() {
        doodleService.getDoodles(new ServiceCallback<List<Doodle>>() {
            @Override
            public void onSuccess(List<Doodle> items) {
                List<Doodle> doodles = new ArrayList<>();
                for(Doodle doodle : items) {
                    doodles.add(new Doodle(doodle.content));
                }

                DoodleListAdapter doodleListAdapter = (DoodleListAdapter) contentListView.getAdapter();
                doodleListAdapter.refreshDoodles(doodles);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        super.onResume();
    }

    @Override
    public void onDoodleClick(Doodle doodle) {

    }
}
