package com.dpt.itnews.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.dpt.itnews.R;
import com.dpt.itnews.data.db.CnBlogDbHelper;
import com.dpt.itnews.data.db.News;
import com.dpt.itnews.data.net.RetrofitNetClient;
import com.dpt.itnews.data.net.api.CnBlogApi;
import com.dpt.itnews.data.net.parser.CnBlogNewsItemInfoParser;
import com.dpt.itnews.data.vo.CnBlogNewsContent;
import com.dpt.itnews.data.vo.CnBlogNewsContentDetailItem;
import com.dpt.itnews.data.vo.CnBlogNewsItemInfo;
import com.dpt.itnews.ui.ICnBlogNewsDelegate;
import com.dpt.itnews.ui.widget.ScrollingLinearLayoutManager;
import com.dpt.itnews.ui.widget.adapter.CnArticleAdapter;
import com.dpt.itnews.ui.widget.adapter.CnBlogNewsAdapter;
import com.dpt.itnews.ui.widget.listener.OnBaseItemClickListener;
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
public class CnBlogPresenter {

    private static final String TAG = "CnBlogEvents";
    private static final String NET_EXCEPTION = "NET_EXCEPTION";
    private CompositeSubscription compositeSubscription;
    private final CnBlogApi cnBlogApi;
    private final Set<News> adapterNewsSet = new LinkedHashSet<>();
    private final List<News> needSaveDbList = new ArrayList<>();
    private ICnBlogNewsDelegate mCnBlogNewsDelegate;
    private RecyclerView mCnBlogRv;
    private CnBlogNewsAdapter mCnBlogNewsAdapter;
    private int mHeightPixels;
    private ScrollingLinearLayoutManager mScrollingLinearLayoutManager;
    private ValueAnimator contentExpandAnim;
    private boolean isContentExpand;
    private ValueAnimator contentCloseAnim;
    private int selectItemHeight;

