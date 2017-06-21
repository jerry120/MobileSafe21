package com.itheima.mobilesafe21.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe21.AppLockActivity;
import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.AppInfoBean;
import com.itheima.mobilesafe21.db.AppLockDao;

public class AppLockAdapter extends BaseAdapter {

	private Context mContext;
	private List<AppInfoBean> mDatas;
	private boolean mIsLocked;//用来区分未加锁与已加锁的界面展示，true，已加锁，false，未加锁
	private AppLockDao appLockDao;
	private List<AppInfoBean> mOtherDatas;
	//如果isLocked为true ，mDatas：已加锁数据集合，otherDatas：未加锁数据集合
	//如果isLocked为false ，mDatas：未加锁数据集合，otherDatas：已加锁数据集合
	public AppLockAdapter(Context mContext, List<AppInfoBean> mDatas, List<AppInfoBean> otherDatas, boolean isLocked) {
		this.mContext = mContext;
		this.mDatas = mDatas;//址传递
		this.mOtherDatas = otherDatas;
		this.mIsLocked = isLocked;
		appLockDao = new AppLockDao(mContext);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public AppInfoBean getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_applock_item, null);
			holder = new ViewHolder();
			holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_applock_icon);
			holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_applock);
			holder.appName = (TextView) convertView.findViewById(R.id.tv_applock_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final View itemView = convertView;
		
		
		final AppInfoBean appInfoBean = getItem(position);
		holder.appIcon.setImageDrawable(appInfoBean.appIcon);
		holder.appName.setText(appInfoBean.appName);
		
		if (mIsLocked) {//已加锁适配器,解锁图片
			holder.iv_lock.setImageResource(R.drawable.selector_applock_unlock);
		}else{//未加锁适配器,加锁图片
			holder.iv_lock.setImageResource(R.drawable.selector_applock_locked);
			
		}
		
		if (mIsLocked) {//加锁适配器，点击的操作解锁操作
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//点击时，先进行数据库的删除操作，如果成功，则从当前列表数据中删除并刷新适配，最后修改未加锁的数量,还要修改掉已加锁的数据量
					if (appLockDao.delete(appInfoBean.packageName)) {
						
						
						Animation unlockAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_applock_unlock);
						unlockAnimation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								mDatas.remove(appInfoBean);
								notifyDataSetChanged();
								((AppLockActivity)mContext).updateCount(mIsLocked);
								mOtherDatas.add(0, appInfoBean);
							}
						});
						itemView.startAnimation(unlockAnimation);
						
						
					}
				}
			});
			
		}else{//未加锁适配器，点击的操作加锁操作
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//点击时，先进行数据库的插入操作，如果成功，则从当前列表数据中删除并刷新适配，最后修改未加锁的数量,还要修改掉已加锁的数据量
					if (appLockDao.insert(appInfoBean.packageName)) {
						//执行加锁动画
						Animation lockAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_applock_lock);
						lockAnimation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								mDatas.remove(appInfoBean);
								notifyDataSetChanged();
								//通过上下文强转得到activity对象调用方法来修改数量即可
								((AppLockActivity)mContext).updateCount(mIsLocked);
								
								//将当前删除的集合添加到另一个数据集合中进行显示
								mOtherDatas.add(0,appInfoBean);//点击按钮时，listview从gone 变为visiable，会进行刷新
							}
						});
						itemView.startAnimation(lockAnimation);
						
						
						
						
						
					}
				}
			});
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView appIcon;
		ImageView iv_lock;
		TextView appName;
	}

}
