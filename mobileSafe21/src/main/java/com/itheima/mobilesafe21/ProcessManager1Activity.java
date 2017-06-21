package com.itheima.mobilesafe21;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.itheima.mobilesafe21.adapter.ProcessAdapter;
import com.itheima.mobilesafe21.bean.ProcessBean;
import com.itheima.mobilesafe21.engine.ProcessInfoProvider;
import com.itheima.mobilesafe21.service.AutoCleanService;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.SeriveStateUtil;
import com.itheima.mobilesafe21.utils.SpUtil;
import com.itheima.mobilesafe21.view.ProcessDescView;
import com.itheima.mobilesafe21.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class ProcessManager1Activity extends Activity implements
		OnClickListener {

	private ProcessDescView pdv_process_num;
	private ProcessDescView pdv_process_memory;
	private StickyListHeadersListView lv_process;
	private List<ProcessBean> datas = new ArrayList<ProcessBean>();
	private List<ProcessBean> userDatas = new ArrayList<ProcessBean>();// 用户进程的数据集合
	private List<ProcessBean> systemDatas = new ArrayList<ProcessBean>();// 系统进程的数据集合
	private View ll_loading;
	private Button bt_process_all;
	private Button bt_process_reverse;
	private MyAdapter adapter;
	private ImageButton ib_process_clean;
	private int runningProcessCount;
	SlidingDrawer  dd;
	private ImageView iv_arrow1;
	private ImageView iv_arrow2;
	private SlidingDrawer sldingdrawer;
	private SettingItemView siv_process_show;
	private SettingItemView siv_process_clean;
	private boolean isSystemShow;//用于记录系统进程是否要显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager1);
		initView();
		initData();
	}

	private void initView() {
		pdv_process_num = (ProcessDescView) findViewById(R.id.pdv_process_num);
		pdv_process_memory = (ProcessDescView) findViewById(R.id.pdv_process_memory);

		lv_process = (StickyListHeadersListView) findViewById(R.id.lv_process);
		ll_loading = findViewById(R.id.ll_loading);

		bt_process_all = (Button) findViewById(R.id.bt_process_all);
		bt_process_reverse = (Button) findViewById(R.id.bt_process_reverse);

		bt_process_all.setOnClickListener(this);
		bt_process_reverse.setOnClickListener(this);

		// 获取清理按钮
		ib_process_clean = (ImageButton) findViewById(R.id.ib_process_clean);

		ib_process_clean.setOnClickListener(this);
		
		//获取箭头控件
		iv_arrow1 = (ImageView) findViewById(R.id.iv_arrow1);
		iv_arrow2 = (ImageView) findViewById(R.id.iv_arrow2);
		startArrowAnimation();
		
		sldingdrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		sldingdrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				startArrowAnimation();
			}
		});
		sldingdrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				stopArrowAnimation();
			}
		});
		
		//获取siv控件
		siv_process_show = (SettingItemView) findViewById(R.id.siv_process_show);
		siv_process_clean = (SettingItemView) findViewById(R.id.siv_process_clean);
		siv_process_clean.setOnClickListener(this);
		siv_process_show.setOnClickListener(this);
		
		//动态从sp中获取用户设置的状态
		 isSystemShow = SpUtil.getBoolean(getApplicationContext(), Constant.KEY_SYSTEM_SHOW, true);
		siv_process_show.setToggle(isSystemShow);
		
	}

	protected void stopArrowAnimation() {
		iv_arrow1.clearAnimation();
		iv_arrow2.clearAnimation();
		iv_arrow1.setImageResource(R.drawable.drawer_arrow_down);
		iv_arrow2.setImageResource(R.drawable.drawer_arrow_down);
	}

	private void startArrowAnimation() {
		iv_arrow1.setImageResource(R.drawable.drawer_arrow_up);
		iv_arrow2.setImageResource(R.drawable.drawer_arrow_up);
		
		AlphaAnimation aa1 = new AlphaAnimation(0.2f, 1.0f);
		aa1.setDuration(500);
		aa1.setRepeatCount(Animation.INFINITE);
		aa1.setRepeatMode(Animation.REVERSE);
		iv_arrow1.startAnimation(aa1);
		AlphaAnimation aa2 = new AlphaAnimation(1.0f, 0.2f);
		aa2.setDuration(500);
		aa2.setRepeatCount(Animation.INFINITE);
		aa2.setRepeatMode(Animation.REVERSE);
		iv_arrow2.startAnimation(aa2);
	}

	private void initData() {
		// 设置进程数展示
		runningProcessCount = ProcessInfoProvider
				.getRunningProcessCount(ProcessManager1Activity.this);
		initProcessNum();
		// 设置内存的展示
		initMemory();

		new Thread() {
			public void run() {
				SystemClock.sleep(2000);

				List<ProcessBean> processInfos = ProcessInfoProvider
						.getRunningProcessInfos(ProcessManager1Activity.this);

				for (ProcessBean processBean : processInfos) {
					if (processBean.isSystem) {
						systemDatas.add(processBean);
					} else {
						userDatas.add(processBean);
					}
				}

				datas.addAll(userDatas);
				datas.addAll(systemDatas);
				runOnUiThread(new Runnable() {

					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						adapter = new MyAdapter();

						lv_process.setAdapter(adapter);
					}
				});

			};
		}.start();

	}

	private void initMemory() {
		long avialMemory = ProcessInfoProvider
				.getAvialMemory(getApplicationContext());
		long totalMemory = ProcessInfoProvider
				.getTotalMemory(getApplicationContext());
		long usedMemory = totalMemory - avialMemory;

		pdv_process_memory.setTitle("内存：");
		pdv_process_memory
				.setLeftText("占用内存:"
						+ Formatter.formatFileSize(getApplicationContext(),
								usedMemory));
		pdv_process_memory.setRightText("可用内存:"
				+ Formatter
						.formatFileSize(getApplicationContext(), avialMemory));
		// 为了四舍五入加上0.5f 4.9 ->5.4 4.1->4.6
		pdv_process_memory
				.setProgress((int) (usedMemory * 100f / totalMemory + 0.5f));
	}

	private void initProcessNum() {
		
		int allProcessCount = ProcessInfoProvider
				.getAllProcessCount(ProcessManager1Activity.this);
		pdv_process_num.setTitle("进程数：");
		pdv_process_num.setLeftText("正在运行(" + runningProcessCount + ")个");
		pdv_process_num.setRightText("总进程(" + allProcessCount + ")个");
		// 为了四舍五入加上0.5f 4.9 ->5.4 4.1->4.6
		pdv_process_num.setProgress((int) (runningProcessCount * 100f
				/ allProcessCount + 0.5f));
	}

	private class MyAdapter extends BaseAdapter implements
			StickyListHeadersAdapter {

		@Override
		public int getCount() {
			if (isSystemShow) {
				return datas.size();
			}else{
				return userDatas.size();
			}
		}

		@Override
		public ProcessBean getItem(int position) {
			if (isSystemShow) {
				return datas.get(position);
			}else{
				return userDatas.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(ProcessManager1Activity.this,
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

			final ProcessBean processBean = getItem(position);

			holder.appName.setText(processBean.appName);

			holder.appIcon.setImageDrawable(processBean.appIcon);
			holder.appMemory.setText(Formatter.formatFileSize(
					ProcessManager1Activity.this, processBean.appMemory));

			// 添加选中监听，为了在数据层记录条目状态解决视图复用bug
			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					processBean.isChecked = isChecked;
				}
			});

			// 根据每个条目的数据设置是否选中
			// if (processBean.isChecked) {
			// holder.cb.setChecked(true);
			// }else{
			// holder.cb.setChecked(false);
			// }

			holder.cb.setChecked(processBean.isChecked);
			//判断当前条目的包名是否与本应用一致，如果一致则隐藏
			if (TextUtils.equals(processBean.packageName, getPackageName())) {
				holder.cb.setVisibility(View.INVISIBLE);
			}else{
				holder.cb.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView appIcon;
			TextView appName;
			TextView appMemory;
			CheckBox cb;
		}

		// 获取头视图
		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			// 根据头视图的id创建不同头视图进行显示
			int headerId = (int) getHeaderId(position);

			convertView = View.inflate(ProcessManager1Activity.this,
					R.layout.view_process_item_title, null);
			TextView tv_process_title_item = (TextView) convertView
					.findViewById(R.id.tv_process_title_item);
			switch (headerId) {
			case 0:
				tv_process_title_item.setText("系统进程(" + systemDatas.size()
						+ ")个");
				break;
			case 1:

				tv_process_title_item
						.setText("用户进程(" + userDatas.size() + ")个");
				break;

			default:
				break;
			}
			return convertView;
		}

		// 获取头视图的id（类型与复杂的listview的类型值方法）
		@Override
		public long getHeaderId(int position) {
			ProcessBean processBean = getItem(position);
			if (processBean.isSystem) {

				return 0;// 系统头视图的id
			} else {
				return 1;// 用户头视图的id
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_process_all:// 全选
			
			List<ProcessBean> allDatas= null;
			if (isSystemShow) {
				allDatas = datas;
			}else{
				allDatas = userDatas;
			}
			
			// 遍历所有的数据，将每个数据的状态设置为选中并刷新适配器即可
			for (ProcessBean bean : allDatas) {
				//如果遇到了本应用的包名就跳过
				if (TextUtils.equals(bean.packageName, getPackageName())) {
					continue;
				}
				bean.isChecked = true;
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_process_reverse:// 反选
			
			List<ProcessBean> reverseDatas= null;
			if (isSystemShow) {
				reverseDatas = datas;
			}else{
				reverseDatas = userDatas;
			}
			// 遍历所有的数据，将每个数据的状态设置为相反状态并刷新适配器即可
			for (ProcessBean bean : reverseDatas) {
				//如果遇到了本应用的包名就跳过
				if (TextUtils.equals(bean.packageName, getPackageName())) {
					continue;
				}
				bean.isChecked = !bean.isChecked;
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.ib_process_clean:// 清理
			// 遍历所有的数据，如果勾选了，先清理，再从集合中删除，刷新即可
			// for (ProcessBean bean : datas) {
			// if (bean.isChecked) {
			// ProcessInfoProvider.cleanProcess(getApplicationContext(),
			// bean.packageName);
			// datas.remove(bean);
			// }
			// }
			
			List<ProcessBean> cleanDatas= null;
			if (isSystemShow) {
				cleanDatas = datas;
			}else{
				cleanDatas = userDatas;
			}
			
			ListIterator<ProcessBean> listIterator = cleanDatas.listIterator();
			while (listIterator.hasNext()) {
				ProcessBean processBean = (ProcessBean) listIterator.next();
				if (processBean.isChecked) {
					ProcessInfoProvider.cleanProcess(getApplicationContext(),
							processBean.packageName);
					listIterator.remove();
					//进行判断，如果删除的是用户数据则对应用户集合也要删除保证userdatas与datas里面的数据统一（系统数据集合也做相同操作）
					if (isSystemShow) {
						if (processBean.isSystem) {
							systemDatas.remove(processBean);
						}else{
							userDatas.remove(processBean);
						}
					}else{//系统进程隐藏，remove删除的是userdatas里面的数据，因此，要将datas里面的数据也删除保证统一性
						
						datas.remove(processBean);
					}
					
				}
			}
			adapter.notifyDataSetChanged();
			
			//初始化进程数量
			runningProcessCount = datas.size();
			initProcessNum();
			//初始化内存数量
			long usedMemory = 0;
			for (ProcessBean bean : datas) {
				usedMemory += bean.appMemory;
			}
			initCleanMemory(usedMemory);

			break;
			
		case R.id.siv_process_show://是否显示系统进程
			siv_process_show.toggle();
			SpUtil.saveBoolean(getApplicationContext(), Constant.KEY_SYSTEM_SHOW, siv_process_show.isToggle());
			//根据状态值，动态刷新listview的界面展示
			isSystemShow = siv_process_show.isToggle();
			adapter.notifyDataSetChanged();
			break;
		case R.id.siv_process_clean://锁屏自动清理
			siv_process_clean.toggle();
			//根据服务的状态，取反操作
			if (SeriveStateUtil.isServiceRunning(getApplicationContext(), AutoCleanService.class)) {
				stopService(new Intent(ProcessManager1Activity.this, AutoCleanService.class));
			}else{
				startService(new Intent(ProcessManager1Activity.this, AutoCleanService.class));
			}
			break;

		default:
			break;
		}
	}
	
	
	private void initCleanMemory(long usedMemory) {
		long totalMemory = ProcessInfoProvider
				.getTotalMemory(getApplicationContext());
		long avialMemory = totalMemory - usedMemory;

		pdv_process_memory.setTitle("内存：");
		pdv_process_memory
				.setLeftText("占用内存:"
						+ Formatter.formatFileSize(getApplicationContext(),
								usedMemory));
		pdv_process_memory.setRightText("可用内存:"
				+ Formatter
						.formatFileSize(getApplicationContext(), avialMemory));
		// 为了四舍五入加上0.5f 4.9 ->5.4 4.1->4.6
		pdv_process_memory
				.setProgress((int) (usedMemory * 100f / totalMemory + 0.5f));
	}
	
	//由于用户会手动将服务关闭
	@Override
	protected void onResume() {
		super.onResume();
		if (SeriveStateUtil.isServiceRunning(getApplicationContext(), AutoCleanService.class)) {
			siv_process_clean.setToggle(true);
		}else{
			siv_process_clean.setToggle(false);
		}
	}

}
