package ga.slpdev.newsagregator.utils;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ga.slpdev.newsagregator.classes.News;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NewsParser {
    private static final String RBC = "https://www.rbc.ru/";
    private static final String RUSSIA_TODAY = "https://russian.rt.com";

    public static ArrayList<News> parseRT() throws IOException {
        ArrayList<News> list = new ArrayList<>();

        Document doc = Jsoup.connect(String.format("%s/news", RUSSIA_TODAY)).get();
        Elements links = doc.select(".card__heading > .link");

        for (Element link : links) {
            String url = String.format("%s%s", RUSSIA_TODAY, link.attr("href"));
            doc = Jsoup.connect(url).get();
            Log.d("RUSSIA_TODAY", doc.title());
            try {
                News item = new News("rt", "Russia Today", "", doc.title(),
                        String.format("%s\n%s\n%s", doc.select(".article__heading").get(0).text(),
                                doc.select(".article__summary").get(0).text(),
                                doc.select(".article__text").get(0).text()),
                        url,
                        doc.select(".article__cover > img").size() != 0 ?
                                doc.select(".article__cover > img").get(0).attr("src") :
                                "https://cdn-st1.rtr-vesti.ru/p/xw_1092152.jpg",
                        doc.select(".article__date > .date").get(0).text()
                );
                list.add(item);
            } catch(Exception e) {
                Log.d("RUSSIA_TODAY", e.getMessage());
            }
        }
        return list;
    }
    public static ArrayList<News> parseRBC() throws IOException {
        ArrayList<News> list = new ArrayList<>();

        Document doc = Jsoup.connect(RBC).get();
        List<Element> links = doc.select(".item > a").subList(0, 20);

        System.out.println(links.size());

        for (Element link : links) {
            if (!link.attr("href").contains("rbc") ||
                    link.attr("href").contains("photoreport") ||
                    link.attr("href").contains("style") ||
                    link.attr("href").contains("pink"))
                continue;

            doc = Jsoup.connect(link.attr("href")).get();

            News item = new News("rbc", "RBC",
                    doc.select(".article__authors__name").size() == 0 ? "" :
                            doc.select(".article__authors__name").get(0).text(),
                    doc.title(),
                    doc.select(".article__content").get(0).text(),
                    link.attr("href"),
                    doc.select(".article__main-image__image").size() == 0 ?
                            "http://nerabotaetsite.ru/wp-content/uploads/2016/01/rbk.jpg" :
                            doc.select(".article__main-image__image").get(0).attr("src"),
                    doc.select(".article__header__date").get(0).text()
            );
            list.add(item);
        }

        return list;
    }
    public static ArrayList<News> parseNewsAPI() throws IOException {
        String url = String.format("https://newsapi.org/v2/top-headlines?country=ru&apiKey=%s",
                "0d5bac486d414684a532ea0ae31ef856");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Read response here
        String json = client.newCall(request).execute().body().string();

        ArrayList<News> list = new ArrayList<>();
        try {
            JSONObject wholeObject = new JSONObject(json);
            JSONArray articles = wholeObject.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject news = articles.getJSONObject(i);

                Log.d("Parsing", news.getString("title"));

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
