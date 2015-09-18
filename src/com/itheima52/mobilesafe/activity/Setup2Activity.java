package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima52.mobilesafe.R;

/**
 * 第二个设置向导页
 * 
 * @author baoliang.zhao
 * 
 */
public class Setup2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}

	// 下一页
	public void next(View view) {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
	}

	// 上一页
	public void previous(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}

}
