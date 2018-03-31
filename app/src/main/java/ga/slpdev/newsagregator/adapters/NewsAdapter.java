package ga.slpdev.newsagregator.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.frags.FragNewsPage;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<News> list;

    public NewsAdapter(Context context, ArrayList<News> list) {
        this.context = context;
        this.list = list;
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {
        private final TextView articlePreview;
        private final ImageView picture;
        private final ImageView more, share, favorite;
        private final ArrayList<News> newsObject;

        public NewsHolder(View itemView, final Context context, ArrayList<News> __newsObject) {
            super(itemView);
            articlePreview = itemView.findViewById(R.id.text_preview);
            picture = itemView.findViewById(R.id.image_preview);
            newsObject = __newsObject;

            Log.d("NewsAdapter", "" + getAdapterPosition());

            more = itemView.findViewById(R.id.more);
            share = itemView.findViewById(R.id.share);
            favorite = itemView.findViewById(R.id.favorite);

            // BEGIN: on more click
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(context, "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        Bundle b = new Bundle();

                        b.putString("urlImage", newsObject.get(getAdapterPosition()).getUrlImage());
                        b.putString("url", newsObject.get(getAdapterPosition()).getUrl());
                        b.putString("text", newsObject.get(getAdapterPosition()).getDescription());

                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        FragNewsPage fragment = new FragNewsPage();
                        fragment.setArguments(b);

                        ft.replace(R.id.content, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //END: on more click

            // BEGIN:  on favorite click
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        switch ((int) (favorite.getTag() == null ? 0 : favorite.getTag())) {
                            case R.drawable.ic_favorite_black_24dp:
                                favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                favorite.setTag(R.drawable.ic_favorite_border_black_24dp);
                                break;
                            case R.drawable.ic_favorite_border_black_24dp:
                                favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                favorite.setTag(R.drawable.ic_favorite_black_24dp);
                                break;
                            default:
                                favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                favorite.setTag(R.drawable.ic_favorite_black_24dp);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // END: on favorite click
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new NewsHolder(v, context, list);
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
