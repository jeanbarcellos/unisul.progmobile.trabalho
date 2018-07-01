package jv.cadastrodelivros.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jv.cadastrodelivros.model.Category;

public class CategoryDao {

    private DBHelper dbHelper;

    public CategoryDao(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public Category insert(Category category) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        category.setCode(this.getLastCod() + 1);

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_NAME_CODE, category.getCode());
        values.put(Category.COLUMN_NAME_DESCRIPTION, category.getDescription());

        long count = db.insert(Category.TABLE_NAME, null, values);

        if (count != -1) {
            return category;
        } else {
            return null;
        }

    }

    public Category update(Category category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_NAME_DESCRIPTION, category.getDescription());

        String selection = Category.COLUMN_NAME_CODE+ " = ? ";
        String[] selectionArgs = {String.valueOf(category.getCode())};

        long count = db.update(Category.TABLE_NAME, values, selection, selectionArgs);

        if (count > 0) {
            return category;
        } else {
            return null;
        }

    }

    public Category delete(Category category) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = Category.COLUMN_NAME_CODE + " = ?";
        String[] selectionArgs = {String.valueOf(category.getCode())};

        int count = db.delete(Category.TABLE_NAME, selection, selectionArgs);

        if (count > 0) {
            return category;
        } else {
            return null;
        }
    }

    public List findAll() {
        List lista = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Category.TABLE_NAME, null, null, null, null, null, Category.COLUMN_NAME_DESCRIPTION);

        while (cursor.moveToNext()) {
            int code = cursor.getInt(cursor.getColumnIndex(Category.COLUMN_NAME_CODE));
            String description = cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME_DESCRIPTION));

            lista.add(new Category(code, description));
        }

        return lista;
    }


    public Category find(int code) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Category category = null;

        String selection = Category.COLUMN_NAME_CODE + " = ?";
        String[] selectionArgs = {String.valueOf(code)};

        Cursor cursor = db.query(Category.TABLE_NAME, null, selection, selectionArgs, null, null, null, String.valueOf(1));

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int code2 = cursor.getInt(cursor.getColumnIndex(Category.COLUMN_NAME_CODE));
                String description = cursor.getString(cursor.getColumnIndex(Category.COLUMN_NAME_DESCRIPTION));

                category = new Category(code2, description);
            }
        }

        return category;
    }

    public int getLastCod() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int max = 0;

        Cursor cursor = db.rawQuery("SELECT max(code) AS id FROM  category", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                max = cursor.getInt(0);
            }
        }

        return max;
    }


}
