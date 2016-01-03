package com.dpt.itnews.data.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NEWS".
 */
public class News {

    private String news_id;
    private String title;
    private String link;
    private String summary;
    private String published;
    private String topicIcon;
    private String comment_count;
    private String content;
    private String image_url;
    private String next_news;
    private String prev_news;
    private String source_name;
    private String submit_data;
    private Integer off_line;
    private Integer collect;
    private Long published_ms;

    public News() {
    }

    public News(String news_id) {
        this.news_id = news_id;
    }

    public News(String news_id, String title, String link, String summary, String published, String topicIcon, String comment_count, String content, String image_url, String next_news, String prev_news, String source_name, String submit_data, Integer off_line, Integer collect, Long published_ms) {
        this.news_id = news_id;
        this.title = title;
        this.link = link;
        this.summary = summary;
        this.published = published;
        this.topicIcon = topicIcon;
        this.comment_count = comment_count;
        this.content = content;
        this.image_url = image_url;
        this.next_news = next_news;
        this.prev_news = prev_news;
        this.source_name = source_name;
        this.submit_data = submit_data;
        this.off_line = off_line;
        this.collect = collect;
        this.published_ms = published_ms;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getTopicIcon() {
        return topicIcon;
    }

    public void setTopicIcon(String topicIcon) {
        this.topicIcon = topicIcon;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNext_news() {
        return next_news;
    }

    public void setNext_news(String next_news) {
        this.next_news = next_news;
    }

    public String getPrev_news() {
        return prev_news;
    }

    public void setPrev_news(String prev_news) {
        this.prev_news = prev_news;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSubmit_data() {
        return submit_data;
    }

    public void setSubmit_data(String submit_data) {
        this.submit_data = submit_data;
    }

    public Integer getOff_line() {
        return off_line;
    }

    public void setOff_line(Integer off_line) {
        this.off_line = off_line;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Long getPublished_ms() {
        return published_ms;
    }

    public void setPublished_ms(Long published_ms) {
        this.published_ms = published_ms;
    }

}