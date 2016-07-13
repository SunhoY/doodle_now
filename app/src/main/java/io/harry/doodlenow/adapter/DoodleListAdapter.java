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
    private OnDoodleClickListener doodleClickListener;

    public interface OnDoodleClickListener {
        void onDoodleClick(Doodle doodle);
    }

    public DoodleListAdapter(Context context, List<Doodle> doodles) {
        this.context = context;
        this.doodles = doodles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doodle_list_item, null);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Doodle doodle = doodles.get(position);

        ((SimpleViewHolder) holder).title.setText(doodle.getTitle());
        ((SimpleViewHolder) holder).content.setText(doodle.getContent());

        ((SimpleViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doodleClickListener.onDoodleClick(doodle);
            }
        });

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
        public View container;
        public TextView title;
        public TextView content;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            title = (TextView) itemView.findViewById(R.id.doodle_title);
            content = (TextView) itemView.findViewById(R.id.doodle_content);
        }
    }
}
