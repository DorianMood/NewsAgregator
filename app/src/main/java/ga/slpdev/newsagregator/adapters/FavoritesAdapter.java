package ga.slpdev.newsagregator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.classes.News;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<News> favoritesListNews;


    public FavoritesAdapter(Context __context, ArrayList<News> __list) {
        this.context = __context;
        this.favoritesListNews = __list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_card, parent, false);
        return new FavoritesHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final News object = favoritesListNews.get(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class FavoritesHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView favorite;

        public FavoritesHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            favorite = itemView.findViewById(R.id.favorite);


        }
    }

}
