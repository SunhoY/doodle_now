package io.harry.doodlenow.chrometab;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
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

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.activity.LandingActivity;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.converter.DoodleBitmapFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChromeTabHelperTest {

    public static final String ANY_URL = "http://whatever.com";
    public static final int TITLE_SHOWING = 1;
    public static final int ANY_DEFAULT_INT = -999;
    @Inject
    DoodleBitmapFactory mockDoodleBitmapFactory;

    @Mock
    AppCompatActivity mockActivity;
    @Mock
    Resources mockResources;
    @Mock
    Bitmap mockBitmap;

    private ChromeTabHelper subject;
    private LandingActivity testActivity;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication)RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        when(mockActivity.getResources()).thenReturn(mockResources);
        testActivity = Robolectric.setupActivity(LandingActivity.class);

        subject = new ChromeTabHelper(RuntimeEnvironment.application);
    }

    @Test
    public void launchChromeTab_getBitmapFromDoodleBitmapFactory() throws Exception {
        subject.launchChromeTab(mockActivity, ANY_URL);

        verify(mockDoodleBitmapFactory).createBitmapFromVector(mockResources, R.drawable.back_arrow_vector);
    }

    @Test
    public void chromeTabIntent_shouldShowTitle() throws Exception {
        subject.launchChromeTab(testActivity, ANY_URL);

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

        assertThat(actual.getIntExtra(CustomTabsIntent.EXTRA_TITLE_VISIBILITY_STATE, ANY_DEFAULT_INT)).isEqualTo(TITLE_SHOWING);
    }
    @Test
    public void chromeTabIntent_hasWhiteColorAsToolbarColor() throws Exception {
        subject.launchChromeTab(testActivity, ANY_URL);

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

        assertThat(actual.getIntExtra(CustomTabsIntent.EXTRA_TOOLBAR_COLOR, ANY_DEFAULT_INT))
                .isEqualTo(ContextCompat.getColor(RuntimeEnvironment.application, R.color.white));
    }
    @Test
    public void chromeTabIntent_closeButtonShouldBeBackArrow() throws Exception {
        when(mockDoodleBitmapFactory.createBitmapFromVector(any(Resources.class), anyInt())).thenReturn(mockBitmap);
        subject.launchChromeTab(testActivity, ANY_URL);

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

        assertThat(actual.getParcelableExtra(CustomTabsIntent.EXTRA_CLOSE_BUTTON_ICON)).isEqualTo(mockBitmap);
    }

    @Test
    public void chromeTabIntent_shouldHaveCorrectUrlAsData() throws Exception {
        subject.launchChromeTab(testActivity, "http://thisiswhatiwant.com");

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

        assertThat(actual.getData()).isEqualTo(Uri.parse("http://thisiswhatiwant.com"));
    }

    @Test
    public void chromeTabIntent_shouldHaveDefaultValuesForOtherProperties() throws Exception {
        subject.launchChromeTab(testActivity, ANY_URL);

        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

        assertThat(actual.getParcelableExtra(CustomTabsIntent.EXTRA_SESSION)).isNull();
    }
}