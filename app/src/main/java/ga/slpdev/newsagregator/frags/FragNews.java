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
import android.widget.Toast;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.adapters.NewsAdapter;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.NewsParser;


public class FragNews extends Fragment {

    enum Source { RUSSIA_TODAY, RBC, NEWS_API }

    private static FragNews instance;
    private View rootView;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

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

    class LoadData extends AsyncTask<Void, Void, ArrayList<News>> {
        private Source source;
        public LoadData(Source source) {
            super();
            this.source = source;
        }
        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            swipeRefreshLayout.setRefreshing(true);
            ArrayList<News> newsList = new ArrayList<>();
            try {
                switch (source) {
                    case RBC:
                        newsList = NewsParser.parseRBC();
                        break;
                    case RUSSIA_TODAY:
                        newsList = NewsParser.parseRT();
                        break;
                    case NEWS_API:
                        newsList = NewsParser.parseNewsAPI();
                        break;
                }
            } catch (Exception e) {
                Log.d("LoadNews", e.getMessage());
            }
            return newsList;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);

            Log.d("LoadNews", String.format("News amount : %s", news.size()));

            swipeRefreshLayout.setRefreshing(false);
            newsAdapter = new NewsAdapter(news);
            recyclerView.setAdapter(newsAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newsAdapter != null) {
            newsAdapter.notifyItemChanged(newsAdapter.clicked);
        }
    }
}
