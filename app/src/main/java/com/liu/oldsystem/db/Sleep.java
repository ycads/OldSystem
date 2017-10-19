package com.liu.oldsystem.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 舞动的心 on 2017/10/17.
 */

public class Sleep  extends DataSupport {
    private int id;
    private String morningTime;
    private String nightTime;
    private String storeTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMorningTime() {
        return morningTime;
    }

    public void setMorningTime(String morningTime) {
        this.morningTime = morningTime;
    }

    public String getNightTime() {
        return nightTime;
    }

    public void setNightTime(String nightTime) {
        this.nightTime = nightTime;
    }

    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

 }
