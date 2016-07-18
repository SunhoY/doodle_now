package io.harry.doodlenow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.wrapper.PicassoWrapper;

public class DoodleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Inject
    PicassoWrapper picassoWrapper;

    private final List<Doodle> doodles;
    private final Context context;

    private Picasso picasso;

    OnDoodleItemClickListener onDoodleItemClickListener;

    public void setOnDoodleClickListener(OnDoodleItemClickListener onDoodleItemClickListener) {
        this.onDoodleItemClickListener = onDoodleItemClickListener;
    }

    public interface OnDoodleItemClickListener {
        void onDoodleItemClick(Doodle doodle);
    }

    public DoodleListAdapter(Context context, List<Doodle> doodles) {
        ((DoodleApplication) context).getDoodleComponent().inject(this);
        picasso = picassoWrapper.getPicasso(context);

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
        ((SimpleViewHolder) holder).hoursElapsed.setText(doodle.getElapsedHours());
        if(TextUtils.isEmpty(doodle.getImageUrl())) {
            picasso.load(R.drawable.main_logo).into(((SimpleViewHolder) holder).preview);
        } else {
            picasso.load(doodle.getImageUrl()).into(((SimpleViewHolder) holder).preview);
        }

        ((SimpleViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDoodleItemClickListener != null) {
                    onDoodleItemClickListener.onDoodleItemClick(doodle);
                }
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
        public ImageView preview;
        public TextView hoursElapsed;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            title = (TextView) itemView.findViewById(R.id.doodle_title);
            content = (TextView) itemView.findViewById(R.id.doodle_content);
            preview = (ImageView) itemView.findViewById(R.id.doodle_preview);
            hoursElapsed = (TextView) itemView.findViewById(R.id.hours_elapsed);
        }
    }
}
