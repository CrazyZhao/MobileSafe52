package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.dao.AddressDao;

/**
 * 归属地查询页面
 * 
 * @author baoliang.zhao
 * 
 */
public class AddressActivity extends Activity {

	private EditText etNumber;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);

		// 监听EditText变化
		etNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	/*
	 * 查询
	 */
	public void query(View view) {
		String number = etNumber.getText().toString().trim();

		if (!TextUtils.isEmpty(number)) {
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			etNumber.startAnimation(shake);
			vibrate();
		}

	}

	/**
	 * 震动
	 */
	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// vibrator.vibrate(2000);震动两秒
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);//等待一秒，震动两秒，等待1秒，震动3秒；-1表示只执行一次，不循环，0表示从头开始循环
		
	}
}
