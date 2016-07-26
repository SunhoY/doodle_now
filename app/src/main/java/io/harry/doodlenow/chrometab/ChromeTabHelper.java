package io.harry.doodlenow.chrometab;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.converter.DoodleBitmapFactory;

public class ChromeTabHelper {
    @Inject
    DoodleBitmapFactory doodleBitmapFactory;

    public ChromeTabHelper(Context context) {
        ((DoodleApplication) context).getDoodleComponent().inject(this);
    }

    public void launchChromeTab(AppCompatActivity activity, String url) {
        Bitmap backArrowBitmap = doodleBitmapFactory.createBitmapFromVector(activity.getResources(), R.drawable.back_arrow_vector);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.white));
        builder.setShowTitle(true);
        builder.setCloseButtonIcon(backArrowBitmap);

        CustomTabsIntent intent = builder.build();
        intent.launchUrl(activity, Uri.parse(url));
    }
}
