package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

/**
 * 修改归属地显示位置
 * 
 * @author baoliang.zhao
 * 
 */
public class DragViewActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrag;

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);

		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);

		// 设置触摸监听
		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标

					break;
				case MotionEvent.ACTION_MOVE:
					// 计算移动偏移量

					// 更新左上右下距离

					// 更新界面

					// 重新初始化起点坐标

					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					break;

				default:
					break;
				}
				return true;
			}
		});

	}
}
