package com.seeker.demo.LazyFragmetn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Seeker on 2016/8/31.
 */

public class LazyFragmentActivity extends BaseActivity{

    private Unbinder unbinder;

    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.contentLayout)
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lazy_fragment;
    }

    @Override
    public void onbind() {
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public void doWork() {
        fragments.add(LazyFragment.newLazyFragment("Tab1"));
        fragments.add(LazyFragment.newLazyFragment("Tab2"));
        fragments.add(LazyFragment.newLazyFragment("Tab3"));
        fragments.add(LazyFragment.newLazyFragment("Tab4"));
        fragments.add(LazyFragment.newLazyFragment("Tab5"));
        viewPager.setAdapter(fragmentPagerAdapter);
        setTabSelection(0);
    }

    @OnClick({R.id.Tab1,R.id.Tab2,R.id.Tab3,R.id.Tab4,R.id.Tab5})
    public void onTabClick(View view){
        switch (view.getId()){
            case R.id.Tab1:
                setTabSelection(0);
                break;
            case R.id.Tab2:
                setTabSelection(1);
                break;
            case R.id.Tab3:
                setTabSelection(2);
                break;
            case R.id.Tab4:
                setTabSelection(3);
                break;
            case R.id.Tab5:
                setTabSelection(4);
                break;
        }

    }

    private void setTabSelection(int tab){
        switch (tab){
            case 0:
                viewPager.setCurrentItem(0,false);
                break;
            case 1:
                viewPager.setCurrentItem(1,false);
                break;
            case 2:
                viewPager.setCurrentItem(2,false);
                break;
            case 3:
                viewPager.setCurrentItem(3,false);
                break;
            case 4:
                viewPager.setCurrentItem(4,false);
                break;
        }
    }

    private FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    };

}
