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

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.component.DoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    private LandingActivity subject;

    DoodleService doodleService;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Doodle>>> contentListServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        DoodleComponent doodleComponent = ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent();
        doodleService = doodleComponent.contentService();

        subject = Robolectric.setupActivity(LandingActivity.class);
    }

    @Test
    public void onResume_callsContentServiceToGetContents() throws Exception {
        verify(doodleService).getDoodles(Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void afterGettingContentList_refreshesContentListView() throws Exception {
        verify(doodleService).getDoodles(contentListServiceCallbackCaptor.capture());

        ArrayList<Doodle> items = new ArrayList<>();
        Doodle firstDoodle = new Doodle();
        firstDoodle.title = "hello jackson";
        firstDoodle.content = "beat it";
        Doodle secondDoodle = new Doodle();
        secondDoodle.title = "hello jordan";
        secondDoodle.content = "air walk";

        items.add(firstDoodle);
        items.add(secondDoodle);

        contentListServiceCallbackCaptor.getValue().onSuccess(items);

        RecyclerView contentListView = (RecyclerView) subject.findViewById(R.id.contentList);
        assertThat( contentListView.getAdapter().getItemCount()).isEqualTo(2);
    }
}