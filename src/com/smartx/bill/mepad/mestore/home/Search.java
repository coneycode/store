package com.smartx.bill.mepad.mestore.home;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartx.bill.mepad.mestore.R;
import com.smartx.bill.mepad.mestore.adapter.RecomGridviewAdapter;
import com.smartx.bill.mepad.mestore.adapter.SearchGridviewAdapter;
import com.smartx.bill.mepad.mestore.iostream.DownLoadDatas;
import com.smartx.bill.mepad.mestore.listener.BackClickListener;
import com.smartx.bill.mepad.mestore.matadata.IOStreamDatas;
import com.smartx.bill.mepad.mestore.myview.MyGridView;
import com.smartx.bill.mepad.mestore.uimgloader.AbsListViewBaseActivity;
import com.smartx.bill.mepad.mestore.util.CommonTools;

public class Search extends AbsListViewBaseActivity {

	// private MyGridView mRankingGridView;
	private SearchGridviewAdapter mSearchGridviewAdapter;
	private JSONArray jsonArraySearch;
	private String searchName;
	protected DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_search);
		searchName = getIntent().getStringExtra("searchName");
		Log.i("searchName", searchName + "");
		initDatas();
		initGridView();
		CommonTools.onSearchClick(this);
	}

	private void initDatas() {
		 try {
		 jsonArraySearch = DownLoadDatas.getDatasFromServer(null, null,
		 null,
		 searchName, IOStreamDatas.APP_DATA);
		 } catch (IOException | JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		mSearchGridviewAdapter = new SearchGridviewAdapter(this,
				jsonArraySearch, imageLoader);
		myGridView = (MyGridView) findViewById(R.id.search_gridView);
		TextView backText = (TextView) findViewById(R.id.common_backhome);
		ImageView backArray = (ImageView) findViewById(R.id.common_back_array);
		backText.setOnClickListener(new BackClickListener(this));
		backArray.setOnClickListener(new BackClickListener(this));
	}

	private void initGridView() {
		((GridView) myGridView).setNumColumns(2);
		myGridView.setAdapter(mSearchGridviewAdapter);
//		myGridView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
//				true, true));
		myGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			}
		});
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
				Toast.makeText(Search.this, "现在是横屏", Toast.LENGTH_SHORT)
						.show();
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				imageCol = 3;
			}
			((GridView) myGridView).setNumColumns(imageCol);
			myGridView.setAdapter(mSearchGridviewAdapter);
			// ia.notifyDataSetChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}