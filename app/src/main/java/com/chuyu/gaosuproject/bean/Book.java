package com.chuyu.gaosuproject.bean;

/**
 * Created by wo on 2017/7/12.
 */

public class Book {
    private String bookName;
    private boolean isSelected = false;
    public Book(){};

    public Book(String bookName,String author,int publishTime){
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
