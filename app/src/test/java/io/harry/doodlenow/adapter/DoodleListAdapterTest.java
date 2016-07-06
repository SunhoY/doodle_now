package io.harry.doodlenow.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.harry.doodlenow.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListAdapterTest {
    private DoodleListAdapter subject;
    private final int ANY_VIEW_TYPE = 99;

    @Before
    public void setUp() throws Exception {
        ArrayList<String> strings = new ArrayList<>();

        strings.add("first content");
        strings.add("second content");

        subject = new DoodleListAdapter(RuntimeEnvironment.application, strings);
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
        List<String> newDoodles = new ArrayList<>();
        newDoodles.add("doodle 1");
        newDoodles.add("doodle 2");
        newDoodles.add("doodle 3");

        subject.refreshDoodles(newDoodles);

        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void onItemClick_runsOnDoodleClickListener() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);
    }
}