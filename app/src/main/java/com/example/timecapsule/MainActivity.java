package com.example.timecapsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.example.timecapsule.adapter.MyPagerAdapter;
import com.example.timecapsule.bean.TabEntity;
import com.example.timecapsule.config.SettingConstant;
import com.example.timecapsule.login.LoginActivity;
import com.example.timecapsule.mine.MineFragment;
import com.example.timecapsule.news.NewsListFragment;
import com.example.timecapsule.capsule.CapsuleFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.tl_home)
    CommonTabLayout tlHome;
    @BindView(R.id.myCoordinatorLayout)
    CoordinatorLayout myCoordinatorLayout;
    private SharedPreferences mSettings;
    private String account;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"TimeCapsule", "DailyNews", "Account"};
    private int[] mIconUnselectIds = {R.drawable.ic_capsule_unselect, R.drawable.ic_recommend_unselect, R.drawable.ic_mine_unselect};
    private int[] mIconSelectIds = {R.drawable.ic_capsule_select, R.drawable.ic_recommend_select, R.drawable.ic_mine_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSettings = getSharedPreferences(SettingConstant.SETTING_NAME, MODE_PRIVATE);
        account = mSettings.getString(SettingConstant.ACCOUNT, SettingConstant.Account_Default);
        if ("".equals(account)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        mFragments.add(CapsuleFragment.newInstance(account,""));
        mFragments.add(NewsListFragment.newInstance("",""));
        mFragments.add(MineFragment.newInstance(account, ""));

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        vpHome.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        tablayoutSel();
    }
    private void tablayoutSel() {
        tlHome.setTabData(mTabEntities);
        tlHome.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpHome.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                }
            }
        });

        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tlHome.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpHome.setCurrentItem(0);
    }
}