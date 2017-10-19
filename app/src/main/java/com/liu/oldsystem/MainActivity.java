package com.liu.oldsystem;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.liu.oldsystem.fragment.HealthFragment;
import com.liu.oldsystem.fragment.HelpFragment;
import com.liu.oldsystem.fragment.LocationFragment;
import com.liu.oldsystem.fragment.ToolFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mFragments;
    private String[] mTabTitles = {"一键求救", "位置服务", "健康监测","智能工具"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initToolBar();
        initFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_main);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_main);

        // 初始化Adapter
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        // 设置adapter将ViewPager和Fragment关联起来
        viewPager.setAdapter(adapter);
        // 将TabLayout和ViewPager关联，达到TabLayout和Viewpager、Fragment联动的效果
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 初始化toolBar
     */
//    private void initToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
//        // 指定ToolBar的标题
//        toolbar.setTitle("CoordinatorLayout");
//        // 将toolBar和actionBar进行关联
//        setSupportActionBar(toolbar);
//    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        HelpFragment helpFragment = new HelpFragment();
        LocationFragment locationFragment = new LocationFragment();
        HealthFragment healthFragment = new HealthFragment();
        ToolFragment toolFragment = new ToolFragment();
        // 将四个fragment放入List里面管理，方便使用
        mFragments = new ArrayList<>();
        mFragments.add(helpFragment);
        mFragments.add(locationFragment);
        mFragments.add(healthFragment);
        mFragments.add(toolFragment);
        getSupportActionBar().setTitle("老年人便捷服务系统");
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            // 获取指定位置的Fragment对象
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // ViewPager管理页面的数量
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 设置indicator的标题（TabLayout中tab的标题）
            return mTabTitles[position];
        }
    }
}
