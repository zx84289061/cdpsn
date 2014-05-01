package com.example.cdpsn;


import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cdpsn.data.DetailsInfo;
import com.example.cdpsn.tool.JsonUtil;

	public class DetailInfoActivity extends Activity {
		
		private TextView channeltxt;
		private TextView titletxt;
		private TextView from_time;
		private TextView content;
		private DetailsInfo _info;
		private ProgressBar progressBar;
		Button backbtn;
		public int type = 0;//0新闻  1法律
	    @SuppressLint("SetJavaScriptEnabled")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.details_info);
	        Bundle bundle = getIntent().getExtras();    
	        final String url=bundle.getString("URL");//读出数据 
	        final String channel=bundle.getString("channel");//读出数据 
	        final String title=bundle.getString("title");//读出数据 
	        type = bundle.getInt("type");
	        initView();

	        channeltxt.setText(channel);
	        titletxt.setText(title);
	        
	        new Thread() {
				@Override
				public void run() {
					try {
							GetDetailData(url);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handle.sendEmptyMessage(0);
				}
			}.start();
	        
	        
	        backbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent();
		            myIntent = new Intent(DetailInfoActivity.this, ContentActivity.class);
		            startActivity(myIntent);
		            finish();
		            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
				}
			}); 
	        	progressBar.setVisibility(View.GONE);
	    }
	    
	    @SuppressLint("HandlerLeak")
		private Handler handle = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					if(_info != null){
						from_time.setText(_info.comeFrom + " " + _info.createDate);
						//显示里面的图片
						ImageGetter imgGetter = new Html.ImageGetter() {
							
							@Override
							public Drawable getDrawable(String source) {
								// TODO Auto-generated method stub
								Drawable drawable = null;
								URL url;
								try{
									url = new URL(source);
									drawable = Drawable.createFromStream(url.openStream(),"");
								}
								catch (Exception e){
									return null;
								}
								drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
								return drawable;
							}
						};
						content.setText(Html.fromHtml(_info.htmlcontent,imgGetter,null));
						progressBar.setVisibility(View.GONE);
					}
					else{
						Toast.makeText(DetailInfoActivity.this, "加载失败，请检查网络连接是否正常。", 5).show();
						progressBar.setVisibility(View.GONE);
						
					}
					break;
					}
				}
			};

	    public void  initView(){
	        progressBar = (ProgressBar)findViewById(R.id.detailnew_pb);
	        backbtn = (Button)findViewById(R.id.btnbk);
	        channeltxt = (TextView) findViewById(R.id.head_title);
	        titletxt = (TextView) findViewById(R.id.title);
	        from_time = (TextView) findViewById(R.id.time_from);
	        content = (TextView) findViewById(R.id.content);
	        
	    }
	    
	    
	    public void GetDetailData(String url) throws Exception{
	    	NewsDetailJsonParser(JsonUtil.getJsonString(url));
	    	
	    }
	    public void  NewsDetailJsonParser(String jsonStr) throws Exception {  
			JSONObject jsonObject=new JSONObject(jsonStr.substring(1,jsonStr.length()-1));     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object   
			String str;
			DetailsInfo info;
			JSONObject jsonObject2;
			if(type == 0){
				str = jsonObject.getString("publishDate");//里面有一个数组数据，可以用getJSONArray获取数组  
				jsonObject2=new JSONObject(str);
				info = new DetailsInfo(jsonObject.getString("content"),jsonObject.getString("comeFrom"),
						jsonObject.getString("metaDesc"),jsonObject.getString("author"),jsonObject2.getLong("time"));
				
			}
			else{
				str = jsonObject.getString("releaseTime");//里面有一个数组数据，可以用getJSONArray获取数组   
				jsonObject2=new JSONObject(str);
				info = new DetailsInfo(jsonObject.getString("content"),"",
						jsonObject.getString("title"),"",jsonObject2.getLong("time"));
			}
			_info = info;
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
