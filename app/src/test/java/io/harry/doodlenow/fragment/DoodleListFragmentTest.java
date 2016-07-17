package io.harry.doodlenow.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListFragmentTest {
    private long MILLIS_2016_6_19_9_0 = 1466294400000L;
    private long MILLIS_2016_6_19_8_0 = 1466290800000L;
    private long MILLIS_2016_6_19_7_0 = 1466287200000L;

    @BindView(R.id.doodle_list)
    RecyclerView doodleList;

    @Inject
    DoodleService mockDoodleService;
    @Inject
    DoodleListAdapter mockDoodleListAdapter;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Doodle>>> doodleListServiceCallbackCaptor;

    private DoodleListFragment subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((DoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_9_0);
    }

    private void setupWithStartAndEnd(long start, long end) {
        subject = new DoodleListFragment();
        Bundle arguments = new Bundle();
        arguments.putLong("start", start);
        arguments.putLong("end", end);

        subject.setArguments(arguments);

        SupportFragmentTestUtil.startFragment(subject);

        ButterKnife.bind(this, subject.getView());
    }

    @Test
    public void onResume_callsDoodleServiceWithStartAndEnd() throws Exception {
        setupWithStartAndEnd(MILLIS_2016_6_19_7_0, MILLIS_2016_6_19_8_0);

        verify(mockDoodleService).getDoodles(eq(MILLIS_2016_6_19_7_0), eq(MILLIS_2016_6_19_8_0), Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void onResume_callsDoodleServiceWithStartAndNow_whenEndIsMaxValue() throws Exception {
        setupWithStartAndEnd(MILLIS_2016_6_19_7_0, Long.MAX_VALUE);

        verify(mockDoodleService).getDoodles(eq(MILLIS_2016_6_19_7_0), eq(MILLIS_2016_6_19_9_0), Matchers.<ServiceCallback<List<Doodle>>>any());
    }

    @Test
    public void afterGettingDoodleList_refreshesContentListView() throws Exception {
        setupWithStartAndEnd(MILLIS_2016_6_19_7_0, MILLIS_2016_6_19_9_0);

        verify(mockDoodleService).getDoodles(anyLong(), anyLong(), doodleListServiceCallbackCaptor.capture());

        ArrayList<Doodle> items = new ArrayList<>();
        items.add(new Doodle("beat it", "beat it!", "http://beatit.com", MILLIS_2016_6_19_8_0));
        items.add(new Doodle("air walk", "air walk!", "http://airwalk.com", MILLIS_2016_6_19_7_0));

        doodleListServiceCallbackCaptor.getValue().onSuccess(items);

        ArrayList<Doodle> expected = new ArrayList<>();
        expected.add(new Doodle("beat it", "beat it!", "http://beatit.com", MILLIS_2016_6_19_8_0));
        expected.add(new Doodle("air walk", "air walk!", "http://airwalk.com", MILLIS_2016_6_19_7_0));

        verify(mockDoodleListAdapter).refreshDoodles(expected);
    }

    @Test
    public void doodleList_shouldHaveInjectedAdapter() throws Exception {
        setupWithStartAndEnd(MILLIS_2016_6_19_7_0, MILLIS_2016_6_19_9_0);

        assertThat(doodleList.getAdapter()).isEqualTo(mockDoodleListAdapter);
    }

    @Test
    public void doodleList_shouldHaveLinearLayoutManager() throws Exception {
        setupWithStartAndEnd(MILLIS_2016_6_19_7_0, MILLIS_2016_6_19_9_0);
        
        assertThat(doodleList.getLayoutManager() instanceof LinearLayoutManager).isTrue();
    }
}