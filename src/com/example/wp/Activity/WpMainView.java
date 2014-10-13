package com.example.wp.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.wp.R;
import com.example.wp.Data.WpPOJO;
import com.example.wp.Service.WpIntentService;

public class WpMainView extends Activity implements OnClickListener {

	TableLayout tableView;

	/**
	 * this will store all data fetched from URL in POJO list
	 * 
	 * NOTE : it is a static data maintained globally
	 */
	public static List<WpPOJO> rowItems = new ArrayList<WpPOJO>();

	private Spinner spinner;
	private Context context;

	/**
	 * data fetched indication
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent) {
			populateDataToTable();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstscreen);

		spinner = (Spinner) findViewById(R.id.spinner1);
		List<String> spinnerList = new ArrayList<String>();
		spinnerList.add("sort by title");
		spinnerList.add("sort by date");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				if (spinner.getSelectedItem().equals("sort by title")) {

					Collections.sort(rowItems, new Comparator<WpPOJO>() {
						@Override
						public int compare(WpPOJO lhs, WpPOJO rhs) {
							return lhs.getTitle().compareToIgnoreCase(
									rhs.getTitle());
						}
					});
					removeView();
					populateDataToTable();

				} else if (spinner.getSelectedItem().equals("sort by date")) {

					Collections.sort(rowItems, new Comparator<WpPOJO>() {
						@Override
						public int compare(WpPOJO lhs, WpPOJO rhs) {
							return lhs.getDate().compareToIgnoreCase(
									rhs.getDate());
						}
					});
					removeView();
					populateDataToTable();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		tableView = (TableLayout) findViewById(R.id.displayLinear);

		/**
		 * start service to fetch data
		 */
		Intent dataRequestIntent = new Intent(this, WpIntentService.class);
		startService(dataRequestIntent);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {

		context = getApplicationContext();

		context.registerReceiver(receiver, new IntentFilter(
				"DATA_FETCHED_INDICATOR"));
		super.onResume();
	}

	@Override
	protected void onPause() {

		context.unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		rowItems.clear();
	}

	private void populateDataToTable() {

		TextView title;
		TextView date;
		int i = 0;
		for (WpPOJO pojo : rowItems) {

			LayoutInflater inflater = LayoutInflater.from(context);
			RelativeLayout layout = (RelativeLayout) inflater.inflate(
					R.layout.list_item, null, false);

			title = (TextView) layout.findViewById(R.id.title);
			date = (TextView) layout.findViewById(R.id.date);

			title.setText(pojo.getTitle());
			date.setText(pojo.getDate());
			layout.setId(i);
			layout.setOnClickListener(WpMainView.this);

			tableView.addView(layout);
			i++;
		}
	}

	private void removeView() {
		for (int i = 0, j = tableView.getChildCount(); i < j; i++) {
			if (tableView.getChildAt(i) != null) {
				if (tableView.getChildAt(i).getId() != spinner.getId()) {
					tableView.removeViewAt(i);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent(this, ContentDisplay.class);
		intent.putExtra("row_no", v.getId());
		this.startActivity(intent);

	}
}
