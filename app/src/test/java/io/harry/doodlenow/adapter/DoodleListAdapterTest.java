package io.harry.doodlenow.adapter;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.wrapper.PicassoWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleListAdapterTest {
    @Inject
    PicassoWrapper mockPicassoWrapper;

    @Mock
    Picasso mockPicasso;
    @Mock
    RequestCreator mockRequestCreator;

    @Captor
    ArgumentCaptor<Doodle> doodleCaptor;

    private DoodleListAdapter subject;

    private final int ANY_VIEW_TYPE = 99;
    private DoodleListAdapter.OnDoodleItemClickListener mockOnDoodleItemClickListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestDoodleComponent) ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);

        when(mockPicassoWrapper.getPicasso(any(Context.class))).thenReturn(mockPicasso);
        when(mockPicasso.load(anyString())).thenReturn(mockRequestCreator);
        when(mockPicasso.load(anyInt())).thenReturn(mockRequestCreator);

        ArrayList<Doodle> doodle = new ArrayList<>();

        doodle.add(createMockDoodle("title 1", "content 1", "image url 1", "1"));
        doodle.add(createMockDoodle("title 2", "content 2", "image url 2", "2"));
        doodle.add(createMockDoodle("title 3", "content 3", "", "3"));

        subject = new DoodleListAdapter(RuntimeEnvironment.application, doodle);

        mockOnDoodleItemClickListener = mock(DoodleListAdapter.OnDoodleItemClickListener.class);
        subject.setOnDoodleClickListener(mockOnDoodleItemClickListener);
    }

    @Test
    public void eachViewShouldShowDoodleContent() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        assertThat(firstViewHolder.content.getText()).isEqualTo("content 1");
        assertThat(secondViewHolder.content.getText()).isEqualTo("content 2");
    }

    @Test
    public void eachViewShouldShowDoodleTitle() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        assertThat(firstViewHolder.title.getText()).isEqualTo("title 1");
        assertThat(secondViewHolder.title.getText()).isEqualTo("title 2");
    }

    @Test
    public void eachViewShouldShowDoodlePreviewImage() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        verify(mockPicasso).load("image url 1");
        verify(mockPicasso).load("image url 2");
        verify(mockRequestCreator).into(firstViewHolder.preview);
        verify(mockRequestCreator).into(secondViewHolder.preview);
    }

    @Test
    public void eachViewShouldLoadMainLogo_whenPreviewUrlIsEmpty() throws Exception {
        DoodleListAdapter.SimpleViewHolder thirdViewHolder = createAndBindViewHolder(2);

        verify(mockPicasso).load(R.drawable.main_logo);
        verify(mockRequestCreator).into(thirdViewHolder.preview);
    }

    @Test
    public void eachViewShouldShowHoursElapsed() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        assertThat(firstViewHolder.hoursElapsed.getText()).isEqualTo("1 hours ago");
        assertThat(secondViewHolder.hoursElapsed.getText()).isEqualTo("2 hours ago");
    }

    @Test
    public void getItemCount_returnsLengthOfItemList() throws Exception {
        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void refreshDoodles_clearsAllDoodlesAndAddNewDoodles() throws Exception {
        List<Doodle> newDoodles = new ArrayList<>();
        newDoodles.add(createMockDoodle("title 1", "content 1", "image url 1", "1"));
        newDoodles.add(createMockDoodle("title 2", "content 2", "image url 2", "2"));
        newDoodles.add(createMockDoodle("title 3", "content 3", "", "3"));
        newDoodles.add(createMockDoodle("title 4", "content 4", "image url 4", "4"));

        subject.refreshDoodles(newDoodles);

        assertThat(subject.getItemCount()).isEqualTo(4);
    }

    @Test
    public void clickOnDoodleItem_launchesSetClickListener() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);

        firstViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener).onDoodleItemClick(doodleCaptor.capture());

        assertMockDoodle(doodleCaptor.getValue(), "title 1", "content 1", "image url 1", "1 hours ago");

        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1);

        secondViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener, times(2)).onDoodleItemClick(doodleCaptor.capture());

        assertMockDoodle(doodleCaptor.getValue(), "title 2", "content 2", "image url 2", "2 hours ago");
    }

    @Test
    public void clickOnDoodleItem_doesNothing_whenListenerIsNotSet() throws Exception {
        subject.setOnDoodleClickListener(null);

        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0);

        firstViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener, never()).onDoodleItemClick(any(Doodle.class));
    }

    private void assertMockDoodle(Doodle mockDoodle, String title, String content, String imageUrl, String elapsedHours) {
        assertThat(mockDoodle.getTitle()).isEqualTo(title);
        assertThat(mockDoodle.getContent()).isEqualTo(content);
        assertThat(mockDoodle.getImageUrl()).isEqualTo(imageUrl);
        assertThat(mockDoodle.getElapsedHours()).isEqualTo(elapsedHours);
    }

    private Doodle createMockDoodle(String title, String content, String imageUrl, String hours) {
        Doodle doodle = mock(Doodle.class);

        when(doodle.getTitle()).thenReturn(title);
        when(doodle.getContent()).thenReturn(content);
        when(doodle.getImageUrl()).thenReturn(imageUrl);
        when(doodle.getElapsedHours()).thenReturn(hours + " hours ago");

        return doodle;
    }

    private DoodleListAdapter.SimpleViewHolder createAndBindViewHolder(int position) {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        subject.onBindViewHolder(firstViewHolder, position);
        return firstViewHolder;
    }
}