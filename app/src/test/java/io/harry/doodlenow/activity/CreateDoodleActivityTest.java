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
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CreateDoodleActivityTest {
    public static final String EMPTY_URL = "";
    public static final String EMPTY_IMAGE_URL = "";
    private long MILLIS_2016_6_19_10_0 = 1466298000000L;
    @Inject
    FirebaseHelperWrapper mockFirebaseHelperWrapper;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;

    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;

    @Mock
    FirebaseHelper mockFirebaseHelper;

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
    public void actionBar_doesNotHaveElevation() throws Exception {
        assertThat(subject.getSupportActionBar().getElevation()).isEqualTo(0);
    }

    @Test
    public void onCreate_addChildEventListenerToFirebaseHelper() throws Exception {
        verify(mockFirebaseHelper).addChildEventListener(subject);
    }

    @Test
    public void onDestroy_removeChildEventListenerFromFirebaseHelper() throws Exception {
        subject.onDestroy();

        verify(mockFirebaseHelper).removeChildEventListener(subject);
    }

    @Test
    public void clickOnBackArrowAtActionBar_finishesActivity() throws Exception {
        subject.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void clickOnSaveAtActionBar_callsFirebaseHelperToSaveDoodle() throws Exception {
        tryToSave("This is real doodle", "And this is real content");

        Doodle expectedDoodle = new Doodle("This is real doodle", "And this is real content", EMPTY_URL, EMPTY_IMAGE_URL, MILLIS_2016_6_19_10_0);

        verify(mockFirebaseHelper).saveDoodle(expectedDoodle);
    }

    @Test
    public void clickOnSaveAtActionBar_doesNotCallDoodleService_whenTitleOrContentIsEmpty() throws Exception {
        tryToSave("", "And this is real content");

        verify(mockFirebaseHelper, never()).saveDoodle(any(Doodle.class));
    }

    @Test
    public void clickOnSaveAtActionBar_showsCanNotBeSaveMessage_whenIsNotValid() {
        tryToSave("This is real doodle", "");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장할 수 없습니다");
    }

    @Test
    public void afterSavedSuccessfully_showsToastMessage() throws Exception {
        subject.onChildAdded(mock(DataSnapshot.class), "some string");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장되었습니다");
    }

    @Test
    public void afterSavedSuccessfully_finishesActivity() throws Exception {
        subject.onChildAdded(mock(DataSnapshot.class), "some string");

        assertThat(subject.isFinishing()).isTrue();
    }

    private void tryToSave(String title, String content) {
        doodleTitle.setText(title);
        doodleContent.setText(content);

        subject.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));
    }
}