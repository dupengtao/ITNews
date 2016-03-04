package com.dpt.itnews.ui.widget.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dpt.itnews.R;

/**
 * Created by dupengtao on 16/3/1.
 */
public class CnArticleViewHolder extends RecyclerView.ViewHolder{

    TextView mItemText;
    ImageView mItemImage;

    public CnArticleViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(itemView);
        mItemText= (TextView) itemView.findViewById(R.id.tv_item_article_text);
        mItemImage= (ImageView) itemView.findViewById(R.id.iv_item_article_image);
    }

    public TextView getItemText() {
        return mItemText;
    }

    public ImageView getItemImage() {
        return mItemImage;
    }
}
