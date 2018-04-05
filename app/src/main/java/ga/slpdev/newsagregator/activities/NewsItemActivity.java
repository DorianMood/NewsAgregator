package ga.slpdev.newsagregator.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ga.slpdev.newsagregator.R;

public class NewsItemActivity extends AppCompatActivity {

    private Bundle arguments;
    ImageView imagePreview;
    TextView description;
    String url = "", urlImage = "", text = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imagePreview = findViewById(R.id.image_preview);
        description = findViewById(R.id.news_text);
        setSupportActionBar(toolbar);

        Toast.makeText(this, "LOL", Toast.LENGTH_SHORT).show();

        arguments = getIntent().getBundleExtra("bundle");
        url = arguments.getString("url");
        urlImage = arguments.getString("urlImage");
        text = arguments.getString("description");

        description.setText(text);
        Glide.with(this).load(urlImage).thumbnail(0.5f).into(imagePreview);

    }

}
