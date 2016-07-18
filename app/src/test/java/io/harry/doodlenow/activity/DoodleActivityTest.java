package io.harry.doodlenow.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.joda.time.DateTimeUtils;
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
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.wrapper.PicassoWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private long MILLIS_2016_6_19_10_0 = 1466298000000L;
    private long MILLIS_2016_6_19_9_0 = 1466294400000L;

    private DoodleActivity subject;

    @BindView(R.id.doodle_title)
    TextView doodleTitle;
    @BindView(R.id.hours_elapsed)
    TextView hoursElapsed;
    @BindView(R.id.doodle_content)
    TextView doodleContent;
    @BindView(R.id.doodle_image)
    ImageView doodleImage;

    @Inject
    PicassoWrapper mockPicassoWrapper;

    @Mock
    Picasso mockPicasso;
    @Mock
    RequestCreator mockRequestCreator;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        when(mockPicassoWrapper.getPicasso(any(Context.class))).thenReturn(mockPicasso);
        when(mockPicasso.load(anyString())).thenReturn(mockRequestCreator);
        when(mockPicasso.load(anyInt())).thenReturn(mockRequestCreator);
        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_10_0);
    }

    private void setupActivity(Intent intent) {
        subject = Robolectric.buildActivity(DoodleActivity.class).withIntent(intent).setup().get();

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_showsDoodleTitle() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("doodle", new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0));

        setupActivity(intent);
        assertThat(doodleTitle.getText()).isEqualTo("title");
    }

    @Test
    public void onCreate_showsDoodleElapsedHours() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("doodle", new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0));

        setupActivity(intent);
        assertThat(hoursElapsed.getText()).isEqualTo("1 hours ago");
    }

    @Test
    public void onCreate_showsDoodleContent() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("doodle", new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0));

        setupActivity(intent);
        assertThat(doodleContent.getText()).isEqualTo("content");
    }

    @Test
    public void onCreate_setsImageWithUrlViaPicasso() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("doodle", new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0));

        setupActivity(intent);
        verify(mockPicasso).load("image url");
        verify(mockRequestCreator).into(doodleImage);
    }

    @Test
    public void onCreate_setsDefaultImage_whenImageUrlIsEmpty() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("doodle", new Doodle("title", "content", "", MILLIS_2016_6_19_9_0));

        setupActivity(intent);

        verify(mockPicasso).load(R.drawable.main_logo);
        verify(mockRequestCreator).into(doodleImage);
    }
}