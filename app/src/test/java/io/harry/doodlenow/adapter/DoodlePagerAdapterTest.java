package io.harry.doodlenow.adapter;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodlePagerAdapterTest {
    @Inject
    DoodleListFragmentWrapper mockDoodleListFragmentWrapper;

    private long MILLIS_2016_7_17_10_0 = 1468717200000L;
    private long MILLIS_2016_7_17_9_0 = 1468713600000L;
    private long MILLIS_2016_7_16_9_0 = 1468627200000L;
    private long MILLIS_2016_7_10_0_0 = 1468076400000L;

    private DoodlePagerAdapter subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((DoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_7_17_10_0);

        subject = new DoodlePagerAdapter(Robolectric.setupActivity(LandingActivity.class));
    }

    @Test
    public void getFirstItem_returnsFragmentWith9AMYesterdayTo9AMToday() throws Exception {
        subject.getItem(0);

        verify(mockDoodleListFragmentWrapper).getDoodleListFragment(MILLIS_2016_7_16_9_0, MILLIS_2016_7_17_9_0);
    }

    @Test
    public void getSecondItem_returnsFragmentWith0AMWeekAgoToLongMaxValue() throws Exception {
        subject.getItem(1);

        verify(mockDoodleListFragmentWrapper).getDoodleListFragment(MILLIS_2016_7_10_0_0, Long.MAX_VALUE);
    }

    @Test
    public void getCount_returns2() throws Exception {
        assertThat(subject.getCount()).isEqualTo(2);
    }
}