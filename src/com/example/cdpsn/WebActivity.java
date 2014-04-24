package com.example.cdpsn;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

	public class WebActivity extends Activity {
		WebView wv ;
		ProgressBar progressBar;
		Button backbtn;
	    @SuppressLint("SetJavaScriptEnabled")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_web);
	        Bundle bundle = getIntent().getExtras();    
	        String url=bundle.getString("URL");//¶Á³öÊý¾Ý   
	        progressBar=(ProgressBar)findViewById(R.id.pbweb);
	        wv = (WebView)findViewById(R.id.wv); 
	        backbtn = (Button)findViewById(R.id.btnbk);
	        backbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent();
		            myIntent = new Intent(WebActivity.this, ContentActivity.class);
		            startActivity(myIntent);
		            finish();
		            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
				}
			});
	        try {   
	        	wv.loadUrl(url); 
	        	wv.setWillNotCacheDrawing(true);
	        	wv.getSettings().setJavaScriptEnabled(true);  
	        	progressBar.setVisibility(View.GONE);
	        	wv.setVisibility(0);
	        	Log.d("ddd3", "len=");    
	        } catch (Exception ex) 
	        {   ex.printStackTrace();   }  
	    }


	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event)
	    {
	        if(keyCode == KeyEvent.KEYCODE_BACK){
	            Intent myIntent = new Intent();
	            myIntent = new Intent(this, ContentActivity.class);
	            startActivity(myIntent);
	            this.finish();
	            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

}
