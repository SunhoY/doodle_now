package io.harry.doodlenow.service;

import android.support.annotation.NonNull;

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
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;
import io.harry.doodlenow.model.cloudant.CloudantQueryResponse;
import io.harry.doodlenow.model.cloudant.CreatedAtQuery;
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
    private final long ANY_MILLIS = 0L;
    private final long SOME_MILLIS = 1L;

    @Mock
    DoodleApi mockDoodleApi;
    @Mock
    Call<CloudantQueryResponse> mockCloudantQueryCall;
    @Mock
    ServiceCallback<List<Doodle>> mockDoodleListServiceCallback;
    @Mock
    Call<Void> mockVoidCall;
    @Mock
    Call<DoodleJson> mockDoodleCall;
    @Mock
    ServiceCallback<Void> mockVoidServiceCallback;
    @Captor
    ArgumentCaptor<Callback<CloudantQueryResponse>> cloudantQueryCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<DoodleJson>> doodleCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        DateTimeUtils.setCurrentMillisFixed(SOME_MILLIS);

        mockDoodleApi = mock(DoodleApi.class);
        subject = new DoodleService(mockDoodleApi);
    }

    @Test
    public void getDoodles_callDoodlesApiWithFromAndToMillis() throws Exception {
        when(mockDoodleApi.getDoodles(any(CreatedAtQuery.class))).thenReturn(mockCloudantQueryCall);
        subject.getDoodles(0L, 1L, mockDoodleListServiceCallback);

        CreatedAtQuery createdTimeQuery = new CreatedAtQuery(0L, 1L);

        verify(mockDoodleApi).getDoodles(createdTimeQuery);
    }

    @Test
    public void getDoodles_enqueuesCallbackOnCallObject() throws Exception {
        when(mockDoodleApi.getDoodles(new CreatedAtQuery(0L, 1L))).thenReturn(mockCloudantQueryCall);

        subject.getDoodles(0L, 1L, mockDoodleListServiceCallback);

        verify(mockCloudantQueryCall).enqueue(Matchers.<Callback<CloudantQueryResponse>>any());
    }

    @Test
    public void afterGettingDoodles_runsSuccessServiceCallback() throws Exception {
        when(mockDoodleApi.getDoodles(any(CreatedAtQuery.class))).thenReturn(mockCloudantQueryCall);

        subject.getDoodles(ANY_MILLIS, ANY_MILLIS, mockDoodleListServiceCallback);

        verify(mockCloudantQueryCall).enqueue(cloudantQueryCallbackCaptor.capture());

        CloudantQueryResponse body = new CloudantQueryResponse();
        DoodleJson first = new DoodleJson(createMockDoodle("title 1", "content 1" , "image url 1", 1L, "some url"));
        DoodleJson second = new DoodleJson(createMockDoodle("title 2", "content 2" , "image url 2", 2L, "some url"));

        body.docs = Arrays.asList(first, second);

        Response<CloudantQueryResponse> response = Response.success(body);
        cloudantQueryCallbackCaptor.getValue().onResponse(mockCloudantQueryCall, response);

        verify(mockDoodleListServiceCallback).onSuccess(Arrays.asList(
                new Doodle(first),
                new Doodle(second)
        ));
    }

    @Test
    public void saveDoodle_getsCallObjectWithContent() throws Exception {
        when(mockDoodleApi.postDoodle(any(DoodleJson.class))).thenReturn(mockVoidCall);

        Doodle mockDoodle = createMockDoodle("this title", "that content", "imageUrl", 2L, "some url");

        subject.saveDoodle(mockDoodle, mockVoidServiceCallback);

        DoodleJson doodleJson = new DoodleJson(createMockDoodle("this title", "that content", "imageUrl", 2L, "some url"));
        verify(mockDoodleApi).postDoodle(doodleJson);
    }

    @Test
    public void saveDoodle_enqueuesCallbackOnCallObject() throws Exception {
        when(mockDoodleApi.postDoodle(any(DoodleJson.class))).thenReturn(mockVoidCall);

        subject.saveDoodle(mock(Doodle.class), mockVoidServiceCallback);

        verify(mockVoidCall).enqueue(Matchers.<Callback<Void>>any());
    }

    @Test
    public void whenSaveDoodleSuccessfully_runsSuccessServiceCallback() throws Exception {
        when(mockDoodleApi.postDoodle(any(DoodleJson.class))).thenReturn(mockVoidCall);
        Doodle mockDoodle = createMockDoodle("this title", "that content", "imageUrl", 2L, "some url");

        subject.saveDoodle(mockDoodle, mockVoidServiceCallback);

        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        Response<Void> response = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockVoidCall, response);

        verify(mockVoidServiceCallback).onSuccess(null);
    }

    @NonNull
    private Doodle createMockDoodle(String title, String content, String imageUrl, Long createdAt, String url) {
        Doodle mockDoodle = mock(Doodle.class);
        when(mockDoodle.getTitle()).thenReturn(title);
        when(mockDoodle.getContent()).thenReturn(content);
        when(mockDoodle.getImageUrl()).thenReturn(imageUrl);
        when(mockDoodle.getCreatedAt()).thenReturn(createdAt);
        when(mockDoodle.getUrl()).thenReturn(url);
        return mockDoodle;
    }
}