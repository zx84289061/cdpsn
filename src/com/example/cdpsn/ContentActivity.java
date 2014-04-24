package com.example.cdpsn;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ContentActivity extends Activity implements DragListView.OnRefreshLoadingMoreListener{
	private ViewPager mPager;private static Context Context = null;
	private TextView btna;
	private TextView btnb;
	private Button jgsbtn;
	private TextView  gps,citytv,usernametv;
	private TextView  btnjobs;
	private TextView  btnres;
	private List<View> listViews; 
	private ImageView cursor;
	private TextView t1, t2, t3, t4 ,b1 ,b2 ,b3 ,b4 , b5 ,b6,jgs;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpWidth;
	private int yaowenpage = 1;
	private int jobpage = 1;
	private int law1page = 1;
	private int law2page = 1;
	private int law3page = 1;
	private int law4page = 1;
	private int law5page = 1;
	private int law6page = 1;
	private int ztpage = 1;
	private int lawnumber = 1;
	
	ProgressBar progressBar,pbcity;
	ProgressBar progressBar2;
	ProgressBar progressBar3;
	ProgressBar progressBar4;
	LinearLayout btnsbg;
	Spinner spv; 
	boolean isfinished = false;
	private ArrayAdapter<CharSequence> adapterpv; 
	String YAOWENpage="http://www.cdpsn.org.cn/news/lt2p";
	String ZTpage="http://www.cdpsn.org.cn/news/lt6p";
	String JOBpage="http://job.cdpsn.org.cn/job_search.htm?page=";
	String Lawpage="http://www.cdpsn.org.cn/policy/lt202l20";//1p1.htm";

	String HTM=".htm";
	private String regExhref="((?<=href=\").*(?=.*\".*title))";
	private String regExzt="((?<=title=\").*(?=\" target))";
	private String regExnews="((?<=title=\").*(?=.*\"))";
	ArrayList<String> jobname = new ArrayList<String>();
	ArrayList<String> jobweb = new ArrayList<String>();
	ArrayList<String> jobtime = new ArrayList<String>();
	ArrayList<String> jobcity = new ArrayList<String>();
	ArrayList<String> jobcomp = new ArrayList<String>();
	ArrayList<String> jobhref = new ArrayList<String>();
	ArrayList<String> newshref = new ArrayList<String>();
	ArrayList<String> news = new ArrayList<String>();
	ArrayList<String> zt = new ArrayList<String>();
	ArrayList<String> zthref = new ArrayList<String>();
	private  int pageflag = 1;// 下拉刷新标识
	private final static int DRAG_INDEX = 1;// 下拉刷新标识
	private final static int LOADMORE_INDEX = 2;// 加载更多标识
	ArrayList<String> law1 = new ArrayList<String>();
	ArrayList<String> law2 = new ArrayList<String>();
	ArrayList<String> law3 = new ArrayList<String>();
	ArrayList<String> law4 = new ArrayList<String>();
	ArrayList<String> law5 = new ArrayList<String>();
	ArrayList<String> law6 = new ArrayList<String>();
	ArrayList<String> law1href = new ArrayList<String>();
	ArrayList<String> law2href = new ArrayList<String>();
	ArrayList<String> law3href = new ArrayList<String>();
	ArrayList<String> law4href = new ArrayList<String>();
	ArrayList<String> law5href = new ArrayList<String>();
	ArrayList<String> law6href = new ArrayList<String>();
	ArrayList<String> pro = new ArrayList<String>();
//	private static WrappedHttpClient whc = new WrappedHttpClient();

	TextView datetv;
	boolean hasNetwork=false;
	double d1=30.27,d2=120.1211;
	boolean newsztflag=false; 
	private DragListView l1,l2,l4;// 下拉ListView
	String city="杭州";
	String proselected=null;
	String tablehtml="";
	String username;
	int lawbtnflag=1;
	//private MyAdapter adapter;
	//SimpleAdapter listItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_content);
		InitImageView();
		InitTextView();
		InitViewPager();
		Bundle bundle = getIntent().getExtras();    
        username = bundle.getString("_id");//读出数据   
        usernametv.setText(username);
		new Thread(){
			@Override
			public void run(){
				try {	
					//GetCheckcode gt = new GetCheckcode("http://www.cdpsn.org.cn/register/image.htm?1376309636410",ContentActivity.this);
			        //gt.getRemoteFile();Log.d("ret","getcheck1");
			       // gt.predo();Log.d("ret","getcheck2");
			      //  gt.find();Log.d("ret","getcheck3");
			     //   String stmp = null;
			     //   for(String s : gt.getcodes()){
			     //	   System.out.println(s);
			     //	   stmp+=s;Log.d("ret","getcheck"+stmp);
			     //   }
					
						if(news.size()==0)
						getcontent(YAOWENpage+yaowenpage+HTM,newshref,news,regExhref,regExnews);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handle.sendEmptyMessage(11);
			}
			}.start(); 

		}



	@SuppressLint("ShowToast")
	private void newslistshow(){
		l1.setVisibility(0);
		Log.d("ddd2", "newslist");
		int len = news.size();
    	System.out.println(len);
		if(len==0){
			Toast.makeText(getApplicationContext(), "正在加载新闻列表，请稍后。。。", 5).show();
			return;
		}
		Log.d("ddd", "len="+len);
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<len; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("topic", news.get(i));
        	listItem.add(map);
        }
        
       
        Log.d("ddd", "len="+111);
        
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,listItem,//数据源 
            R.layout.item1,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"topic"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.topic}
        );
        Log.d("ddd1", "len="+len);
        //添加并且显示
        l1.setAdapter(listItemAdapter);
        l1.setVisibility(0);
       // Log.d("ddd2news", "len="+len);
       // Log.d("ddd2news", "hreflen="+newshref.size());
        
	}
	@SuppressLint("ShowToast")
	private void ztlistshow(){
		l1.setVisibility(0);
		Log.d("ddd2", "ztlist");
		int len = zt.size();
    	System.out.println(len);
		if(len==0){
			Toast.makeText(getApplicationContext(), "正在加载新闻列表，请稍后。。。", 5).show();
			return;
		}
		Log.d("ddd", "len="+len);
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<len; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("topic", zt.get(i));
        	listItem.add(map);
        }
        
       
        Log.d("ddd", "len="+111);
        
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,listItem,//数据源 
            R.layout.item2,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"topic"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.topic}
        );
        Log.d("ddd1", "len="+len);
        //添加并且显示
        l1.setAdapter(listItemAdapter);
        Log.d("ddd2zt", "ztlen="+len);
        Log.d("ddd2zt", "zthreflen="+zthref.size());
	}
	private void joblistshow(){
		int len=jobname.size();
		Log.d("ddd2", " joblistshow() len="+len);
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<len; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("t1", jobname.get(i));
        	map.put("t2", jobcity.get(i));
        	map.put("t3", jobcomp.get(i));
        	map.put("t4", jobweb.get(i));
        	map.put("t5", jobtime.get(i));
        	listItem.add(map);
        }
        
       
	
        Log.d("ddd1", "len="+len);
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,listItem,//数据源 
            R.layout.jobitem,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"t1","t2","t3","t4","t5"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.jobname,R.id.jobcity,R.id.jobcomp,R.id.jobweb,R.id.jobtime}
        );
       
        //添加并且显示
        l2.setAdapter(listItemAdapter);	
        Log.d("ddd2job", "listItemAdapter len="+len);
        Log.d("ddd2job", "href="+jobhref.size());
	}
	private void lawlistshow(ArrayList<String> lawn){
		l4.setVisibility(0);
		Log.d("ddd2", "lawshow");
		int len=lawn.size();
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	        for(int i=0; i<len; i++)
	        {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("t1", lawn.get(i));
	        	listItem.add(map);
	        }

	        //生成适配器的Item和动态数组对应的元素
	        SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,listItem,//数据源 
	            R.layout.itemlaw,//ListItem的XML实现
	            //动态数组与ImageItem对应的子项        
	            new String[] {"t1"}, 
	            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
	            new int[] {R.id.topic}
	        );
	       
	        //添加并且显示
	        l4.setAdapter(listItemAdapter);		
	        Log.d("ddd2law", "lenlaw1="+len);
	        Log.d("ddd2law", "lenhref="+law1href.size());
	}
	public static Context getContext() {
		return Context;
		}
	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	private Handler handle = new Handler() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					citytv.setText(Html.fromHtml("<u>"+city+"</u>"));
		        	pbcity.setVisibility(View.GONE);  
					break;
				case 11:
					newslistshow();
		        	progressBar.setVisibility(View.GONE);
					Date date = new Date();
					SimpleDateFormat df = new SimpleDateFormat("MMdd HH:mm:ss");
					String time = df.format(date);Log.d("ddd1", "len="+17);// 更新时间
					datetv.setText("最后一次更新时间:\n"+time);
					//Toast.makeText(getApplicationContext(), "更新完成", 5).show();
					l1.onRefreshComplete();Log.d("ddd1", "len="+14);
					break;
				case 12:
					ztlistshow();
		        	progressBar.setVisibility(View.GONE);
		        	Date date2 = new Date();
					SimpleDateFormat df2 = new SimpleDateFormat("MMdd HH:mm:ss");
					String time2 = df2.format(date2);Log.d("ddd1", "len="+17);// 更新时间
					datetv.setText("最后一次更新时间:\n"+time2);
					break;
				
				case 2:
					joblistshow();
			        progressBar2.setVisibility(View.GONE);
					break;
				case 3:
					jgs.setText(Html.fromHtml(tablehtml));
			        progressBar3.setVisibility(View.GONE);
					break;
				
			    case 41:
			    	lawlistshow(law1);
		   	        progressBar4.setVisibility(View.GONE);
					break;
			    case 42:
			    	lawlistshow(law2);
		   	        progressBar4.setVisibility(View.GONE);
					break;
			    case 43:
			    	lawlistshow(law3);
		   	        progressBar4.setVisibility(View.GONE);
					break;
			    case 44:
			    	lawlistshow(law4);
		   	        progressBar4.setVisibility(View.GONE);
					break;
			    case 45:
			    	lawlistshow(law5);
		   	        progressBar4.setVisibility(View.GONE);
					break;
			    case 46:
			    	lawlistshow(law6);
		   	        progressBar4.setVisibility(View.GONE);
					break;
				}
				super.handleMessage(msg);
			}

		};
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);
		usernametv = (TextView) findViewById(R.id.usernametv);
		gps = (TextView) findViewById(R.id.locationchange);
		citytv = (TextView) findViewById(R.id.location);
		pbcity = (ProgressBar) findViewById(R.id.pbarcity); 
		gps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentActivity.this, "请确保GPS已经打开并连接网络\n否则无法进行定位",
						Toast.LENGTH_LONG).show();
				pbcity.setVisibility(0);
				GPSLocationHelper.getIntance().startGPSService(ContentActivity.this, 0);
				if (GPSLocationHelper.getIntance().getGPSLocation() != null) {
					d1 = GPSLocationHelper.getIntance().getGPSLocation()
							.getLatitude();
					d2 = GPSLocationHelper.getIntance().getGPSLocation()
							.getLongitude();
					Toast.makeText(ContentActivity.this, "经纬度是：" + d1 + ";" + d2,
							Toast.LENGTH_LONG).show();
					
					new Thread(){
						@Override
						public void run(){
							try {
								city=GetCity(d1,d2);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handle.sendEmptyMessage(0);
						}
						}.start();

				}
				else{
					GPSLocationHelper.getIntance().stopGPSService();
					//citytv.setText(Html.fromHtml(city));
		        	pbcity.setVisibility(View.GONE); 
				}
			}
		});
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
	}
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursort);
		bmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.ad)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpWidth) / 2;	
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix); 
	}
   
        
          
	/**
	 * viewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.lay1, null));
		listViews.add(mInflater.inflate(R.layout.lay2, null));
		listViews.add(mInflater.inflate(R.layout.lay3, null));
		listViews.add(mInflater.inflate(R.layout.lay4, null));
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		progressBar4 = (ProgressBar) listViews.get(3).findViewById(R.id.pBar4);
		l4 = (DragListView) listViews.get(3).findViewById(R.id.l4);
		b1 = (TextView) listViews.get(3).findViewById(R.id.t41);
		b2 = (TextView) listViews.get(3).findViewById(R.id.t42);
		b3 = (TextView) listViews.get(3).findViewById(R.id.t43);
		b4 = (TextView) listViews.get(3).findViewById(R.id.t44);
		b5 = (TextView) listViews.get(3).findViewById(R.id.t45);
		b6 = (TextView) listViews.get(3).findViewById(R.id.t46);
		l4.setOnRefreshListener(ContentActivity.this);
		l4.setOnItemClickListener(new OnItemClickListener(){
     		 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub 
            		Intent intent = new Intent();  
            		intent.setClass(ContentActivity.this, WebActivity.class);  
            		Bundle mBundle = new Bundle();  
            		
            		if(lawnumber==1){
            			mBundle.putString("URL", law1href.get(arg2-1));//压入数据  
					}
            		else if(lawnumber==2){
						mBundle.putString("URL", law2href.get(arg2-1));//压入数据  
					}
            		else if(lawnumber==3){
						mBundle.putString("URL", law3href.get(arg2-1));//压入数据  
					}
            		else if(lawnumber==4){
            			mBundle.putString("URL", law4href.get(arg2-1));//压入数据  
					}
            		else if(lawnumber==5){
            			mBundle.putString("URL", law5href.get(arg2-1));//压入数据  
					}
            		else if(lawnumber==6){
            			mBundle.putString("URL", law6href.get(arg2-1));//压入数据  
					}
                    intent.putExtras(mBundle);  
                    startActivity(intent);  
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
   	});
		b1.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				lawnumber=1;
				if(lawbtnflag!=1){
					b1.setBackgroundResource(R.drawable.button_press);
					switch (lawbtnflag){
						case 1:b1.setBackgroundResource(R.drawable.button_up); break;
						case 2:b2.setBackgroundResource(R.drawable.button_up); break;
						case 3:b3.setBackgroundResource(R.drawable.button_up); break;
						case 4:b4.setBackgroundResource(R.drawable.button_up); break;
						case 5:b5.setBackgroundResource(R.drawable.button_up); break;
						case 6:b6.setBackgroundResource(R.drawable.button_up); break;
					}
					}
				lawbtnflag=1;
				
				new Thread(){
					@Override
					public void run(){
						try {
							if(law1.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law1page+HTM,law1href,law1,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(41);
					}
					}.start();  
			
			}});
		b2.setOnClickListener(new OnClickListener() {
		        	
					@SuppressLint("ShowToast")
					@Override
					public void onClick(View v) {
						lawnumber=2;
						if(lawbtnflag!=2){
						
							b2.setBackgroundResource(R.drawable.button_press);
							switch (lawbtnflag){
								case 1:b1.setBackgroundResource(R.drawable.button_up); break;
								case 2:b2.setBackgroundResource(R.drawable.button_up); break;
								case 3:b3.setBackgroundResource(R.drawable.button_up); break;
								case 4:b4.setBackgroundResource(R.drawable.button_up); break;
								case 5:b5.setBackgroundResource(R.drawable.button_up); break;
								case 6:b6.setBackgroundResource(R.drawable.button_up); break;
							}
							}
						lawbtnflag=2;
						progressBar4.setVisibility(0);
						l4.setVisibility(4);
						new Thread(){
							@Override
							public void run(){
								try {
									if(law2.size()==0)
										getcontent(Lawpage+lawnumber+"p"+law2page+HTM,law2href,law2,regExhref,regExnews);
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								handle.sendEmptyMessage(42);
							}
							}.start();  
					
					}});
		b3.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				lawnumber=3;
				if(lawbtnflag!=3){
					
					b3.setBackgroundResource(R.drawable.button_press);
					switch (lawbtnflag){
						case 1:b1.setBackgroundResource(R.drawable.button_up); break;
						case 2:b2.setBackgroundResource(R.drawable.button_up); break;
						case 3:b3.setBackgroundResource(R.drawable.button_up); break;
						case 4:b4.setBackgroundResource(R.drawable.button_up); break;
						case 5:b5.setBackgroundResource(R.drawable.button_up); break;
						case 6:b6.setBackgroundResource(R.drawable.button_up); break;
					}
					}
				lawbtnflag=3;
				progressBar4.setVisibility(0);
				l4.setVisibility(4);
				new Thread(){
					@Override
					public void run(){
						try {
							if(law3.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law3page+HTM,law3href,law3,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(43);
					}
					}.start();  
			
			}});
		b4.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
			
				lawnumber=4;
				if(lawbtnflag!=4){
					
					b4.setBackgroundResource(R.drawable.button_press);
					switch (lawbtnflag){
						case 1:b1.setBackgroundResource(R.drawable.button_up); break;
						case 2:b2.setBackgroundResource(R.drawable.button_up); break;
						case 3:b3.setBackgroundResource(R.drawable.button_up); break;
						case 4:b4.setBackgroundResource(R.drawable.button_up); break;
						case 5:b5.setBackgroundResource(R.drawable.button_up); break;
						case 6:b6.setBackgroundResource(R.drawable.button_up); break;
					}
					}
				lawbtnflag=4;
				progressBar4.setVisibility(0);
				l4.setVisibility(4);
				new Thread(){
					@Override
					public void run(){
						try {
							if(law4.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law4page+HTM,law4href,law4,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(44);
					}
					}.start();  
			
			}});
		b5.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				
				lawnumber=5;
				if(lawbtnflag!=5){
					b5.setBackgroundResource(R.drawable.button_press);
					switch (lawbtnflag){
						case 1:b1.setBackgroundResource(R.drawable.button_up); break;
						case 2:b2.setBackgroundResource(R.drawable.button_up); break;
						case 3:b3.setBackgroundResource(R.drawable.button_up); break;
						case 4:b4.setBackgroundResource(R.drawable.button_up); break;
						case 5:b5.setBackgroundResource(R.drawable.button_up); break;
						case 6:b6.setBackgroundResource(R.drawable.button_up); break;
					}
					}
				lawbtnflag=5;
				progressBar4.setVisibility(0);
				l4.setVisibility(4);
				new Thread(){
					@Override
					public void run(){
						try {
							if(law5.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law5page+HTM,law5href,law5,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(45);
					}
					}.start();  
			
			}});
		b6.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
			
				lawnumber=6;
				if(lawbtnflag!=6){
					b6.setBackgroundResource(R.drawable.button_press);
					switch (lawbtnflag){
						case 1:b1.setBackgroundResource(R.drawable.button_up); break;
						case 2:b2.setBackgroundResource(R.drawable.button_up); break;
						case 3:b3.setBackgroundResource(R.drawable.button_up); break;
						case 4:b4.setBackgroundResource(R.drawable.button_up); break;
						case 5:b5.setBackgroundResource(R.drawable.button_up); break;
						case 6:b6.setBackgroundResource(R.drawable.button_up); break;
					}
					}
				lawbtnflag=6;
				progressBar4.setVisibility(0);
				l4.setVisibility(4);
				new Thread(){
					@Override
					public void run(){
						try {
							if(law6.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law6page+HTM,law6href,law6,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(46);
					}
					}.start();  
			
			}});
		{
		pro.add("0");
		pro.add("817");
		pro.add("819");
		pro.add("827");
		pro.add("828");
		pro.add("832");
		pro.add("821");
		pro.add("830");
		pro.add("831");
		pro.add("818");
		pro.add("822");
		pro.add("835");
		pro.add("834");
		pro.add("836");
		pro.add("838");
		pro.add("833");
		pro.add("829");
		pro.add("823");
		pro.add("837");
		pro.add("826");
		pro.add("844");
		pro.add("1763");
		pro.add("820");
		pro.add("824");
		pro.add("839");
		pro.add("840");
		pro.add("846");
		pro.add("825");
		pro.add("841");
		pro.add("843");
		pro.add("842");
		pro.add("845");
		pro.add("1776");
		pro.add("1777");
		}
		spv=(Spinner)listViews.get(2).findViewById(R.id.spinner1); 
		//progressBar3 = (ProgressBar) listViews.get(2).findViewById(R.id.pBar3);
		//将可选内容与ArrayAdapter连接起来  
        adapterpv = ArrayAdapter.createFromResource(ContentActivity.this, R.array.provinces, android.R.layout.simple_spinner_item);  
        //设置下拉列表的风格   
        adapterpv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将adapterpv 添加到spinner中  
        spv.setAdapter(adapterpv);  
        //添加事件Spinner事件监听    
        spv.setOnItemSelectedListener(new SpinnerXMLSelectedListener());  
        //设置默认值  
        spv.setVisibility(View.VISIBLE);  
        jgs= (TextView) listViews.get(2).findViewById(R.id.jgtv);
        jgsbtn=(Button) listViews.get(2).findViewById(R.id.searchbtn);
        progressBar3 = (ProgressBar) listViews.get(2).findViewById(R.id.pBar3);
        jgsbtn.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				if(proselected=="0") 
					{
					Toast.makeText(ContentActivity.this, "请先选择省份！",
							Toast.LENGTH_LONG).show();
						return;
					
					}
				progressBar3.setVisibility(0);
				new Thread(){
    				@Override
    				public void run(){
	
    					tablehtml=PostSearch(proselected);
    					handle.sendEmptyMessage(3);
    				}
    				}.start();  

			}});
    
        
        
		progressBar2 = (ProgressBar) listViews.get(1).findViewById(R.id.pBar2);
		btnres=(TextView) listViews.get(1).findViewById(R.id.restv);
		btnjobs = (TextView) listViews.get(1).findViewById(R.id.jobstv);
		l2 = (DragListView) listViews.get(1).findViewById(R.id.l2);
		l2.setOnRefreshListener(ContentActivity.this);
       	l2.setOnItemClickListener(new OnItemClickListener(){
      		 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub 
            		Intent intent = new Intent();  
            		intent.setClass(ContentActivity.this, JobinfoActivity.class);  
            		Bundle mBundle = new Bundle();  
                    mBundle.putString("URL", jobhref.get(arg2-1));//压入数据  
                    intent.putExtras(mBundle);  
                    startActivity(intent);  
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
       	});
       	btnjobs.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(ContentActivity.this,JobsearchActivity.class);//跳转
				startActivity(it);
			}});
		btnres.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(ContentActivity.this,ResumeActivity.class);//跳转
				startActivity(it);
			}});
		btnsbg = (LinearLayout)listViews.get(0).findViewById(R.id.btnsbg);
		datetv = (TextView) listViews.get(0).findViewById(R.id.updatetv);
    	l1 = (DragListView) listViews.get(0).findViewById(R.id.l1);
        l1.setOnRefreshListener(ContentActivity.this);
    	btna=(TextView) listViews.get(0).findViewById(R.id.btn1);
        btnb=(TextView) listViews.get(0).findViewById(R.id.btn2);
        progressBar = (ProgressBar) listViews.get(0).findViewById(R.id.pBar1);
        btna.setOnClickListener(new OnClickListener() {
        	
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				newsztflag=false;
				progressBar.setVisibility(0);
				l1.setVisibility(4);
				btnsbg.setBackgroundResource(R.drawable.left_choose);
				new Thread(){
					@Override
					public void run(){
						try {	
								if(news.size()==0)
								getcontent(YAOWENpage+yaowenpage+HTM,newshref,news,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(11);
					}
					}.start(); 
		        progressBar.setVisibility(View.GONE);
			}
		});
        btnb.setOnClickListener(new OnClickListener() {
        	
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				newsztflag=true;
				progressBar.setVisibility(0);
				l1.setVisibility(4);
				btnsbg.setBackgroundResource(R.drawable.right_choose);
				new Thread(){
    				@Override
    				public void run(){
    					try {
    							if(zt.size()==0)
    								getcontent(ZTpage+ztpage+HTM,zthref,zt,regExhref,regExzt);
    					} catch (UnsupportedEncodingException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					handle.sendEmptyMessage(12);
    				}
    				}.start();  
				l1.setVisibility(0);
		        progressBar.setVisibility(View.GONE);
			}
		}); 
    	l1.setOnItemClickListener(new OnItemClickListener(){
   		 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub 
            		Intent intent = new Intent();  
            		intent.setClass(ContentActivity.this, WebActivity.class);  
            		Bundle mBundle = new Bundle();  
            		if(newsztflag)
            			mBundle.putString("URL", zthref.get(arg2-1));
            		else 
            			mBundle.putString("URL", newshref.get(arg2-1));//压入数据  
                    intent.putExtras(mBundle);  
                    startActivity(intent);  
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
   	});
	}


	@Override
	public void onBackPressed()
	{
		//super.onBackPressed();
		if (isfinished)
		{
			System.exit(0);//finish();
		} else
		{
			Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
			new Thread()
			{
				public void run()
				{
					isfinished = true;
					try
					{
						Thread.sleep(2000);
						isfinished = false;
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				};
			}.start();
		}
	}
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			
			((ViewPager) arg0).removeView((View) arg2);//((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			if (arg1 < 4) {
                ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            }    
			if(arg1==0){
			}
            return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}


	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpWidth;	
		int two = one * 2;
		int three = one * 3;
		@Override
		public void onPageSelected(int arg0) {
			Log.d("inonPageSelected", "arg0 == "+arg0);
			Animation animation = null;
			pageflag=arg0+1;
			if (arg0 == 3) {
				new Thread(){
					@Override
					public void run(){
						try {
							if(law1.size()==0)
								getcontent(Lawpage+lawnumber+"p"+law1page+HTM,law1href,law1,regExhref,regExnews);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(41);
					}
					}.start();  
			}
			
			if (arg0 == 2) {
				
				
				
			}
			
			
			if (arg0 == 1) {
				Log.d("ddd2", "arg1 == 2");
				
        		
				new Thread(){
					@Override
					public void run(){
						try {
								if(jobname.size()==0)
									getjobs(JOBpage+jobpage);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handle.sendEmptyMessage(2);
					}
					}.start();  			
				
				
			}
            if (arg0 == 0) { 
            	
        				                   
            }
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				}else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
  
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
		
	
	public String PostSearch(String param)
	 {
		 String url = "http://www.cdpsn.org.cn/direct/ltcid.htm";
		 String partmp = "province=";
		 PrintWriter out = null;
		 BufferedReader in = null;
		 String result = "";
		 try
		 {
		 URL realUrl = new URL(url);
		 //打开和URL之间的连接
		 URLConnection conn = realUrl.openConnection();
		 //设置通用的请求属性
		 conn.setRequestProperty("accept", "*/*"); 
		 conn.setRequestProperty("connection", "Keep-Alive"); 
		 conn.setRequestProperty("user-agent", 
		 "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); 
		 //发送POST请求必须设置如下两行
		 conn.setDoOutput(true);
		 conn.setDoInput(true);
		 //获取URLConnection对象对应的输出流
		 out = new PrintWriter(conn.getOutputStream());
		 //发送请求参数
		 out.print(partmp+param);
		 //flush输出流的缓冲
		 out.flush();
		 //定义BufferedReader输入流来读取URL的响应
		 in = new BufferedReader(
		 new InputStreamReader(conn.getInputStream(),"utf-8"));
		 String str;
		 int count=0;
		 while ((str = in.readLine())!= null)
		 {
	
			 count++;
				if(count<180) continue;
				if(count>366) break;
	
			 
		 result += "\n" + str;
		 }
		 }
		 catch(Exception e)
		 {
		 System.out.println("发送POST请求出现异常！" + e);
		 e.printStackTrace();
		 }
		 //使用finally块来关闭输出流、输入流
		 finally
		 {
		 try
		 {
		 if (out != null)
		 {
		 out.close();
		 }
		 if (in != null)
		 {
		 in.close();
		 }
		 }
		 catch (IOException ex)
		 {
		 ex.printStackTrace();
		 }
		 }
		 return result;
	 }
 	public String GetCity(double d1,double d2) throws UnsupportedEncodingException, IOException{
	 
 		ArrayList<String> cityarr= new ArrayList<String>();
    	URL homepage=new URL(String.format("http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&language=zh-CN&sensor=true",Double.toString(d1),Double.toString(d2)));
   		BufferedReader in=new BufferedReader(new InputStreamReader(homepage.openStream(),"utf-8"));
   		String str;
   		while ((str = in.readLine()) != null) {
   			//System.out.println(str);
   			if(str.contains("formatted"))
   				{
   					cityarr.add((str.substring(str.indexOf("国")+1)).replace("\",", ""));
   					System.out.println(str);
   				}
   		}
   		Log.d("ddd",""+cityarr.size());
   		if(cityarr.size()==1||cityarr.size()==0){}
   		else
   			{
   				System.out.println(cityarr.get(cityarr.size()-3));
   				return cityarr.get(cityarr.size()-3);
   			}
		return "杭州";
   	}
	public void getcontent(String s,ArrayList<String> href,ArrayList<String> topic,String regEx,String regEx2) throws UnsupportedEncodingException, IOException{
		Log.d("l1more", "getcontent"+news.size());
		int count=0;
    	URL homepage=new URL(s);
   		BufferedReader in=new BufferedReader(new InputStreamReader(homepage.openStream(),"utf-8"));
   		String str;
   		while ((str = in.readLine()) != null) {
   			count++;
   			if(count<90) continue;
   			if(count>180) break;
   			//System.out.println(str);
   			
	   		Pattern p=Pattern.compile(regEx);
	   		Pattern p2=Pattern.compile(regEx2);

	   		Matcher m=p.matcher(str);
	   		Matcher m2=p2.matcher(str);

	   		if(m.find()) {
	   			//System.out.println(m.group());
	   			String s1;
	   			s1 = m.group();
	   			href.add(s1);   			
	   		}
	   		if(m2.find()) {
	   			//System.out.println(m2.group());
	   			String s1;
	   			s1 = m2.group();
	   			topic.add(s1);   			
	   		}

   		
   		}
   		Log.d("l1more", "getcontent   DONE!"+news.size());
   	}

	public void getjobs(String page) throws UnsupportedEncodingException, IOException{
		
		URL homepage=new URL(page);
		BufferedReader in=new BufferedReader(new InputStreamReader(homepage.openStream(),"gbk"));
   		String str;
   		int flag=0;
   		int count=0;
   		while ((str = in.readLine()) != null) {
   			
   			//System.out.println(str);
   			count++;
   			if(count<300) continue;
   			if(count>850) break;
   			String regEx="((?<=职位名称：).*(?=\">))";
   			String regEx2="((?<=工作地区：).*(?=\">))";
   			String regEx3="((?<=职位来源：).*(?=</li>))";
   			String regEx4="((?<=公司名称：).*(?=\">))";
   			String regEx5="((?<=发布时间：).*(?=\">))";
   			String regEx6="((?<=href=\").*(?=\".*?target))";
	   		Pattern p=Pattern.compile(regEx);
	   		Pattern p2=Pattern.compile(regEx2);
	   		Pattern p3=Pattern.compile(regEx3);
	   		Pattern p4=Pattern.compile(regEx4);
	   		Pattern p5=Pattern.compile(regEx5);
	   		Pattern p6=Pattern.compile(regEx6);
	   		
	   		Matcher m=p.matcher(str);
	   		Matcher m2=p2.matcher(str);
	   		Matcher m3=p3.matcher(str);
	   		Matcher m4=p4.matcher(str);
	   		Matcher m5=p5.matcher(str);
	   		Matcher m6=p6.matcher(str);
	   		if(m.find()) {
	   			//System.out.println(m.group());
	   			String s1;
	   			s1 = m.group();
	   			jobname.add(s1);   			
	   		}
	   		if(m2.find()) {
	   			//System.out.println(m2.group());
	   			String s1;
	   			s1 = m2.group();
	   			jobcity.add(s1);   			
	   			Log.d("s1", "len="+s1);
	   		}
	   		if(m3.find()) {
	   			//System.out.println(m3.group());
	   			String s1;
	   			s1 = m3.group();
	   			jobweb.add(s1);   			
	   		}
	   		if(m4.find()) {
	   			//System.out.println(m4.group());
	   			String s1;
	   			s1 = m4.group();
	   			jobcomp.add(s1);   			
	   		}
	   		if(m5.find()) {
	   			//System.out.println(m5.group());
	   			String s1;
	   			s1 = m5.group();
	   			jobtime.add(s1);   			
	   		}
	   		if(m6.find()) {
	   			flag++;
	   			//System.out.println(m6.group());
	   			String s1;
	   			s1 = m6.group();
	   			if(flag%2==1&&flag<=30) jobhref.add(s1);   			
	   		}
	   		}
	   	}
	class SpinnerXMLSelectedListener implements OnItemSelectedListener{  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  

    				proselected = pro.get(arg2);
    		
            
        	//Toast.makeText(getApplicationContext(), "您选择的是"+adapterpv.getItem(arg2), 5).show();
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
              
        }  
          
    }  

	/***
	 * 执行类 异步
	 * 
	 */
	class MyAsyncTask extends AsyncTask<Void, Void, Void> {
	//	private Context context;
		private int index;// 用于判断是下拉刷新还是点击加载更多

		public MyAsyncTask(Context context, int index) {
			//this.context = context;
			this.index = index;
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				if(pageflag==1){
					 if(newsztflag==false)
						{
							yaowenpage++;
							if (index != DRAG_INDEX){
								getcontent(YAOWENpage+yaowenpage+HTM,newshref,news,regExhref,regExnews);
							}
						}
					 else {
							if(ztpage==2) ;
							else{
									ztpage++;Log.d("ddd", "len="+ztpage);
									if (index != DRAG_INDEX){
										getcontent(ZTpage+ztpage+HTM,zthref,zt,regExhref,regExzt);
								}
							}
						}
	
					
				}
				else if(pageflag==2)
					{Log.d("ddd2", "pageflag==doback2");
						jobpage++;Log.d("ddd", "len="+jobpage);
						if (index != DRAG_INDEX){Log.d("ddd2", "getjobs(JOBpage+jobpage)");
							getjobs(JOBpage+jobpage);Log.d("ddd2", "doneget");
						}
					}
//					else
//						if(pageflag==3)
//						{ztpage++;
//						Log.d("ddd",""+yaowenpage);
//						if (index != DRAG_INDEX){
//						getcontent(YAOWENpage+yaowenpage+HTM,newshref,news,regExhref,regExnews);}}
				else if(pageflag==4)
					{
						if(lawnumber==2){
							law2page++;
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law2page+HTM,law2href,law2,regExhref,regExnews);
							}
						}
						else if(lawnumber==3){
							law3page++;
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law3page+HTM,law3href,law3,regExhref,regExnews);
							}
						}
						else if(lawnumber==4){
							law4page++;
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law4page+HTM,law4href,law4,regExhref,regExnews);
							}
						}
						else if(lawnumber==5){
							Log.d("ddd2", "pageflag==doback4");
							law1page++;Log.d("ddd", "len="+law1page);
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law5page+HTM,law5href,law5,regExhref,regExnews);
							}
						}
						else if(lawnumber==6){
							Log.d("ddd2", "pageflag==doback4");
							law1page++;Log.d("ddd", "len="+law1page);
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law6page+HTM,law6href,law6,regExhref,regExnews);
							}
						}						
						else if(lawnumber==1){
							law1page++;
							if (index != DRAG_INDEX){
								getcontent(Lawpage+lawnumber+"p"+law1page+HTM,law1href,law1,regExhref,regExnews);
							}
						}
					
					}
				Log.d("l1more;", "doInBackground5");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//do adding things
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//下拉函数
			if (index == DRAG_INDEX){
				Log.d("l1more;", "doInBackground666");
				if(pageflag==1){Log.d("l1more;", "doInBackground6");
					l1.onRefreshComplete();
				}
				
				if(pageflag==2){
					l2.onRefreshComplete();
				}
//					if(pageflag==3){
//						newslistshow();
//						l1.setSelection(news.size()-32);
//						l1.onLoadMoreComplete(false);
//					}
				if(pageflag==4){
					l4.onRefreshComplete();
				}
			}
			//点击more函数
			else if (index == LOADMORE_INDEX){
				if(pageflag==1&&!newsztflag){
					newslistshow();
					if(news.size()>30)
						l1.setSelection(news.size()-48);
					l1.onLoadMoreComplete(false);
				}else
				if(pageflag==1&&newsztflag){
					ztlistshow();
					l1.setSelection(zt.size()-39);
					l1.onLoadMoreComplete(false);
				}else
				
				if(pageflag==2){
					joblistshow();
					l2.setSelection(jobname.size()-19);
					l2.onLoadMoreComplete(false);
				}else
//					if(pageflag==3){
//						newslistshow();
//						l1.setSelection(news.size()-32);
//						l1.onLoadMoreComplete(false);
		//		}
				if(pageflag==4){

					if(lawnumber==1){
						lawlistshow(law1);
						l4.setSelection(law1.size()-38);
					}
					else if(lawnumber==2){
						lawlistshow(law2);
						l4.setSelection(law2.size()-38);
					}
					else if(lawnumber==3){
						lawlistshow(law3);
						l4.setSelection(law3.size()-38);
					}
					else if(lawnumber==4){
						lawlistshow(law4);
						l4.setSelection(law4.size()-38);
					}
					else if(lawnumber==5){
						lawlistshow(law5);
						l4.setSelection(law5.size()-38);
					}
					else if(lawnumber==6){
						lawlistshow(law6);
						l4.setSelection(law6.size()-38);
					}
					l4.onLoadMoreComplete(false);
				}
				
				
				
			}
		}

	}

	/***
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		new MyAsyncTask(this, DRAG_INDEX).execute();
	}

	/***
	 * 点击加载更多
	 */
	@Override
	public void onLoadMore() {
		new MyAsyncTask(this, LOADMORE_INDEX).execute();
	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {

	        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "注销").setIcon(

	        android.R.drawable.ic_menu_manage);

//
//	        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(
//
//	        android.R.drawable.ic_menu_edit);
//
//
	        return true;

	    }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case Menu.FIRST + 1:

        	Intent intent = new Intent();  
			intent.setClass(ContentActivity.this, LoginActivity.class);  
			startActivity(intent);
            //Toast.makeText(this, "1删除菜单被点击了", Toast.LENGTH_LONG).show();
        	finish();
            break;

//        case Menu.FIRST + 2:
//
//            Toast.makeText(this, "2保存菜单被点击了", Toast.LENGTH_LONG).show();
//
//            break;

        }

        return false;

    }

	
		
}