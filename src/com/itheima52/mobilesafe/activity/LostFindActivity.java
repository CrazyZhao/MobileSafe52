package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 手机防盗页面
 * 
 * @author baoliang.zhao
 * 
 */
public class LostFindActivity extends Activity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		boolean configed = mPref.getBoolean("configed", false);
		if (configed) {
			// 如果进过向导了，则直接进入手机防盗页面
			setContentView(R.layout.activity_lost_find);
		} else {
			// 跳转设置向导页面
			startActivity(new Intent(LostFindActivity.this,
					Setup1Activity.class));
			finish();
		}

	}
}
