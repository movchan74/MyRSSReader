package com.example.myrssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class ViewItemActivity extends ActionBarActivity {
	private RSSDatabaseAdapter adapter;
	private String source_id;
	private String item_id;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private Cursor mCurrentCursor;
	private boolean showAll = false;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_item_activity);
		adapter = new RSSDatabaseAdapter(this);
		source_id = getIntent().getStringExtra("source_id");
		item_id = getIntent().getStringExtra("item_id");
		mPager = (ViewPager) findViewById(R.id.pager);
		showAll = getIntent().getBooleanExtra("showAll", false);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrentCursor.moveToPosition(position);
				adapter.setItemAsReaded(mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_ID)));
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {				
			}
		});
		adapter.open();
		initPager();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initPager() {
		updateCursor();
		mCurrentCursor.moveToFirst();
		while (!mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_ID)).equals(item_id)) {
			mCurrentCursor.moveToNext();
		}
		adapter.setItemAsReaded(mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_ID)));
		mPagerAdapter.notifyDataSetChanged();
		mPager.setCurrentItem(mCurrentCursor.getPosition());
	}

	private void updatePager() {
		updateCursor();
		mPagerAdapter.notifyDataSetChanged();
		mPager.setCurrentItem(0);
	}

	private void updateCursor(){
		if (showAll)
			mCurrentCursor = adapter.fetchAllItems(source_id);
		else
			mCurrentCursor = adapter.fetchUnreadedItems(source_id);
		mCurrentCursor.moveToFirst();
	}

	@Override
	protected void onStart() {
		super.onStart();
//		adapter.open();
//		updatePager();
	}

	@Override
	protected void onStop() {
		super.onStop();
		adapter.close();
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			ViewItemFragment fragment = new ViewItemFragment();
			Bundle args = new Bundle();
			if  (mCurrentCursor != null) {
				mCurrentCursor.moveToPosition(position);
				args.putString("title", mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_TITLE)));
				args.putString("link", mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_LINK)));
				args.putString("pubDate", mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_PUBDATE)));
				args.putString("description", mCurrentCursor.getString(mCurrentCursor.getColumnIndex(RSSDatabaseAdapter.KEY_ITEM_DESCRIPTION)));
				fragment.setArguments(args);
			}
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			if  (mCurrentCursor != null)
				return mCurrentCursor.getCount();
			return 0;
		}
	}
}
