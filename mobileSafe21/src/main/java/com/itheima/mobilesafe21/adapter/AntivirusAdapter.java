package com.itheima.mobilesafe21.adapter;

import java.util.List;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.AntivirusBean;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AntivirusAdapter extends BaseAdapter {
	private Context mContext;
	private List<AntivirusBean> mDatas;
	
	

	public AntivirusAdapter(Context mContext, List<AntivirusBean> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public AntivirusBean getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_antivitus_item, null);
			holder = new  ViewHolder();
			holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_antivirus_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.tv_antivirus_name);
			holder.tv_antivirus = (TextView) convertView.findViewById(R.id.tv_antivirus);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		AntivirusBean antivirusBean = getItem(position);
		holder.appIcon.setImageDrawable(antivirusBean.appIcon);
		holder.appName.setText(antivirusBean.appName);
		holder.tv_antivirus.setText(antivirusBean.isAntivirus?"病毒":"安全");
		holder.tv_antivirus.setTextColor(antivirusBean.isAntivirus?Color.RED:Color.GREEN);
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView appIcon;
		TextView appName;
		TextView tv_antivirus;
	}

}
