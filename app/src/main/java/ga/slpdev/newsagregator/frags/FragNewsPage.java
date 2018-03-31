package ga.slpdev.newsagregator.frags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import ga.slpdev.newsagregator.R;


public class FragNewsPage extends Fragment {

    public static FragNewsPage instance;
    View rootView;
    ImageView imagePreview;
    TextView description;
    SwipeRefreshLayout swipeRefreshLayout;
    String url, urlImage, text;

    public FragNewsPage() {

    }

    public static FragNewsPage newInstance() {
        return new FragNewsPage();
    }

    public static FragNewsPage getInstance() {
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
