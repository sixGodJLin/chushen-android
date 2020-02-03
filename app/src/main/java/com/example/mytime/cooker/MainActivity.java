package com.example.mytime.cooker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.cooker.fragment.DiscoverFragment;
import com.example.mytime.cooker.fragment.HomeFragment;
import com.example.mytime.cooker.fragment.MallFragment;
import com.example.mytime.cooker.fragment.MineFragment;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_framelayout)
    FrameLayout mainFramelayout;

    @Bind(R.id.tab_home_image)
    ImageView tabHomeImage;
    @Bind(R.id.tab_home_text)
    TextView tabHomeText;
    @Bind(R.id.tab_home_layout)
    LinearLayout tabHomeLayout;

    @Bind(R.id.tab_mall_image)
    ImageView tabMallImage;
    @Bind(R.id.tab_mall_text)
    TextView tabMallText;
    @Bind(R.id.tab_mall_layout)
    LinearLayout tabMallLayout;

    @Bind(R.id.tab_discover_image)
    ImageView tabDiscoverImage;
    @Bind(R.id.tab_discover_text)
    TextView tabDiscoverText;
    @Bind(R.id.tab_discover_layout)
    LinearLayout tabDiscoverLayout;

    @Bind(R.id.tab_mine_image)
    ImageView tabMineImage;
    @Bind(R.id.tab_mine_text)
    TextView tabMineText;
    @Bind(R.id.tab_mine_layout)
    LinearLayout tabMineLayout;

    private Context mContext;

    private HomeFragment homeFragment = null;
    private DiscoverFragment discoverFragment = null;
    private MineFragment mineFragment = null;
    private MallFragment mallFragment = null;


    private FragmentManager fragmentManager; //fragment管理器

    private List<TextView> tabTexts;
    private List<ImageView> tabImges;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        CrashReport.initCrashReport(getApplicationContext(), "b82d9ed5db", true);

        initProperties();
        initArrays();
        setChoiceItem(0);
    }
    private void initProperties() {
        mContext = MainActivity.this;
        // 这里已经可以确定列表的大小，所以直接定死大小节省空间
        tabTexts = new ArrayList<>(4);
        tabImges = new ArrayList<>(4);
        fragments = new ArrayList<>(4);
    }

    private void initArrays() {
        tabTexts.add(tabHomeText);
        tabTexts.add(tabDiscoverText);
        tabTexts.add(tabMallText);
        tabTexts.add(tabMineText);

        tabImges.add(tabHomeImage);
        tabImges.add(tabDiscoverImage);
        tabImges.add(tabMallImage);
        tabImges.add(tabMineImage);

        fragments.add(homeFragment);
        fragments.add(discoverFragment);
        fragments.add(mallFragment);
        fragments.add(mineFragment);
    }

    @OnClick({R.id.tab_home_layout, R.id.tab_discover_layout, R.id.tab_mall_layout, R.id.tab_mine_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home_layout:
                setChoiceItem(0);
                break;
            case R.id.tab_discover_layout:
                setChoiceItem(1);
                break;
            case R.id.tab_mall_layout:
                setChoiceItem(2);
                break;
            case R.id.tab_mine_layout:
                setChoiceItem(3);
                break;
        }
    }

    private void setChoiceItem(int position) {
        clearChoose();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);      //隐藏所有的Fragment
        showFragment(transaction, position);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        for (Fragment f : fragments){
            if (f != null){
                transaction.hide(f);
            }
        }
    }

    private void showFragment(FragmentTransaction transaction, int position) {
        tabTexts.get(position).setSelected(true);
        tabImges.get(position).setSelected(true);

        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new DiscoverFragment();
                    break;
                case 2:
                    fragment = new MallFragment();
                    break;
                case 3:
                    fragment = new MineFragment();
                    break;
                default:
                    break;
            }
            fragments.set(position,fragment);
            transaction.add(R.id.main_framelayout, fragment);
        } else {    //如果mainShippingFragment不为空，则直接显示
            transaction.show(fragment);
        }
    }

    /**
     * 重置所有的选项
     */
    public void clearChoose() {
        for (int i = 0; i < 4; i++) {
            tabImges.get(i).setSelected(false);
            tabTexts.get(i).setSelected(false);
        }
    }
}
