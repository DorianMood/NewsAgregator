package ga.slpdev.newsagregator.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ga.slpdev.newsagregator.R;
import ga.slpdev.newsagregator.frags.FragFavorites;
import ga.slpdev.newsagregator.frags.FragNews;
import ga.slpdev.newsagregator.frags.FragSettings;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getFragNews();
                    return true;
                case R.id.navigation_dashboard:
                    getFragSettings();
                    return true;
                case R.id.navigation_favorites:
                    getFragFavorites();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean dark = settings.getString("dark", "2").compareTo("1") == 0;


        Log.d("MainActivityDark", "theme " + dark);
        if (dark) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        getFragNews();
    }

    private void getFragNews() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, FragNews.getInstance());
        ft.commit();
    }
    private void getFragFavorites() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, FragFavorites.getInstance());
        ft.commit();
    }
    private void getFragSettings() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, FragSettings.getInstance());
        ft.commit();
    }
}
