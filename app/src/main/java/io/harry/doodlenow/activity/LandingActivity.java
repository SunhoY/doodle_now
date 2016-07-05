package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.ContentListAdapter;
import io.harry.doodlenow.component.ContentComponent;
import io.harry.doodlenow.model.Content;
import io.harry.doodlenow.service.ContentService;
import io.harry.doodlenow.service.ServiceCallback;

public class LandingActivity extends AppCompatActivity {

    @Inject
    ContentService contentService;

    @BindView(R.id.contentList)
    RecyclerView contentListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ButterKnife.bind(this);

        ContentComponent contentComponent = ((DoodleApplication) getApplicationContext()).getContentComponent();
        contentService = contentComponent.contentService();

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(LandingActivity.this);
        contentListView.setLayoutManager(contentListLayoutManager);
        contentListView.setAdapter(new ContentListAdapter(this, new ArrayList<String>()));
    }

    @Override
    protected void onResume() {
        contentService.getContents(new ServiceCallback<List<Content>>() {
            @Override
            public void onSuccess(List<Content> items) {
                List<String> contentList = new ArrayList<>();
                for(Content content : items) {
                    contentList.add(content.value);
                }

                ContentListAdapter contentListAdapter = (ContentListAdapter) contentListView.getAdapter();
                contentListAdapter.refreshContents(contentList);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        super.onResume();
    }
}
