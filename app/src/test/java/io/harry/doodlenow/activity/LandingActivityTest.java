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
import io.harry.doodlenow.service.DoodleServiceCloudantAPI;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    public static final long ANY_TIME_MILLIS = 1234L;
    private LandingActivity subject;

    @Inject
    DoodleServiceCloudantAPI doodleServiceCloudantAPI;
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
        verify(doodleServiceCloudantAPI).retrieveDoodles(Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void afterGettingContentList_refreshesContentListView() throws Exception {
        verify(doodleServiceCloudantAPI).retrieveDoodles(contentListServiceCallbackCaptor.capture());

        ArrayList<Doodle> items = new ArrayList<>();
        items.add(new Doodle("1", "", "beat it", "beat it!", "beatit.com", ANY_TIME_MILLIS));
        items.add(new Doodle("2", "", "air walk", "air walk!", "airwork.com", ANY_TIME_MILLIS));

        contentListServiceCallbackCaptor.getValue().onSuccess(items);

        ArrayList<Doodle> expected = new ArrayList<>();
        expected.add(new Doodle("1", "", "beat it", "beat it!", "beatit.com", ANY_TIME_MILLIS));
        expected.add(new Doodle("2", "", "air walk", "air walk!", "airwork.com", ANY_TIME_MILLIS));

        verify(doodleListAdapter).refreshDoodles(expected);
    }

    @Test
    public void onDoodleClick_startsDoodleActivityWithDoodleId() throws Exception {
        Doodle doodle = new Doodle("1", "", "title", "content", "url", ANY_TIME_MILLIS);

        Intent expectedIntent = new Intent(subject, DoodleActivity.class);
        expectedIntent.putExtra("DOODLE_ID", "1");

        subject.onDoodleClick(doodle);

        assertThat(shadowOf(subject).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}