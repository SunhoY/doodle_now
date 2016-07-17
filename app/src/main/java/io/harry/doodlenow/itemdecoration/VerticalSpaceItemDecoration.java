package io.harry.doodlenow.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(Context context, int verticalSpaceHeightDimension) {
        float density = context.getResources().getDisplayMetrics().density;
        float dimension = context.getResources().getDimension(verticalSpaceHeightDimension);

        this.verticalSpaceHeight = (int) (density * dimension);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
