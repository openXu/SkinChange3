package com.openxu.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
	
	protected Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView();
	}
	
	protected abstract void initView();

}
