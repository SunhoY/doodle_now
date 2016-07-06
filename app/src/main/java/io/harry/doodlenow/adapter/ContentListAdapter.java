package io.harry.doodlenow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.harry.doodlenow.R;

public class ContentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> items;
    private final Context context;

    public ContentListAdapter(Context context, List<String> contentList) {
        this.context = context;
        items = contentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, null);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String content = items.get(position);

        ((SimpleViewHolder) holder).content.setText(content);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void refreshContents(List<String> contentList) {
        items.clear();
        items.addAll(contentList);

        notifyDataSetChanged();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
