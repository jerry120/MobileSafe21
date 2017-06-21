/**
 * ============================================================
 * 
 * 版 权 ： 黑马程序员教育集团 版权所有 (c)
 * 
 * 作 者 :xx
 * 
 * 版 本 ： 1.0
 * 
 * 描 述 ：xxx
 * 
 * 
 * 修订历史 ：xxx6.4号
 * 
 * 
 * 
 * ============================================================
 **/
package com.itheima.mobilesafe21.activity;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.application.MyApplication;
import com.itheima.mobilesafe21.bean.UpdateBean;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.GzipUtil;
import com.itheima.mobilesafe21.utils.HttpUtil;
import com.itheima.mobilesafe21.utils.PackageUtil;
import com.itheima.mobilesafe21.utils.SpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

/**
 * @Description: 手机卫士的闪屏界面
 * @author xx
 */
public class SplashActivity extends Activity {

	private static final int REQUEST_INSTALL = 100;// 安装请求码
	private TextView tv_version;
	private ProgressDialog pd;

	private Handler mhHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		int i = 1/0;
		
		System.out.println("SplashActivity");
		
		
		MyApplication application = (MyApplication) getApplication();
		application.doSomething();
		application.data = "被修改后的data";
		
		setContentView(R.layout.activity_splash);

