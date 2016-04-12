package com.openxu.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class ChangeSkinActivity extends BaseActivity
{
	private ListView list;
	private MyAdapter adapter;
	private String skinNow;

	@Override
	protected void initView() {
		setContentView(R.layout.change_skin);
		adapter = new MyAdapter();
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		//查找所有已安装的皮肤
		ArrayList<PackageInfo> skinList = getAllSkin();
		adapter.setData(skinList);
	}
	
	/**
	 * 获取所有已安装的皮肤主题
	 * @return
	 */
	private ArrayList<PackageInfo> getAllSkin() {
		ArrayList<PackageInfo> skinList = new ArrayList<PackageInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (PackageInfo p : packs) {
			if (isSkinPackage(p.packageName)) {
				skinList.add(p);
			}
		}
		return skinList;
	}

	/**
	 * 判断是否是皮肤主题
	 */
	private boolean isSkinPackage(String packageName) {
		String rex = Contact.SKIN_APP_PACKNAME+"\\w";
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(packageName);
		return matcher.find();
	}
	
	
	class MyAdapter extends BaseAdapter {
		SharedPreferences sp;
		private ArrayList<PackageInfo> skinList;
		public MyAdapter(){
			sp = mContext.getSharedPreferences(Contact.SP_NAME, Context.MODE_PRIVATE); 
			skinNow = sp.getString(Contact.SP_KEY_SKIN, "");
			Log.d("openxu", "当前选中的皮肤包名"+skinNow);
		}
		@Override
		public int getCount() {
			return skinList == null?0:skinList.size();
		}

		public void setData(ArrayList<PackageInfo> skinList){
			this.skinList = skinList;
			notifyDataSetChanged();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.change_skin_item, null);
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
			final String packname = skinList.get(position).packageName;
			cb.setText(getApplicationName(packname));
			if(!TextUtils.isEmpty(skinNow) && skinNow.equals(packname)){
				cb.setChecked(true);
			}else{
				cb.setChecked(false);
			}
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						sp.edit().putString(Contact.SP_KEY_SKIN, packname).commit();
						skinNow = packname;
						notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}

	}

	 public String getApplicationName(String packname) { 
		 
	        PackageManager packageManager = null; 
	        ApplicationInfo applicationInfo = null; 
	        try { 
	        	Context context = createPackageContext(packname, Context.CONTEXT_IGNORE_SECURITY);
	            packageManager = context.getPackageManager(); 
	            applicationInfo = packageManager.getApplicationInfo(packname, 0); 
	        } catch (PackageManager.NameNotFoundException e) { 
	            applicationInfo = null; 
	        } 
	        return (String) packageManager.getApplicationLabel(applicationInfo); 
	    } 
	 

	
}
