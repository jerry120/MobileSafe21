package com.itheima.mobilesafe21.adapter;

import java.util.List;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.LocationBean;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.SpUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {

	private List<LocationBean> mDatas;
	private Context mContext;

	public LocationAdapter(Context context, List<LocationBean> datas) {
		this.mContext = context;
		this.mDatas = datas;
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
		ViewHolder holder =  null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_location_dialog_item, null);
			holder = new ViewHolder();
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		LocationBean locationBean = mDatas.get(position);
		holder.tv.setText(locationBean.title);
		holder.iv_icon.setImageResource(locationBean.imageId);
		//判断sp里面的颜色与当前条目的颜色是否一致，如果一致，则显示选中图标，否则就隐藏
		int colorId = SpUtil.getInt(mContext, Constant.KEY_LOCATION_STYLE_COLOR, mDatas.get(0).imageId);
		if (colorId == locationBean.imageId) {
			holder.iv_selected.setVisibility(View.VISIBLE);
		}else {
			holder.iv_selected.setVisibility(View.INVISIBLE);
		}
		
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv_icon;
		ImageView iv_selected;
		TextView tv;
	}

}
