package io.harry.doodlenow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.harry.doodlenow.R;
import io.harry.doodlenow.model.Doodle;

public class DoodleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Doodle> doodles;
    private final Context context;
    private final OnDoodleClickListener doodleClickListener;

    public interface OnDoodleClickListener {
        void onDoodleClick(Doodle doodle);
    }

    public DoodleListAdapter(Context context, List<Doodle> doodles, OnDoodleClickListener doodleClickListener) {
        this.context = context;
        this.doodles = doodles;
        this.doodleClickListener = doodleClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_list_item, null);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String content = doodles.get(position).content;

        ((SimpleViewHolder) holder).content.setText(content);
    }

    @Override
    public int getItemCount() {
        return doodles.size();
    }

    public void refreshDoodles(List<Doodle> doodles) {
        this.doodles.clear();
        this.doodles.addAll(doodles);

        notifyDataSetChanged();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView content;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
