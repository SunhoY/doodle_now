package io.harry.doodlenow.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.adapter.DoodlePagerAdapter;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.fragment.DoodleListFragment;
import io.harry.doodlenow.wrapper.DoodlePagerAdapterWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    public static final int TAB_COUNT = 2;
    private LandingActivity subject;

    @BindView(R.id.doodle_pager)
    ViewPager doodleViewPager;
    @BindView(R.id.doodle_tabs)
    TabLayout doodleTabs;

    @Inject
    DoodlePagerAdapterWrapper mockDoodlePagerAdapterWrapper;

    @Mock
    DoodlePagerAdapter mockDoodlePagerAdapter;
    @Mock
    DoodleListFragment firstMockFragment;
    @Mock
    DoodleListFragment secondMockFragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestDoodleComponent doodleComponent = (TestDoodleComponent) ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent();
        doodleComponent.inject(this);

        when(mockDoodlePagerAdapterWrapper.getDoodlePagerAdapter(any(AppCompatActivity.class))).thenReturn(mockDoodlePagerAdapter);
        when(mockDoodlePagerAdapter.getCount()).thenReturn(TAB_COUNT);
        when(mockDoodlePagerAdapter.getItem(0)).thenReturn(firstMockFragment);
        when(mockDoodlePagerAdapter.getItem(1)).thenReturn(secondMockFragment);

        subject = Robolectric.setupActivity(LandingActivity.class);

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_setsElevationOfActionBarWith0() throws Exception {
        assertThat(subject.getSupportActionBar().getElevation()).isEqualTo(0);
    }

    @Test
    public void doodleTabs_shouldHave2Tabs() throws Exception {
        assertThat(doodleTabs.getTabCount()).isEqualTo(2);
    }

    @Test
    public void doodleTabs_hasTabNameAsTodayAndArchive() throws Exception {
        assertThat(doodleTabs.getTabAt(0).getText()).isEqualTo("Today");
        assertThat(doodleTabs.getTabAt(1).getText()).isEqualTo("Archive");
    }

    @Test
    public void doodleViewPager_hasViewPagerAdapter() throws Exception {
        assertThat(doodleViewPager.getAdapter()).isEqualTo(mockDoodlePagerAdapter);
    }

    @Test
    public void onDoodleTabSelected_changesCurrentItemOnDoodleViewPager() throws Exception {
        doodleTabs.getTabAt(1).select();
        assertThat(doodleViewPager.getCurrentItem()).isEqualTo(1);

        doodleTabs.getTabAt(0).select();
        assertThat(doodleViewPager.getCurrentItem()).isEqualTo(0);
    }
}