package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.ToastUtils;
import com.itheima52.mobilesafe.view.SettingItemView;

/**
 * 第二个设置向导页
 * 
 * @author baoliang.zhao
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		sivSim = (SettingItemView) findViewById(R.id.siv_sim);

		String sim = mPref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
		}

		sivSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
					// 删除sp中保存的SIM卡序列号
					mPref.edit().remove("sim").commit();
				} else {
					sivSim.setChecked(true);
					// 保存SIM卡信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNember = tm.getSimSerialNumber();
					// 将SIM卡序列号保存在sp中
					mPref.edit().putString("sim", simSerialNember).commit();
				}
			}
		});

	}

	@Override
	public void showNextPage() {
		// 如果SIM卡没有绑定，不允许进入下一个页面
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			ToastUtils.showToast(this, "SIM卡没有绑定！");
			return;
		}

		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}

}