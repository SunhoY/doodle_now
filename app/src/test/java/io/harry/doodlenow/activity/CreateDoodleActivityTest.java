package io.harry.doodlenow.activity;

import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CreateDoodleActivityTest {
    public static final String EMPTY_IMAGE_URL = "";
    private long MILLIS_2016_6_19_10_0 = 1466298000000L;

    @Inject
    FirebaseHelperWrapper mockFirebaseHelperWrapper;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.doodle_url)
    EditText doodleUrl;

    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;

    @Mock
    FirebaseHelper mockFirebaseHelper;
    @Mock
    DataSnapshot mockDataSnapshot;

    private CreateDoodleActivity subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication)RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        when(mockFirebaseHelperWrapper.getFirebaseHelper("doodles")).thenReturn(mockFirebaseHelper);
        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_10_0);

        subject = Robolectric.setupActivity(CreateDoodleActivity.class);

        ButterKnife.bind(this, subject);
    }

    @Test
    public void actionBar_doesNotHaveTitle() throws Exception {
        assertThat(subject.getSupportActionBar().getTitle()).isEqualTo("");
    }

    @Test
    public void clickOnBackArrowAtActionBar_finishesActivity() throws Exception {
        subject.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void clickOnSaveOnActionBar_doesNotCallFirebaseHelper_whenTitleOrContentIsEmpty() throws Exception {
        tryToSave("", "And this is real content", "http://awesome.com");

        verify(mockFirebaseHelper, never()).saveDoodle(any(Doodle.class));
    }

    @Test
    public void clickOnSaveOnActionBar_showsCanNotBeSaveMessage_whenIsNotValid() {
        tryToSave("This is real doodle", "", "http://awesome.com");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장할 수 없습니다");
    }

    @Test
    public void clickOnSaveOnActionBar_callsFirebaseHelperToSaveDoodle() throws Exception {
        tryToSave("This is real doodle", "And this is real content", "http://awesome.com");

        Doodle expectedDoodle =
                new Doodle("This is real doodle", "And this is real content", "http://awesome.com",
                        EMPTY_IMAGE_URL, MILLIS_2016_6_19_10_0);
        DoodleJson expectedDoodleJson = new DoodleJson(expectedDoodle);

        verify(mockFirebaseHelper).saveDoodle(expectedDoodleJson);
    }

    @Test
    public void whenUrlIsNotValid_showsToastMessageURLIsWrong() throws Exception {
        tryToSave("This is real doodle", "And this is real content", "I'm bad. http://awesome.com");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("링크(URL) 형식이 올바르지 않습니다.");
    }

    @Test
    public void afterObtainKeyFromFirebase_andAddSingleValueChangeListenerToFirebaseHelper() throws Exception {
        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is key");

        tryToSave("This is real doodle", "And this is real content", "http://awesome.com");

        verify(mockFirebaseHelper).addSingleValueChangeListener("this is key", subject);
    }

    @Test
    public void afterSavedSuccessfully_showsToastMessage() throws Exception {
        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");

        tryToSave("This is real doodle", "And this is real content", "http://awesome.com");

        when(mockDataSnapshot.getKey()).thenReturn("this is valid key");

        subject.onDataChange(mockDataSnapshot);

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장되었습니다");
    }

    @Test
    public void afterSavedSuccessfully_finishesActivity() throws Exception {
        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");

        tryToSave("This is real doodle", "And this is real content", "http://awesome.com");

        when(mockDataSnapshot.getKey()).thenReturn("this is valid key");

        subject.onDataChange(mockDataSnapshot);

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void whenDoodleIsNotSavedSuccessfully_doesNothing() throws Exception {
        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");

        tryToSave("This is real doodle", "And this is real content", "http://awesome.com");

        when(mockDataSnapshot.getKey()).thenReturn("this is invalid key");

        subject.onDataChange(mockDataSnapshot);

        assertThat(subject.isFinishing()).isFalse();
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(null);
    }

    private void tryToSave(String title, String content, String url) {
        doodleTitle.setText(title);
        doodleContent.setText(content);
        doodleUrl.setText(url);

        subject.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));
    }
}