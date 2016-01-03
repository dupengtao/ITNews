package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ItNewsDb {


    public  String id;
    public  String title;
    public  String link;
    public  String summary;
    public  String published;
    public  String topicIcon;
    public  String sourceName;

    public String CommentCount;
    public String Content;
    public String ImageUrl;
    public String NextNews;
    public String PrevNews;
    public String SourceName;
    public String SubmitDate;
    public String Title;

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1,"com.dpt.itnews.data.db");
        addItNews(schema);
        new DaoGenerator().generateAll(schema, "/Users/dupengtao/code/Rx/ItNews/app/src/main/java");
    }

    private static void addItNews(Schema schema) {
        Entity news = schema.addEntity("News");
        //list
        news.addStringProperty("news_id").primaryKey();
        news.addStringProperty("title");
        news.addStringProperty("link");
        news.addStringProperty("summary");
        news.addStringProperty("published");
        news.addStringProperty("topicIcon");
        //news.addStringProperty("sourceName");
        //content
        news.addStringProperty("comment_count");
        news.addStringProperty("content");
        news.addStringProperty("image_url");
        news.addStringProperty("next_news");
        news.addStringProperty("prev_news");
        news.addStringProperty("source_name");
        news.addStringProperty("submit_data");
        //custom
        news.addIntProperty("off_line");
        news.addIntProperty("collect");
        news.addLongProperty("published_ms");
        //news.addContentProvider();
    }
}
