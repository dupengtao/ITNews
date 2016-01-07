package com.dpt.itnews.event;

import com.dpt.itnews.data.db.CnBlogDbHelper;
import com.dpt.itnews.data.db.News;
import com.dpt.itnews.data.db.NewsDao;
import com.dpt.itnews.data.net.RetrofitNetClient;
import com.dpt.itnews.data.net.api.CnBlogApi;
import com.dpt.itnews.data.net.parser.CnBlogNewsItemInfoParser;
import com.dpt.itnews.data.vo.CnBlogNewsItemInfo;
import com.dpt.itnews.util.LogHelper;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dupengtao on 15/12/19.
 */
public class CnBlogEvents {

    private static final String TAG = "CnBlogEvents";
    private static final String NET_EXCEPTION = "NET_EXCEPTION";
    private final CnBlogApi cnBlogApi;

    public CnBlogEvents() {
        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();
    }


    public void loadLastedRecentNewsList(int pageIndex, final int pageSize) {
        cnBlogApi.getRecentNewsList(String.valueOf(pageIndex), String.valueOf(pageSize))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onExceptionResumeNext(Observable.just(NET_EXCEPTION))
                .map(new Func1<String, List<CnBlogNewsItemInfo>>() {
                    @Override
                    public List<CnBlogNewsItemInfo> call(String s) {
                        LogHelper.e(TAG, "loadRemoteRecentNewsList # map");
                        List<CnBlogNewsItemInfo> itemInfoList = null;
                        if (NET_EXCEPTION.equals(s)) {
                            itemInfoList = new ArrayList<>();
                            return itemInfoList;
                        }
                        try {
                            itemInfoList = CnBlogNewsItemInfoParser.parse(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (itemInfoList == null) {
                            itemInfoList = new ArrayList<>();
                        }
                        return itemInfoList;
                    }
                })
                .flatMap(new Func1<List<CnBlogNewsItemInfo>, Observable<CnBlogNewsItemInfo>>() {
                    @Override
                    public Observable<CnBlogNewsItemInfo> call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {

                        return Observable.from(cnBlogNewsItemInfoList);
                    }
                })
                .map(new Func1<CnBlogNewsItemInfo, News>() {
                    @Override
                    public News call(CnBlogNewsItemInfo cnBlogNewsItemInfo) {
                        LogHelper.e(TAG, "saveNewsInDb # map");
                        String id = cnBlogNewsItemInfo.id;
                        String link = cnBlogNewsItemInfo.link;
                        String published = cnBlogNewsItemInfo.published;
                        String sourceName = cnBlogNewsItemInfo.sourceName;
                        String summary = cnBlogNewsItemInfo.summary;
                        String title = cnBlogNewsItemInfo.title;
                        String topicIcon = cnBlogNewsItemInfo.topicIcon;
                        long timeMs = cnBlogNewsItemInfo.getTimeMs();

                        News news = new News();
                        news.setNews_id(id);
                        news.setTitle(title);
                        news.setLink(link);
                        news.setSummary(summary);
                        news.setPublished(published);
                        news.setSource_name(sourceName);
                        news.setTopicIcon(topicIcon);
                        news.setPublished_ms(timeMs);
                        return news;
                    }
                })
                .toList()
                .doOnNext(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        if (newses.size() > 0) {
                            CnBlogDbHelper cnBlogDbHelper = new CnBlogDbHelper();
                            cnBlogDbHelper.getDaoSession().insertOrReplaceInTx(newses);
                        }
                    }
                })
                .map(new Func1<List<News>, List<News>>() {
                    @Override
                    public List<News> call(List<News> newses) {
                        CnBlogDbHelper cnBlogDbHelper = new CnBlogDbHelper();
                        Query<News> build = cnBlogDbHelper.getDaoSession().queryBuilder().orderDesc(NewsDao.Properties.Published_ms).limit(pageSize).build();
                        return build.list();
                    }
                })
                .subscribe(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        LogHelper.e(TAG, "subscribe success call");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogHelper.e(TAG, "subscribe failure call");
                        throwable.printStackTrace();
                    }
                });
    }
}
