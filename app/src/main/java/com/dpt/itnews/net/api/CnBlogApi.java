package com.dpt.itnews.net.api;

import com.dpt.itnews.vo.CnBlogNewsContent;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by dupengtao on 15/12/17.
 */
public interface CnBlogApi {

    //http://wcf.open.cnblogs.com/news/recent/paged/{PAGEINDEX}/{PAGESIZE}
    //headers.put("Accept", "application/json");
    //@Headers("Accept: application/json")
    @GET("recent/paged/{pageIndex}/{pageSize}")
    Observable<String> getRecentNewsList(@Path("pageIndex") String pageIndex,@Path("pageSize") String pageSize);

    //http://wcf.open.cnblogs.com/news/item/{CONTENTID}
    @Headers("Accept: application/json")
    @GET("item/{contentId}")
    Call<CnBlogNewsContent> getNewsContentById(@Path("contentId") String id);
}
