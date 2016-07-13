package io.harry.doodlenow.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListAdapterTest {
    private DoodleListAdapter subject;
    private final int ANY_VIEW_TYPE = 99;
    private long MILLIS_2016_6_19_8_0 = 1466290800000L;
    private long MILLIS_2016_6_19_7_0 = 1466287200000L;
    private long ANY_MILLIS = 0L;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ArrayList<Doodle> doodle = new ArrayList<>();

        doodle.add(new Doodle(new DoodleJson("first title", "first content", MILLIS_2016_6_19_8_0)));
        doodle.add(new Doodle(new DoodleJson("second title", "second content", MILLIS_2016_6_19_7_0)));

        subject = new DoodleListAdapter(RuntimeEnvironment.application, doodle);
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
        newDoodles.add(new Doodle(new DoodleJson("first title", "first content", ANY_MILLIS)));
        newDoodles.add(new Doodle(new DoodleJson("second title", "second content", ANY_MILLIS)));
        newDoodles.add(new Doodle(new DoodleJson("third title", "third content", ANY_MILLIS)));

        subject.refreshDoodles(newDoodles);

        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    private DoodleListAdapter.SimpleViewHolder createAndBindViewHolder(int position) {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        subject.onBindViewHolder(firstViewHolder, position);
        return firstViewHolder;
    }
}