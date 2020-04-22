package net.wangyl.behavior.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import net.wangyl.behavior.behaviors.MainHeaderBehavior
import net.wangyl.behavior.behaviors.MainHeaderBehavior.OnHeaderStateListener
import net.wangyl.behavior.R
import net.wangyl.behavior.ui.fragment.TextFragment
import net.wangyl.behavior.ui.fragment.TypeFragment
import net.wangyl.behavior.adapter.TypePageAdapter
import java.util.*
import kotlin.math.abs


class TestActivity : AppCompatActivity(), OnHeaderStateListener {
    private var mViewPager: ViewPager? = null
//    private var mBackgroundPager: ViewPager? = null
    private var mBackgroundPager: ViewPager2? = null
    private val mHeaderBehavior: MainHeaderBehavior? = null
    private var mLastX:Int = 0
    private var mLastY:Int = 0
    private val mDelta:Int = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //
//        mHeaderBehavior = (HeaderBehavior) ((CoordinatorLayout.LayoutParams) (findViewById(R.id.header)).getLayoutParams()).getBehavior();
//
//        if (mHeaderBehavior != null) {
////            mHeaderBehavior.setTabSuspension(true);
//            mHeaderBehavior.setHeaderStateListener(this);
//        }
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        fragments.add(TypeFragment.newInstance())
        fragments.add(TypeFragment.newInstance())
        fragments.add(TypeFragment.newInstance())
        titles.add("tab1")
        titles.add("tab2")
        titles.add("tab3")
        mViewPager = findViewById(R.id.viewpager)
        mBackgroundPager = findViewById(R.id.viewpager_background)
        val tableLayout = findViewById<TabLayout>(R.id.tablayout)
        val mTypeAdapter = TypePageAdapter(
            supportFragmentManager
        )
        mTypeAdapter.setData(fragments, titles)
        mViewPager!!.adapter = mTypeAdapter
        mViewPager!!.offscreenPageLimit = titles.size - 1
        tableLayout.setupWithViewPager(mViewPager)
        tableLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        initBackgroundVP()
    }

    fun initBackgroundVP() {
        val mTypeAdapter = TypePageAdapter(
            supportFragmentManager
        )
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        fragments.add(TextFragment.newInstance())
        fragments.add(TextFragment.newInstance())
        fragments.add(TextFragment.newInstance())
        titles.add("tab1")
        titles.add("tab2")
        titles.add("tab3")
        mTypeAdapter.setData(fragments, titles)
//        mBackgroundPager?.adapter =mTypeAdapter
        mBackgroundPager?.adapter =object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return TextFragment.newInstance()
            }

            override fun getItemCount(): Int {
                return 3
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev?.action
        val x =  ev?.getX()?.toInt() ?: 0
        val y =  ev?.getY()?.toInt() ?: 0
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x ?: 0
                mLastY = y ?: 0
            }
            MotionEvent.ACTION_MOVE -> {
                mBackgroundPager?.isUserInputEnabled = false
                if (abs(x - mLastX) > abs(y - mLastY) + 50) {
                    Log.e("TestActivity", "horizontal")
                    mBackgroundPager?.isUserInputEnabled = true
                } else  {
                    Log.e("TestActivity", "vertical")
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onHeaderClosed() {
        Log.e("status", "closed")
    }

    override fun onHeaderOpened() {
        Log.e("status", "opened")
    }

    override fun onBackPressed() {
        if (mHeaderBehavior != null && mHeaderBehavior.isClosed) {
            mHeaderBehavior.openHeader()
        } else {
            super.onBackPressed()
        }
    }
}