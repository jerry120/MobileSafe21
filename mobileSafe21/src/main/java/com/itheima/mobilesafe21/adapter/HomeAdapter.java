package com.itheima.mobilesafe21.adapter;

import java.util.List;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.HomeBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {

	private List<HomeBean> mDatas;
	private Context mContext;

	public HomeAdapter(List<HomeBean> datas, Context context) {
		this.mDatas = datas;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), R.layout.view_gridview_item, null);
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_gridview_item, parent, false);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		HomeBean homeBean = mDatas.get(position);
		holder.tv_title.setText(homeBean.title);
		holder.tv_desc.setText(homeBean.desc);
//		holder.iv.setBackgroundResource(homeBean.imageId);//与android:background属性一一对应，会覆盖掉背景
		holder.iv.setImageResource(homeBean.imageId);//与android:src属性一一对应，会覆盖掉图片
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView tv_title;
		TextView tv_desc;
		
	}

}
