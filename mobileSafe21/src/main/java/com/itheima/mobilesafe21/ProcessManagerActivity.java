package com.itheima.mobilesafe21;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe21.adapter.ProcessAdapter;
import com.itheima.mobilesafe21.bean.ProcessBean;
import com.itheima.mobilesafe21.engine.ProcessInfoProvider;
import com.itheima.mobilesafe21.view.ProcessDescView;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity {

	private ProcessDescView pdv_process_num;
	private ProcessDescView pdv_process_memory;
	private ListView lv_process;
	private List<ProcessBean> datas = new ArrayList<ProcessBean>();
	private List<ProcessBean> userDatas = new ArrayList<ProcessBean>();//用户进程的数据集合
	private List<ProcessBean> systemDatas = new ArrayList<ProcessBean>();//系统进程的数据集合
	private View ll_loading;
	private TextView tv_process_title_item;
	private View ll_process_item_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		initView();
		initData();
	}

	private void initView() {
		pdv_process_num = (ProcessDescView) findViewById(R.id.pdv_process_num);
		pdv_process_memory = (ProcessDescView) findViewById(R.id.pdv_process_memory);

		lv_process = (ListView) findViewById(R.id.lv_process);
		ll_loading = findViewById(R.id.ll_loading);

		tv_process_title_item = (TextView) findViewById(R.id.tv_process_title_item);
		ll_process_item_bg = findViewById(R.id.ll_process_item_bg);
		ll_process_item_bg.setVisibility(View.INVISIBLE);
		
		//给listview设置滚动监听
		lv_process.setOnScrollListener(new OnScrollListener() {
			//当滚动状态改变时，调用该方法
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			//当滚动时，实时被调用
			/**
			 * view listview自身
			 * firstVisibleItem：第一个可见条目的索引
			 * visibleItemCount：可见的条目的数量
			 * totalItemCount：总数量
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem >= userDatas.size()+1 ) {
					tv_process_title_item.setText("系统进程("+systemDatas.size()+")个");
				}else{
					tv_process_title_item.setText("用户进程("+userDatas.size()+")个");
				}
			}
		});
		
	}

	private void initData() {
		// 设置进程数展示
		initProcessNum();
		// 设置内存的展示
		initMemory();

		new Thread() {
			public void run() {
				SystemClock.sleep(2000);

				List<ProcessBean> processInfos = ProcessInfoProvider
						.getRunningProcessInfos(ProcessManagerActivity.this);
				
				for (ProcessBean processBean : processInfos) {
					if (processBean.isSystem) {
						systemDatas.add(processBean);
					}else{
						userDatas.add(processBean);
					}
				}

				datas.addAll(userDatas);
				datas.addAll(systemDatas);
				runOnUiThread(new Runnable() {
					public void run() {
						
						
						ll_loading.setVisibility(View.INVISIBLE);
						ll_process_item_bg.setVisibility(View.VISIBLE);
						
						tv_process_title_item.setText("用户进程("+userDatas.size()+")个");
						ProcessAdapter adapter = new ProcessAdapter(
								ProcessManagerActivity.this, userDatas,systemDatas);
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
		int runningProcessCount = ProcessInfoProvider
				.getRunningProcessCount(ProcessManagerActivity.this);
		int allProcessCount = ProcessInfoProvider
				.getAllProcessCount(ProcessManagerActivity.this);
		pdv_process_num.setTitle("进程数：");
		pdv_process_num.setLeftText("正在运行(" + runningProcessCount + ")个");
		pdv_process_num.setRightText("总进程(" + allProcessCount + ")个");
		// 为了四舍五入加上0.5f 4.9 ->5.4 4.1->4.6
		pdv_process_num.setProgress((int) (runningProcessCount * 100f
				/ allProcessCount + 0.5f));
	}

}
