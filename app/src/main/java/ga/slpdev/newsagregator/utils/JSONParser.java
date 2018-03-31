package ga.slpdev.newsagregator.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ga.slpdev.newsagregator.classes.News;


@SuppressWarnings("DefaultFileTemplate")
public class JSONParser {
    public static ArrayList<News> parseNews(String json) {
        ArrayList<News> list = new ArrayList<>();
        try {
            JSONObject wholeObject = new JSONObject(json);
            JSONArray articles = wholeObject.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject news = articles.getJSONObject(i);

                list.add(new News(
                        news.getJSONObject("source").getString("id"),
                        news.getJSONObject("source").getString("name"),
                        news.getString("author"),
                        news.getString("title"),
                        news.getString("description"),
                        news.getString("url"),
                        news.getString("urlToImage"),
                        news.getString("publishedAt")
                ));
                Log.d("MY", list.get(i).getSourceName() + " " + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
