package jv.cadastrodelivros.model;

public class Category {
    public static final String TABLE_NAME = "category";
    public static final String COLUMN_NAME_CODE = "code";
    public static final String COLUMN_NAME_DESCRIPTION = "description";


    private int code;

    private String description;

    public Category() {
    }

    public Category(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "" + this.description  + " (Code "+ this.code + ")";
    }
}

