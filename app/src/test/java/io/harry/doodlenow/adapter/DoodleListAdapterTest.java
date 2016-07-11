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
    public static final long ANY_TIME_MILLIS = 1234L;
    private DoodleListAdapter subject;
    private final int ANY_VIEW_TYPE = 99;

    @Mock
    OnDoodleClickListener mockDoodleClickListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ArrayList<Doodle> doodles = new ArrayList<>();

        doodles.add(new Doodle("first", "", "first title", "first content", "first url", ANY_TIME_MILLIS));
        doodles.add(new Doodle("second", "", "second title", "second content", "second url", ANY_TIME_MILLIS));

        subject = new DoodleListAdapter(RuntimeEnvironment.application, doodles);
        subject.setDoodleClickListener(mockDoodleClickListener);
    }

    @Test
    public void eachViewShouldShowsDoodleContent() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        assertThat(firstViewHolder.content.getText()).isEqualTo("first content");
        assertThat(secondViewHolder.content.getText()).isEqualTo("second content");
    }

    @Test
    public void eachViewShouldShowsDoodleTitle() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        assertThat(firstViewHolder.title.getText()).isEqualTo("first title");
        assertThat(secondViewHolder.title.getText()).isEqualTo("second title");

    }

    @Test
    public void getItemCount_returnsLengthOfItemList() throws Exception {
        assertThat(subject.getItemCount()).isEqualTo(2);
    }

    @Test
    public void refreshDoodles_clearsAllDoodlesAndAddNewDoodles() throws Exception {
        List<Doodle> newDoodles = new ArrayList<>();
        newDoodles.add(new Doodle("first id", "", "first title", "first content", "first url", ANY_TIME_MILLIS));
        newDoodles.add(new Doodle("second id", "", "second title", "second content", "second url", ANY_TIME_MILLIS));
        newDoodles.add(new Doodle("third id", "", "third title", "third content", "third url", ANY_TIME_MILLIS));

        subject.refreshDoodles(newDoodles);

        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void onItemClick_runsOnDoodleClickListener() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);
        subject.onBindViewHolder(firstViewHolder, 0);

        firstViewHolder.container.performClick();

        verify(mockDoodleClickListener).onDoodleClick(new Doodle("first", "", "first title", "first content", "first url", ANY_TIME_MILLIS));
    }

    private DoodleListAdapter.SimpleViewHolder createAndBindViewHolder(int position) {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        subject.onBindViewHolder(firstViewHolder, position);
        return firstViewHolder;
    }
}