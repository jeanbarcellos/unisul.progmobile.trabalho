package jv.cadastrodelivros.model;

import android.provider.BaseColumns;

public class Book implements BaseColumns {
    public static final String TABLE_NAME = "book";
    public static final String COLUMN_NAME_CODE = "code";
    public static final String COLUMN_NAME_CATEGORY_CODE = "category_code";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_AUTHOR = "author";

    private int code;
    private Category category;
    private String title;
    private String description;
    private String author;

    public Book() {
    }

    public Book(int code, String title, Category category, String author, String description) {
        this.code = code;
        this.title = title;
        this.category = category;
        this.author = author;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "" + title + "";
    }
}
