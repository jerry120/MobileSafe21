package com.itheima.mobilesafe21.view;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.adapter.LocationAdapter;
import com.itheima.mobilesafe21.bean.LocationBean;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.SpUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LocationStyleDialog extends Dialog implements OnItemClickListener {

	private String[] mTitles = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	private int[] imageIds = new int[] { R.drawable.shape_location_bg,
			R.drawable.shape_location_orange, R.drawable.shape_location_blue,
			R.drawable.shape_location_gray, R.drawable.shape_location_green };

	private ListView lv;

	private List<LocationBean> datas;

	private LocationAdapter adapter;

	public LocationStyleDialog(Context context) {
		super(context, R.style.Style_Location_Dialog);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_locaiton_dialog);
		lv = (ListView) findViewById(R.id.lv);
		datas = new ArrayList<LocationBean>();
		for (int i = 0; i < mTitles.length; i++) {
			LocationBean bean = new LocationBean();
			bean.title = mTitles[i];
			bean.imageId = imageIds[i];
			datas.add(bean);
		}
		adapter = new LocationAdapter(getContext(),datas);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//记录点击的索引，并让适配器的getview方法重新执行，并根据点击索引将图片设置为显示与隐藏即可
		//必须记录颜色，为归属地的显示做准备
		SpUtil.saveInt(getContext(), Constant.KEY_LOCATION_STYLE_COLOR, imageIds[position]);
		//重新设置适配让getview方法重新执行
		//在一个界面中适配器只会被创建一次，而且只会被设置一次，如果要以最新的数据刷新listview的话，必须使用刷新方法notifyDataSetChanged
//		LocationAdapter adapter = new LocationAdapter(getContext(),datas);
//		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();//可以让getview重新执行，并且不会改变当前的listview展示列表状态
		
	}

}
