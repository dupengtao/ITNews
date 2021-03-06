package com.dpt.itnews.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.dpt.itnews.data.db.News;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEWS".
*/
public class NewsDao extends AbstractDao<News, String> {

    public static final String TABLENAME = "NEWS";

    /**
     * Properties of entity News.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property News_id = new Property(0, String.class, "news_id", true, "NEWS_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Link = new Property(2, String.class, "link", false, "LINK");
        public final static Property Summary = new Property(3, String.class, "summary", false, "SUMMARY");
        public final static Property Published = new Property(4, String.class, "published", false, "PUBLISHED");
        public final static Property TopicIcon = new Property(5, String.class, "topicIcon", false, "TOPIC_ICON");
        public final static Property Comment_count = new Property(6, String.class, "comment_count", false, "COMMENT_COUNT");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
        public final static Property Image_url = new Property(8, String.class, "image_url", false, "IMAGE_URL");
        public final static Property Next_news = new Property(9, String.class, "next_news", false, "NEXT_NEWS");
        public final static Property Prev_news = new Property(10, String.class, "prev_news", false, "PREV_NEWS");
        public final static Property Source_name = new Property(11, String.class, "source_name", false, "SOURCE_NAME");
        public final static Property Submit_data = new Property(12, String.class, "submit_data", false, "SUBMIT_DATA");
        public final static Property Off_line = new Property(13, Integer.class, "off_line", false, "OFF_LINE");
        public final static Property Collect = new Property(14, Integer.class, "collect", false, "COLLECT");
        public final static Property Published_ms = new Property(15, Long.class, "published_ms", false, "PUBLISHED_MS");
    };


    public NewsDao(DaoConfig config) {
        super(config);
    }
    
    public NewsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEWS\" (" + //
                "\"NEWS_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: news_id
                "\"TITLE\" TEXT," + // 1: title
                "\"LINK\" TEXT," + // 2: link
                "\"SUMMARY\" TEXT," + // 3: summary
                "\"PUBLISHED\" TEXT," + // 4: published
                "\"TOPIC_ICON\" TEXT," + // 5: topicIcon
                "\"COMMENT_COUNT\" TEXT," + // 6: comment_count
                "\"CONTENT\" TEXT," + // 7: content
                "\"IMAGE_URL\" TEXT," + // 8: image_url
                "\"NEXT_NEWS\" TEXT," + // 9: next_news
                "\"PREV_NEWS\" TEXT," + // 10: prev_news
                "\"SOURCE_NAME\" TEXT," + // 11: source_name
                "\"SUBMIT_DATA\" TEXT," + // 12: submit_data
                "\"OFF_LINE\" INTEGER," + // 13: off_line
                "\"COLLECT\" INTEGER," + // 14: collect
                "\"PUBLISHED_MS\" INTEGER);"); // 15: published_ms
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEWS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, News entity) {
        stmt.clearBindings();
 
        String news_id = entity.getNews_id();
        if (news_id != null) {
            stmt.bindString(1, news_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(3, link);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(4, summary);
        }
 
        String published = entity.getPublished();
        if (published != null) {
            stmt.bindString(5, published);
        }
 
        String topicIcon = entity.getTopicIcon();
        if (topicIcon != null) {
            stmt.bindString(6, topicIcon);
        }
 
        String comment_count = entity.getComment_count();
        if (comment_count != null) {
            stmt.bindString(7, comment_count);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
 
        String image_url = entity.getImage_url();
        if (image_url != null) {
            stmt.bindString(9, image_url);
        }
 
        String next_news = entity.getNext_news();
        if (next_news != null) {
            stmt.bindString(10, next_news);
        }
 
        String prev_news = entity.getPrev_news();
        if (prev_news != null) {
            stmt.bindString(11, prev_news);
        }
 
        String source_name = entity.getSource_name();
        if (source_name != null) {
            stmt.bindString(12, source_name);
        }
 
        String submit_data = entity.getSubmit_data();
        if (submit_data != null) {
            stmt.bindString(13, submit_data);
        }
 
        Integer off_line = entity.getOff_line();
        if (off_line != null) {
            stmt.bindLong(14, off_line);
        }
 
        Integer collect = entity.getCollect();
        if (collect != null) {
            stmt.bindLong(15, collect);
        }
 
        Long published_ms = entity.getPublished_ms();
        if (published_ms != null) {
            stmt.bindLong(16, published_ms);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public News readEntity(Cursor cursor, int offset) {
        News entity = new News( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // news_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // link
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // summary
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // published
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // topicIcon
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // comment_count
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // content
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // image_url
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // next_news
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // prev_news
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // source_name
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // submit_data
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // off_line
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // collect
            cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15) // published_ms
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, News entity, int offset) {
        entity.setNews_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLink(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSummary(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPublished(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTopicIcon(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setComment_count(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setImage_url(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setNext_news(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPrev_news(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSource_name(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSubmit_data(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setOff_line(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setCollect(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setPublished_ms(cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(News entity, long rowId) {
        return entity.getNews_id();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(News entity) {
        if(entity != null) {
            return entity.getNews_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
