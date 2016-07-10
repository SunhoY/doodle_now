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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleServiceTest {
    private DoodleService subject;

    @Mock
    DoodleApi mockDoodleApi;
    @Mock
    Call<CloudantResponse<Doodle>> mockCloudantCall;
    @Mock
    ServiceCallback<List<Doodle>> mockDoodleListServiceCallback;
    @Mock
    Call<Void> mockVoidCall;
    @Mock
    Call<Doodle> mockDoodleCall;
    @Captor
    ArgumentCaptor<Callback<CloudantResponse<Doodle>>> cloudantCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Doodle>> doodleCallbackCaptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockDoodleApi = mock(DoodleApi.class);
        subject = new DoodleService(mockDoodleApi);

        when(mockDoodleApi.getAllDoodles()).thenReturn(mockCloudantCall);
    }

    @Test
    public void getContents_callsContentApiToGetAllDoodles() throws Exception {
        subject.getDoodles(mockDoodleListServiceCallback);

        verify(mockCloudantCall).enqueue(Matchers.<Callback<CloudantResponse<Doodle>>>any());
    }

    @Test
    public void afterGettingContents_runsSuccessServiceCallback() throws Exception {
        subject.getDoodles(mockDoodleListServiceCallback);

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
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockVoidCall);
        subject.saveDoodle(new Doodle("", "title", "content", "url"), mock(ServiceCallback.class));

        verify(mockDoodleApi).postDoodle(new Doodle("", "title", "content", "url"));
    }

    @Test
    public void saveDoodle_enqueuesCallbackOnCallObject() throws Exception {
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockVoidCall);
        subject.saveDoodle(new Doodle("", "title", "content", "url"), mock(ServiceCallback.class));

        verify(mockVoidCall).enqueue(Matchers.<Callback<Void>>any());
    }

    @Test
    public void whenPostDoodleSuccessfully_runsSuccessServiceCallback() throws Exception {
        when(mockDoodleApi.postDoodle(any(Doodle.class))).thenReturn(mockVoidCall);
        ServiceCallback<Void> mockServiceCallback = mock(ServiceCallback.class);
        subject.saveDoodle(new Doodle("", "title", "content", "url"), mockServiceCallback);

        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        Response<Void> response = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockVoidCall, response);

        verify(mockServiceCallback).onSuccess(null);
    }

    @Test
    public void getDoodle_getsCallObjectFromDoodleApiWithDoodleId() throws Exception {
        when(mockDoodleApi.getDoodle(anyString())).thenReturn(mockDoodleCall);
        subject.getDoodle("some id", mock(ServiceCallback.class));

        verify(mockDoodleApi).getDoodle("some id");
    }

    @Test
    public void getDoodle_enqueuesCallbackOnObtainedCallObject() throws Exception {
        when(mockDoodleApi.getDoodle("some id")).thenReturn(mockDoodleCall);
        subject.getDoodle("some id", mock(ServiceCallback.class));

        verify(mockDoodleCall).enqueue(Matchers.<Callback<Doodle>>any());
    }

    @Test
    public void getDoodle_runsServiceCallbackWithResponse_whenCallIsSuccessful() throws Exception {
        when(mockDoodleApi.getDoodle("some id")).thenReturn(mockDoodleCall);
        ServiceCallback<Doodle> mockServiceCallback = mock(ServiceCallback.class);
        subject.getDoodle("some id", mockServiceCallback);

        verify(mockDoodleCall).enqueue(doodleCallbackCaptor.capture());

        Response<Doodle> response = Response.success(new Doodle("some id", "title", "content", "url"));
        doodleCallbackCaptor.getValue().onResponse(mockDoodleCall, response);

        verify(mockServiceCallback).onSuccess(new Doodle("some id", "title", "content", "url"));
    }

    private CloudantDocument<Doodle> createDoodleDocument(String id, String title, String content, String url) {
        CloudantDocument<Doodle> document = new CloudantDocument<>();
        document.doc = new Doodle(id, title, content, url);

        return document;
    }
}