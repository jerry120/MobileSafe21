package com.itheima.mobilesafe21;

import com.itheima.mobilesafe21.db.CommonNumberDao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class CommonNumberActivity extends Activity {

	private ExpandableListView elv;

	private int prevousExpandedPosition = -1;// 用来记录前一个打开的索引

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);
		initView();
	}

	private void initView() {
		elv = (ExpandableListView) findViewById(R.id.elv);
		MyAdapter adapter = new MyAdapter();
		elv.setAdapter(adapter);
		// 组的点击事件
		elv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// 2.判断当前是否有组展开，如果有 先关闭
				if (prevousExpandedPosition != -1
						&& prevousExpandedPosition != groupPosition) {
					elv.collapseGroup(prevousExpandedPosition);
				}

				// 1.点击组的时候根据组的状态来打开或关
				if (elv.isGroupExpanded(groupPosition)) {
					// 当前是开，则关闭组
					elv.collapseGroup(groupPosition);
				} else {
					// 当前是关，则打开组
					elv.expandGroup(groupPosition);

					prevousExpandedPosition = groupPosition;// 记录展示的索引
				}

				// return false;//不消费事件，将事件的处理传递给ExpandableListView的源码来进行处理

				// 点击哪一个组 ，直接跳转对应组
				elv.setSelection(groupPosition);

				return true;// 自己处理
			}
		});

		// 子条目点击事件
		elv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String[] childText = CommonNumberDao.getChildText(groupPosition, childPosition, CommonNumberActivity.this);
				
				Intent intent = new Intent();
				// 06-10 06:34:54.594: I/ActivityManager(1023): START
				// {act=android.intent.action.DIAL dat=tel:xxxxxxxxxx
				// cmp=com.android.contacts/.activities.DialtactsActivity u=0}
				// from pid 1975
				intent.setAction("android.intent.action.DIAL");
				intent.setData(Uri.parse("tel:"+childText[1]));
				startActivity(intent);
				return true;
			}
		});
	}

	private class MyAdapter extends BaseExpandableListAdapter {

		// 获取组条目的数量（与getCount类似）
		@Override
		public int getGroupCount() {
			int groupCount = CommonNumberDao
					.getGroupCount(CommonNumberActivity.this);
			return groupCount;
		}

		// 获取某一个组的子条目数量
		@Override
		public int getChildrenCount(int groupPosition) {
			int childrencount = CommonNumberDao.getChildrenCount(groupPosition,
					CommonNumberActivity.this);
			return childrencount;
		}

		// 获取组条目的视图
		/**
		 * groupPosition:组的索引 isExpanded：是否打开状态 convertView：复用类 parent：父容器
		 * ExpandableListView
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv = new TextView(CommonNumberActivity.this);
			// tv.setText("groupPosition:"+groupPosition);
			String groupText = CommonNumberDao.getGroupText(groupPosition,
					CommonNumberActivity.this);
			tv.setPadding(5, 5, 5, 5);
			tv.setText(groupText);
			tv.setBackgroundColor(Color.parseColor("#9C9A9C"));// 直接将16进制的颜色值设置进来
			return tv;
		}

		// 获取哪一个组的哪一个子条目的视图
		/**
		 * groupPosition:组的索引 childPosition:子条目的索引 isLastChild：是否是最后一个子条目
		 * convertView：复用类 parent：父容器 ExpandableListView
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			TextView tv = new TextView(CommonNumberActivity.this);
			// tv.setText("groupPosition:"+groupPosition+"childPosition:"+childPosition);
			String[] data = CommonNumberDao.getChildText(groupPosition,
					childPosition, CommonNumberActivity.this);
			tv.setText(data[0] + "\n" + data[1]);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackgroundColor(Color.parseColor("#D6D7D6"));// 直接将16进制的颜色值设置进来
			return tv;
		}

		// 设置子条目是否可以被点击
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		// ----------------------------以下方法不用获取可以不实现

		// 获取某一个组的条目对象（与getitem类似）
		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		// 获取某一个组的某一个子条目对象
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		// 获取某一个组条目的id（类似getItemId方法）
		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		// 获取某一个组的某一个子条目的id
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		// 是否拥有稳定的id（如果一个id对应多个条目那么就不稳定，一个id对应一个条目那就是稳定的）
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
