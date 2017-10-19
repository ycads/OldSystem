package com.liu.oldsystem.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 舞动的心 on 2017/10/17.
 */

public class Word extends DataSupport {
    private int id;
    private String wordSize;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWordSize() {
        return wordSize;
    }

    public void setWordSize(String wordSize) {
        this.wordSize = wordSize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
