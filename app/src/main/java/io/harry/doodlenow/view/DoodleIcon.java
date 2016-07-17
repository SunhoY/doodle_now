package io.harry.doodlenow.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import io.harry.doodlenow.R;

public class DoodleIcon extends View {
    private WindowManager windowManager;
    private FrameLayout frameLayout;

    public DoodleIcon(Context context) {
        super(context);

        addToWindowManager(context);
    }

    private void addToWindowManager(Context context) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.BOTTOM;

        frameLayout = new FrameLayout(context);
        frameLayout.setVisibility(GONE);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(frameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.doodle_icon, frameLayout);
    }

    public void show() {
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void hide() {
        frameLayout.setVisibility(View.GONE);
    }
}
