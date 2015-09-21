package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

/**
 * 设置引导的基类,不需要注册，因为不需要界面展示
 * 
 * @author baoliang.zhao
 * 
 */
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mDetector;

	protected SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		// 手势识别器
		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {

			// 监听手势滑动事件
			/**
			 * e1表示滑动的起点，e2表示滑动的终点;velocityX表示水平速度,velocttyY表示垂直速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				// 判断纵向滑动幅度是否 过大，过大的话不允许切换界面
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					Toast.makeText(BaseSetupActivity.this, "不能这样滑动噢！",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// 判断滑动速度是否过慢
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "滑动速度太慢啦！",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// 向右滑，上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// 向左滑，下一页
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * 展示下一页,子类必须实现
	 */
	public abstract void showNextPage();

	/**
	 * 展示上一页，子类必须实现
	 * 
	 * @param view
	 */
	public abstract void showPreviousPage();

	// 下一页的按钮点击事件
	public void next(View view) {
		showNextPage();
	}

	// 上一页的按钮点击事件
	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event); // 委托手势识别器处理
		return super.onTouchEvent(event);
	}

}
