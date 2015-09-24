package com.itheima52.mobilesafe.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

	public static void showToast(Context ctx, String text) {
		Toast toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
