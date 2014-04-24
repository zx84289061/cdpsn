package com.example.cdpsn;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class JobsearchActivity extends Activity {

	private Spinner province;
	private Spinner city;
	private ArrayAdapter<CharSequence> adapterpv; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jobsearch);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jobsearch, menu);
		return true;
	}

}
