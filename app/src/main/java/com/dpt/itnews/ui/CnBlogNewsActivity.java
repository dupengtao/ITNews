package com.dpt.itnews.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dpt.itnews.R;
import com.dpt.itnews.presenter.CnBlogPresenter;
import com.dpt.itnews.ui.widget.adapter.MarginDecoration;

public class CnBlogNewsActivity extends AppCompatActivity implements ICnBlogNewsDelegate{

    private CnBlogPresenter cnBlogPresenter;
    @Bind(R.id.rv_demo) RecyclerView mRvList;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cnBlogPresenter = new CnBlogPresenter(this);
        cnBlogPresenter.setCnBlogDelegate(this);
        initView();
        loadLaunchData();
    }

    private void loadLaunchData() {
        cnBlogPresenter.init();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        mRvList.addItemDecoration(new MarginDecoration(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_it_news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        cnBlogPresenter.unSubscribeCollection();
        super.onDestroy();
    }

    @Override
    public View getCnBlogListView() {
        return mRvList;
    }
}
