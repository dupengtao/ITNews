package com.dpt.itnews.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dpt.itnews.R;
import com.dpt.itnews.presenter.CnBlogPresenter;

/**
 * Created by dupengtao on 16/3/1.
 */
public class CnBlogArticleActivity extends AppCompatActivity{

    private CnBlogPresenter cnBlogPresenter;

    @Bind(R.id.rv_article) RecyclerView mRvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_it_news_article);
        ButterKnife.bind(this);
        cnBlogPresenter = new CnBlogPresenter(this);
    }

    @OnClick(R.id.btn_request)
    public void testRequest(){
        cnBlogPresenter.loadArticleById("539988",mRvArticle);
    }

    @Override
    protected void onDestroy() {
        cnBlogPresenter.unSubscribeCollection();
        super.onDestroy();
    }
}
