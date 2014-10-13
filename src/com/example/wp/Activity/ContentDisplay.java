package com.example.wp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wp.R;
import com.example.wp.Data.WpPOJO;

public class ContentDisplay extends Activity {

	private TextView txt;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		txt = (TextView) findViewById(R.id.textView1);
		
		//if(intent != null && intent.getExtras() != null){
		     intent = getIntent();
		//}
		int rowno = intent.getIntExtra("row_no", 0);
		WpPOJO mytxt = WpMainView.rowItems.get(rowno);

		txt.setText(mytxt.getContent());

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
