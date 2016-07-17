package io.harry.doodlenow.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.fragment.DoodleListFragment;
import io.harry.doodlenow.wrapper.DoodleListFragmentWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodlePagerAdapterTest {
    @Inject
    DoodleListFragmentWrapper mockDoodleListFragmentWrapper;

    @Mock
    DoodleListFragment mockTodayFragment;
    @Mock
    DoodleListFragment mockThisWeekFragment;

    private DoodlePagerAdapter subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((DoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);

        when(mockDoodleListFragmentWrapper.getDoodleListFragment(DoodleListFragment.DoodleListType.Today))
                .thenReturn(mockTodayFragment);
        when(mockDoodleListFragmentWrapper.getDoodleListFragment(DoodleListFragment.DoodleListType.ThisWeek))
                .thenReturn(mockThisWeekFragment);

        subject = new DoodlePagerAdapter(Robolectric.setupActivity(LandingActivity.class));
    }

    @Test
    public void getFirstItem_returnsFragmentWith9AMYesterdayTo9AMToday() throws Exception {
        assertThat(subject.getItem(0)).isEqualTo(mockTodayFragment);
    }

    @Test
    public void getSecondItem_returnsFragmentWith0AMWeekAgoToLongMaxValue() throws Exception {
        assertThat(subject.getItem(1)).isEqualTo(mockThisWeekFragment);
    }

    @Test
    public void getCount_returns2() throws Exception {
        assertThat(subject.getCount()).isEqualTo(2);
    }
}