package com.itheima.mobilesafe21;

import java.util.ArrayList;
import java.util.List;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.itheima.mobilesafe21.adapter.AntivirusAdapter;
import com.itheima.mobilesafe21.bean.AntivirusBean;
import com.itheima.mobilesafe21.bean.AppInfoBean;
import com.itheima.mobilesafe21.db.AntivirusDao;
import com.itheima.mobilesafe21.engine.AppInfoProvider;
import com.itheima.mobilesafe21.utils.MD5Utils;

import android.R.array;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AntvirusActivity extends Activity implements OnClickListener {

	private ListView lv_antvirus;
	private List<AntivirusBean> datas = new ArrayList<AntivirusBean>();
	private AntivirusAdapter adapter;
	private LinearLayout ll_antivirus_scan;
	private ArcProgress arc_progress;
	private TextView tv_antivirus_scan;
	private LinearLayout ll_antivirus_result;
	private TextView tv_antivirus_result;
	private Button bt_antivirus_result;
	private LinearLayout ll_antivirus_animation;
	private ImageView iv_antivirus_animation_left;
	private ImageView iv_antivirus_animation_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antvirus);

		initView();
		initData();
	}

	private void initData() {
		AntvirusTask antvirusTask = new AntvirusTask();
		antvirusTask.execute();
	}

	private void initView() {
		lv_antvirus = (ListView) findViewById(R.id.lv_antvirus);

		// 扫描界面
		ll_antivirus_scan = (LinearLayout) findViewById(R.id.ll_antivirus_scan);
		arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
		tv_antivirus_scan = (TextView) findViewById(R.id.tv_antivirus_scan);

		// 扫描结果界面
		ll_antivirus_result = (LinearLayout) findViewById(R.id.ll_antivirus_result);
		tv_antivirus_result = (TextView) findViewById(R.id.tv_antivirus_result);
		bt_antivirus_result = (Button) findViewById(R.id.bt_antivirus_result);

		bt_antivirus_result.setOnClickListener(this);

		// 扫描动画界面
		ll_antivirus_animation = (LinearLayout) findViewById(R.id.ll_antivirus_animation);
		iv_antivirus_animation_left = (ImageView) findViewById(R.id.iv_antivirus_animation_left);
		iv_antivirus_animation_right = (ImageView) findViewById(R.id.iv_antivirus_animation_right);

	}

	private class AntvirusTask extends AsyncTask<Void, AntivirusBean, Void> {

		private int max;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			adapter = new AntivirusAdapter(AntvirusActivity.this, datas);
			lv_antvirus.setAdapter(adapter);
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<AppInfoBean> allAppInfos = AppInfoProvider
					.getAllAppInfos(AntvirusActivity.this);
			max = allAppInfos.size();
			for (AppInfoBean appInfoBean : allAppInfos) {
				String fileMd5 = MD5Utils.getFileMd5(appInfoBean.apkPath);
				boolean isAntivirus = false;
				if (AntivirusDao.queryAntivirus(AntvirusActivity.this, fileMd5)) {
					isAntivirus = true;
				}

				System.out.println(appInfoBean.packageName + " : " + fileMd5
						+ "," + isAntivirus);
				AntivirusBean bean = new AntivirusBean();
				bean.appIcon = appInfoBean.appIcon;
				bean.appName = appInfoBean.appName;
				bean.isAntivirus = isAntivirus;
				// if (isAntivirus) {
				// datas.add(0, bean);
				// } else {
				// datas.add(bean);
				// }

				// 只要列表数据添加一个之后，立马让listview显示到底部即可
				publishProgress(bean);

				SystemClock.sleep(50);

			}

			return null;
		}

		// The content of the adapter has changed but ListView did not receive a
		// notification. Make sure the content of your adapter is not modified
		// from a background thread, but only from the UI thread. [in
		// ListView(2131034115, class android.widget.ListView) with
		// Adapter(class com.itheima.mobilesafe21.adapter.AntivirusAdapter)]

		@Override
		protected void onProgressUpdate(AntivirusBean... values) {
			super.onProgressUpdate(values);
			AntivirusBean antivirusBean = values[0];

			if (antivirusBean.isAntivirus) {
				datas.add(0, antivirusBean);
			} else {
				datas.add(antivirusBean);
			}

			arc_progress.setProgress((int) (datas.size() * 100f / max + 0.5f));

			tv_antivirus_scan.setText(antivirusBean.appName);

			adapter.notifyDataSetChanged();
			// lv_antvirus.setSelection(datas.size() -1);
			// lv_antvirus.setSelection(adapter.getCount() -1);//直接跳转到某个条目不带滚动效果
			lv_antvirus.smoothScrollToPosition(adapter.getCount() - 1);// 跳转到某个条目带有滚动效果
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// 扫描结束后，将结果显示出来
			lv_antvirus.smoothScrollToPosition(0);
			ll_antivirus_result.setVisibility(View.VISIBLE);
			AntivirusBean antivirusBean = datas.get(0);
			tv_antivirus_result
					.setText(antivirusBean.isAntivirus ? "您的手机很危险，请注意"
							: "您的手机很安全，请保持");

			// 将扫描结束后的百分之百的界面获取到
			ll_antivirus_animation.setVisibility(View.VISIBLE);
			ll_antivirus_scan.setDrawingCacheEnabled(true);
			Bitmap drawingCache = ll_antivirus_scan.getDrawingCache();

			// 获取左边半的图片
			Bitmap leftBitmap = getLeftBitmap(drawingCache);
			iv_antivirus_animation_left.setImageBitmap(leftBitmap);
			// 获取右半边图片
			Bitmap rightBitmap = getRightBitmap(drawingCache);
			iv_antivirus_animation_right.setImageBitmap(rightBitmap);

			// 开门动画
			ll_antivirus_scan.setVisibility(View.INVISIBLE);
			startOpenDoorAnimation();
		}

	}

	public Bitmap getLeftBitmap(Bitmap drawingCache) {
		// 创建出一个空白，宽是原图的一半，高是原图的高度
		int width = drawingCache.getWidth() / 2;
		int height = drawingCache.getHeight();
		Config config = drawingCache.getConfig();
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);// 创建出一个与bitmap同样大小的画布出来
		Matrix matrix = new Matrix();// 矩阵是设置drawingCache图片以什么形式（旋转，平移，缩放，透明度）绘制到画布上
		canvas.drawBitmap(drawingCache, matrix, null);
		return bitmap;
	}

	public void startOpenDoorAnimation() {
		// 左边图片左移，透明度
		ObjectAnimator oa1 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_left, "translationX", 0,
				-iv_antivirus_animation_left.getWidth());
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_left, "alpha", 1.0f, 0.0f);
		// 右边图片右移，透明度

		ObjectAnimator oa3 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_right, "translationX", 0,
				iv_antivirus_animation_right.getWidth());
		ObjectAnimator oa4 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_right, "alpha", 1.0f, 0.0f);
		// 结果界面透明度
		ObjectAnimator oa5 = ObjectAnimator.ofFloat(ll_antivirus_result,
				"alpha", 0.0f, 1.0f);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(oa1, oa2, oa3, oa4, oa5);
		set.setDuration(1500);
		set.start();

	}

	public Bitmap getRightBitmap(Bitmap drawingCache) {
		// 创建出一个空白，宽是原图的一半，高是原图的高度
		int width = drawingCache.getWidth() / 2;
		int height = drawingCache.getHeight();
		Config config = drawingCache.getConfig();
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);// 创建出一个与bitmap同样大小的画布出来
		Matrix matrix = new Matrix();// 矩阵是设置drawingCache图片以什么形式（旋转，平移，缩放）绘制到画布上
		matrix.setTranslate(-width, 0);
		canvas.drawBitmap(drawingCache, matrix, null);
		return bitmap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_antivirus_result:
			stopDoorAnimation();
			break;

		default:
			break;
		}
	}

	private void stopDoorAnimation() {
		// 左边图片右移，透明度
		ObjectAnimator oa1 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_left, "translationX",
				-iv_antivirus_animation_left.getWidth(), 0);
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_left, "alpha", 0.0f, 1.0f);
		// 右边图片左移，透明度

		ObjectAnimator oa3 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_right, "translationX",
				iv_antivirus_animation_right.getWidth(), 0);
		ObjectAnimator oa4 = ObjectAnimator.ofFloat(
				iv_antivirus_animation_right, "alpha", 0.0f, 1.0f);
		// 结果界面透明度
		ObjectAnimator oa5 = ObjectAnimator.ofFloat(ll_antivirus_result,
				"alpha", 1.0f, 0.0f);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(oa1, oa2, oa3, oa4, oa5);
		set.setDuration(1500);
		set.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				//重新扫描
				//隐藏动画界面
				ll_antivirus_animation.setVisibility(View.INVISIBLE);
				//清空数据，避免数据重复
				datas.clear();
				//显示扫描界面
				ll_antivirus_scan.setVisibility(View.VISIBLE);
				initData();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		set.start();
	}
}
