package io.harry.doodlenow.wrapper;

import android.content.Context;

import com.squareup.picasso.Picasso;

public class PicassoWrapper {
    public Picasso getPicasso(Context context) {
        return Picasso.with(context);
    }
}
