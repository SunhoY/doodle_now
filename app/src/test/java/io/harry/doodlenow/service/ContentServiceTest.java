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

import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.api.ContentApi;
import io.harry.doodlenow.model.CloudantResponse;
import io.harry.doodlenow.model.Content;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContentServiceTest {
    private ContentService subject;

    @Mock
    ContentApi mockContentApi;
    @Mock
    Call<CloudantResponse<Content>> mockCloudantCall;
    @Mock
    ServiceCallback<List<Content>> mockServiceCallback;
    @Captor
    ArgumentCaptor<Callback<CloudantResponse<Content>>> cloudantCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockContentApi = mock(ContentApi.class);
        subject = new ContentService(mockContentApi);

        when(mockContentApi.getAllDoodles()).thenReturn(mockCloudantCall);
    }

    @Test
    public void getContents_callsContentApiToGetAllDoodles() throws Exception {
        subject.getContents(mockServiceCallback);

        verify(mockCloudantCall).enqueue(Matchers.<Callback<CloudantResponse<Content>>>any());
    }

    @Test
    public void afterGettingContents_runsSuccessServiceCallback() throws Exception {
        subject.getContents(mockServiceCallback);

        verify(mockCloudantCall).enqueue(cloudantCallbackCaptor.capture());

        CloudantResponse<Content> body = new CloudantResponse<>();
        List mockList = mock(List.class);
        body.rows = mockList;
        Response<CloudantResponse<Content>> response = Response.success(body);
        cloudantCallbackCaptor.getValue().onResponse(mockCloudantCall, response);

        verify(mockServiceCallback).onSuccess(mockList);
    }
}