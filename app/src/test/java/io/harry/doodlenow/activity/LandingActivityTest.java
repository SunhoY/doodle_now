package io.harry.doodlenow.activity;

import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.component.ContentComponent;
import io.harry.doodlenow.model.Content;
import io.harry.doodlenow.service.ContentService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    private LandingActivity subject;

    @Inject
    ContentService contentService;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Content>>> contentListServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ContentComponent contentComponent = ((TestDoodleApplication) RuntimeEnvironment.application).getContentComponent();
        contentService = contentComponent.contentService();

        subject = Robolectric.setupActivity(LandingActivity.class);
    }

    @Test
    public void onResume_callsContentServiceToGetContents() throws Exception {
        verify(contentService).getContents(Matchers.<ServiceCallback<List<Content>>>any());
    }

    @Test
    public void afterGettingContentList_refreshesContentListView() throws Exception {
        verify(contentService).getContents(contentListServiceCallbackCaptor.capture());

        ArrayList<Content> items = new ArrayList<>();
        Content firstContent = new Content();
        firstContent.value = "hello jackson";
        Content secondContent = new Content();
        secondContent.value = "hello jordan";

        items.add(firstContent);
        items.add(secondContent);

        contentListServiceCallbackCaptor.getValue().onSuccess(items);

        RecyclerView contentListView = (RecyclerView) subject.findViewById(R.id.contentList);
        assertThat( contentListView.getAdapter().getItemCount()).isEqualTo(2);
    }
}