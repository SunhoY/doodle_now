package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.ContentListAdapter;
import io.harry.doodlenow.model.Content;
import io.harry.doodlenow.service.ContentService;
import io.harry.doodlenow.service.ServiceCallback;

public class LandingActivity extends AppCompatActivity {

    private RecyclerView contentListView;

    @Inject
    ContentService contentService;

    {
        DoodleApplication.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_landing);
        super.onCreate(savedInstanceState);

        contentListView = (RecyclerView) findViewById(R.id.contentList);
        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(LandingActivity.this);
        contentListView.setLayoutManager(contentListLayoutManager);
        contentListView.setAdapter(new ContentListAdapter(this, new ArrayList<String>()));

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
    }
}
