package io.harry.doodlenow.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.cloudant.CloudantDocument;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.Doodle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleServiceCloudantAPITest {
    public static final long ANY_TIME_MILLIS = 1234L;
    private DoodleServiceCloudantAPI subject;

    @Mock
    DoodleApi mockDoodleApi;
    @Mock
    Call<CloudantResponse<Doodle>> mockCloudantCall;
    @Mock
    ServiceCallback<List<Doodle>> mockDoodleListServiceCallback;
    @Mock
    Call<Doodle> mockDoodleCall;
    @Mock
    Call<Void> mockVoidCall;
    @Captor
    ArgumentCaptor<Callback<CloudantResponse<Doodle>>> cloudantCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Doodle>> doodleCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockDoodleApi = mock(DoodleApi.class);
        subject = new DoodleServiceCloudantAPI(mockDoodleApi);

        when(mockDoodleApi.getAllDoodles()).thenReturn(mockCloudantCall);
    }

    @Test
    public void getContents_callsContentApiToGetAllDoodles() throws Exception {
        subject.retrieveDoodles(mockDoodleListServiceCallback);

        verify(mockCloudantCall).enqueue(Matchers.<Callback<CloudantResponse<Doodle>>>any());
    }

    @Test
    public void afterGettingContents_runsSuccessServiceCallback() throws Exception {
        subject.retrieveDoodles(mockDoodleListServiceCallback);

        verify(mockCloudantCall).enqueue(cloudantCallbackCaptor.capture());

        CloudantResponse<Doodle> body = new CloudantResponse<>();
        CloudantDocument<Doodle> first = createDoodleDocument("first id", "first title", "first content", "first url");
        CloudantDocument<Doodle> second = createDoodleDocument("second id", "second title", "second content", "second url");

        body.rows = Arrays.asList(first, second);

        Response<CloudantResponse<Doodle>> response = Response.success(body);
        cloudantCallbackCaptor.getValue().onResponse(mockCloudantCall, response);

        verify(mockDoodleListServiceCallback).onSuccess(Arrays.asList(
                createDoodleDocument("first id", "first title", "first content", "first url").doc,
                createDoodleDocument("second id", "second title", "second content", "second url").doc
        ));
    }

    @Test
    public void saveDoodle_getsCallObjectWithContent() throws Exception {
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockDoodleCall);
        subject.createDoodle(new Doodle("", "", "title", "content", "url", ANY_TIME_MILLIS), mock(ServiceCallback.class));

        verify(mockDoodleApi).postDoodle(new Doodle("", "", "title", "content", "url", ANY_TIME_MILLIS));
    }

    @Test
    public void saveDoodle_enqueuesCallbackOnCallObject() throws Exception {
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockDoodleCall);
        subject.createDoodle(new Doodle("", "", "title", "content", "url", ANY_TIME_MILLIS), mock(ServiceCallback.class));

        verify(mockDoodleCall).enqueue(Matchers.<Callback<Doodle>>any());
    }

    @Test
    public void whenPostDoodleSuccessfully_runsSuccessServiceCallback() throws Exception {
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockDoodleCall);
        ServiceCallback<String> mockServiceCallback = mock(ServiceCallback.class);
        subject.createDoodle(any(Doodle.class), mockServiceCallback);

        verify(mockDoodleCall).enqueue(doodleCallbackCaptor.capture());

        Response<Doodle> response = Response.success(new Doodle("id", "rev", "title", "content", "url", ANY_TIME_MILLIS));
        doodleCallbackCaptor.getValue().onResponse(mockDoodleCall, response);

        verify(mockServiceCallback).onSuccess("id");
    }

    @Test
    public void getDoodle_getsCallObjectFromDoodleApiWithDoodleId() throws Exception {
        when(mockDoodleApi.getDoodle(anyString())).thenReturn(mockDoodleCall);
        subject.retrieveDoodle("some id", mock(ServiceCallback.class));

        verify(mockDoodleApi).getDoodle("some id");
    }

    @Test
    public void getDoodle_enqueuesCallbackOnObtainedCallObject() throws Exception {
        when(mockDoodleApi.getDoodle("some id")).thenReturn(mockDoodleCall);
        subject.retrieveDoodle("some id", mock(ServiceCallback.class));

        verify(mockDoodleCall).enqueue(Matchers.<Callback<Doodle>>any());
    }

    @Test
    public void getDoodle_runsServiceCallbackWithResponse_whenCallIsSuccessful() throws Exception {
        when(mockDoodleApi.getDoodle(anyString())).thenReturn(mockDoodleCall);
        ServiceCallback<Doodle> mockServiceCallback = mock(ServiceCallback.class);
        subject.retrieveDoodle(anyString(), mockServiceCallback);

        verify(mockDoodleCall).enqueue(doodleCallbackCaptor.capture());

        Response<Doodle> response = Response.success(new Doodle("some id", "", "title", "content", "url", ANY_TIME_MILLIS));
        doodleCallbackCaptor.getValue().onResponse(mockDoodleCall, response);

        verify(mockServiceCallback).onSuccess(new Doodle("some id", "", "title", "content", "url", ANY_TIME_MILLIS));
    }

    @Test
    public void updateDoodle_getsCallObjectFromDoodleApiWithDoodleRevision() throws Exception {
        when(mockDoodleApi.putDoodle(anyString(), any(Doodle.class))).thenReturn(mockVoidCall);
        subject.updateDoodle(new Doodle("some id", "rev 1", "title", "content", "url", ANY_TIME_MILLIS), mock(ServiceCallback.class));

        verify(mockDoodleApi).putDoodle(eq("some id"), eq(new Doodle("some id", "rev 1", "title", "content", "url", ANY_TIME_MILLIS)));
    }

    @Test
    public void updateDoodle_enqueuesCallbackOnObtainedCallObject() throws Exception {
        when(mockDoodleApi.putDoodle("update id", new Doodle("update id", "rev 1", "title", "content", "url", ANY_TIME_MILLIS))).thenReturn(mockVoidCall);
        subject.updateDoodle(new Doodle("update id", "rev 1", "title", "content", "url", ANY_TIME_MILLIS), mock(ServiceCallback.class));

        verify(mockVoidCall).enqueue(Matchers.<Callback<Void>>any());
    }

    @Test
    public void updateDoodle_runsSuccessCallback_whenCallIsSuccessful() throws Exception {
        when(mockDoodleApi.putDoodle(anyString(), any(Doodle.class))).thenReturn(mockVoidCall);
        ServiceCallback<Void> mockServiceCallback = mock(ServiceCallback.class);
        subject.updateDoodle(mock(Doodle.class), mockServiceCallback);

        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        Response<Void> response = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockVoidCall, response);

        verify(mockServiceCallback).onSuccess(null);
    }

    private CloudantDocument<Doodle> createDoodleDocument(String id, String title, String content, String url) {
        CloudantDocument<Doodle> document = new CloudantDocument<>();
        document.doc = new Doodle(id, "", title, content, url, ANY_TIME_MILLIS);

        return document;
    }
}