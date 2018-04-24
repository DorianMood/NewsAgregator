package ga.slpdev.newsagregator.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.activities.NewsItemActivity;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.Crypto;
import ga.slpdev.newsagregator.utils.FavoritesDbHelper;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> list;
    private static boolean[] liked;
    private static FavoritesDbHelper helper;

    public NewsAdapter(ArrayList<News> list) {
        this.list = list;
        liked = new boolean[list.size()];
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {
        private final TextView articlePreview;
        private final ImageView picture;
        private final ImageView more, share, favorite;
        private final ArrayList<News> newsObject;

        private final Context context;

        public NewsHolder(View itemView, ArrayList<News> __newsObject) {
            super(itemView);
            context = itemView.getContext();

            articlePreview = itemView.findViewById(R.id.text_preview);
            picture = itemView.findViewById(R.id.image_preview);
            newsObject = __newsObject;

            more = itemView.findViewById(R.id.more);
            share = itemView.findViewById(R.id.share);
            favorite = itemView.findViewById(R.id.favorite);

            ShareActionProvider shareActionProvider;

            // BEGIN: on more click
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(context, "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                        Bundle b = new Bundle();

                        b.putString("urlImage", newsObject.get(getAdapterPosition()).getUrlImage());
                        b.putString("url", newsObject.get(getAdapterPosition()).getUrl());
                        b.putString("description", newsObject.get(getAdapterPosition()).getDescription());
                        // TODO: make it better then now.
                        ArrayList<News> serializableList = new ArrayList<>();
                        serializableList.add(newsObject.get(getAdapterPosition()));
                        b.putSerializable("news", serializableList);

                        Intent intent = new Intent(context, NewsItemActivity.class);
                        intent.putExtra("bundle", b);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // END: on more click

            // BEGIN:  on favorite click
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String hash = Crypto.Sha1(newsObject.get(getAdapterPosition()).getDescription());

                        if (helper.isLiked(hash)) {
                            helper.onDislike(hash);
                            helper.onDeleteNews(hash);
                            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            liked[getAdapterPosition()] = false;
                        } else {
                            helper.onLike(hash);
                            helper.onAddNews(newsObject.get(getAdapterPosition()));
                            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            liked[getAdapterPosition()] = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // END: on favorite click

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, newsObject.get(getAdapterPosition()).getUrl());
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (helper == null) {
            helper = new FavoritesDbHelper(parent.getContext());
        }
        Log.d("News", helper.getAllNews().size() + "");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);
        return new NewsHolder(v, list);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final News object = list.get(position);
        ((NewsHolder) holder).articlePreview.setText(object.getDescription());
        if (helper.isLiked(Crypto.Sha1(object.getDescription()))) {//liked[position]) {
            ((NewsHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            ((NewsHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        Glide.with(((NewsHolder) holder).context).load(object.getUrlImage()).thumbnail(0.5f).into(((NewsHolder) holder).picture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
