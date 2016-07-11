package io.harry.doodlenow.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

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
import org.robolectric.shadows.ShadowToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleServiceCloudantAPI;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.wrapper.JsoupWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private static final String ANY_STRING = "any string";
    private static final String ANY_DOODLE_ID = "any doodle id";
    public static final long ANY_TIME_MILLIS = 1234L;
    private DoodleActivity subject;

    @Inject
    DoodleServiceCloudantAPI mockDoodleServiceCloudantAPI;
    @Inject
    JsoupWrapper jsoupWrapper;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.doodle_title)
    EditText doodleTitle;

    @Captor
    ArgumentCaptor<ServiceCallback<String>> stringServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<JsoupCallback> jsoupCallbackCaptor;
    @Captor
    ArgumentCaptor<ServiceCallback<Doodle>> doodleServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
    }

    @Test
    public void onCreate_getDocumentWithIntentStringExtra_whenSendIntent() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), Matchers.<JsoupCallback>any());
    }

    @Test
    public void afterGettingDocument_setsTitle_whenSendIntent() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess("Awesome article", ANY_STRING);

        assertThat(doodleTitle.getText().toString()).isEqualTo("Awesome article");
    }

    @Test
    public void afterGettingDocument_setsContent_whenSendIntent() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess(ANY_STRING, "Brilliant contents");

        assertThat(doodleContent.getText().toString()).isEqualTo("Brilliant contents");
    }

    @Test
    public void failedGettingDocument_setsEmptyStringsForTitleAndContent_whenSendIntent() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        doodleTitle.setText(ANY_STRING);
        doodleContent.setText(ANY_STRING);

        jsoupCallbackCaptor.getValue().onFailure();

        assertThat(doodleTitle.getText().toString()).isEqualTo("");
        assertThat(doodleContent.getText().toString()).isEqualTo("");
    }

    @Test
    public void onCreate_getsDoodleViaDoodleService_whenDoodleIdIntent() throws Exception {
        setupActivityWithDoodleIdIntent("some doodle id");

        verify(mockDoodleServiceCloudantAPI).retrieveDoodle(eq("some doodle id"), Matchers.<ServiceCallback<Doodle>>any());
    }

    @Test
    public void afterGettingDoodle_setsTitle_whenDoodleIntent() throws Exception {
        setupActivityWithDoodleIdIntent(ANY_DOODLE_ID);

        getDoodleViaDoodleServiceAndSuccessWith(new Doodle("", "", "doodled", "", "", ANY_TIME_MILLIS));

        assertThat(doodleTitle.getText().toString()).isEqualTo("doodled");
    }

    @Test
    public void afterGettingDoodle_setsContent_whenDoodleIntent() throws Exception {
        setupActivityWithDoodleIdIntent(ANY_DOODLE_ID);

        getDoodleViaDoodleServiceAndSuccessWith(new Doodle("", "", "", "doodle doodle doodle pop", "", ANY_TIME_MILLIS));

        assertThat(doodleContent.getText().toString()).isEqualTo("doodle doodle doodle pop");

    }

    @Test
    public void onSubmitClick_callsDoodleServiceToSaveContentInEditText_withEmptyId() throws Exception {
        setupActivityWithSendIntent("http://someurl");
        doodleTitle.setText("Jordan");
        doodleContent.setText("dunk jordan dunk!");

        submit.performClick();

        Doodle doodle = new Doodle(null, null, "Jordan", "dunk jordan dunk!", "http://someurl", ANY_TIME_MILLIS);
        verify(mockDoodleServiceCloudantAPI).createDoodle(eq(doodle),
                Matchers.<ServiceCallback<String>>any());
    }

    @Test
    public void onSubmitClick_callsDoodleServiceToUpdate_withExistingId() throws Exception {
        setupActivityWithDoodleIdIntent("some id");
        Doodle doodle = new Doodle("some id", "", "Curry", "three pointer!", "http://otherurl", ANY_TIME_MILLIS);
        getDoodleViaDoodleServiceAndSuccessWith(doodle);

        submit.performClick();

        Doodle expected = new Doodle("some id", "", "Curry", "three pointer!", "http://otherurl", ANY_TIME_MILLIS);

        verify(mockDoodleServiceCloudantAPI).updateDoodle(eq(expected), Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void afterSavingDoodleSuccessfully_finishesActivity() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        clickSubmitToSaveDoodle();

        stringServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void afterSavingDoodleSuccessfully_toastsMessage() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        clickSubmitToSaveDoodle();

        stringServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("참 잘했어요! 엄지 척!");
    }

    private void setupActivityWithSendIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        subject = Robolectric.buildActivity(DoodleActivity.class)
                .withIntent(intent)
                .create().get();

        ButterKnife.bind(this, subject);
    }

    private void setupActivityWithDoodleIdIntent(String doodleId) {
        Intent intent = new Intent();
        intent.putExtra("DOODLE_ID", doodleId);
        subject = Robolectric.buildActivity(DoodleActivity.class)
                .withIntent(intent)
                .create().get();

        ButterKnife.bind(this, subject);
    }

    private void clickSubmitToSaveDoodle() {
        submit.performClick();

        verify(mockDoodleServiceCloudantAPI).createDoodle(any(Doodle.class), stringServiceCallbackCaptor.capture());
    }

    private void getDoodleViaDoodleServiceAndSuccessWith(Doodle doodle) {
        verify(mockDoodleServiceCloudantAPI).retrieveDoodle(anyString(), doodleServiceCallbackCaptor.capture());

        doodleServiceCallbackCaptor.getValue().onSuccess(doodle);
    }
}