		initView();
		initData();

	}

	private void initView() {
		tv_version = (TextView) findViewById(R.id.tv_version);

	}

	private void initData() {
		// PackageManager packageManager = getPackageManager();
		// /**
		// * 参数一：要查询的包名
		// * 参数二：标记 给0，获取清单文件的基本信息(如果要获取对应的信息（四大组件，权限要设置对应的标记）)
		// */
		// try {
		// PackageInfo packageInfo =
		// packageManager.getPackageInfo(getPackageName(), 0);
		// String versionName = packageInfo.versionName;
		// tv_version.setText(versionName);
		//
		// } catch (NameNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		String versionName = PackageUtil.getVersionName(this, getPackageName());
		tv_version.setText(versionName);

		// 自动更新功能

		// 先判断用户是否打开了该功能
		boolean flag = SpUtil.getBoolean(SplashActivity.this,
				Constant.KEY_AUTO_UPDATE, true);
		if (flag) {
			autoUpdate();
		} else {
			enterHome();
		}

		// 数据初始（外部数据库的复制）
		copyLocationDb();

		// 常用号码数据库的复制；
		copyDb("commonnum.db");
		// 病毒数据库的复制；
		copyDb("antivirus.db");

	}

	private void copyDb(final String dbName) {
		final File dbFile = new File(getFilesDir(),dbName);

		if (dbFile.exists()) {
			return;
		}
		new Thread() {
			public void run() {
				AssetManager assetManager = getAssets();
				InputStream inputStream = null;
				FileOutputStream fos = null;
				try {
					inputStream = assetManager.open(dbName);
					fos = new FileOutputStream(dbFile);

					int len = -1;
					byte[] buffer = new byte[1024];

					while ((len = inputStream.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}

					fos.flush();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					GzipUtil.closeIos(inputStream, fos);
				}

			};
		}.start();

		
	}

	// 复制归属地压缩包
	private void copyLocationDb() {
		// 通过外部资源文件管理者获取输入流对象(data/data/包名/files)
		final File targetFile = new File(getFilesDir(), "address.db");
		// 第二次进来不需要重复解压
		if (targetFile.exists()) {
			return;
		}

		new Thread() {
			public void run() {

				AssetManager assetManager = getAssets();
				try {
					InputStream inputStream = assetManager.open("address.zip");
					GzipUtil.unzip(inputStream, targetFile);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void autoUpdate() {

		// 1.访问服务获取远程版本号
		new Thread() {
			public void run() {
				// 01. 定义okhttp
				// OkHttpClient okHttpClient_get = new OkHttpClient();

				OkHttpClient.Builder builder = new OkHttpClient.Builder();
				builder.connectTimeout(1, TimeUnit.SECONDS);
				OkHttpClient okHttpClient_get = builder.build();
				// 02.请求体
				Request request = new Request.Builder().get()// get请求方式
						.url("http://192.168.43.47:8080/update21.txt")// 网址
						.build();

				// 03.执行okhttp

				Response response;
				try {
					// execute:同步请求（线程阻塞）
					// Call call = okHttpClient_get.newCall(request);
					// call.execute();//同步
					// call.enqueue(new Callback() {//异步请求
					//
					// @Override
					// public void onResponse(Call arg0, Response arg1) throws
					// IOException {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onFailure(Call arg0, IOException arg1) {
					// // TODO Auto-generated method stub
					//
					// }
					// })

					response = okHttpClient_get.newCall(request).execute();

					String json = response.body().string();
					System.out.println(json);
					JSONObject jsonObject = new JSONObject(json);
					int remoteVersion = jsonObject.getInt("remoteVersion");
					String desc = jsonObject.getString("desc");
					String url = jsonObject.getString("apkUrl");
					UpdateBean updateBean = new UpdateBean();
					updateBean.remoteVersion = remoteVersion;
					updateBean.desc = desc;
					updateBean.apkUrl = url;

					int versionCode = PackageUtil.getVersionCode(
							SplashActivity.this, getPackageName());

					if (versionCode < remoteVersion) {
						// 2.判断是否有新的版本如果有展示对话框
						initUpdateDialog(updateBean);

					} else {
						enterHome();

					}

				} catch (IOException e) {
					enterHome();
				} catch (JSONException e) {
					enterHome();
				}

			}

		}.start();

	}

	private void enterHome() {
		mhHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	};

	protected void initUpdateDialog(final UpdateBean updateBean) {
		// 展示对话框

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SplashActivity.this);
				builder.setTitle("版本更新提示");
				builder.setMessage(updateBean.desc);
				builder.setPositiveButton("立刻升级", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pd = new ProgressDialog(SplashActivity.this);
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.show();

						// 3.如果升级，进行下载apk
						// onclick方法是个接口回调，一般到主线程中
						downLoadApk(updateBean);

					}

				});
				builder.setNegativeButton("稍后再说", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						enterHome();
					}
				});
				AlertDialog dialog = builder.create();
				// dialog.setCanceledOnTouchOutside(false);//设置点击dialog以外的部分不能销毁
				// 但是点击返回键还是可以销毁
				dialog.setCancelable(false);
				dialog.show();
			}
		});

	}

	// 下载apk
	private void downLoadApk(UpdateBean updateBean) {
		new Thread(new DownLoadTask(updateBean)) {
		}.start();
	}

	private void closeIos(Closeable... io) {
		if (io != null) {
			for (int i = 0; i < io.length; i++) {
				Closeable closeable = io[i];
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						enterHome();
						e.printStackTrace();
					}
				}

			}

		}
	}

	private void closeIo(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class DownLoadTask implements Runnable {

		private UpdateBean bean;

		public DownLoadTask(UpdateBean updateBean) {
			this.bean = updateBean;
		}

		@Override
		public void run() {
			InputStream inputStream = null;
			FileOutputStream fos = null;
			try {
				Response response = HttpUtil.httpGet(bean.apkUrl);
				inputStream = response.body().byteStream();
				// 设置最大值
				long max = response.body().contentLength();
				pd.setMax((int) max);

				File apkFile = new File(
						Environment.getExternalStorageDirectory(),
						"MobileSafe21.apk");
				fos = new FileOutputStream(apkFile);

				int len = 0;
				int progress = 0;
				byte[] buffer = new byte[1024];
				while ((len = inputStream.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					progress += len;
					pd.setProgress(progress);
					// SystemClock.sleep(10);
				}

				fos.flush();

				// 下载成功，销毁dialog
				pd.dismiss();

				// 4.下载成功，提示进行安装
				installApk(apkFile);
				// 5.如果安装，安装成功

			} catch (IOException e) {
				enterHome();
				e.printStackTrace();
			} finally {
				// closeIo(inputStream);
				// closeIo(fos);
				closeIos(inputStream, fos);
			}
		}

	}

	// 安装apk
	public void installApk(File apkFile) {
		// <intent-filter>
		// <action android:name="android.intent.action.VIEW" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:scheme="content" />
		// <data android:scheme="file" />
		// <data android:mimeType="application/vnd.android.package-archive" />
		// </intent-filter>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		// startActivity(intent);
		startActivityForResult(intent, REQUEST_INSTALL);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_INSTALL) {
			switch (resultCode) {
			case RESULT_OK:// 确定
				System.out.println("RESULT_OK");
				break;
			case RESULT_CANCELED:// 取消
				System.out.println("RESULT_CANCELED");
				enterHome();
				break;

			default:
				break;
			}
		}

	}

}
