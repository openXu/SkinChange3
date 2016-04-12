package com.openxu.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity {
	
	private LinearLayout layout;
	@Override
	protected void initView() {
		setContentView(R.layout.main);
		layout = (LinearLayout) findViewById(R.id.layout);
		changeSkin();
	}
	
	private void changeSkin(){
		String skinPackName = mContext.getSharedPreferences(Contact.SP_NAME, Context.MODE_PRIVATE).getString(Contact.SP_KEY_SKIN, ""); 
		if(TextUtils.isEmpty(skinPackName)){
			//使用默认皮肤
			layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_bg));
		}else{
			//使用皮肤主题
			try {
				final Context context = createPackageContext(skinPackName, Context.CONTEXT_IGNORE_SECURITY);
				//此处应该根据皮肤apk中的资源ID找到皮肤资源，而不能直接用本应用中的R
				//如果皮肤资源和应用中的资源完全一样（数量和种类）才能直接使用应用中的R
				//layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_bg));
				Resources resources = context.getResources();
				int indentify = resources.getIdentifier(context.getPackageName()+":drawable/activity_bg", null, null);
				if(indentify>0){
					layout.setBackgroundDrawable(context.getResources().getDrawable(indentify));
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		changeSkin();
	}
	
	public void change(View v){
		startActivityForResult(new Intent(mContext, ChangeSkinActivity.class), 0);
	}
	
}
