package jv.cadastrodelivros.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jv.cadastrodelivros.model.Book;
import jv.cadastrodelivros.model.Category;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trabalho.db";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table description
        String sql = "CREATE TABLE IF NOT EXISTS " + Category.TABLE_NAME + " (" +
                Category.COLUMN_NAME_CODE + " INTEGER(11) NOT NULL PRIMARY KEY, " +
                Category.COLUMN_NAME_DESCRIPTION + " VARCHAR(255) " +
                ");";
        db.execSQL(sql);

        // Create books
        String sql2 =  "CREATE TABLE IF NOT EXISTS " + Book.TABLE_NAME + " (" +
                Book.COLUMN_NAME_CODE + " INTEGER(11) NOT NULL PRIMARY KEY, " +
                Book.COLUMN_NAME_CATEGORY_CODE + " INTEGER(11), " +
                Book.COLUMN_NAME_TITLE + " VARCHAR(255), " +
                Book.COLUMN_NAME_DESCRIPTION + " VARCHAR(255), " +
                Book.COLUMN_NAME_AUTHOR + " VARCHAR(255) " +
                ");";

        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + Category.TABLE_NAME + ";";
        db.execSQL(sql1);

        String sql2 = "DROP TABLE IF EXISTS " + Book.TABLE_NAME + ";";
        db.execSQL(sql2);
    }
}

