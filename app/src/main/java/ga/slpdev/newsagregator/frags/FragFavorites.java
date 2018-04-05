package ga.slpdev.newsagregator.frags;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.adapters.FavoritesAdapter;
import ga.slpdev.newsagregator.classes.News;
import ga.slpdev.newsagregator.utils.FavoritesDbHelper;

public class FragFavorites extends Fragment {

    public static FragFavorites instance;
    View rootView;
    RecyclerView recyclerView;
    SwipeRefreshLayout favoritesRefresh;
    FavoritesAdapter favoritesAdapter;

    public FragFavorites() {

    }

    public static FragFavorites newInstance() {
        return new FragFavorites();
    }

    public static FragFavorites getInstance() {
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
        rootView = inflater.inflate(R.layout.frag_favorites, container, false);
        recyclerView = rootView.findViewById(R.id.rv_favorites);
        favoritesRefresh = rootView.findViewById(R.id.favorites_refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        favoritesRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favoritesRefresh.setRefreshing(true);
                loadData();
            }
        });

        loadData();

        return rootView;
    }

    public void loadData() {
        new FragFavorites.LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Void, ArrayList<News>> {
        public LoadData() {
            super();
        }
        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            FavoritesDbHelper helper = new FavoritesDbHelper(getContext());
            return helper.getAllNews();
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);

            favoritesAdapter = new FavoritesAdapter(news);
            recyclerView.setAdapter(favoritesAdapter);
            favoritesRefresh.setRefreshing(false);
        }
    }
}
