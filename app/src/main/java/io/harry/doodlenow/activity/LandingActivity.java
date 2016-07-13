package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

public class LandingActivity extends AppCompatActivity {

    @Inject
    DoodleService doodleService;
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

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(LandingActivity.this);
        doodleListView.setLayoutManager(contentListLayoutManager);
        doodleListView.setAdapter(doodleListAdapter);
    }

    @Override
    protected void onResume() {
        long startOfYesterday = new DateTime().minusDays(1).withTimeAtStartOfDay().getMillis();
        long endOfYesterday = new DateTime().withTimeAtStartOfDay().minusSeconds(1).getMillis();

        doodleService.getDoodles(0L, 1468325618001L, new ServiceCallback<List<Doodle>>() {
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
}
