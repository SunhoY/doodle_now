package io.harry.doodlenow.activity;

import android.content.Intent;

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
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    private LandingActivity subject;

    @Inject
    DoodleService doodleService;
    @Inject
    DoodleListAdapter doodleListAdapter;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Doodle>>> contentListServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        TestDoodleComponent doodleComponent = (TestDoodleComponent) ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent();
        doodleComponent.inject(this);

        subject = Robolectric.setupActivity(LandingActivity.class);
    }

    @Test
    public void onCreate_setsSubjectOnListAdapterAsAnClickListener() throws Exception {
        verify(doodleListAdapter).setDoodleClickListener(subject);
    }

    @Test
    public void onResume_callsContentServiceToGetContents() throws Exception {
        verify(doodleService).getDoodles(Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void afterGettingContentList_refreshesContentListView() throws Exception {
        verify(doodleService).getDoodles(contentListServiceCallbackCaptor.capture());

        ArrayList<Doodle> items = new ArrayList<>();
        items.add(new Doodle("11", "beat it", "beat it!", "beatit.com"));
        items.add(new Doodle("22", "air walk", "air walk!", "airwork.com"));

        contentListServiceCallbackCaptor.getValue().onSuccess(items);

        ArrayList<Doodle> expected = new ArrayList<>();
        expected.add(new Doodle("11", "beat it", "beat it!", "beatit.com"));
        expected.add(new Doodle("22", "air walk", "air walk!", "airwork.com"));

        verify(doodleListAdapter).refreshDoodles(expected);
    }

    @Test
    public void onDoodleClick_startsDoodleActivityWithDoodleId() throws Exception {
        Intent expectedIntent = new Intent(subject, DoodleActivity.class);
        expectedIntent.putExtra("DOODLE_ID", "doodle id");

        subject.onDoodleClick(new Doodle("doodle id", "title", "content", "url"));

        assertThat(shadowOf(subject).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}