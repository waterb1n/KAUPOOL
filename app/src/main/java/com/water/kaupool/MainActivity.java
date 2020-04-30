package com.water.kaupool;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

/**
 * 참고 블로그
 * https://blog.naver.com/gsh960913/221496207987
 * https://tedrepository.tistory.com/6?category=707850
 */

public class MainActivity extends AppCompatActivity implements
        FragmentA.OnFragmentInteractionListener, FragmentB.OnFragmentInteractionListener,
        FragmentC.OnFragmentInteractionListener, FragmentD.OnFragmentInteractionListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);

        Fragment[] arrFragments = new Fragment[4];
        arrFragments[0] = new FragmentA();
        arrFragments[1] = new FragmentB();
        arrFragments[2] = new FragmentC();
        arrFragments[3] = new FragmentD();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), arrFragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("등록"));
        tabLayout.addTab(tabLayout.newTab().setText("목록"));
        tabLayout.addTab(tabLayout.newTab().setText("이력"));
        tabLayout.addTab(tabLayout.newTab().setText("설정"));

    }

    public void onFragmentInteraction(Uri uri) {
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private Fragment[] arrFragments;

        public PagerAdapter(FragmentManager fm, Fragment[] arrFragments) {
            super(fm);
            this.arrFragments = arrFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return arrFragments[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "등록";
                case 1:
                    return "목록";
                case 2:
                    return "이력";
                case 3:
                    return "설정";
                default:
                    return "";
            }
        }
    }

}
