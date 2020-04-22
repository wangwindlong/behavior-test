package net.wangyl.behavior.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.wangyl.behavior.R;

public class TestTouchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_touch);
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
