package com.example.myrssreader;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	
	private RSSDatabaseAdapter adapter;
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new RSSDatabaseAdapter(this);
		list = (ListView)findViewById(R.id.listView1);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) list.getItemAtPosition(position);
				String source_id = cursor.getString(cursor.getColumnIndex(RSSDatabaseAdapter.KEY_SOURCE_ID));
				Intent intent = new Intent(MainActivity.this, ViewItemListActivity.class);
				intent.putExtra("source_id", source_id);
				MainActivity.this.startActivity(intent);				
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		adapter.open();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		adapter.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateSourceList();
	}
	
	private void updateSourceList() {
		Cursor cursor = adapter.fetchSources();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, 
				new String[] {RSSDatabaseAdapter.KEY_SOURCE_NAME}, new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_new_source:
			startActivity(new Intent(this, AddNewSourceActivity.class));
			return true;
		}
		return false;
	}

}
