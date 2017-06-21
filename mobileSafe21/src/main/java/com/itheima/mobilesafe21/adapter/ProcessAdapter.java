package com.itheima.mobilesafe21.adapter;

import java.util.List;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.ProcessBean;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProcessAdapter extends BaseAdapter {

	private Context mContext;
	private List<ProcessBean> mUserDatas;
	private List<ProcessBean> mSystemDatas;

	public ProcessAdapter(Context mContext, List<ProcessBean> userDatas,
			List<ProcessBean> systemDatas) {
		this.mContext = mContext;
		this.mUserDatas = userDatas;
		this.mSystemDatas = systemDatas;
	}

	@Override
	public int getCount() {
		return mUserDatas.size() + mSystemDatas.size() + 2;
	}

	// 获取对应条目的数据对象
	@Override
	public ProcessBean getItem(int position) {
		// if (position < mUserDatas.size()) {
		// return mUserDatas.get(position);
		// }else{
		// return mSystemDatas.get(position - mUserDatas.size());
		// }

//		if (position == 0) {// 第一个用户标题
//			return null;
//		}
//
//		if (position < mUserDatas.size() + 1) {
//			return mUserDatas.get(position - 1);
//		}
//
//		if (position == mUserDatas.size() + 1) {// 第二个系统标题
//			return null;
//		}
//
//		if (position > mUserDatas.size() + 1) {
//			return mSystemDatas.get(position - (mUserDatas.size() + 2));
//		}
//		return null;

		
		if (position == 0 || position == mUserDatas.size() + 1) {
			return null;
		} else if (position < mUserDatas.size() + 1) {
			return mUserDatas.get(position - 1);
		} else {
			return mSystemDatas.get(position - (mUserDatas.size() + 2));
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 获取条目视图的类型(注意：类型值必须从0开始)
	@Override
	public int getItemViewType(int position) {
		if (position == 0 || position == mUserDatas.size() + 1) {
			return 0;// 标题型
		} else {
			return 1;// 列表型
		}

	}

	// 获取视图类型的数量
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// 根据索引获取当前条目的类型值，根据类型值创建不同的条目进行展示
		int itemViewType = getItemViewType(position);

		switch (itemViewType) {
		case 0:// 标题型
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.view_process_item_title, null);
			}

			TextView tv = (TextView) convertView
					.findViewById(R.id.tv_process_title_item);

			if (position == 0) {
				tv.setText("用户进程(" + mUserDatas.size() + ")个");
			} else {
				tv.setText("系统进程(" + mSystemDatas.size() + ")个");
			}
			break;
		case 1:// 列表型
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.view_process_item, null);
				holder = new ViewHolder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.appMemory = (TextView) convertView
						.findViewById(R.id.tv_memory);
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ProcessBean processBean = getItem(position);

			holder.appName.setText(processBean.appName);

			holder.appIcon.setImageDrawable(processBean.appIcon);
			holder.appMemory.setText(Formatter.formatFileSize(mContext,
					processBean.appMemory));
			break;

		default:
			break;
		}

		return convertView;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appName;
		TextView appMemory;
		CheckBox cb;
	}

}
