package com.dpt.itnews.data.vo;

/**
 * Created by dupengtao on 15/12/18.
 */
public class CnBlogNewsItemInfo {

    public  String id;
    public  String title;
    public  String link;
    public  String summary;
    public  String published;
    public  String topicIcon;
    public  String sourceName;

    public CnBlogNewsItemInfo(String id, String title, String summary, String link, String published, String topicIcon, String sourceName) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.id = id;
        this.published = published;
        this.topicIcon = topicIcon;
        this.sourceName = sourceName;
    }

    public String getSimpleTime() {
        return published.substring(0, 10);
    }

    @Override
    public boolean equals(Object o) {
        CnBlogNewsItemInfo entry=(CnBlogNewsItemInfo)o;
        return this.id.equals(entry.id);
    }

    @Override
    public int hashCode() {
        int hashcode=-1;
        try {
            hashcode=Integer.valueOf(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hashcode;
    }


}
