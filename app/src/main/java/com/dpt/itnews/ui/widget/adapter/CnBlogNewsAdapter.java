package com.dpt.itnews.ui.widget.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dpt.itnews.R;
import com.dpt.itnews.data.db.News;
import com.dpt.itnews.data.vo.CnBlogNewsContent;
import com.dpt.itnews.ui.widget.listener.OnBaseItemClickListener;

import java.util.LinkedList;
import java.util.List;

public final class CnBlogNewsAdapter extends RecyclerView.Adapter<CnBlogNewsAdapter.ViewHolder> {
    private List<News> mNewsList = new LinkedList<>();
    OnBaseItemClickListener itemClickListener;

    public CnBlogNewsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.setTitle(Html.fromHtml(news.getTitle()));
        holder.setSummary("        " + Html.fromHtml(news.getSummary()));
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public List<News> getNewsList() {
        return mNewsList;
    }

    public void setNewsList(List<News> newsList) {
        this.mNewsList = newsList;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.item_title)
        TextView mTvTitle;
        @Bind(R.id.item_summary)
        TextView mTvSummary;
        @Bind(R.id.rl_more_box)
        RelativeLayout mRlMoreBox;
        @Bind(R.id.rv_article)
        RecyclerView mRvArticle;
        private View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemView = itemView;
            setListener(mRlMoreBox);
            mRvArticle.setAdapter(new CnArticleAdapter(new CnBlogNewsContent()));
            mRvArticle.setLayoutManager(new LinearLayoutManager(mRvArticle.getContext(), LinearLayoutManager.VERTICAL, false));
            mRvArticle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                    //int action = e.getAction();
                    //switch (action) {
                    //    case MotionEvent.ACTION_MOVE:
                    //        break;
                    //}
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }

        private void setListener(View view) {
            view.setOnClickListener(this);
        }

        public void setTitle(CharSequence text) {
            mTvTitle.setText(text);
        }

        public void setSummary(CharSequence text) {
            mTvSummary.setText(text);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, mItemView, getAdapterPosition(), getItemCount());
            }
        }
    }

    public OnBaseItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnBaseItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
