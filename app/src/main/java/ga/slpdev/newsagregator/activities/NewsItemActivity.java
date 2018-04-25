package ga.slpdev.newsagregator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.Crypto;
import ga.slpdev.newsagregator.utils.FavoritesDbHelper;

public class NewsItemActivity extends AppCompatActivity {

    private Bundle arguments;
    ImageView imagePreview;
    TextView description;
    String url = "", urlImage = "", text = "";
    FavoritesDbHelper helper;
    boolean liked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imagePreview = findViewById(R.id.image_preview);
        description = findViewById(R.id.news_text);

        ImageView share = findViewById(R.id.share);
        final ImageView favorite = findViewById(R.id.favorite);

        setSupportActionBar(toolbar);


        arguments = getIntent().getBundleExtra("bundle");
        final News newsObject = (News) arguments.getSerializable("news");
        url = newsObject.getUrl();
        urlImage = newsObject.getUrlImage();
        text = newsObject.getDescription();

        description.setText(text);
        Glide.with(this).load(urlImage).thumbnail(0.5f).into(imagePreview);

        // Create database helper to work with favorites and save them
        helper = new FavoritesDbHelper(this);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });
        // BEGIN:  on favorite click
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String hash = Crypto.Sha1(text);

                    if (helper.isLiked(hash)) {
                        helper.onDislike(hash);
                        helper.onDeleteNews(hash);
                        favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        liked = false;
                    } else {
                        helper.onLike(hash);
                        helper.onAddNews(newsObject);
                        favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        liked = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // END: on favorite click

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String dark = settings.getString("dark", "2");


        Log.d("MainActivityDark", "theme " + dark);
        if (dark.compareTo("1") == 0) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

}
