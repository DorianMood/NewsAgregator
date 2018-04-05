package ga.slpdev.newsagregator.frags;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.activities.NewsItemActivity;
import ga.slpdev.newsagregator.adapters.NewsAdapter;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.FavoritesDbHelper;
import ga.slpdev.newsagregator.utils.JSONParser;
import ga.slpdev.newsagregator.utils.NewsParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class FragNews extends Fragment {

    enum Source { RUSSIA_TODAY, RBC, NEWS_API }

    private static FragNews instance;
    private View rootView;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FavoritesDbHelper helper;

    public FragNews() {
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
        rootView = inflater.inflate(R.layout.frag_news, container, false);
        recyclerView = rootView.findViewById(R.id.news_rv);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadData();

        return rootView;
    }

    public void loadData() {
        new LoadData(Source.RUSSIA_TODAY).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadData extends AsyncTask<Void, Void, ArrayList<News>> {
        private Source source;
        public LoadData(Source source) {
            super();
            this.source = source;
        }
        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            ArrayList<News> newsList = new ArrayList<>();
            try {
                switch (source) {
                    case RBC:
                        newsList.addAll(NewsParser.parseRBC());
                        break;
                    case RUSSIA_TODAY:
                        newsList.addAll(NewsParser.parseRT());
                        break;
                    case NEWS_API:
                        newsList.addAll(NewsParser.parseNewsAPI());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newsList;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);

            swipeRefreshLayout.setRefreshing(false);
            newsAdapter = new NewsAdapter(news);
            recyclerView.setAdapter(newsAdapter);
        }
    }
}
