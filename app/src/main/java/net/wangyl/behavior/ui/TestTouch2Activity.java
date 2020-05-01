package net.wangyl.behavior.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.wangyl.behavior.R;
import net.wangyl.behavior.ui.fragment.PageTwoFragment;
import net.wangyl.behavior.ui.fragment.SwipeFragment;

public class TestTouch2Activity extends AppCompatActivity {
    private static final String TAG = "TestTouch2Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_touch2);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new SwipeFragment() : new PageTwoFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TestTouchActivity", "onTouchEvent :" + event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("TestTouchActivity", "dispatchTouchEvent :" + ev);
        return super.dispatchTouchEvent(ev);
    }
}
