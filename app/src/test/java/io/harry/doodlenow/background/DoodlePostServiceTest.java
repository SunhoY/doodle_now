package io.harry.doodlenow.background;

import android.animation.AnimatorListenerAdapter;
import android.view.ViewPropertyAnimator;

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
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleRestfulService;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.view.DoodleIcon;
import io.harry.doodlenow.wrapper.JsoupWrapper;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodlePostServiceTest {
    @Inject
    JsoupWrapper mockJsoupWrapper;
    @Inject
    DoodleRestfulService mockDoodleRestfulService;
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

    private static final int ANY_FLAG = 99;
    private static final int ANY_START_ID = 88;
    private long MILLIS_2016_6_19_9_0 = 1466294400000L;

    private final String ANY_STRING = "any string";
    private DoodlePostService subject;

    @Before
    public void setUp() throws Exception {
        ((TestDoodleComponent)((TestDoodleApplication) RuntimeEnvironment.application).getDoodleComponent()).inject(this);
        MockitoAnnotations.initMocks(this);

        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_6_19_9_0);

        when(mockDoodleIcon.animate()).thenReturn(mockViewPropertyAnimator);
        when(mockViewPropertyAnimator.setDuration(anyLong())).thenReturn(mockViewPropertyAnimator);
        when(mockViewPropertyAnimator.setListener(any(AnimatorListenerAdapter.class))).thenReturn(mockViewPropertyAnimator);

        subject = new DoodlePostService();
        subject.onStartCommand(null, ANY_FLAG, ANY_START_ID);
    }

    @Test
    public void postDoodle_callsJsoupWrapperToGetDocumentWithProvidedUrl() throws Exception {
        subject.postDoodle("this is url");

        verify(mockJsoupWrapper).getDocument(eq("this is url"), Matchers.<JsoupCallback>any());
    }

    @Test
    public void afterGettingDocument_callsDoodleServiceToSaveDoodle() throws Exception {
        subject.postDoodle(ANY_STRING);

        verify(mockJsoupWrapper).getDocument(anyString(), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess("title", "content", "image url");

        Doodle expectedDoodle = new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0);

        verify(mockDoodleRestfulService).saveDoodle(eq(expectedDoodle),
                Matchers.<ServiceCallback<Void>>any());
    }

    @Test
    public void afterPostingDoodle_showsDoodleIcon() throws Exception {
        subject.postDoodle(ANY_STRING);

        verify(mockJsoupWrapper).getDocument(anyString(), jsoupCallbackCaptor.capture());

        jsoupCallbackCaptor.getValue().onSuccess("title", "content", "image url");

        Doodle expectedDoodle = new Doodle("title", "content", "image url", MILLIS_2016_6_19_9_0);

        verify(mockDoodleRestfulService).saveDoodle(eq(expectedDoodle), voidServiceCallbackCaptor.capture());

        voidServiceCallbackCaptor.getValue().onSuccess(null);

        verify(mockDoodleIcon).show();
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
        verify(mockViewPropertyAnimator).setDuration(2000);
    }

    @Test
    public void showDoodled_onAnimationEnd_hidesDoodleIcon() throws Exception {
        subject.showDoodled();

        verify(mockViewPropertyAnimator).setListener(animatorListenerCaptor.capture());
        animatorListenerCaptor.getValue().onAnimationEnd(null);

        verify(mockDoodleIcon).hide();
    }
}