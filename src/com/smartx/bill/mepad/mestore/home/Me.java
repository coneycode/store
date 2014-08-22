package com.smartx.bill.mepad.mestore.home;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.smartx.bill.mepad.mestore.R;
import com.smartx.bill.mepad.mestore.R.id;
import com.smartx.bill.mepad.mestore.adapter.MeGalleryAdapter;
import com.smartx.bill.mepad.mestore.adapter.MeGridviewAdapter;
import com.smartx.bill.mepad.mestore.listener.ItemClickListener;
import com.smartx.bill.mepad.mestore.matadata.IOStreamDatas;
import com.smartx.bill.mepad.mestore.myview.MyGalleryView;
import com.smartx.bill.mepad.mestore.myview.MyGridView;
import com.smartx.bill.mepad.mestore.recommend.Recommendation;
import com.smartx.bill.mepad.mestore.uimgloader.AbsListViewBaseActivity;
import com.smartx.bill.mepad.mestore.util.HttpUtil;

@SuppressWarnings("deprecation")
public class Me extends AbsListViewBaseActivity {

	private MeGridviewAdapter mCompetitiveAdapter;
	private MeGridviewAdapter mNewAdapter;
	private MyGridView mCompetitiveGridView;
	private MyGridView mNewGridView;
	private MyGalleryView mySpecialGallery;
	private TextView mUpdateApps;
	private TextView mAllApps;
	private TextView mName;
	private TextView mComIntroduce;
	private TextView mComMore;
	private TextView mNewIntroduce;
	private TextView mNewMore;
	private ImageView mHeadPic;
	private ImageView mSetting;
	private JSONArray jsonArrayExcellent;
	private JSONArray jsonArrayNew;
	private ProgressDialog dialog;

	// DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_me);
		initCommonDatas(this, this, savedInstanceState);
		setDialog();
		initDatas();

		HttpUtil.get(
				getDataUrl(IOStreamDatas.APP_DATA),
				getParams(null, null, IOStreamDatas.POSITION_EXCELLENT, null,
						null, null), new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray response) {
						initExcellentDatas(response);
						findViewById(id.home_me).setVisibility(View.VISIBLE);
						ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();   
						Runnable runner = new Runnable() {    
				            public void run() {    
				                dialog.dismiss();    
				            }    
				        };    
				        executor.schedule(runner,2000, TimeUnit.MILLISECONDS);  
					}

					@Override
					public void onFailure(Throwable e, JSONArray errorResponse) {
					}
				});
		HttpUtil.get(
				getDataUrl(IOStreamDatas.APP_DATA),
				getParams(null, null, IOStreamDatas.POSITION_NEW, null, null,
						null), new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray response) {
						initNewDatas(response);
					}

					@Override
					public void onFailure(Throwable e, JSONArray errorResponse) {
					}
				});
		initSpecialDatas(null);
	}

	private void initDatas() {
		mName = (TextView) findViewById(R.id.me_name);
		mComIntroduce = (TextView) findViewById(R.id.me_competitve_introduce);
		mComMore = (TextView) findViewById(R.id.me_competitve_more);

		mHeadPic = (ImageView) findViewById(R.id.me_head_pic);
		mSetting = (ImageView) findViewById(R.id.me_setting);
		mUpdateApps = (TextView) findViewById(R.id.me_update_apps);
		mAllApps = (TextView) findViewById(R.id.me_all_apps);
		mySpecialGallery = (MyGalleryView) findViewById(R.id.me_special_gallery);

		mName = (TextView) findViewById(R.id.me_name);
		mNewIntroduce = (TextView) findViewById(R.id.me_new_introduce);
		mNewMore = (TextView) findViewById(R.id.me_new_more);

		mCompetitiveGridView = (MyGridView) findViewById(R.id.me_competitive_girdview);
		mNewGridView = (MyGridView) findViewById(R.id.me_new_girdview);
		myGridView = mCompetitiveGridView;
		setListener();
	}

	private void initExcellentDatas(JSONArray response) {

		jsonArrayExcellent = response;
		mCompetitiveAdapter = new MeGridviewAdapter(this, jsonArrayExcellent,
				imageLoader);
		myGridView = mCompetitiveGridView;
		mCompetitiveGridView.setNumColumns(3);
		mCompetitiveGridView.setAdapter(mCompetitiveAdapter);
		mCompetitiveGridView.setOnTouchListener(myGestureListener);
		mCompetitiveGridView.setOnItemClickListener(new ItemClickListener(
				mContext, mActivity, savedInstanceState, response));
	}

	private void initNewDatas(JSONArray response) {
		jsonArrayNew = response;
		mNewAdapter = new MeGridviewAdapter(this, jsonArrayNew, imageLoader);
		mNewGridView.setNumColumns(3);
		mNewGridView.setAdapter(mNewAdapter);
		mNewGridView.setFocusable(false);
		mNewGridView.setOnTouchListener(myGestureListener);
		mNewGridView.setOnItemClickListener(new ItemClickListener(mContext,
				mActivity, savedInstanceState, response));
	}

	private void initSpecialDatas(JSONArray response) {
		mySpecialGallery.setAdapter(new MeGalleryAdapter(this, null));// 暂时没有数据
	}

	private void setListener() {
		// setGridViewListener();
		mComMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Me.this, Recommendation.class);
				intent.putExtra("recomType", IOStreamDatas.POSITION_EXCELLENT);
				startActivity(intent);
			}
		});
		mNewMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Me.this, Recommendation.class);
				intent.putExtra("recomType", IOStreamDatas.POSITION_NEW);
				startActivity(intent);
			}
		});
	}

	private void setDialog() {
		dialog = new ProgressDialog(this,R.style.welcome_dialog);
		dialog.setCancelable(false);
		dialog.show();
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.wlecome_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		// tipTextView.setText(msg);// 设置加载信息
		dialog.setContentView(layout);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 屏幕切换时进行处理 如果屏幕是竖屏，则显示3列，如果是横屏，则显示4列
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int imageCol = 3;
		try {

			super.onConfigurationChanged(newConfig);
			// 如果屏幕是竖屏，则显示3列，如果是横屏，则显示4列
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				imageCol = 4;
				Toast.makeText(Me.this, "现在是横屏", Toast.LENGTH_SHORT).show();
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				imageCol = 3;
			}
			mCompetitiveGridView.setNumColumns(imageCol);
			mCompetitiveGridView.setAdapter(mCompetitiveAdapter);
			mNewGridView.setNumColumns(imageCol);
			mNewGridView.setAdapter(mNewAdapter);
			// ia.notifyDataSetChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
