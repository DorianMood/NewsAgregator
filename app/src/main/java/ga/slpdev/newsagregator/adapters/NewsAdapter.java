package ga.slpdev.newsagregator.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.classes.News;

/**
 * Created by nikita on 22.03.2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<News> list;

    public NewsAdapter(Context context, ArrayList<News> list) {
        this.context = context;
        this.list = list;
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {
        private final CardView newsCard;
        private final TextView articlePreview;
        private final ImageView picture;

        public NewsHolder(View itemView) {
            super(itemView);
            newsCard = itemView.findViewById(R.id.card_view);
            articlePreview = itemView.findViewById(R.id.text_preview);
            picture = itemView.findViewById(R.id.image_preview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final News object = list.get(position);
        ((NewsHolder) holder).articlePreview.setText(object.getDescription());
        Glide.with(context).load(object.getUrlImage()).thumbnail(0.5f).into(((NewsHolder) holder).picture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
