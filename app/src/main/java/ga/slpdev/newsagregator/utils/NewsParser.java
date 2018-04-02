package ga.slpdev.newsagregator.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ga.slpdev.newsagregator.classes.News;

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
}