    public CnBlogPresenter(Context context) {
        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();
        if(mHeightPixels ==0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            mHeightPixels = dm.heightPixels;
        }
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
                        mCnBlogNewsAdapter.getNewsList().clear();
                        mCnBlogNewsAdapter.getNewsList().addAll(curNewsList);
                        mCnBlogNewsAdapter.notifyDataSetChanged();
                        //LogHelper.e(TAG, "save begin");
                        //saveLocalData(needSaveDbList);
                        //LogHelper.e(TAG, "save finish");
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

    public void loadArticleById(String id, final View itemView){
        cnBlogApi.getNewsContentById(id)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<CnBlogNewsContent>() {
                    @Override
                    public void call(CnBlogNewsContent cnBlogNewsContent) {
                        if (TextUtils.isEmpty(cnBlogNewsContent.Content)) {
                            return;
                        }
                        String[] texts = cnBlogNewsContent.Content.split("\r\n");
                        String[] images = cnBlogNewsContent.ImageUrl.split(";");
                        int imageOrder = 0;
                        for (int i = 0, j = texts.length; i < j; i++) {
                            String[] imgs = texts[i].split("<img");
                            if (imgs.length > 1) { // is image
                                for (int x = 1, y = imgs.length; x < y; x++) {
                                    CnBlogNewsContentDetailItem item = new CnBlogNewsContentDetailItem();
                                    item.imageUrl = images[imageOrder];
                                    item.type = 2;
                                    cnBlogNewsContent.detialItemList.add(item);
                                    imageOrder++;
                                }
                            } else {
                                CnBlogNewsContentDetailItem item = new CnBlogNewsContentDetailItem();
                                item.text = texts[i].replace("<p>","        ").replace("</p>","");
                                item.type = 1;
                                cnBlogNewsContent.detialItemList.add(item);
                            }
                        }
                        System.out.print(1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CnBlogNewsContent>() {
                    @Override
                    public void call(CnBlogNewsContent cnBlogNewsContent) {
                        setArticleResult(cnBlogNewsContent, itemView);
                    }
                });
    }

    private void setArticleResult(final CnBlogNewsContent cnBlogNewsContent, View itemView) {

        if(TextUtils.isEmpty(cnBlogNewsContent.Content)){
            return;
        }

        RecyclerView  itemRecyclerView = ButterKnife.findById(itemView, R.id.rv_article);
        CnArticleAdapter articleAdapter = (CnArticleAdapter) itemRecyclerView.getAdapter();
        articleAdapter.setNewsContent(cnBlogNewsContent);
        articleAdapter.notifyDataSetChanged();
        LogHelper.e(TAG, "CnBlogNewsContent -- " + cnBlogNewsContent.toString());
        mCnBlogNewsAdapter.notifyDataSetChanged();
        contentExpandAnim.start();
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

    public void loadNewsInfoById(String id) {
        //
    }

    public CompositeSubscription getCompositeSubscription() {

        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        return compositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(s);
    }

    public void unSubscribeCollection() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
        }
    }

    public void setCnBlogDelegate(ICnBlogNewsDelegate delegate) {
        mCnBlogNewsDelegate = delegate;
    }

    public void init() {
        initList();
    }

    private void initList() {
        mCnBlogRv = (RecyclerView) mCnBlogNewsDelegate.getCnBlogListView();
        mCnBlogNewsAdapter = new CnBlogNewsAdapter();
        mCnBlogRv.setAdapter(mCnBlogNewsAdapter);
        mScrollingLinearLayoutManager = new ScrollingLinearLayoutManager(mCnBlogRv.getContext(), LinearLayoutManager.VERTICAL, false, 200);
        mCnBlogRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogHelper.e(TAG, "onScrolled -- dy = " + dy);
            }
        });
        mCnBlogRv.setLayoutManager(mScrollingLinearLayoutManager);
        mCnBlogNewsAdapter.setItemClickListener(new OnBaseItemClickListener() {
            @Override
            public void onItemClick(View clickView, View itemView, int position, int count) {
                if(isContentExpand) {
                    contentCloseAnim.start();
                }else {
                    mCnBlogRv.smoothScrollToPosition(position);
                    changeItem(clickView, itemView, position, count);
                }
            }
        });
        loadNewsListByPageIndex(1, 50);
    }

    private void changeItem(View clickView, View itemView, int position, int count) {
        Toast.makeText(clickView.getContext(), "position -- " + position, Toast.LENGTH_SHORT).show();
        News news = mCnBlogNewsAdapter.getNewsList().get(position);
        int moveDistance=calculateMoveDistance(itemView);
        contentExpandAnim=initContentExpandAnim(itemView,moveDistance);
        contentCloseAnim=initContentCloseAnim(itemView,moveDistance,selectItemHeight);
        loadArticleById(news.getNews_id(), itemView);
    }

    private ValueAnimator initContentExpandAnim(final View itemView, int moveDistance) {
        final RelativeLayout contentRl=ButterKnife.findById(itemView, R.id.rl_content_box);
        final TextView itemSummary=ButterKnife.findById(itemView, R.id.item_summary);
        final RecyclerView  itemRecyclerView = ButterKnife.findById(itemView, R.id.rv_article);
        selectItemHeight=contentRl.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(contentRl.getHeight(), moveDistance);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = contentRl.getLayoutParams();
                layoutParams.height = val;
                contentRl.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                itemSummary.setVisibility(View.INVISIBLE);
                itemRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //mCnBlogRv.setLayoutFrozen(true);
                isContentExpand = true;
                LogHelper.e(TAG, "onAnimationEnd");
            }
        });
        return valueAnimator;
    }

    private ValueAnimator initContentCloseAnim(final View itemView, int moveDistance,int viewHeight) {
        final RelativeLayout contentRl=ButterKnife.findById(itemView, R.id.rl_content_box);
        final TextView itemSummary=ButterKnife.findById(itemView, R.id.item_summary);
        final RecyclerView  itemRecyclerView = ButterKnife.findById(itemView, R.id.rv_article);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(moveDistance,viewHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = contentRl.getLayoutParams();
                layoutParams.height = val;
                contentRl.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //mCnBlogRv.setLayoutFrozen(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isContentExpand = false;
                itemSummary.setVisibility(View.VISIBLE);
                itemRecyclerView.setVisibility(View.GONE);
                LogHelper.e(TAG, "onAnimationEnd");
            }
        });
        return valueAnimator;
    }

    private int calculateMoveDistance(View itemView) {
        RelativeLayout bottomRl=ButterKnife.findById(itemView, R.id.rl_more_box);
        RelativeLayout topRl=ButterKnife.findById(itemView, R.id.rl_top_box);
        int moveDistance = mHeightPixels - bottomRl.getHeight()-topRl.getHeight()-dip2px(itemView.getContext(), 112);
        return moveDistance;
    }


    protected int dip2px(Context context,float dpValue) {
        final float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
