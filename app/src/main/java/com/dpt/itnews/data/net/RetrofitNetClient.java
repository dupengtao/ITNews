package com.dpt.itnews.data.net;

import com.dpt.itnews.data.net.api.CnBlogApi;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

import java.io.IOException;

/**
 * Created by dupengtao on 15/12/17.
 */
public class RetrofitNetClient {
    private static RetrofitNetClient ourInstance = new RetrofitNetClient();
    private CnBlogApi cnBlogApi;

    public static RetrofitNetClient getInstance() {
        return ourInstance;
    }

    private RetrofitNetClient() {
        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://wcf.open.cnblogs.com/news/")
                //.addConverter(String.class, new ToStringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        cnBlogApi = retrofit.create(CnBlogApi.class);
    }

    public CnBlogApi getCnBlogApi() {
        return cnBlogApi;
    }
}
