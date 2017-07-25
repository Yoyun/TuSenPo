package com.yoyun.tusenpo.data.remote;

import android.text.TextUtils;

import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.data.model.Photo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class Api {

    private static Api instance;

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public Observable<List<Article>> obtainArticles(final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Article>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Article>> e) throws Exception {

                Document document = Jsoup.connect("https://www.doutula.com/article/list")
                        .data("page", String.valueOf(page))
                        .get();

                List<Article> articles = new ArrayList<>();

                Elements elements = document.select("#home > div > div.col-sm-9 > a");
                for (Element ele : elements) {
                    String pageUrl = ele.attr("href");
                    pageUrl = toHttps(pageUrl);
                    String title = ele.select("div.random_title").get(0).text();
                    String date = ele.select("div.random_title > div.date").get(0).text();
                    title = title.replace(date, "").trim();
                    title = filter(title);
                    List<Photo> photos = new ArrayList<>();
                    Elements elements1 = ele.select("div.random_article > div");
                    for (Element ele1 : elements1) {
                        String imgUrl = ele1.select("img.img-responsive").get(0).attr("data-original");
                        imgUrl = toHttps(imgUrl);
                        String describe = ele1.select("p").get(0).text();
                        describe = filter(describe);
                        photos.add(new Photo().setImgUrl(imgUrl).setDescribe(describe));
                    }

                    articles.add(new Article().setPageUrl(pageUrl).setTitle(title).setDate(date).setPhotos(photos));
                }

                e.onNext(articles);
                e.onComplete();
            }
        });
    }

    public Observable<Article> obtainArticleDetail(final String pageUrl) {
        return Observable.create(new ObservableOnSubscribe<Article>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Article> e) throws Exception {
                Document document = Jsoup.connect(pageUrl).get();

                Element titleElement = document.select("body > div.container_ > div.container > div > div.col-sm-9 > li > div.pic-title > h1 > a").get(0);
                String pageUrl = titleElement.attr("href");
                pageUrl = toHttps(pageUrl);
                String title = titleElement.text();
                title = filter(title);

                Element dateElement = document.select("body > div.container_ > div.container > div > div.col-sm-9 > li > div.pic-title > div > span").get(0);
                String date = dateElement.text();

                List<Photo> photos = new ArrayList<>();
                Elements photoElements = document.select("body > div.container_ > div.container > div > div.col-sm-9 > li > div.pic-content > div");
                for (Element element : photoElements) {
                    Elements elements = element.select("table > tbody > tr:nth-child(1) > td > a > img");
                    if (elements == null || elements.size() == 0) {
                        continue;
                    }
                    Element ele = elements.get(0);
                    String imgUrl = ele.attr("src");
                    imgUrl = toHttps(imgUrl);
                    String describe = ele.attr("alt");
                    photos.add(new Photo().setImgUrl(imgUrl).setDescribe(describe));

                }
                Article article = new Article().setPageUrl(pageUrl).setTitle(title).setDate(date).setPhotos(photos);

                e.onNext(article);
                e.onComplete();
            }
        });
    }

    public Observable<List<Photo>> obtainPhotos(final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Photo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Photo>> e) throws Exception {
                Document document = Jsoup.connect("https://www.doutula.com/photo/list")
                        .data("page", String.valueOf(page))
                        .get();

                List<Photo> photos = new ArrayList<>();
                Elements photoElements = document.select("#pic-detail > div > div.col-sm-9 > div.random_picture > ul > li:nth-child(1) > div > div > a > img.img-responsive");
                for (Element element : photoElements) {
                    String imgUrl = element.attr("data-original");
                    if (TextUtils.isEmpty(imgUrl)) {
                        continue;
                    }
                    imgUrl = toHttps(imgUrl);
                    String describe = element.attr("alt");
                    describe = filter(describe);
                    photos.add(new Photo().setImgUrl(imgUrl).setDescribe(describe));
                }

                e.onNext(photos);
                e.onComplete();
            }
        });
    }

    public Observable<List<Photo>> searchPhotos(final String keyword, final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Photo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Photo>> e) throws Exception {

                Map<String, String> datas = new HashMap<>();
                datas.put("type", "photo");
                datas.put("more", String.valueOf(1));
                datas.put("keyword", keyword);
                datas.put("page", String.valueOf(page));

                Document document = Jsoup.connect("https://www.doutula.com/search")
                        .data(datas)
                        .get();

                List<Photo> photos = new ArrayList<>();
                Elements photoElements = document.select("#search-result-page > div > div > div:nth-child(2) > div > div.search-result.list-group-item > div > div > a");
                for (Element element : photoElements) {
                    String imgUrl = element.select("img.img-responsive").get(0).attr("data-original");
                    if (TextUtils.isEmpty(imgUrl)) {
                        continue;
                    }
                    imgUrl = toHttps(imgUrl);
                    String describe = element.select("p").get(0).text();
                    describe = filter(describe);
                    photos.add(new Photo().setImgUrl(imgUrl).setDescribe(describe));
                }

                e.onNext(photos);
                e.onComplete();

            }
        });
    }

    public Observable<List<Article>> searchArticles(final String keyword, final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Article>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Article>> e) throws Exception {

                Map<String, String> datas = new HashMap<>();
                datas.put("type", "article");
                datas.put("more", String.valueOf(1));
                datas.put("keyword", keyword);
                datas.put("page", String.valueOf(page));

                Document document = Jsoup.connect("https://www.doutula.com/search")
                        .data(datas)
                        .get();

                List<Article> articles = new ArrayList<>();
                Elements articleElements = document.select("#search-result-page > div > div > div:nth-child(2) > div > div.search-result.article > div > div");
                for (Element element : articleElements) {
                    Elements eles = element.select("a");
                    if (eles == null || eles.size() == 0) {
                        continue;
                    }
                    Element aEle = eles.get(0);
                    String pageUrl = aEle.attr("href");
                    pageUrl = toHttps(pageUrl);
                    String imgUrl = aEle.select("img.img-responsive").get(0).attr("data-original");
                    imgUrl = toHttps(imgUrl);
                    String describe = aEle.select("p").get(0).text();
                    describe = filter(describe);

                    List<Photo> photos = new ArrayList<>();
                    photos.add(new Photo().setImgUrl(imgUrl).setDescribe(describe));
                    articles.add(new Article().setTitle(describe).setPageUrl(pageUrl).setPhotos(photos));
                }

                e.onNext(articles);
                e.onComplete();

            }
        });
    }

    public String toHttps(String url) {
        if (url == null) {
            return null;
        }
        if (url.indexOf("https:") == 0) {
            return url;
        } else if (url.indexOf("http:") == 0) {
            return url.replace("http:", "https:");
        } else {
            return "https:" + url;
        }
    }

    private String filter(String string) {

        return string;
    }
}
