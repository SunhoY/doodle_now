package io.harry.doodlenow.activity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Ignore;
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

import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    private LandingActivity subject;
    private long MILLIS_2016_6_19_9_0 = 1466294400000L;
    private long MILLIS_2016_6_19_8_0 = 1466290800000L;
    private long MILLIS_2016_6_19_7_0 = 1466287200000L;

    @Inject
    DoodleService doodleService;
    @Inject
    DoodleListAdapter doodleListAdapter;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Doodle>>> doodleListServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        TestDoodleComponent doodleComponent = (TestDoodleComponent) ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent();
        doodleComponent.inject(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_9_0);

        subject = Robolectric.setupActivity(LandingActivity.class);

        ButterKnife.bind(this, subject);
    }

    @Test
    @Ignore
    public void onResume_callsDoodleServiceToGetDoodlesCreatedYesterday() throws Exception {
        long from = new DateTime(2016, 6, 18, 0, 0, DateTimeZone.forOffsetHours(9)).getMillis();
        long to = new DateTime(2016, 6, 18, 23, 59, 59, DateTimeZone.forOffsetHours(9)).getMillis();

        verify(doodleService).getDoodles(eq(from), eq(to), Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void afterGettingDoodleList_refreshesContentListView() throws Exception {
        verify(doodleService).getDoodles(anyLong(), anyLong(), doodleListServiceCallbackCaptor.capture());

        ArrayList<Doodle> items = new ArrayList<>();
        items.add(new Doodle("beat it", "beat it!", "http://beatit.com", MILLIS_2016_6_19_8_0));
        items.add(new Doodle("air walk", "air walk!", "http://airwalk.com", MILLIS_2016_6_19_7_0));

        doodleListServiceCallbackCaptor.getValue().onSuccess(items);

        ArrayList<Doodle> expected = new ArrayList<>();
        expected.add(new Doodle("beat it", "beat it!", "http://beatit.com", MILLIS_2016_6_19_8_0));
        expected.add(new Doodle("air walk", "air walk!", "http://airwalk.com", MILLIS_2016_6_19_7_0));

        verify(doodleListAdapter).refreshDoodles(expected);
    }
}