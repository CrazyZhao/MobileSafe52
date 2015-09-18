package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima52.mobilesafe.R;

/**
 * 第三个设置向导页
 * 
 * @author baoliang.zhao
 * 
 */
public class Setup3Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}

	// 下一页
	public void next(View view) {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
	}

	// 上一页
	public void previous(View view) {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
	}

}
