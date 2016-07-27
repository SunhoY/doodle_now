package io.harry.doodlenow.converter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.graphics.drawable.VectorDrawableCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@Config(constants = BuildConfig.class)
@PrepareForTest({VectorDrawableCompat.class, Bitmap.class})
public class DoodleBitmapFactoryTest {

    private static final int ANY_RESOURCE_ID = -999;

    private DoodleBitmapFactory subject;

    @Mock
    Resources mockResources;
    @Mock
    VectorDrawableCompat mockVectorDrawableCompat;
    @Mock
    Bitmap mockBitmap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        subject = new DoodleBitmapFactory();
    }

    @Test
    public void createBitmapFromVector_createsBitmapFromResourceId() throws Exception {
        mockStatic(VectorDrawableCompat.class);

        expect(VectorDrawableCompat.create(mockResources, R.drawable.back_arrow_vector, null)).andReturn(mockVectorDrawableCompat);
        replay(VectorDrawableCompat.class);

        subject.createBitmapFromVector(mockResources, R.drawable.back_arrow_vector);
    }

    @Test
    public void createBitmapFromVector_createsBitmapFromVectorDrawable() throws Exception {
        when(mockVectorDrawableCompat.getIntrinsicWidth()).thenReturn(24);
        when(mockVectorDrawableCompat.getIntrinsicHeight()).thenReturn(28);

        mockStatic(VectorDrawableCompat.class);
        mockStatic(Bitmap.class);

        expect(VectorDrawableCompat.create(anyObject(Resources.class), anyInt(), anyObject(Resources.Theme.class))).andReturn(mockVectorDrawableCompat);
        expect(Bitmap.createBitmap(24, 28, Bitmap.Config.ARGB_8888)).andReturn(mockBitmap);

        replay(VectorDrawableCompat.class);
        replay(Bitmap.class);

        subject.createBitmapFromVector(mockResources, ANY_RESOURCE_ID);
    }

    @Test
    public void createBitmapFromVector_setsBoundsWithCanvasWidthAndHeight() throws Exception {
        when(mockVectorDrawableCompat.getIntrinsicWidth()).thenReturn(24);
        when(mockVectorDrawableCompat.getIntrinsicHeight()).thenReturn(28);

        mockStatic(VectorDrawableCompat.class);
        mockStatic(Bitmap.class);

        expect(VectorDrawableCompat.create(anyObject(Resources.class), anyInt(), anyObject(Resources.Theme.class))).andReturn(mockVectorDrawableCompat);
        expect(Bitmap.createBitmap(anyInt(), anyInt(), anyObject(Bitmap.Config.class))).andReturn(mockBitmap);

        replay(VectorDrawableCompat.class);
        replay(Bitmap.class);

        subject.createBitmapFromVector(mockResources, ANY_RESOURCE_ID);

        //can not create mock bitmap under PowermockRunner
        //Any one who helps me would be appreciated.
        verify(mockVectorDrawableCompat).setBounds(0, 0, 0, 0);
        verify(mockVectorDrawableCompat).draw(any(Canvas.class));
    }
}