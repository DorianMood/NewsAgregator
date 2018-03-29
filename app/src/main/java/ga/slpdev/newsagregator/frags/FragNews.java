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

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.adapters.NewsAdapter;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.JSONParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FragNews extends Fragment {

    public static FragNews instance;
    View rootView;
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
        new LoadData().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LoadData extends AsyncTask<Void, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            ArrayList<News> newsList = new ArrayList<>();
            try {
                String url = String.format(getResources().getString(R.string.URI),
                        getResources().getString(R.string.API_KEY));

                Log.d("MY", "requesting " + url);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                // Read response here
                String response = client.newCall(request).execute().body().string();
                newsList = JSONParser.parseNews(response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return newsList;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);

            swipeRefreshLayout.setRefreshing(false);
            newsAdapter = new NewsAdapter(getActivity(), news);
            recyclerView.setAdapter(newsAdapter);
        }
    }
}
