package io.harry.doodlenow.activity;

import android.widget.Button;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
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
    private final long MILLIS_2016_6_19_7_0 = 1466287200000L;
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
    @Captor
    ArgumentCaptor<ServiceCallback<DoodleJson>> doodleServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_7_0);

        subject = Robolectric.setupActivity(DoodleActivity.class);

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onSubmitClick_callsDoodleServiceToSave() throws Exception {
        doodleTitle.setText("this title");
        doodleContent.setText("that content");

        submit.performClick();

        Doodle expected = new Doodle("this title", "that content");

        verify(mockDoodleService).saveDoodle(eq(expected), Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void onSubmitClick_finishesActivity_whenSuccessfullySaved() throws Exception {
        submit.performClick();

        verify(mockDoodleService).saveDoodle(any(Doodle.class), voidServiceCallbackCaptor.capture());

        voidServiceCallbackCaptor.getValue().onSuccess(null);

        assertThat(subject.isFinishing()).isTrue();
    }
}