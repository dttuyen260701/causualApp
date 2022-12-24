package com.example.pbl6app.Retrofit;/*
 * Created by tuyen.dang on 12/24/2022
 */

import com.google.gson.annotations.SerializedName;

public class ItemPaging <T> {
    @SerializedName("items")
    private T items;
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private int pageSize;
    @SerializedName("totalRecords")
    private int totalRecords;
    @SerializedName("pageCount")
    private int pageCount;

    public ItemPaging(T items, int pageIndex, int pageSize, int totalRecords, int pageCount) {
        this.items = items;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.pageCount = pageCount;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
