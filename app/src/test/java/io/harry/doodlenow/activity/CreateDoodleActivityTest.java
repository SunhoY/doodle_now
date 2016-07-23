package io.harry.doodlenow.activity;

import android.widget.EditText;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
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
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CreateDoodleActivityTest {
    private long MILLIS_2016_6_19_10_0 = 1466298000000L;
    @Inject
    DoodleService mockDoodleService;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;

    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;

    private CreateDoodleActivity subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication)RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);
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
    public void clickOnBackArrowAtActionBar_finishesActivity() throws Exception {
        subject.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void clickOnSaveAtActionBar_callsDoodleService() throws Exception {
        tryToSave("This is real doodle", "And this is real content");

        Doodle expectedDoodle = new Doodle("This is real doodle", "And this is real content", "", "", MILLIS_2016_6_19_10_0);

        verify(mockDoodleService).saveDoodle(
                eq(expectedDoodle),
                Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void clickOnSaveAtActionBar_doesNotCallDoodleService_whenTitleOrContentIsEmpty() throws Exception {
        tryToSave("", "And this is real content");

        verify(mockDoodleService, never()).saveDoodle(any(Doodle.class), Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void clickOnSaveAtActionBar_showsCanNotBeSaveMessage_whenIsNotValid() {
        tryToSave("This is real doodle", "");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장할 수 없습니다");
    }

    @Test
    public void afterSavedSuccessfully_showsToastMessage() throws Exception {
        tryToSave("This is real doodle", "And this is real content");
        doodleSuccessfullySaved();

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("저장되었습니다");
    }

    @Test
    public void afterSavedSuccessfully_finishesActivity() throws Exception {
        tryToSave("This is real doodle", "And this is real content");
        doodleSuccessfullySaved();

        assertThat(subject.isFinishing()).isTrue();
    }

    private void tryToSave(String text, String text2) {
        doodleTitle.setText(text);
        doodleContent.setText(text2);

        subject.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));
    }

    private void doodleSuccessfullySaved() {
        verify(mockDoodleService).saveDoodle(
                any(Doodle.class),
                voidServiceCallbackCaptor.capture());

        voidServiceCallbackCaptor.getValue().onSuccess(null);
    }
}