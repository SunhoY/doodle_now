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
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.wrapper.JsoupWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private static final String ANY_STRING = "any string";
    private DoodleActivity subject;

    @Inject
    DoodleService mockDoodleService;
    @Inject
    JsoupWrapper jsoupWrapper;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.doodle_title)
    EditText doodleTitle;

    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<JsoupCallback> jsoupCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
    }

    @Test
    public void onCreate_getDocumentWithIntentStringExtra() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), Matchers.<JsoupCallback>any());
    }

    @Test
    public void onCreate_setTitleOnGetDocumentSuccess() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess("Awesome article", ANY_STRING);

        assertThat(doodleTitle.getText().toString()).isEqualTo("Awesome article");
    }

    @Test
    public void onCreate_setsContentOnGetDocumentSuccess() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess(ANY_STRING, "Brilliant contents");

        assertThat(doodleContent.getText().toString()).isEqualTo("Brilliant contents");
    }

    @Test
    public void onCreate_setsEmptyStringsForTitleAndContentOnGetDocumentFailure() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        verify(jsoupWrapper).getDocument(eq("http://someurl"), jsoupCallbackCaptor.capture());

        doodleTitle.setText(ANY_STRING);
        doodleContent.setText(ANY_STRING);

        jsoupCallbackCaptor.getValue().onFailure();

        assertThat(doodleTitle.getText().toString()).isEqualTo("");
        assertThat(doodleContent.getText().toString()).isEqualTo("");
    }

    @Test
    public void onSubmitClick_callsDoodleServiceToSaveContentInEditText() throws Exception {
        setupActivityWithSendIntent("http://someurl");
        doodleTitle.setText("Jordan");
        doodleContent.setText("dunk jordan dunk!");

        submit.performClick();

        Doodle doodle = new Doodle("", "Jordan", "dunk jordan dunk!", "http://someurl");
        verify(mockDoodleService).saveDoodle(eq(doodle),
                Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void afterSavingDoodleSuccessfully_finishesActivity() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        clickSubmitToSaveDoodle();

        voidServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void afterSavingDoodleSuccessfully_toastsMessage() throws Exception {
        setupActivityWithSendIntent("http://someurl");

        clickSubmitToSaveDoodle();

        voidServiceCallbackCaptor.getValue().onSuccess(null);

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

    private void clickSubmitToSaveDoodle() {
        submit.performClick();

        verify(mockDoodleService).saveDoodle(any(Doodle.class), voidServiceCallbackCaptor.capture());
    }
}