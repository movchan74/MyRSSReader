package com.example.myrssreader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddNewSourceActivity extends Activity {
	private RSSDatabaseAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_source_activity);
		adapter = new RSSDatabaseAdapter(this);
		Button add_new_source_button = (Button) findViewById(R.id.add_new_source_button);
		add_new_source_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText edit_name = (EditText) findViewById(R.id.edit_name);
				EditText edit_url = (EditText) findViewById(R.id.edit_url);
				try {
					new URL(edit_url.getText().toString());
					adapter.addNewSource(edit_name.getText().toString(), edit_url.getText().toString());
					finish();
				} catch (MalformedURLException e) {
					Toast toast = Toast.makeText(AddNewSourceActivity.this, R.string.invalid_url, 1);
					toast.show();
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		adapter.open();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		adapter.close();
	}
	
}