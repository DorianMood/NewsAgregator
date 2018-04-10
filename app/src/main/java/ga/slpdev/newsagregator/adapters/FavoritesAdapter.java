package ga.slpdev.newsagregator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.Crypto;
import ga.slpdev.newsagregator.utils.FavoritesDbHelper;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<News> favoritesListNews;
    FavoritesDbHelper helper;
    Context context;

    public FavoritesAdapter(ArrayList<News> __list, Context __context) {
        this.favoritesListNews = __list;
        this.context = __context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (helper == null) {
            helper = new FavoritesDbHelper(parent.getContext());
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_card, parent, false);
        return new FavoritesHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final News object = favoritesListNews.get(position);
        ((FavoritesHolder) holder).title.setText(object.getTitle());
    }

    @Override
    public int getItemCount() {
        return favoritesListNews.size();
    }

    private class FavoritesHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView favorite;
        private final ImageView share;

        public FavoritesHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            favorite = itemView.findViewById(R.id.favorite);
            share = itemView.findViewById(R.id.share);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String hash = Crypto.Sha1(favoritesListNews.get(getAdapterPosition()).getDescription());

                        helper.onDislike(hash);
                        helper.onDeleteNews(hash);
                        favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, favoritesListNews.get(getAdapterPosition()).getUrl());
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            });
        }
    }

}
