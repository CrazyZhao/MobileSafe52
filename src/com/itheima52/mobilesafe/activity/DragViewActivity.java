package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

	private int startX;
	private int startY;
	private SharedPreferences mPref;

	long[] mHits = new long[2];// 数组长度表示要点击的次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		// onMeasure(测量view),onLayout(安放位置),onDraw(绘制);
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight()); 不能用这个方法，得按照固定的顺序完成，还没有测量完成，就不能安放位置

		// 屏幕宽度和高度
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		if (lastY > height / 2) { // 文字提示：上面的显示，下面的隐藏
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();// 获取布局对象
		layoutParams.leftMargin = lastX;// 设置左边距
		layoutParams.topMargin = lastY;// 设置上边距

		ivDrag.setLayoutParams(layoutParams);// 重新设置位置

		ivDrag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
				if ((SystemClock.uptimeMillis() - mHits[0]) <= 500) {
					// 把图片居中
					ivDrag.layout(width / 2 - ivDrag.getWidth() / 2,
							ivDrag.getTop(), width / 2 + ivDrag.getWidth() / 2,
							ivDrag.getBottom());
				}
			}
		});

		// 设置触摸监听
		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					// 更新左上右下距离
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;
					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;
					// 判断是否超出屏幕边界，注意状态栏的高度
					if (l < 0 || r > width || t < 0 || b > height - 20) {
						break;
					}

					if (t > height / 2) { // 文字提示：上面的显示，下面的隐藏
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}

					// 更新界面
					ivDrag.layout(l, t, r, b);
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor editor = mPref.edit();
					editor.putInt("lastX", ivDrag.getLeft());
					editor.putInt("lastY", ivDrag.getTop());
					editor.commit();
					break;

				default:
					break;
				}
				return false;//让时间继续往下传递，让onClick(双击时间)可以响应
			}
		});

	}
}
