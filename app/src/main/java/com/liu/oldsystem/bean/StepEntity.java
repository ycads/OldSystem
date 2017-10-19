package com.liu.oldsystem.bean;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :
 */

public class StepEntity {

    private String curDate; //当天的日期
    private String steps;   //当天的步数

    public StepEntity() {
    }

    public StepEntity(String curDate, String steps) {
        this.curDate = curDate;
        this.steps = steps;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }


    @Override
    public String toString() {
        return "StepEntity{" +
                "curDate='" + curDate + '\'' +
                ", steps=" + steps +
                '}';
    }
}
