package jv.cadastrodelivros.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jv.cadastrodelivros.model.Book;
import jv.cadastrodelivros.model.Category;

public class BookDao {

    private DBHelper dbHelper;

    private CategoryDao categoryDao;

    public BookDao(Context context) {
        this.dbHelper = new DBHelper(context);

        this.categoryDao = new CategoryDao(context);
    }

    public Book insert(Book book) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        book.setCode(this.getLastCod() + 1);

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_NAME_CODE, book.getCode());
        values.put(Book.COLUMN_NAME_CATEGORY_CODE, book.getCategory().getCode());
        values.put(Book.COLUMN_NAME_TITLE, book.getTitle());
        values.put(Book.COLUMN_NAME_DESCRIPTION, book.getDescription());
        values.put(Book.COLUMN_NAME_AUTHOR, book.getAuthor());

        long count = db.insert(Book.TABLE_NAME, null, values);

        if (count != -1) {
            return book;
        } else {
            return null;
        }

    }

    public Book update(Book book) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_NAME_CATEGORY_CODE, book.getCategory().getCode());
        values.put(Book.COLUMN_NAME_TITLE, book.getTitle());
        values.put(Book.COLUMN_NAME_DESCRIPTION, book.getDescription());
        values.put(Book.COLUMN_NAME_AUTHOR, book.getAuthor());

        String selection = Book.COLUMN_NAME_CODE + " = ? ";
        String[] selectionArgs = {String.valueOf(book.getCode())};

        long count = db.update(Book.TABLE_NAME, values, selection, selectionArgs);

        if (count > 0) {
            return book;
        } else {
            return null;
        }

    }

    public Book delete(Book book) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = Book.COLUMN_NAME_CODE + " = ?";
        String[] selectionArgs = {String.valueOf(book.getCode())};

        int count = db.delete(Book.TABLE_NAME, selection, selectionArgs);

        if (count > 0) {
            return book;
        } else {
            return null;
        }
    }

    public List findAll() {
        List lista = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Book.TABLE_NAME, null, null, null, null, null, Book.COLUMN_NAME_DESCRIPTION); //

        while (cursor.moveToNext()) {
            int code = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CODE));
            int categoryCode = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CATEGORY_CODE));
            String title = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_DESCRIPTION));
            String author = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_AUTHOR));

            Category category = this.categoryDao.find(categoryCode);

            Book book = new Book();
            book.setCode(code);
            book.setCategory(category);
            book.setTitle(title);
            book.setDescription(description);
            book.setAuthor(author);

            lista.add(book);
        }

        return lista;
    }

    public List findByTitle(String word) {
        List lista = new ArrayList<>();

        String selection = Book.COLUMN_NAME_TITLE + " like '%" + word + "%'";
        String[] selectionArgs = {String.valueOf(word)};

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Book.TABLE_NAME, null, selection, null, null, null, Book.COLUMN_NAME_DESCRIPTION); //

        while (cursor.moveToNext()) {
            int code = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CODE));
            int categoryCode = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CATEGORY_CODE));
            String title = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_DESCRIPTION));
            String author = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_AUTHOR));

            Category category = this.categoryDao.find(categoryCode);

            Book book = new Book();
            book.setCode(code);
            book.setCategory(category);
            book.setTitle(title);
            book.setDescription(description);
            book.setAuthor(author);

            lista.add(book);
        }

        return lista;
    }


    public Book find(int code) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Book book = null;

        String selection = Book.COLUMN_NAME_CODE + " = ?";
        String[] selectionArgs = {String.valueOf(code)};

        Cursor cursor = db.query(Book.TABLE_NAME, null, selection, selectionArgs, null, null, null, String.valueOf(1));

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int code2 = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CODE));
                int categoryCode = cursor.getInt(cursor.getColumnIndex(Book.COLUMN_NAME_CATEGORY_CODE));
                String title = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_DESCRIPTION));
                String author = cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME_AUTHOR));

                Category category = this.categoryDao.find(categoryCode);

                book = new Book();
                book.setCode(code2);
                book.setCategory(category);
                book.setTitle(title);
                book.setDescription(description);
                book.setAuthor(author);
            }
        }

        return book;
    }

    public int getLastCod() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int max = 0;

        Cursor cursor = db.rawQuery("SELECT max(code) AS id FROM  book", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                max = cursor.getInt(0);
            }
        }

        return max;
    }


}

