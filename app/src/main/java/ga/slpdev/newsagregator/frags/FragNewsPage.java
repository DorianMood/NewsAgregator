package ga.slpdev.newsagregator.frags;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.adapters.NewsAdapter;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.JSONParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FragNewsPage extends Fragment {

    public static FragNews instance;
    View rootView;
    ImageView imagePreview;
    TextView description;
    SwipeRefreshLayout swipeRefreshLayout;
    String url, urlImage, text;

    public FragNewsPage() {

    }

    public static FragNews newInstance() {
        return new FragNews();
    }

    public static FragNews getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = newInstance();
            return instance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.news_page, container, false);
        imagePreview = rootView.findViewById(R.id.image_preview);
        description = rootView.findViewById(R.id.news_text);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        return rootView;
    }

}
