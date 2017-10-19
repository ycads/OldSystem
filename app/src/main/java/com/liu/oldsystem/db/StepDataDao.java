package com.liu.oldsystem.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liu.oldsystem.bean.StepEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :
 */

public class StepDataDao {
    private DBOpenHelper stepHelper;
    private SQLiteDatabase stepDb;

    public StepDataDao(Context context) {
        stepHelper = new DBOpenHelper(context);
    }

    /**
     * 添加一条新记录
     *
     * @param stepEntity
     */
    public void addNewData(StepEntity stepEntity) {
//        LitePal.getDatabase();
//        Step step = new Step();
//        step.setCurDate(stepEntity.getCurDate());
//        step.setSteps(stepEntity.getSteps());
//        step.save();

        stepDb = stepHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        stepDb.insert("step", null, values);

        stepDb.close();
    }

    /**
     * 根据日期查询记录
     *
     * @param curDate
     * @return
     */
    public StepEntity getCurDataByDate(String curDate) {
        StepEntity stepEntity = null;
//        LitePal.getDatabase();
//        List<Step> steps = DataSupport.findAll(Step.class);
//        for(Step step: steps) {
//            if(step.getCurDate().equals(curDate)) {
//                stepEntity = new StepEntity(step.getCurDate(), step.getSteps());
//                //跳出循环
//                break;
//            }
//        }

        stepDb = stepHelper.getReadableDatabase();

        Cursor cursor = stepDb.query("step", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                stepEntity = new StepEntity(date, steps);
                //跳出循环
                break;
            }
        }
        //关闭
        stepDb.close();
        cursor.close();
        return stepEntity;
    }

    /**
     * 查询所有的记录
     *
     * @return
     */
    public List<StepEntity> getAllDatas() {
        List<StepEntity> dataList = new ArrayList<>();
        stepDb = stepHelper.getReadableDatabase();
        Cursor cursor = stepDb.rawQuery("select * from step", null);

//        LitePal.getDatabase();
//
//        List<Step> steps = DataSupport.findAll(Step.class);
//        for(Step step: steps) {
//            String curDate = step.getCurDate();
//            String totalSteps = step.getSteps();
//            StepEntity entity = new StepEntity(curDate, totalSteps);
//            dataList.add(entity);
//        }

        while (cursor.moveToNext()) {
            String curDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String totalSteps = cursor.getString(cursor.getColumnIndex("totalSteps"));
            StepEntity entity = new StepEntity(curDate, totalSteps);
            dataList.add(entity);
        }

        //关闭数据库
        stepDb.close();
        cursor.close();
        return dataList;
    }

    /**
     * 更新数据
     * @param stepEntity
     */
    public void updateCurData(StepEntity stepEntity) {
        stepDb = stepHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate",stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        stepDb.update("step", values, "curDate=?", new String[]{stepEntity.getCurDate()});

        stepDb.close();


//        LitePal.getDatabase();
//        Step step = new Step();
//        step.setSteps(stepEntity.getSteps());
//        step.setCurDate(stepEntity.getCurDate());
//        step.updateAll("curDate = ?", stepEntity.getCurDate());
    }


    /**
     * 删除指定日期的记录
     *
     * @param curDate1
     */
//    public void deleteCurData(String curDate1) {
////        LitePal.getDatabase();
//        DataSupport.deleteAll(Step.class, "curDate=?", curDate1);
//
////        stepDb = stepHelper.getReadableDatabase();
////
////        if (stepDb.isOpen())
////            stepDb.delete("step", "curDate", new String[]{curDate});
////        stepDb.close();
//    }
}
