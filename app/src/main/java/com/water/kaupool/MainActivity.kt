package com.water.kaupool

import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


/**
 * https://blog.naver.com/gsh960913/221496207987
 * https://tedrepository.tistory.com/6?category=707850
 * https://dico.me/android/articles/177
 */
class MainActivity : AppCompatActivity(),
        FragmentA.OnFragmentInteractionListener, FragmentB.OnFragmentInteractionListener,
        FragmentC.OnFragmentInteractionListener, FragmentD.OnFragmentInteractionListener {

    var tabLayout: TabLayout? = null
    var pager: ViewPager? = null
    var fragment: String? = null
    var manager: LocationManager? = null

    /*fun getViewPager(): ViewPager? {
        return pager
    }*/

    fun getViewPager(): ViewPager? {
        if (null == pager) {
            pager = findViewById(R.id.pagers) as ViewPager
        }
        return pager
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.pagers)

        //val arrFragments = arrayOfNulls<Fragment>(4)
        val arrFragments = arrayOf(FragmentA(), FragmentB(), FragmentC(), FragmentD())
        arrFragments[0] = FragmentA()
        arrFragments[1] = FragmentB()
        arrFragments[2] = FragmentC()
        arrFragments[3] = FragmentD()

        val pagerAdapter = MyPagerAdapter(supportFragmentManager, arrFragments)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    fun OnFragmentInteractionListener(uri: Uri) {}

    private inner class MyPagerAdapter(fm: FragmentManager?, private val arrFragments: Array<Fragment>)
        : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return arrFragments[position]
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "등록"
                1 -> "목록"
                2 -> "이력"
                3 -> "채팅"
                else -> ""
            }
        }

    }
}