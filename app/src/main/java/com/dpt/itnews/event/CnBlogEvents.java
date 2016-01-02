package com.dpt.itnews.event;

import android.util.Log;
import com.dpt.itnews.data.net.RetrofitNetClient;
import com.dpt.itnews.data.net.api.CnBlogApi;
import com.dpt.itnews.data.net.parser.CnBlogNewsItemInfoParser;
import com.dpt.itnews.data.vo.CnBlogNewsItemInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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

    public void loadRecentNewsList(int pageIndex , int pageSize){
        cnBlogApi.getRecentNewsList(String.valueOf(pageIndex), String.valueOf(pageSize))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<CnBlogNewsItemInfo>>() {
                    @Override
                    public List<CnBlogNewsItemInfo> call(String s) {
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
                })
                .single(new Func1<List<CnBlogNewsItemInfo>, Boolean>() {
                    @Override
                    public Boolean call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                        return cnBlogNewsItemInfoList.size() > 0;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //TODO 开始加载
                        Log.e(TAG,"start load list");
                    }
                })
                .subscribe(new Action1<List<CnBlogNewsItemInfo>>() {
                    @Override
                    public void call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                        System.out.print(1);
                        Log.e(TAG, "success load list");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.e(TAG, "failure load list");
                    }
                });
    }
}
