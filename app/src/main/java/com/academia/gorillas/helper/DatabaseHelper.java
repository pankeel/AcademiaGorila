package com.academia.gorillas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.academia.gorillas.model.Post;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TablesInfo.PostEntry.TABLE_NAME + " (" +
                    TablesInfo.PostEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TablesInfo.PostEntry.COLUMN_POST_ID + " INTEGER, " +
                    TablesInfo.PostEntry.COLUMN_TITLE + " TEXT, " +
                    TablesInfo.PostEntry.COLUMN_IMAGE + " TEXT, " +
                    TablesInfo.PostEntry.COLUMN_CONTENT + " TEXT, " +
                    TablesInfo.PostEntry.COLUMN_EXCERPT + " TEXT, " +
                    TablesInfo.PostEntry.COLUMN_LINK + " TEXT, " +
                    TablesInfo.PostEntry.COLUMN_DATE + " TEXT " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_FAVORITES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TablesInfo.PostEntry.TABLE_NAME);

        onCreate(db);
    }

    public void addFavorite(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.PostEntry.COLUMN_POST_ID, post.getId());
        cv.put(TablesInfo.PostEntry.COLUMN_TITLE, post.getTitle().trim());
        cv.put(TablesInfo.PostEntry.COLUMN_IMAGE, post.getFeaturedMedia().trim());
        cv.put(TablesInfo.PostEntry.COLUMN_CONTENT, post.getContent().trim());
        cv.put(TablesInfo.PostEntry.COLUMN_EXCERPT, post.getExcerpt());
        cv.put(TablesInfo.PostEntry.COLUMN_DATE, post.getDate());
        cv.put(TablesInfo.PostEntry.COLUMN_LINK, post.getLink());

        long result = db.insert(TablesInfo.PostEntry.TABLE_NAME, null, cv);

        if (result > -1)
            Log.i("DatabaseHelper", "Not başarıyla kaydedildi");
        else
            Log.i("DatabaseHelper", "Not kaydedilemedi");

        db.close();

    }

    public ArrayList<Post> getFavoriteList() {
        ArrayList<Post> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TablesInfo.PostEntry.COLUMN_ID,
                TablesInfo.PostEntry.COLUMN_POST_ID,
                TablesInfo.PostEntry.COLUMN_TITLE,
                TablesInfo.PostEntry.COLUMN_IMAGE,
                TablesInfo.PostEntry.COLUMN_CONTENT,
                TablesInfo.PostEntry.COLUMN_EXCERPT,
                TablesInfo.PostEntry.COLUMN_DATE,
                TablesInfo.PostEntry.COLUMN_LINK};

        // public Post(int id, String title, String excerpt, String content, String featuredMedia, String date, String link)

        Cursor c = db.query(TablesInfo.PostEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            Post post = new Post(c.getInt(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_POST_ID)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_TITLE)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_EXCERPT)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_CONTENT)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_IMAGE)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_DATE)),
                    c.getString(c.getColumnIndex(TablesInfo.PostEntry.COLUMN_LINK)));
            data.add(post);
        }

        c.close();
        db.close();



        return data;
    }

    public boolean isIncludeFavorite(int id){
        boolean isInclude = false;
        ArrayList<Post> posts = getFavoriteList();
        for(Post post : posts){
            if(post.getId() == id){
                isInclude = true;
            }
        }
        return isInclude;
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TablesInfo.PostEntry.TABLE_NAME, TablesInfo.PostEntry.COLUMN_POST_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

}