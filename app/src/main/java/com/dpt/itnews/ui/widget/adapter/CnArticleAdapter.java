package com.dpt.itnews.ui.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dpt.itnews.R;
import com.dpt.itnews.data.vo.CnBlogNewsContent;
import com.dpt.itnews.data.vo.CnBlogNewsContentDetailItem;
import com.dpt.itnews.ui.widget.adapter.viewholder.CnArticleViewHolder;

/**
 * Created by dupengtao on 16/3/1.
 */
public class CnArticleAdapter extends RecyclerView.Adapter<CnArticleViewHolder>{

    private static final int ITEM_VIEW_TYPE_TEXT = 1;
    private static final int ITEM_VIEW_TYPE_IMAGE = 2;
    private CnBlogNewsContent mNewsContent;

    public CnArticleAdapter(CnBlogNewsContent newsContent) {
        mNewsContent=newsContent;
    }

    @Override
    public CnArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_TEXT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_text, parent, false);
            return new CnArticleViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_image, parent, false);
            return new CnArticleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(CnArticleViewHolder holder, int position) {
        CnBlogNewsContentDetailItem detailItem = mNewsContent.detialItemList.get(position);
        if(detailItem.type==1){// is text
            if(TextUtils.isEmpty(detailItem.text)){
                return;
            }
            holder.getItemText().setText("        "+Html.fromHtml(detailItem.text));
        }else if(detailItem.type==2){// is image
            if(TextUtils.isEmpty(detailItem.imageUrl)){
                return;
            }
            holder.getItemImage().setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsContent.detialItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isTextType(position) ? ITEM_VIEW_TYPE_TEXT : ITEM_VIEW_TYPE_IMAGE;
    }

    private boolean isTextType(int position) {
        CnBlogNewsContentDetailItem detailItem = mNewsContent.detialItemList.get(position);
        return detailItem.type==1;
    }

    public CnBlogNewsContent getNewsContent() {
        return mNewsContent;
    }

    public void setNewsContent(CnBlogNewsContent newsContent) {
        this.mNewsContent = newsContent;
    }
}
