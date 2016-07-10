package io.harry.doodlenow.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.model.Doodle;

import static io.harry.doodlenow.adapter.DoodleListAdapter.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListAdapterTest {
    private DoodleListAdapter subject;
    private final int ANY_VIEW_TYPE = 99;

    @Mock
    OnDoodleClickListener mockDoodleClickListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ArrayList<Doodle> doodles = new ArrayList<>();

        doodles.add(new Doodle("first", "first title", "first content", "first url"));
        doodles.add(new Doodle("second", "second title", "second content", "second url"));

        subject = new DoodleListAdapter(RuntimeEnvironment.application, doodles);
        subject.setDoodleClickListener(mockDoodleClickListener);
    }

    @Test
    public void eachViewShouldShowsStringContent() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        int firstIndex = 0;
        subject.onBindViewHolder(firstViewHolder, firstIndex);

        DoodleListAdapter.SimpleViewHolder secondViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        int secondIndex = 1;
        subject.onBindViewHolder(secondViewHolder, secondIndex);

        assertThat(firstViewHolder.content.getText()).isEqualTo("first content");
        assertThat(secondViewHolder.content.getText()).isEqualTo("second content");
    }

    @Test
    public void getItemCount_returnsLengthOfItemList() throws Exception {
        assertThat(subject.getItemCount()).isEqualTo(2);
    }

    @Test
    public void refreshDoodles_clearsAllDoodlesAndAddNewDoodles() throws Exception {
        List<Doodle> newDoodles = new ArrayList<>();
        newDoodles.add(new Doodle("first id", "first title", "first content", "first url"));
        newDoodles.add(new Doodle("second id", "second title", "second content", "second url"));
        newDoodles.add(new Doodle("third id", "third title", "third content", "third url"));

        subject.refreshDoodles(newDoodles);

        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void onItemClick_runsOnDoodleClickListener() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);
        subject.onBindViewHolder(firstViewHolder, 0);

        firstViewHolder.content.performClick();

        verify(mockDoodleClickListener).onDoodleClick(new Doodle("first", "first title", "first content", "first url"));
    }
}