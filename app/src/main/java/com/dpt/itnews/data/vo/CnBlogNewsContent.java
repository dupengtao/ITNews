package com.dpt.itnews.data.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dupengtao on 15/12/18.
 */
public class CnBlogNewsContent {

    public String CommentCount;
    public String Content;
    public String ImageUrl;
    public String NextNews;
    public String PrevNews;
    public String SourceName;
    public String SubmitDate;
    public String Title;

    public List<CnBlogNewsContentDetailItem> detialItemList = new ArrayList<>();
}
