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
    private final CnBlogApi cnBlogApi;

    public CnBlogEvents() {
        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();
    }

    public void loadRemoteRecentNewsList(int pageIndex, int pageSize) {
        getRemoteRecentNewsListObservable(pageIndex, pageSize).subscribe(new Action1<List<CnBlogNewsItemInfo>>() {
            @Override
            public void call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                //save in db
                LogHelper.e(TAG, "loadRemoteRecentNewsList # success call");
                saveNewsInDb(cnBlogNewsItemInfoList);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                LogHelper.e(TAG, "loadRemoteRecentNewsList # error call");
            }
        });
    }


    private Observable<List<CnBlogNewsItemInfo>> getRemoteRecentNewsListObservable(int pageIndex, int pageSize) {
        return cnBlogApi.getRecentNewsList(String.valueOf(pageIndex), String.valueOf(pageSize))
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<CnBlogNewsItemInfo>>() {
                    @Override
                    public List<CnBlogNewsItemInfo> call(String s) {
                        LogHelper.e(TAG, "loadRemoteRecentNewsList # map");
                        List<CnBlogNewsItemInfo> itemInfoList = null;
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
                });
    }

    private void saveNewsInDb(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {

        Observable.from(cnBlogNewsItemInfoList)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<CnBlogNewsItemInfo, Observable<CnBlogNewsItemInfo>>() {
                    @Override
                    public Observable<CnBlogNewsItemInfo> call(CnBlogNewsItemInfo cnBlogNewsItemInfo) {
                        LogHelper.e(TAG, "saveNewsInDb # flatMap");
                        return Observable.just(cnBlogNewsItemInfo);
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
                .buffer(cnBlogNewsItemInfoList.size())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        //TODO 更新 adapter 刷新ui
                    }
                })
                .subscribe(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        LogHelper.e(TAG, "saveNewsInDb # success call");
                        CnBlogDbHelper cnBlogDbHelper = new CnBlogDbHelper();
                        cnBlogDbHelper.getDaoSession().insertOrReplaceInTx(newses);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        LogHelper.e(TAG, "saveNewsInDb # failure call");
                    }
                });
    }

    public void getLoaclRecentNewsListObservable(int size){

        Observable.just(size)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, List<News>>() {
                    @Override
                    public List<News> call(Integer size) {
                        CnBlogDbHelper cnBlogDbHelper = new CnBlogDbHelper();
                        Query<News> build = cnBlogDbHelper.getDaoSession().queryBuilder().orderDesc(NewsDao.Properties.Published_ms).limit(size).build();
                        return build.list();
                    }
                })
                .subscribe(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        System.out.print(1);
                    }
                });

    }
}
