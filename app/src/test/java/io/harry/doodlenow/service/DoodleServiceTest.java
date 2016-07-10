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
    ServiceCallback<List<Doodle>> mockServiceCallback;
    @Mock
    Call<Void> mockVoidCall;
    @Captor
    ArgumentCaptor<Callback<CloudantResponse<Doodle>>> cloudantCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockDoodleApi = mock(DoodleApi.class);
        subject = new DoodleService(mockDoodleApi);

        when(mockDoodleApi.getAllDoodles()).thenReturn(mockCloudantCall);
    }

    @Test
    public void getContents_callsContentApiToGetAllDoodles() throws Exception {
        subject.getDoodles(mockServiceCallback);

        verify(mockCloudantCall).enqueue(Matchers.<Callback<CloudantResponse<Doodle>>>any());
    }

    @Test
    public void afterGettingContents_runsSuccessServiceCallback() throws Exception {
        subject.getDoodles(mockServiceCallback);

        verify(mockCloudantCall).enqueue(cloudantCallbackCaptor.capture());

        CloudantResponse<Doodle> body = new CloudantResponse<>();
        CloudantDocument<Doodle> first = createDoodleDocument("first id", "first title", "first content", "first url");
        CloudantDocument<Doodle> second = createDoodleDocument("second id", "second title", "second content", "second url");

        body.rows = Arrays.asList(first, second);

        Response<CloudantResponse<Doodle>> response = Response.success(body);
        cloudantCallbackCaptor.getValue().onResponse(mockCloudantCall, response);

        verify(mockServiceCallback).onSuccess(Arrays.asList(
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

    private CloudantDocument<Doodle> createDoodleDocument(String id, String title, String content, String url) {
        CloudantDocument<Doodle> document = new CloudantDocument<>();
        document.doc = new Doodle(id, title, content, url);

        return document;
    }
}