package ga.slpdev.newsagregator.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import ga.slpdev.newsagregator.classes.News;

public class FavoritesDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_FAVORITES =
            "CREATE TABLE " + FavoritesEntery.TABLE_NAME + " (" +
                    FavoritesEntery._ID + " INTEGER PRIMARY KEY," +
                    FavoritesEntery.COLUMN_NAME_FAVORITES_ID + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_NEWS =
            "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                    NewsEntry._ID + " INTEGER PRIMARY KEY," +
                    NewsEntry.COLUMN_NAME_SRC + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_SRC_IMG + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_PUBLISHED_AT + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_HASH + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FAVORITES =
            "DROP TABLE IF EXISTS " + FavoritesEntery.TABLE_NAME;
    private static final String SQL_DELETE_NEWS =
            "DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME;

    public static abstract class FavoritesEntery implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_FAVORITES_ID = "hash";
    }

    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";

        public static final String COLUMN_NAME_SRC = "src";
        public static final String COLUMN_NAME_SRC_IMG = "src_img";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PUBLISHED_AT = "published_at";
        public static final String COLUMN_NAME_HASH = "hash";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Favorites.db";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITES);
        db.execSQL(SQL_CREATE_NEWS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITES);
        db.execSQL(SQL_DELETE_NEWS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onDislike(String hash) {
        String[] args = { hash };
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(FavoritesEntery.TABLE_NAME,
                String.format("%s = ?", FavoritesEntery.COLUMN_NAME_FAVORITES_ID),
                args);
    }

    public void onLike(String hash) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(FavoritesEntery.COLUMN_NAME_FAVORITES_ID, hash);
        database.insert(FavoritesEntery.TABLE_NAME, null, contentValue);
    }

    public boolean isLiked(String hash) {
        String[] args = { hash };
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(FavoritesEntery.TABLE_NAME,
                null,
                String.format("%s = ?", FavoritesEntery.COLUMN_NAME_FAVORITES_ID),
                args,
                null, null, null);
        boolean isSet = cursor.moveToFirst();
        cursor.close();
        return isSet;
    }

    public ArrayList<News> getAllNews() {
        ArrayList<News> list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(NewsEntry.TABLE_NAME, null, null,
                null,null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String src = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_SRC));
                String srcImg = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_SRC_IMG));
                String description = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_DESCRIPTION));
                String publishedAt = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_PUBLISHED_AT));
                String title = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NAME_TITLE));

                list.add(new News(null, null, null,
                        title, description, src, srcImg, publishedAt));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public void onAddNews(News news) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
            contentValue.put(NewsEntry.COLUMN_NAME_DESCRIPTION, news.getDescription());
            contentValue.put(NewsEntry.COLUMN_NAME_SRC, news.getUrl());
            contentValue.put(NewsEntry.COLUMN_NAME_SRC_IMG, news.getUrlImage());
            contentValue.put(NewsEntry.COLUMN_NAME_TITLE, news.getTitle());
            contentValue.put(NewsEntry.COLUMN_NAME_PUBLISHED_AT, news.getPublishedAt());
            // FIXME: 05.04.2018 Remove a connection to Crypto module from here
            contentValue.put(NewsEntry.COLUMN_NAME_HASH, Crypto.Sha1(news.getDescription()));

        database.insert(NewsEntry.TABLE_NAME, null, contentValue);
    }

    public void onDeleteNews(String hash) {
        String[] args = { hash };
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(NewsEntry.TABLE_NAME,
                String.format("%s = ?", NewsEntry.COLUMN_NAME_HASH), args);
    }
}
