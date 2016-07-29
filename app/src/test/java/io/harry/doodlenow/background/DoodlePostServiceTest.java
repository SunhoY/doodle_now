package io.harry.doodlenow.background;

import android.animation.AnimatorListenerAdapter;
import android.app.Service;
import android.view.ViewPropertyAnimator;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.component.TestDoodleComponent;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.JsoupWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodlePostServiceTest {
    @Inject
    JsoupWrapper mockJsoupWrapper;
    @Inject
    FirebaseHelperWrapper mockFirebaseHelperWrapper;
    @Inject
    DoodleIcon mockDoodleIcon;

    @Captor
    ArgumentCaptor<JsoupCallback> jsoupCallbackCaptor;
    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<AnimatorListenerAdapter> animatorListenerCaptor;

    @Mock
    ViewPropertyAnimator mockViewPropertyAnimator;
    @Mock
    FirebaseHelper mockFirebaseHelper;
    @Mock
    DataSnapshot mockDataSnapshot;

    private static final int ANY_FLAG = 99;
    private static final int ANY_START_ID = 88;
    private static final String ANY_URL = "this is url";
    private static final long MILLIS_2016_6_19_9_0 = 1466294400000L;
    private static final String ANY_STRING = "any string";
    private DoodlePostService subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_9_0);

        when(mockDoodleIcon.animate()).thenReturn(mockViewPropertyAnimator);
        when(mockViewPropertyAnimator.setDuration(anyLong())).thenReturn(mockViewPropertyAnimator);
        when(mockViewPropertyAnimator.setListener(any(AnimatorListenerAdapter.class))).thenReturn(mockViewPropertyAnimator);

        when(mockFirebaseHelperWrapper.getFirebaseHelper("doodles")).thenReturn(mockFirebaseHelper);

        subject = new DoodlePostService();
        assertThat(subject.onStartCommand(null, ANY_FLAG, ANY_START_ID)).isEqualTo(Service.START_STICKY);
    }

    @Test
    public void postDoodle_callsJsoupWrapperToGetDocumentWithProvidedUrl() throws Exception {
        subject.postDoodle("this is url");

        verify(mockJsoupWrapper).getDocument(eq("this is url"), Matchers.<JsoupCallback>any());
    }

    @Test
    public void afterGettingDocument_callsFirebaseHelperToSaveDoodle() throws Exception {
        subject.postDoodle("this is url");

        verify(mockJsoupWrapper).getDocument(anyString(), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess("title", "content", "image url");

        Doodle expectedDoodle = new Doodle("title", "content", "this is url", "image url", MILLIS_2016_6_19_9_0);
        DoodleJson expectedDoodleJson = new DoodleJson(expectedDoodle);

        verify(mockFirebaseHelper).saveDoodle(expectedDoodleJson);
    }

    @Test
    public void afterPostingDoodle_obtainNewlyCreatedKey_andAddSingleEventListenerToFirebaseHelperWithKey() throws Exception {
        gotJsoupSuccessfully();

        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");

        jsoupCallbackCaptor.getValue().onSuccess(ANY_STRING, ANY_STRING, ANY_STRING);

        verify(mockFirebaseHelper).addSingleValueChangeListener("this is valid key", subject);
    }

    @Test
    public void afterPostingDoodle_showsDoodleIcon() throws Exception {
        gotJsoupSuccessfully();

        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");
        jsoupCallbackCaptor.getValue().onSuccess(ANY_STRING, ANY_STRING, ANY_STRING);

        when(mockDataSnapshot.getKey()).thenReturn("this is valid key");
        subject.onDataChange(mockDataSnapshot);

        verify(mockDoodleIcon).show();
    }

    @Test
    public void whenPostingDoodleFailed_doesNotShowDoodleIcon() throws Exception {
        gotJsoupSuccessfully();

        when(mockFirebaseHelper.saveDoodle(any(Doodle.class))).thenReturn("this is valid key");
        jsoupCallbackCaptor.getValue().onSuccess(ANY_STRING, ANY_STRING, ANY_STRING);

        when(mockDataSnapshot.getKey()).thenReturn("this is invalid key");
        subject.onDataChange(mockDataSnapshot);

        verify(mockDoodleIcon, never()).show();
    }

    @Test
    public void showDoodled_showsDoodleIcon() throws Exception {
        subject.showDoodled();

        verify(mockDoodleIcon).show();
    }

    @Test
    public void showDoodled_setAnimationWithDuration2Second() throws Exception {
        subject.showDoodled();

        verify(mockDoodleIcon).animate();
        verify(mockViewPropertyAnimator).setDuration(3000);
    }

    @Test
    public void showDoodled_onAnimationEnd_hidesDoodleIcon() throws Exception {
        subject.showDoodled();

        verify(mockViewPropertyAnimator).setListener(animatorListenerCaptor.capture());
        animatorListenerCaptor.getValue().onAnimationEnd(null);

        verify(mockDoodleIcon).hide();
    }

    private void gotJsoupSuccessfully() {
        subject.postDoodle(ANY_URL);

        verify(mockJsoupWrapper).getDocument(anyString(), jsoupCallbackCaptor.capture());
    }
}