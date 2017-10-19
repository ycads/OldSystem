package com.liu.oldsystem.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 舞动的心 on 2017/10/17.
 */

public class Memo extends DataSupport {
    private int id;

    private String contant;

    private String storeTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContant() {
        return contant;
    }

    public void setContant(String contant) {
        this.contant = contant;
    }

    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }
}
