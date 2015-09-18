package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima52.mobilesafe.R;

/**
 * 第四个设置向导页
 * 
 * @author baoliang.zhao
 * 
 */
public class Setup4Activity extends Activity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}

	// 下一页
	public void next(View view) {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		
		mPref.edit().putBoolean("configed", true).commit();//更新sp，设置为true,表示已经设置过向导了
	}

	// 上一页
	public void previous(View view) {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
	}

}
