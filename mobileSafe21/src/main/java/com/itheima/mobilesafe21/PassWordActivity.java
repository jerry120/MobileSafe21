package com.itheima.mobilesafe21;

import com.itheima.mobilesafe21.utils.Constant;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PassWordActivity extends Activity implements OnClickListener {

	private ImageView iv;
	private TextView tv;
	private EditText et;
	private Button bt;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pass_word);
		initView();
		initData();
	}

	private void initData() {
		packageName = getIntent().getStringExtra(
				Constant.KEY_PACKAGE_NAME);
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(packageName, 0);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable appIcon = applicationInfo.loadIcon(packageManager);
			String appName = applicationInfo.loadLabel(packageManager)
					.toString();

			iv.setImageDrawable(appIcon);
			tv.setText(appName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initView() {
		iv = (ImageView) findViewById(R.id.iv);
		tv = (TextView) findViewById(R.id.tv);
		et = (EditText) findViewById(R.id.et);
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// 使用 home的逻辑处理
		// {act=android.intent.action.MAIN cat=[android.intent.category.HOME]
		// flg=0x10200000
		// cmp=com.android.launcher/com.android.launcher2.Launcher u=0} from pid
		// 1009
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt:
			String password = et.getText().toString();
			if (TextUtils.isEmpty(password)) {
				Toast.makeText(PassWordActivity.this, "密码不能为空", 0).show();
				return;
			}
			
			if (TextUtils.equals("123", password)) {
				Intent intent = new Intent();
				intent.putExtra(Constant.KEY_PACKAGE_NAME, packageName);
				intent.setAction(Constant.ACTION_SKIP_PACKAGE_NAME);
				
				sendBroadcast(intent);
				finish();
			}else{
				Toast.makeText(PassWordActivity.this, "密码不正确", 0).show();
				
			}
			
			break;

		default:
			break;
		}
	}
	
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		initData();
//	}
	
	
	//有新的intent该方法就会执行
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		initData();
	}
}
