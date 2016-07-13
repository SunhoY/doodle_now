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
import io.harry.doodlenow.model.cloudant.CloudantDocument;
import io.harry.doodlenow.model.cloudant.CloudantQueryResponse;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
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
        DoodleJson first = new DoodleJson("first title", "first content", 0L);
        DoodleJson second = new DoodleJson("second title", "second content", 1L);

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

        Doodle mockDoodle = createMockDoodle("this title", "that content");

        subject.saveDoodle(mockDoodle, mock(ServiceCallback.class));

        DoodleJson doodleJson = new DoodleJson("this title", "that content", SOME_MILLIS);
        verify(mockDoodleApi).postDoodle(doodleJson);
    }

    @Test
    public void saveDoodle_enqueuesCallbackOnCallObject() throws Exception {
        when(mockDoodleApi.postDoodle(any(DoodleJson.class))).thenReturn(mockVoidCall);

        subject.saveDoodle(mock(Doodle.class), mock(ServiceCallback.class));

        verify(mockVoidCall).enqueue(Matchers.<Callback<Void>>any());
    }

    @Test
    public void whenSaveDoodleSuccessfully_runsSuccessServiceCallback() throws Exception {
        when(mockDoodleApi.postDoodle(any(DoodleJson.class))).thenReturn(mockVoidCall);
        ServiceCallback<Void> mockServiceCallback = mock(ServiceCallback.class);
        Doodle mockDoodle = createMockDoodle("this title", "that content");

        subject.saveDoodle(mockDoodle, mockServiceCallback);

        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        Response<Void> response = Response.success(null);
        voidCallbackCaptor.getValue().onResponse(mockVoidCall, response);

        verify(mockServiceCallback).onSuccess(null);
    }

    @NonNull
    private Doodle createMockDoodle(String title, String content) {
        Doodle mockDoodle = mock(Doodle.class);
        when(mockDoodle.getTitle()).thenReturn(title);
        when(mockDoodle.getContent()).thenReturn(content);
        return mockDoodle;
    }
}