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
    private static int index=1;
    private CnBlogEvents cnBlogEvents;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_it_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("xxxxx");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        cnBlogEvents = new CnBlogEvents();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //cnBlogEvents.loadLastedRecentNewsList(2,3);
                cnBlogEvents.loadNewsListByPageIndex(index++, 3);
            }
        });

        cnBlogApi = RetrofitNetClient.getInstance().getCnBlogApi();

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rv_demo);
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

    @Override
    protected void onDestroy() {
        cnBlogEvents.unSubscribeCollection();
        super.onDestroy();
    }
}
