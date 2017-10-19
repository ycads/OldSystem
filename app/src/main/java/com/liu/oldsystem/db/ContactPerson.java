package com.liu.oldsystem.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 舞动的心 on 2017/10/8.
 */

public class ContactPerson extends DataSupport{
    private int id;

    private String name;

    private String tel;

    private String isContact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsContact() {
        return isContact;
    }

    public void setIsContact(String isContact) {
        this.isContact = isContact;
    }

}
