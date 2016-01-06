package com.dpt.itnews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.dpt.itnews.data.net.RetrofitNetClient;
import com.dpt.itnews.data.net.api.CnBlogApi;
import com.dpt.itnews.demo.adapter.LargeAdapter;
import com.dpt.itnews.event.CnBlogEvents;

public class ItNewsListActivity extends AppCompatActivity {

    private CnBlogApi cnBlogApi;
    private String TAG = "ItNewsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_it_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //535122
                //cnBlogApi.getRecentNewsList("1", "20")
                //        .map(new Func1<String, List<CnBlogNewsItemInfo>>() {
                //            @Override
                //            public List<CnBlogNewsItemInfo> call(String s) {
                //                List<CnBlogNewsItemInfo> itemInfoList = null;
                //                try {
                //                    itemInfoList = CnBlogNewsItemInfoParser.parse(s);
                //                } catch (Exception e) {
                //                    e.printStackTrace();
                //                    itemInfoList = new ArrayList<>();
                //                }
                //                if(itemInfoList == null){
                //                    itemInfoList = new ArrayList<>();
                //                }
                //                return itemInfoList;
                //            }
                //        })
                //        .single(new Func1<List<CnBlogNewsItemInfo>, Boolean>() {
                //            @Override
                //            public Boolean call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                //                return cnBlogNewsItemInfoList.size()>0;
                //            }
                //        })
                //        .subscribeOn(Schedulers.newThread())
                //        .observeOn(AndroidSchedulers.mainThread())
                //        .subscribe(new Action1<List<CnBlogNewsItemInfo>>() {
                //            @Override
                //            public void call(List<CnBlogNewsItemInfo> cnBlogNewsItemInfoList) {
                //                System.out.print(1);
                //            }
                //        }, new Action1<Throwable>() {
                //            @Override
                //            public void call(Throwable throwable) {
                //                throwable.printStackTrace();
                //            }
                //        });

                CnBlogEvents cnBlogEvents = new CnBlogEvents();
                //cnBlogEvents.loadRemoteRecentNewsList(1,10);
                //cnBlogEvents.getLocalRecentNewsListObservable(3);
                cnBlogEvents.loadLastedRecentNewsList2(1,20);

            }
        });

        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();

        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_demo);
        recyclerView.setAdapter(LargeAdapter.newInstance(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //ArrayList<String> list = new ArrayList<>();
        //String format = getString(R.string.item_string);
        //for(int i=0;i<200;i++){
        //    list.add(String.format(format, i + 1));
        //}
        //
        //
        //ListView lv = (ListView) findViewById(R.id.lv_demo);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //        this,
        //        android.R.layout.simple_expandable_list_item_1,
        //        list);
        //lv.setAdapter(adapter);
        

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
}
