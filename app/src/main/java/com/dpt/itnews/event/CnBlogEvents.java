package com.dpt.itnews.event;

import com.dpt.itnews.data.db.CnBlogDbHelper;
import com.dpt.itnews.data.db.News;
import com.dpt.itnews.data.net.RetrofitNetClient;
import com.dpt.itnews.data.net.api.CnBlogApi;
import com.dpt.itnews.data.net.parser.CnBlogNewsItemInfoParser;
import com.dpt.itnews.data.vo.CnBlogNewsItemInfo;
import com.dpt.itnews.util.LogHelper;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dupengtao on 15/12/19.
 */
public class CnBlogEvents {

    private static final String TAG = "CnBlogEvents";
    private static final String NET_EXCEPTION = "NET_EXCEPTION";
    private CompositeSubscription compositeSubscription ;
    private final CnBlogApi cnBlogApi;
    private final Set<News> adapterNewsSet = new LinkedHashSet<>();
    private final List<News> needSaveDbList = new ArrayList<>();

    public CnBlogEvents() {
        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();
    }

    public void loadNewsListByPageIndex(final int pageIndex, final int pageSize) {
        Subscription subscribe = cnBlogApi.getRecentNewsList(String.valueOf(pageIndex), String.valueOf(pageSize))
                .subscribeOn(Schedulers.io())
                .onExceptionResumeNext(Observable.just(NET_EXCEPTION))
                .map(new Func1<String, List<CnBlogNewsItemInfo>>() {
                    @Override
                    public List<CnBlogNewsItemInfo> call(String s) {
                        LogHelper.e(TAG, "loadRemoteRecentNewsList # map " + Thread.currentThread().getName());
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
                .concatMap(new Func1<List<CnBlogNewsItemInfo>, Observable<CnBlogNewsItemInfo>>() {
                    @Override
                    public Observable<CnBlogNewsItemInfo> call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                        return Observable.from(cnBlogNewsItemInfoList);
                    }
                })
                .map(new Func1<CnBlogNewsItemInfo, News>() {
                    @Override
                    public News call(CnBlogNewsItemInfo cnBlogNewsItemInfo) {
                        LogHelper.e(TAG, "saveNewsInDb # map " + Thread.currentThread().getName());
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
                .map(new Func1<List<News>, Set<News>>() {
                    @Override
                    public Set<News> call(List<News> remoteList) {
                        adapterNewsSet.addAll(remoteList);
                        needSaveDbList.clear();
                        needSaveDbList.addAll(remoteList);
                        return adapterNewsSet;
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Set<News>>() {
                    @Override
                    public void call(Set<News> curNewsList) {
                        LogHelper.e(TAG, "loadNewsListByPageIndex success call -" + Thread.currentThread().getName());
                        LogHelper.e(TAG, "loadNewsListByPageIndex success list size  -" + curNewsList.size());
                        LogHelper.e(TAG, "save begin");
                        saveLocalData(needSaveDbList);
                        LogHelper.e(TAG, "save finish");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogHelper.e(TAG, "loadNewsListByPageIndex failure call -" + Thread.currentThread().getName());
                        throwable.printStackTrace();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        LogHelper.e(TAG, "loadNewsListByPageIndex complete call -" + Thread.currentThread().getName());
                    }
                });

        addSubscription(subscribe);
    }


    private void saveLocalData(List<News> needSaveDbList) {
        Subscription subscribe = Observable.just(needSaveDbList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newses) {
                        if (newses.size() > 0) {
                            LogHelper.e(TAG, "saveLocalData thread " + Thread.currentThread().getName());
                            CnBlogDbHelper cnBlogDbHelper = new CnBlogDbHelper();
                            cnBlogDbHelper.getDaoSession().insertOrReplaceInTx(newses);
                        }
                    }
                });
        addSubscription(subscribe);
    }

    public CompositeSubscription getCompositeSubscription() {

        if (compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
        }
        return compositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (compositeSubscription == null) {
            compositeSubscription= new CompositeSubscription();
        }
        compositeSubscription.add(s);
    }

    public void unSubscribeCollection(){
        if (compositeSubscription!=null&&compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
        }
    }
}
