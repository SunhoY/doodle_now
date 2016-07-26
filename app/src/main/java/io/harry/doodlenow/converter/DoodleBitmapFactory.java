package io.harry.doodlenow.converter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.graphics.drawable.VectorDrawableCompat;

public class DoodleBitmapFactory {
    public Bitmap createBitmapFromVector(Resources resources, int vectorResourceId) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(resources, vectorResourceId, null);

        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawableCompat.getIntrinsicWidth(),
                vectorDrawableCompat.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawableCompat.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawableCompat.draw(canvas);

        return bitmap;
    }
}
