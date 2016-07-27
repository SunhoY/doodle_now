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
import static org.mockito.Mockito.spy;
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
    private DoodleListAdapter spySubject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestDoodleComponent) ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);

        when(mockPicassoWrapper.getPicasso(any(Context.class))).thenReturn(mockPicasso);
        when(mockPicasso.load(anyString())).thenReturn(mockRequestCreator);
        when(mockPicasso.load(anyInt())).thenReturn(mockRequestCreator);

        ArrayList<Doodle> doodle = new ArrayList<>();

        doodle.add(createMockDoodle("title 1", "content 1", "image url 1", "1", "url 1"));
        doodle.add(createMockDoodle("title 2", "content 2", "image url 2", "2", "url 2"));
        doodle.add(createMockDoodle("title 3", "content 3", "", "3", "url 3"));

        subject = new DoodleListAdapter(RuntimeEnvironment.application, doodle);
        spySubject = spy(subject);

        mockOnDoodleItemClickListener = mock(DoodleListAdapter.OnDoodleItemClickListener.class);
        subject.setOnDoodleClickListener(mockOnDoodleItemClickListener);
    }

    @Test
    public void eachViewShouldShowDoodleContent() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1, subject);

        assertThat(firstViewHolder.content.getText()).isEqualTo("content 1");
        assertThat(secondViewHolder.content.getText()).isEqualTo("content 2");
    }

    @Test
    public void eachViewShouldShowDoodleTitle() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1, subject);

        assertThat(firstViewHolder.title.getText()).isEqualTo("title 1");
        assertThat(secondViewHolder.title.getText()).isEqualTo("title 2");
    }

    @Test
    public void eachViewShouldShowDoodlePreviewImage() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1, subject);

        verify(mockPicasso).load("image url 1");
        verify(mockPicasso).load("image url 2");
        verify(mockRequestCreator).into(firstViewHolder.preview);
        verify(mockRequestCreator).into(secondViewHolder.preview);
    }

    @Test
    public void eachViewShouldLoadMainLogo_whenPreviewUrlIsEmpty() throws Exception {
        DoodleListAdapter.SimpleViewHolder thirdViewHolder = createAndBindViewHolder(2, subject);

        verify(mockPicasso).load(R.drawable.main_logo);
        verify(mockRequestCreator).into(thirdViewHolder.preview);
    }

    @Test
    public void eachViewShouldShowHoursElapsed() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);
        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1, subject);

        assertThat(firstViewHolder.hoursElapsed.getText()).isEqualTo("1 hours ago");
        assertThat(secondViewHolder.hoursElapsed.getText()).isEqualTo("2 hours ago");
    }

    @Test
    public void getItemCount_returnsLengthOfItemList() throws Exception {
        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void insertDoodle_insertsDoodleIntoDoodleList() throws Exception {
        spySubject.insertDoodle(0, new Doodle("new title", "new content", "new url", "new image url", 123L));

        assertThat(spySubject.getItemCount()).isEqualTo(4);
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);

        assertThat(firstViewHolder.title.getText()).isEqualTo("new title");
    }

    @Test
    public void insertDoodle_notifiesDataInsertedAtTheFront() throws Exception {
        spySubject.insertDoodle(0, new Doodle("new title", "new content", "new url", "new image url", 123L));

        verify(spySubject).notifyItemInserted(0);
    }

    @Test
    public void clickOnDoodleItem_launchesSetClickListener() throws Exception {
        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);

        firstViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener).onDoodleItemClick(doodleCaptor.capture());

        assertMockDoodle(doodleCaptor.getValue(), "title 1", "content 1", "image url 1", "1 hours ago", "url 1");

        DoodleListAdapter.SimpleViewHolder secondViewHolder = createAndBindViewHolder(1, subject);

        secondViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener, times(2)).onDoodleItemClick(doodleCaptor.capture());

        assertMockDoodle(doodleCaptor.getValue(), "title 2", "content 2", "image url 2", "2 hours ago", "url 2");
    }

    @Test
    public void clickOnDoodleItem_doesNothing_whenListenerIsNotSet() throws Exception {
        subject.setOnDoodleClickListener(null);

        DoodleListAdapter.SimpleViewHolder firstViewHolder = createAndBindViewHolder(0, subject);

        firstViewHolder.container.performClick();

        verify(mockOnDoodleItemClickListener, never()).onDoodleItemClick(any(Doodle.class));
    }

    @Test
    public void clearDoodles_emptiesDoodleList() throws Exception {
        spySubject.clearDoodles();

        assertThat(spySubject.getItemCount()).isEqualTo(0);
        verify(spySubject).notifyDataSetChanged();
    }

    private void assertMockDoodle(Doodle mockDoodle, String title, String content, String imageUrl, String elapsedHours, String url) {
        assertThat(mockDoodle.getTitle()).isEqualTo(title);
        assertThat(mockDoodle.getContent()).isEqualTo(content);
        assertThat(mockDoodle.getImageUrl()).isEqualTo(imageUrl);
        assertThat(mockDoodle.getElapsedHours()).isEqualTo(elapsedHours);
        assertThat(mockDoodle.getUrl()).isEqualTo(url);
    }

    private Doodle createMockDoodle(String title, String content, String imageUrl, String hours, String url) {
        Doodle doodle = mock(Doodle.class);

        when(doodle.getTitle()).thenReturn(title);
        when(doodle.getContent()).thenReturn(content);
        when(doodle.getImageUrl()).thenReturn(imageUrl);
        when(doodle.getElapsedHours()).thenReturn(hours + " hours ago");
        when(doodle.getUrl()).thenReturn(url);

        return doodle;
    }

    private DoodleListAdapter.SimpleViewHolder createAndBindViewHolder(int position, DoodleListAdapter subject) {
        DoodleListAdapter.SimpleViewHolder firstViewHolder =
                (DoodleListAdapter.SimpleViewHolder) subject.onCreateViewHolder(null, ANY_VIEW_TYPE);

        subject.onBindViewHolder(firstViewHolder, position);
        return firstViewHolder;
    }
}