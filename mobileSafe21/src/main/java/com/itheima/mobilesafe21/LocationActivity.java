package com.itheima.mobilesafe21;

import com.itheima.mobilesafe21.db.LocationDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {

	private EditText et_number;
	private TextView tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		initView();
	}

	private void initView() {
		et_number = (EditText) findViewById(R.id.et_number);
		tv_location = (TextView) findViewById(R.id.tv_location);
		
		//添加监听实现实时查询(如果是网络请求，不要使用实时查询),当前本地实时查询
		et_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			//在文本改变之后被调用起来
			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();
				String location = LocationDao.queryLocation(getApplicationContext(), number);
				tv_location.setText(location);
			}
		});
	}
	
	
	public void query(View v){
		String number = et_number.getText().toString();
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(getApplicationContext(), "号码不能为空", 0).show();
			
			//AnimationUtils:动画工具类，将xml类型的动画直接生成对应动画对象（动画抽取）
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			shake.setInterpolator(new CycleInterpolator(7));
			et_number.startAnimation(shake);
			
			return;
		}
		
		String location = LocationDao.queryLocation(getApplicationContext(), number);
		
		tv_location.setText(location);
		
	}
}
