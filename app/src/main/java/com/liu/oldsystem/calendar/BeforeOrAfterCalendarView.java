package com.liu.oldsystem.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liu.oldsystem.R;
import com.liu.oldsystem.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fySpring
 * Date : 2017/2/21
 * To do :查看当前日期前七天或后七天日历
 */

public class BeforeOrAfterCalendarView extends RelativeLayout {

    private List<Integer> dayList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();

    protected List<RecordsCalenderItemView> itemViewList = new ArrayList<>();
    protected Context mContext;
    protected LinearLayout calenderViewLl;
    protected int curPosition;

    public BeforeOrAfterCalendarView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.before_or_after_calendar_layout, this);

        calenderViewLl = (LinearLayout) view.findViewById(R.id.boa_calender_view_ll);

        setBeforeDateViews();

        initItemViews();
    }

    /**
     * 设置之前的日期显示
     */
    private void setBeforeDateViews() {
        //获取日期列表
        dateList.addAll(TimeUtil.getBeforeDateListByNow());
        dayList.addAll(TimeUtil.dateListToDayList(dateList));
    }

    private void initItemViews() {
        for (int i = 0; i < dateList.size(); i++) {
            int day = dayList.get(i);
            String curItemDate = dateList.get(i);
            final RecordsCalenderItemView itemView;
            if(day == TimeUtil.getCurrentDay()){
                itemView = new RecordsCalenderItemView(mContext, "今天", String.valueOf(day), i, curItemDate);
            }else{
                itemView = new RecordsCalenderItemView(mContext, TimeUtil.getCurWeekDay(curItemDate), String.valueOf(day), i, curItemDate);
            }

            itemViewList.add(itemView);
            calenderViewLl.addView(itemView);

            itemView.setOnCalenderItemClick(new RecordsCalenderItemView.OnCalenderItemClick() {
                @Override
                public void onCalenderItemClick() {
                    curPosition = itemView.getPosition();
                    switchPositionView(curPosition);

                    //点击事件
                    if (calenderClickListener != null) {
                        calenderClickListener.onClickToRefresh(curPosition,dateList.get(curPosition));
                    }
                }
            });
        }

        switchPositionView(6);

    }

    private void switchPositionView(int position) {
        for (int i = 0; i < itemViewList.size(); i++) {
            if (position == i) {
                itemViewList.get(i).setChecked(true);
            } else {
                itemViewList.get(i).setChecked(false);
            }
        }
    }

    private BoaCalenderClickListener calenderClickListener;

    public interface BoaCalenderClickListener {
        void onClickToRefresh(int position, String curDate);
    }

    public void setOnBoaCalenderClickListener(BoaCalenderClickListener calenderClickListener) {
        this.calenderClickListener = calenderClickListener;
    }
}
