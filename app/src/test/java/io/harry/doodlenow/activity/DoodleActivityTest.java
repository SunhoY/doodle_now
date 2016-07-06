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

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private DoodleActivity subject;
    private EditText content;

    DoodleService mockDoodleService;

    Button submit;

    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onCreate_showsContentsInEditText() throws Exception {
        setupActivityWithSendIntent();

        assertThat(content.getText().toString()).isEqualTo("hello friends");
    }

    @Test
    public void onSubmitClick_callsDoodleServiceToSaveContentInEditText() throws Exception {
        setupActivityWithSendIntent();
        content.setText("dunk jordan dunk!");

        submit.performClick();

        verify(mockDoodleService).saveContent(eq("dunk jordan dunk!"),
                Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void afterSavingDoodleSuccessfully_finishesActivity() throws Exception {
        setupActivityWithSendIntent();

        clickSubmitToSaveDoodle();

        voidServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void afterSavingDoodleSuccessfully_toastsMessage() throws Exception {
        setupActivityWithSendIntent();

        clickSubmitToSaveDoodle();

        voidServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("참 잘했어요! 엄지 척!");
    }

    private void setupActivityWithSendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "hello friends");
        subject = Robolectric.buildActivity(DoodleActivity.class)
                .withIntent(intent)
                .create().get();

        setupViews();
        injectDependencies();
    }

    private void setupViews() {
        content = (EditText) subject.findViewById(R.id.content);
        submit = (Button) subject.findViewById(R.id.submit);
    }

    private void injectDependencies() {
        mockDoodleService = ((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent().contentService();
    }

    private void clickSubmitToSaveDoodle() {
        submit.performClick();

        verify(mockDoodleService).saveContent(anyString(), voidServiceCallbackCaptor.capture());
    }
}