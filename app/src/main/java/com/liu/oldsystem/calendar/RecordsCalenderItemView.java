package com.liu.oldsystem.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liu.oldsystem.R;
import com.liu.oldsystem.health.StepActivity;

/**
 * 查看一周内每天的历史纪录日历Item布局
 */

public class RecordsCalenderItemView extends RelativeLayout {
    private static final String TAG = "RecordsCalenderItemView";

    private Context mContext;

    private LinearLayout itemLl;
    private View lineView;
    private TextView weekTv;
    private RelativeLayout dateRl;
    private TextView dateTv;
    //日期时间
    private String weekStr, dateStr;
    private int position;

    //当前item 的时间  ，如   2017年02月07日 ，  用以判断当前item是否可以被点击
    protected String curItemDate;


    OnCalenderItemClick itemClick = null;

    public interface OnCalenderItemClick {
        public void onCalenderItemClick();
    }

    public void setOnCalenderItemClick(OnCalenderItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public RecordsCalenderItemView(Context context, String week, String date, int position, String curItemDate) {
        super(context);
        this.mContext = context;
        this.weekStr = week;
        this.dateStr = date;
        this.position = position;
        this.curItemDate = curItemDate;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.records_calender_item_view, this);
        itemLl = (LinearLayout) itemView.findViewById(R.id.records_calender_item_ll);
        weekTv = (TextView) itemView.findViewById(R.id.records_calender_item_week_tv);
        lineView = itemView.findViewById(R.id.calendar_item_line_view);
        dateRl = (RelativeLayout) itemView.findViewById(R.id.records_calender_item_date_rl);
        dateTv = (TextView) itemView.findViewById(R.id.records_calender_item_date_tv);

        //如果日期是今天的话设为选中  目前有BUG

//        if(curItemDate.equals(TimeUtil.getCurrentDate())){
//            dateTv.setBackgroundResource(R.drawable.ic_blue_round_border_bg);
//            dateTv.getBackground().setAlpha(255);
//        }else{
//            if(dateTv.getBackground() != null){
//                dateTv.getBackground().setAlpha(0);
//            }
//        }


        weekTv.setTextSize(15);
        lineView.setVisibility(GONE);

        weekTv.setText(weekStr);
        dateTv.setText(dateStr);

        itemView.setLayoutParams(new LayoutParams((StepActivity.screenWidth) / 7,
                ViewGroup.LayoutParams.MATCH_PARENT));

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onCalenderItemClick();
            }
        });

    }

    //返回当前项的id
    public int getPosition() {
        return position;
    }

    public void setChecked(boolean checkedFlag) {

        if (checkedFlag) {
            //当前item被选中后样式
            weekTv.setTextColor(getResources().getColor(R.color.main_text_color));
            dateTv.setTextColor(getResources().getColor(R.color.white));
            dateRl.setBackgroundResource(R.mipmap.ic_blue_round_bg);
        } else {
            //当前item未被选中样式
            weekTv.setTextColor(getResources().getColor(R.color.gray_default_dark));
            dateTv.setTextColor(getResources().getColor(R.color.gray_default_dark));
            //设置背景透明
            dateRl.setBackgroundColor(Color.TRANSPARENT);
        }

    }
}
