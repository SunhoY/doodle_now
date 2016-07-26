package io.harry.doodlenow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.activity.DoodleActivity;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.fragment.doodlerange.DoodleRange;
import io.harry.doodlenow.fragment.doodlerange.DoodleRangeCalculator;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.service.ServiceCallback;

import static io.harry.doodlenow.fragment.DoodleListType.Archive;
import static io.harry.doodlenow.fragment.DoodleListType.Today;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListFragmentTest {
    private static final DoodleListType ANY_TYPE = Archive;
    private static final long ANY_LONG = -1L;
    public static final String ANY_STRING = "any string";
    private long MILLIS_2016_6_19_10_0 = 1466298000000L;
    private long MILLIS_2016_6_19_9_0 = 1466294400000L;
    private long MILLIS_2016_6_18_9_0 = 1466208000000L;
    private long MILLIS_2016_6_12_0_0 = 1465657200000L;

    @BindView(R.id.doodle_list)
    RecyclerView doodleList;

    @Inject
    DoodleRangeCalculator mockDoodleRangeCalculator;
    @Inject
    FirebaseHelperWrapper mockFirebaseHelperWrapper;
    @Inject
    DoodleListAdapter mockDoodleListAdapter;

    @Captor
    ArgumentCaptor<ServiceCallback<List<Doodle>>> doodleListServiceCallbackCaptor;

    @Mock
    FirebaseHelper mockFirebaseHelper;
    @Mock
    DataSnapshot mockDataSnapshot;
    @Mock
    Query mockQuery;

    private DoodleListFragment subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((DoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_10_0);

        when(mockFirebaseHelperWrapper.getFirebaseHelper("doodles"))
                .thenReturn(mockFirebaseHelper);
        when(mockDoodleRangeCalculator.calculateRange(any(DoodleListType.class), any(DateTime.class), anyInt(), anyInt()))
                .thenReturn(new DoodleRange(MILLIS_2016_6_18_9_0, MILLIS_2016_6_19_9_0));
        when(mockFirebaseHelper.getOrderByChildQuery(anyString(), anyLong(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.startAt(anyLong())).thenReturn(mockQuery);
        when(mockQuery.endAt(anyLong())).thenReturn(mockQuery);
    }

    private void setupWithType(DoodleListType type) {
        subject = new DoodleListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("doodleListType", type);

        subject.setArguments(arguments);

        SupportFragmentTestUtil.startFragment(subject);

        ButterKnife.bind(this, subject.getView());
    }

    @Test
    public void onCreate_setsItemClickListenerOnDoodleListAdapter() throws Exception {
        setupWithType(ANY_TYPE);

        verify(mockDoodleListAdapter).setOnDoodleClickListener(subject);
    }

    @Test
    public void onResume_calculateDateRangeWithCalculator() throws Exception {
        when(mockDoodleRangeCalculator.calculateRange(Today, new DateTime(1466298000000L), 10, 7))
                .thenReturn(new DoodleRange(ANY_LONG, ANY_LONG));

        setupWithType(Today);

        //spying resource in TestDoodleApplication
        verify(mockDoodleRangeCalculator).calculateRange(
                Today, new DateTime(1466298000000L), 10, 7);
    }

    @Test
    public void onResume_callsFirebaseHelperToGetQueryWithCalculatedDateRange() throws Exception {
        when(mockDoodleRangeCalculator.calculateRange(any(DoodleListType.class), any(DateTime.class), anyInt(), anyInt()))
                .thenReturn(new DoodleRange(MILLIS_2016_6_18_9_0, MILLIS_2016_6_19_9_0));

        setupWithType(ANY_TYPE);

        verify(mockFirebaseHelper).getOrderByChildQuery("createdAt", MILLIS_2016_6_18_9_0, MILLIS_2016_6_19_9_0);
    }

    @Test
    public void afterGettingQuery_addChildEventListenerToQuery() throws Exception {
        when(mockDoodleRangeCalculator.calculateRange(any(DoodleListType.class), any(DateTime.class), anyInt(), anyInt()))
                .thenReturn(new DoodleRange(MILLIS_2016_6_18_9_0, MILLIS_2016_6_19_9_0));

        setupWithType(ANY_TYPE);

        verify(mockQuery).addChildEventListener(subject);
    }

    @Test
    public void onChildAdded_insertItemIntoDoodleListAdapter() throws Exception {
        setupWithType(ANY_TYPE);

        DoodleJson doodleJsonFirst = createDoodleJson("beat it", "beat it!", "http://beatit.com", "http://beatit.com/pic.png", MILLIS_2016_6_19_9_0);
        Doodle firstExpectedDoodle = new Doodle(doodleJsonFirst);

        when(mockDataSnapshot.getValue(DoodleJson.class))
                .thenReturn(doodleJsonFirst);

        subject.onChildAdded(mockDataSnapshot, ANY_STRING);

        verify(mockDoodleListAdapter).insertDoodle(0, firstExpectedDoodle);

        DoodleJson doodleJsonSecond = createDoodleJson("air walk", "air walk!", "http://airwalk.com", "http://airwalk.com/ture.png", MILLIS_2016_6_18_9_0);
        Doodle secondExpectedDoodle = new Doodle(doodleJsonSecond);

        when(mockDataSnapshot.getValue(DoodleJson.class))
                .thenReturn(doodleJsonSecond);

        subject.onChildAdded(mockDataSnapshot, ANY_STRING);

        verify(mockDoodleListAdapter).insertDoodle(0, secondExpectedDoodle);
    }

    @NonNull
    private DoodleJson createDoodleJson(String title, String content, String url, String imageUrl, long createdAt) {
        DoodleJson doodleJsonFirst = new DoodleJson();
        doodleJsonFirst.title = title;
        doodleJsonFirst.content = content;
        doodleJsonFirst.url = url;
        doodleJsonFirst.imageUrl = imageUrl;
        doodleJsonFirst.createdAt = createdAt;
        return doodleJsonFirst;
    }

    @Test
    public void doodleList_shouldHaveInjectedAdapter() throws Exception {
        setupWithType(ANY_TYPE);

        assertThat(doodleList.getAdapter()).isEqualTo(mockDoodleListAdapter);
    }

    @Test
    public void doodleList_shouldHaveLinearLayoutManager() throws Exception {
        setupWithType(ANY_TYPE);
        
        assertThat(doodleList.getLayoutManager() instanceof LinearLayoutManager).isTrue();
    }

    @Test
    public void onDoodleItemClick_startsDoodleActivityWithDoodleId() throws Exception {
        setupWithType(ANY_TYPE);

        Doodle doodle = new Doodle("title", "content", "some url", "image url", MILLIS_2016_6_12_0_0);
        subject.onDoodleItemClick(doodle);

        Intent expected = new Intent(subject.getActivity(), DoodleActivity.class);
        expected.putExtra("doodle", new Doodle("title", "content", "some url", "image url", MILLIS_2016_6_12_0_0));

        assertThat(shadowOf(RuntimeEnvironment.application).getNextStartedActivity()).isEqualTo(expected);
    }
}