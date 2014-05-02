package com.example.cdpsn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cdpsn.data.AgencyInfo;
import com.example.cdpsn.data.JobInfo;
import com.example.cdpsn.data.ListInfo;
import com.example.cdpsn.data.PageInfo;
import com.example.cdpsn.data.URLS;
import com.example.cdpsn.tool.JsonUtil;

@SuppressLint("ShowToast")
public class ContentActivity extends FragmentActivity implements
		DragListView.OnRefreshLoadingMoreListener {
	private ViewPager mPager;
	private static Context Context = null;
	private List<View> listViews;
	private ImageView cursor;
	private TextView gps, citytv, usernametv;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpWidth;
	boolean isfinished = false;
	private int pageflag = 1;// 下拉刷新标识
	private final static int DRAG_INDEX = 1;// 下拉刷新标识
	private final static int LOADMORE_INDEX = 2;// 加载更多标识
	String username;
	String city = "杭州";
	ProgressBar progressBar, pbcity;
	ProgressBar progressBar2;
	ProgressBar progressBar4;
	private DragListView l1, l2, l4;// 下拉ListView
	SimpleAdapter listItemAdapter1;
	JobListAdapter listItemAdapter2;
	SimpleAdapter listItemAdapter3;
	
	
	TextView t1,t2,t3,t4;
	private TextView btnjobs;
	private TextView btnres;
	
	ArrayList<ArrayList<ListInfo>> lawdatas = new ArrayList<ArrayList<ListInfo>>();
	ArrayList<PageInfo> lawPagedatas = new ArrayList<PageInfo>();
	
	ArrayList<ArrayList<ListInfo>> newsdatas = new ArrayList<ArrayList<ListInfo>>();
	ArrayList<PageInfo> newsPagedatas = new ArrayList<PageInfo>();
	
	ArrayList<JobInfo> jobdatas = new ArrayList<JobInfo>();
	PageInfo jpagedata = new PageInfo();

	//机构页面 
	String proselected = null;//记录搜索选项
	String typeselected = null;
	ArrayList<String> pro = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();
	ArrayList<AgencyInfo> agentdatas = new ArrayList<AgencyInfo>();
	ArrayList<String> group = new ArrayList<String>();
	ArrayList<ArrayList<String>> child = new ArrayList<ArrayList<String>>();
	private Spinner spv;
	private Spinner spv2;
	private ArrayAdapter<CharSequence> adapterpv;
	private ArrayAdapter<CharSequence> adapterpv2;
	private Button jgSearchBtn;
	private Button btn_pre;
	private Button btn_next;
	private Dialog dialog;
	private EditText _keyedit;
	private ExpandableListView mExpandableListView;
	private int totalpage = 0;//保存搜索总页数 
	private int pagesize = 0;//保存每页条目数 
	private int curPage = 0; //保存当前获取到的页码
	private int startRow = 0; //保存当前起始下标
	private int endRow = 0; //保存当前结束下标
	private TextView searchResultTxt;
	
	//法律导航
	private RadioGroup lawTabRg;
	private RadioGroup newsTabRg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_content);
		Log.d("ppp", "onCreate");
		InitImageView();
		InitTextView();
		InitViewPager();
		initData();
		
		// Bundle bundle = getIntent().getExtras();
		// username = bundle.getString("_id");//读出数据
		// usernametv.setText(username);
		
		new Thread() {
			@Override
			public void run() {
				try {
 					if (newsdatas.get(0).size() == 0)
						GetNewsList(0, 1);
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
	private void onSearchBegin() {
		dialog = ProgressDialog.show(ContentActivity.this, "", "正在搜索...", true);
		dialog.setCancelable(false);
	}
	@SuppressLint("ShowToast")
	private void newslistshow(ArrayList<ListInfo> curnews) {
		l1.setVisibility(0);
		int len = curnews.size();
		System.out.println(len);
		if (len == 0) {
			Toast.makeText(getApplicationContext(), "正在加载新闻列表，请稍后。。。", 5)
					.show();
			return;
		}
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < len; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("topic", curnews.get(i).title);
			listItem.add(map);
		}
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,
				listItem,// 数据源
				R.layout.newsitem,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "topic" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.topic });
		Log.d("ddd1", "len=" + len);
		// 添加并且显示
		l1.setAdapter(listItemAdapter);
		l1.setVisibility(0);

	}

	private void lawlistshow(ArrayList<ListInfo> lawn) {
		l4.setVisibility(0);
		Log.d("ddd2", "lawshow");
		int len = lawn.size();
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < len; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("t1", lawn.get(i).title);
			listItem.add(map);
		}

		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(ContentActivity.this,
				listItem,// 数据源
				R.layout.itemlaw,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "t1" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.topic });

		// 添加并且显示
		l4.setAdapter(listItemAdapter);
	}

	public static Context getContext() {
		return Context;
	}

	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	private Handler handle = new Handler() {
		@SuppressLint({ "SimpleDateFormat", "ShowToast" })
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				//citytv.setText(Html.fromHtml("<u>" + city + "</u>"));
				//pbcity.setVisibility(View.GONE);
				break;
			case 11:
			case 12:
			case 13:
				newslistshow(newsdatas.get(msg.what % 10 - 1));
				progressBar.setVisibility(View.GONE);
				break;

			case 2:
				l2.setAdapter(listItemAdapter2);
				progressBar2.setVisibility(View.GONE);
				break;
			case 3:
				resetBtns();//按钮显示
				Log.d("ppp", "curpage= " + curPage);
				if(totalpage == 0){
					searchResultTxt.setText("搜索结果为空！");
					mExpandableListView.setVisibility(View.INVISIBLE);
				}
				else{
					searchResultTxt.setText("搜索结果共"+totalpage+"页,当前第"+curPage+"页");
					mExpandableListView.setVisibility(View.VISIBLE);
					expandableListInit();
				}
				

				if(dialog != null)
					dialog.dismiss();
				break;
			case 30:
				//expandableListInit();
				Toast.makeText(getApplicationContext(), "网络错误，请检查网络状况", 5).show();
				if(dialog != null)
					dialog.dismiss();
				break;

			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
				lawlistshow(lawdatas.get(msg.what % 10 - 1));
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
		// gps = (TextView) findViewById(R.id.locationchange);
		citytv = (TextView) findViewById(R.id.location);
		pbcity = (ProgressBar) findViewById(R.id.pbarcity);
		
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
		_keyedit = (EditText) listViews.get(2).findViewById(R.id.keyedit);
		progressBar4 = (ProgressBar) listViews.get(3).findViewById(R.id.pBar4);
		l4 = (DragListView) listViews.get(3).findViewById(R.id.l4);
		lawTabRg = (RadioGroup) listViews.get(3).findViewById(R.id.tab_rg_menu4);
		newsTabRg = (RadioGroup) listViews.get(0).findViewById(R.id.tab_rg_menu1);

		lawTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.t41:
				case R.id.t42:
				case R.id.t43:
				case R.id.t44:
				case R.id.t45:
				case R.id.t46:
					final int curNum = checkedId - R.id.t41;
					Log.d("ppp",""+curNum);
					progressBar4.setVisibility(0);
					l4.setVisibility(4);
					new Thread() {
						@Override
						public void run() {
							if (lawdatas.get(curNum).size() == 0)
								try {
									GetLawList(curNum, 1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							handle.sendEmptyMessage(41 + curNum);
						}
					}.start();
					
					break;

				default:
					break;
				}
			}
		});
		newsTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.t11:
				case R.id.t12:
				case R.id.t13:
					final int curNum = checkedId - R.id.t11;
					if(newsdatas.get(curNum).size() == 0)
						progressBar.setVisibility(0);
					l1.setVisibility(4);
					new Thread() {
						@Override
						public void run() {
							if (newsdatas.get(curNum).size() == 0)
								try {
									GetNewsList(curNum, 1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							handle.sendEmptyMessage(11 + curNum);
						}
					}.start();
					
					break;
					
				default:
					break;
				}
			}
		});
		
		l4.setOnRefreshListener(ContentActivity.this);
		
		l4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(ContentActivity.this, DetailInfoActivity.class);
				Bundle mBundle = new Bundle();

				mBundle.putString("URL", lawdatas.get(lawTabRg.getCheckedRadioButtonId() - R.id.t41).get(arg2 - 1).url);// 压入数据
				mBundle.putString("channel", switchKey(lawTabRg.getCheckedRadioButtonId() - R.id.t41 + 201));// 压入数据
				mBundle.putString("title", lawdatas.get(lawTabRg.getCheckedRadioButtonId() - R.id.t41).get(arg2 - 1).title);// 压入数据
				mBundle.putInt("type", 1);// 压入数据
				intent.putExtras(mBundle);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
			}
		});
		
		spv = (Spinner) listViews.get(2).findViewById(R.id.spinner1);
		spv2 = (Spinner) listViews.get(2).findViewById(R.id.spinner2);
		searchResultTxt = (TextView) listViews.get(2).findViewById(R.id.page_res);
		searchResultTxt.setText("搜索结果将显示在下面的列表中");
		
		// 将可选内容与ArrayAdapter连接起来
		adapterpv = ArrayAdapter.createFromResource(ContentActivity.this,
				R.array.provinces, android.R.layout.simple_spinner_item);
		adapterpv2 = ArrayAdapter.createFromResource(ContentActivity.this,
				R.array.types, android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapterpv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterpv2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapterpv 添加到spinner中
		spv.setAdapter(adapterpv);
		spv2.setAdapter(adapterpv2);
		// 添加事件Spinner事件监听
		spv.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		spv2.setOnItemSelectedListener(new SpinnerXMLSelectedListener2());
		// 设置默认值
		//spv.setVisibility(View.VISIBLE);
		mExpandableListView = (ExpandableListView)listViews.get(2).findViewById(R.id.eapandable_listview);  
		jgSearchBtn = (Button) listViews.get(2).findViewById(R.id.searchbtn);
		btn_pre = (Button) listViews.get(2).findViewById(R.id.btn_pre);
		btn_next = (Button) listViews.get(2).findViewById(R.id.btn_next);
		jgSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (proselected == "0") {
					Toast.makeText(ContentActivity.this, "请选择省份！",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (typeselected == "0") {
					Toast.makeText(ContentActivity.this, "请选择机构类别！",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				//Toast.makeText(ContentActivity.this, "请选择机构类别！" + proselected +""+typeselected+_keyedit.getText(),
				//		Toast.LENGTH_LONG).show();
				onSearchBegin();
				new Thread() {
					@Override
					public void run() {
						try {
							DoSearch(proselected,typeselected,_keyedit.getText().toString(),1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							handle.sendEmptyMessage(30);
							e.printStackTrace();
						}
						handle.sendEmptyMessage(3);
					}
				}.start();

			}
		});
		
		btn_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(startRow <= 1 || curPage == 1) return;
				startRow -= pagesize;
				endRow = startRow + pagesize - 1;
				curPage --;
				if(startRow < 0) return;
				group = new ArrayList<String>();
	            child = new ArrayList<ArrayList<String>>();
				for (int i = startRow - 1;i<endRow;i++){
					group.add(agentdatas.get(i).name);
					ArrayList<String> tmp = new ArrayList<String>();
					tmp.add(agentdatas.get(i).addr);
					tmp.add(agentdatas.get(i).phone);
					tmp.add(agentdatas.get(i).postcode);
					child.add(tmp);
				} 
				handle.sendEmptyMessage(3);
			}});
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(curPage == totalpage) return;
				if(agentdatas.size() > curPage * pagesize)//已经缓存
				{
					curPage ++;
					startRow += pagesize;
					endRow = (agentdatas.size() >= endRow + pagesize ? endRow + pagesize : agentdatas.size());
					Log.d("ppp start row", ""+startRow);
					Log.d("ppp end row", ""+endRow);
					group = new ArrayList<String>();
		            child = new ArrayList<ArrayList<String>>();
					for (int i = startRow - 1;i < endRow ;i++){
						group.add(agentdatas.get(i).name);
						ArrayList<String> tmp = new ArrayList<String>();
						tmp.add(agentdatas.get(i).addr);
						tmp.add(agentdatas.get(i).phone);
						tmp.add(agentdatas.get(i).postcode);
						child.add(tmp);
					} 
					handle.sendEmptyMessage(3);
				}
				else
				{
					onSearchBegin();
					new Thread() {
						@Override
						public void run() {
							try {
								DoSearch(proselected,typeselected,_keyedit.getText().toString(),++curPage);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								handle.sendEmptyMessage(30);
								e.printStackTrace();
							}
							handle.sendEmptyMessage(3);
						}
					}.start();
				}
			}});
		btn_pre.setVisibility(View.GONE);
		btn_next.setVisibility(View.GONE);
		
		progressBar2 = (ProgressBar) listViews.get(1).findViewById(R.id.pBar2);
		btnres = (TextView) listViews.get(1).findViewById(R.id.restv);
		btnjobs = (TextView) listViews.get(1).findViewById(R.id.jobstv);
		l2 = (DragListView) listViews.get(1).findViewById(R.id.l2);
		l2.setOnRefreshListener(ContentActivity.this);
		
		listItemAdapter2 = new JobListAdapter(getApplicationContext());
		
//		l2.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(ContentActivity.this, JobinfoActivity.class);
//				Bundle mBundle = new Bundle();
//				mBundle.putString("URL", jobhref.get(arg2 - 1));// 压入数据
//				intent.putExtras(mBundle);
//				startActivity(intent);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
		btnjobs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(ContentActivity.this, JobsearchActivity.class);// 跳转
				startActivity(it);
			}
		});
		btnres.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(ContentActivity.this, ResumeActivity.class);// 跳转
				startActivity(it);
			}
		});
		
		l1 = (DragListView) listViews.get(0).findViewById(R.id.l1);
		l1.setOnRefreshListener(ContentActivity.this);
		progressBar = (ProgressBar) listViews.get(0).findViewById(R.id.pBar1);
		
		l1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int curN = newsTabRg.getCheckedRadioButtonId() - R.id.t11;
				if(curN == 2){
						Toast.makeText(ContentActivity.this,"功能尚未开启",5).show();
						return;
				}
				Intent intent = new Intent();
				intent.setClass(ContentActivity.this, DetailInfoActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("URL", newsdatas.get(newsTabRg.getCheckedRadioButtonId() - R.id.t11).get(arg2 - 1).url);// 压入数据
				mBundle.putString("channel", newsdatas.get(newsTabRg.getCheckedRadioButtonId() - R.id.t11).get(arg2 - 1).channelName);// 压入数据
				mBundle.putString("title", newsdatas.get(newsTabRg.getCheckedRadioButtonId() - R.id.t11).get(arg2 - 1).title);// 压入数据
				mBundle.putInt("type", 0);// 压入数据
				intent.putExtras(mBundle);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
			}
		});
	}
	
	public void expandableListInit(){
		
		final LayoutInflater inflater = getLayoutInflater();
		
		
		mExpandableListView.setAdapter(new ExpandableListAdapter() {  
            @Override  
            public void unregisterDataSetObserver(DataSetObserver observer) {  
                // TODO Auto-generated method stub  
                  
            }  
              
            @Override  
            public void registerDataSetObserver(DataSetObserver observer) {  
                // TODO Auto-generated method stub  
                  
            }  
              
            @Override  
            public void onGroupExpanded(int groupPosition) {  
                // TODO Auto-generated method stub  
                  
            }  
              
            @Override  
            public void onGroupCollapsed(int groupPosition) {  
                // TODO Auto-generated method stub  
                  
            }  
              
            @Override  
            public boolean isEmpty() {  
                return false;  
            }  
              
            @Override  
            public boolean isChildSelectable(int groupPosition, int childPosition) {  
                return true;  
            }  
              
            @Override  
            public boolean hasStableIds() {  
                return true;  
            }  
              
            @Override  
            public View getGroupView(int groupPosition, boolean isExpanded,  
                    View convertView, ViewGroup parent) {  
                TextView mTextView = new TextView(getApplicationContext());  
                mTextView.setText(group.get(groupPosition));  
                mTextView.setPadding(40, 5, 0, 5);  
                mTextView.setTextSize(22);  
                mTextView.setTextColor(Color.BLACK);  
                return mTextView;  
            }  
              
            @Override  
            public long getGroupId(int groupPosition) {  
                return groupPosition;  
            }  
              
            @Override  
            public int getGroupCount() {  
                return group.size();  
            }  
              
            @Override  
            public Object getGroup(int groupPosition) {  
                return group.get(groupPosition);  
            }  
              
            @Override  
            public long getCombinedGroupId(long groupId) {  
                return 0;  
            }  
              
            @Override  
            public long getCombinedChildId(long groupId, long childId) {  
                return 0;  
            }  
              
            @Override  
            public int getChildrenCount(int groupPosition) {  
                return 1;  
            }  
              
            @Override  
            public View getChildView(int groupPosition, int childPosition,  
                    boolean isLastChild, View convertView, ViewGroup parent) {  
            	TextView t1;
            	TextView t2;
            	TextView t3;

                if(convertView == null){
                    convertView = inflater.inflate(R.layout.agentitem, parent, false);
                }
                t1 = (TextView) convertView.findViewById(R.id.addr_a);
                t2 = (TextView) convertView.findViewById(R.id.tel_a);
                t3 = (TextView) convertView.findViewById(R.id.post_a);
                t1.setText(child.get(groupPosition).get(0));
                t2.setText(child.get(groupPosition).get(1));
                t3.setText(child.get(groupPosition).get(2));
                return convertView;
            }  
              
            @Override  
            public long getChildId(int groupPosition, int childPosition) {  
                return childPosition;  
            }  
              
            @Override  
            public Object getChild(int groupPosition, int childPosition) {
                return child.get(childPosition);  
            }  
              
            @Override  
            public boolean areAllItemsEnabled() {  
                return true;  
            }  
        });  
		
	}
	public void resetBtns(){
			if(totalpage <= 1){
				btn_pre.setVisibility(View.INVISIBLE);
				btn_next.setVisibility(View.INVISIBLE);
			}
			else if(curPage == 1){
					btn_pre.setVisibility(View.INVISIBLE);
					btn_next.setVisibility(View.VISIBLE);
				}
			else if(curPage == totalpage){
				btn_next.setVisibility(View.INVISIBLE);
				btn_pre.setVisibility(View.VISIBLE);
			}else{
				btn_pre.setVisibility(View.VISIBLE);
				btn_next.setVisibility(View.VISIBLE);
			}
	}
	public 	void initData()
	{
		for(int i = 0; i < 6; i++ ){
			PageInfo pinfo = new PageInfo();
			pinfo.curPage = 1;
			pinfo.totalPage = 1;
			pinfo.pageSize = 1;
			lawPagedatas.add(pinfo);
		}
		
		for(int i = 0; i < 6; i++ ){
			ArrayList<ListInfo> tmp = new ArrayList<ListInfo>();
			lawdatas.add(tmp);
			
		}
		
		for(int i = 0; i < 3; i++ ){
			PageInfo pinfo = new PageInfo();
			pinfo.curPage = 1;
			pinfo.totalPage = 1;
			pinfo.pageSize = 1;
			newsPagedatas.add(pinfo);
		}
		
		for(int i = 0; i < 3; i++ ){
			ArrayList<ListInfo> tmp = new ArrayList<ListInfo>();
			newsdatas.add(tmp);
			
		}
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
		types.add("10940");
		types.add("10967");
		types.add("10469");
		types.add("11470");
		
		
	}
	
	public String switchKey(int key){
		switch (key){
		case 201:
			return "保障法";
		case 202:
			return "康复医疗";
		case 203:
			return 	"教育就业";
		case 204:
			return "社会保障";
		case 205:
			return "权益维护";
		case 206:
			return "文化体育";
		
		}
		return "法律法规";
		
	}
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (isfinished) {
			System.exit(0);// finish();
		} else {
			Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
			new Thread() {
				public void run() {
					isfinished = true;
					try {
						Thread.sleep(2000);
						isfinished = false;
					} catch (InterruptedException e) {
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

			((ViewPager) arg0).removeView((View) arg2);// ((ViewPager)
														// arg0).removeView(mListViews.get(arg1));
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
			if (arg1 == 0) {
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
			Animation animation = null;
			pageflag = arg0 + 1;
			if (arg0 == 3) {
				new Thread() {
					@Override
					public void run() {
							if (lawdatas.get(0).size() == 0)
								try {
									GetLawList(0, 1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						handle.sendEmptyMessage(41);
					}
				}.start();
			}

			else if (arg0 == 2) {

			}

			else if (arg0 == 1) {
				new Thread() {
					@Override
					public void run() {
						try {
							if (jobdatas.size() == 0)
								GetJobList(1);
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
						handle.sendEmptyMessage(2);
					}
				}.start();

			}
			else if (arg0 == 0) {

			}
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
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

	
	public void AgentJsonParser(String jsonStr) throws Exception {  
		 JSONObject jsonObject=new JSONObject(jsonStr.substring(1,jsonStr.length()-1));     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object   
         totalpage=jsonObject.getInt("totalPage");    
         curPage=jsonObject.getInt("currentPage");    
         startRow=jsonObject.getInt("startRow");    
         endRow=jsonObject.getInt("endRow");    
         pagesize = jsonObject.getInt("pageSize");   
         if(totalpage == 0)//没有结果
         	return ;
            
         JSONArray jsonArray = jsonObject.getJSONArray("odList");//里面有一个数组数据，可以用getJSONArray获取数组   
         if(curPage == 1){
         	agentdatas = new ArrayList<AgencyInfo>();
         }
         group = new ArrayList<String>();
         child = new ArrayList<ArrayList<String>>();
         
         //添加数据
         for (int i = 0; i < jsonArray.length(); i++) {   
             JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象 
             AgencyInfo info = new AgencyInfo();
             info.addr = item.getString("address");
             info.disc = item.getString("discrict");
             info.name = item.getString("orgName");
             info.phone = item.getString("phone");
             info.postcode = item.getString("postcode");
             info.province = item.getString("province");
             info.type = item.getString("chnlDesc");
             agentdatas.add(info);   
         }
         for (int i=startRow-1;i < agentdatas.size();i++){
				group.add(agentdatas.get(i).name);
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.add(agentdatas.get(i).addr);
				tmp.add(agentdatas.get(i).phone);
				tmp.add(agentdatas.get(i).postcode);
				child.add(tmp);
		} 
         
     }   
	public void LawJsonParser(String jsonStr) throws Exception {  
		JSONObject jsonObject=new JSONObject(jsonStr.substring(1,jsonStr.length()-1));     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object   
		int curN = lawTabRg.getCheckedRadioButtonId() - R.id.t41;
		lawPagedatas.get(curN).curPage = jsonObject.getInt("currentPage");  
		lawPagedatas.get(curN).totalPage = jsonObject.getInt("totalPage");  
		lawPagedatas.get(curN).pageSize = jsonObject.getInt("pageSize");  
		JSONArray jsonArray = jsonObject.getJSONArray("lawList");//里面有一个数组数据，可以用getJSONArray获取数组   
		//添加数据
		for (int i = 0; i < jsonArray.length(); i++) {   
			JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象 
			ListInfo info = new ListInfo();
			info.title = item.getString("title");
			info.url = item.getString("lawurl").replace("id", "&id");
			lawdatas.get(curN).add(info);   
		}
		
	}   
	public void JobJsonParser(String jsonStr) throws Exception {  
		JSONObject jsonObject=new JSONObject(jsonStr.substring(1,jsonStr.length()-1));     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object   

		jpagedata.curPage = jsonObject.getInt("currentPage");  
		jpagedata.totalPage = jsonObject.getInt("totalPage");  
		jpagedata.pageSize = jsonObject.getInt("pageSize");  
		JSONArray jsonArray = jsonObject.getJSONArray("joblist");//里面有一个数组数据，可以用getJSONArray获取数组   
		//添加数据
		for (int i = 0; i < jsonArray.length(); i++) {   
			JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象 
			JobInfo info = new JobInfo(item.getString("city"),item.getString("name"),item.getString("companyname"),
					item.getString("detailurl"),item.getString("province"),item.getString("sourceid"),item.getString("ldatetime"));
			jobdatas.add(info);  
		}
		
	}   
	public void NewsJsonParser(String jsonStr) throws Exception {  
		JSONObject jsonObject=new JSONObject(jsonStr.substring(1,jsonStr.length()-1));     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object   
		int curN = newsTabRg.getCheckedRadioButtonId() - R.id.t11;
		newsPagedatas.get(curN).curPage = jsonObject.getInt("currentPage");  
		newsPagedatas.get(curN).totalPage = jsonObject.getInt("totalPage");  
		newsPagedatas.get(curN).pageSize = jsonObject.getInt("pageSize");  
		JSONArray jsonArray = jsonObject.getJSONArray("contentList");//里面有一个数组数据，可以用getJSONArray获取数组   
		//添加数据
		for (int i = 0; i < jsonArray.length(); i++) {   
			JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象 
			ListInfo info = new ListInfo();
			info.title = item.getString("title");
			info.url = item.getString("contentUrl");
			info.channelName = jsonObject.getString("channelName");
			newsdatas.get(curN).add(info);   
		}
		
	}   
    //机构搜索
	public void DoSearch(String province,String type,String key,int page) throws Exception{
		AgentJsonParser(JsonUtil.getJsonString(URLS.SERVICES_URL + "province=" + province + "&id=" + type +"&page=" + page + "&keywords=" + key));
	}
		
	
	public String GetCity(double d1, double d2)
			throws UnsupportedEncodingException, IOException {

		ArrayList<String> cityarr = new ArrayList<String>();
		URL homepage = new URL(
				String.format(
						"http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&language=zh-CN&sensor=true",
						Double.toString(d1), Double.toString(d2)));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				homepage.openStream(), "utf-8"));
		String str;
		while ((str = in.readLine()) != null) {
			// System.out.println(str);
			if (str.contains("formatted")) {
				cityarr.add((str.substring(str.indexOf("国") + 1)).replace(
						"\",", ""));
				System.out.println(str);
			}
		}
		Log.d("ddd", "" + cityarr.size());
		if (cityarr.size() == 1 || cityarr.size() == 0) {
		} else {
			System.out.println(cityarr.get(cityarr.size() - 3));
			return cityarr.get(cityarr.size() - 3);
		}
		return "杭州";
	}

	
	public void GetLawList(int curN,int page) throws Exception{
		int id = 201 + curN;//请求的ID
		LawJsonParser(JsonUtil.getJsonString(URLS.LAWS_URL + "id=" + id +"&page=" + page));
	}
	public void GetJobList(int page) throws Exception{
		JobJsonParser(JsonUtil.getJsonString(URLS.EMPLOYMENT_URL + "page=" + page));
	}
	public void GetNewsList(int curN,int page) throws Exception{
		int channelId = 2;//请求的ID
		switch(curN){
			case 0:
				channelId = 2;
				break;
			case 1:
				channelId = 3;
				break;
			case 2:
				channelId = 6;
				break;
		}
		NewsJsonParser(JsonUtil.getJsonString(URLS.NEWS_URL + "channelId=" + channelId +"&page=" + page));
	}
	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			proselected = pro.get(arg2);

			// Toast.makeText(getApplicationContext(),
			// "您选择的是"+adapterpv.getItem(arg2), 5).show();
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}
	class SpinnerXMLSelectedListener2 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			typeselected = types.get(arg2);
			
			// Toast.makeText(getApplicationContext(),
			// "您选择的是"+adapterpv.getItem(arg2), 5).show();
		}
		
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}

	
	/***
	 * 执行类 异步
	 * 
	 */
	class MyAsyncTask extends AsyncTask<Void, Void, Void> {
		// private Context context;
		private int index;// 用于判断是下拉刷新还是点击加载更多

		public MyAsyncTask(Context context, int index) {
			// this.context = context;
			this.index = index;
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				if (pageflag == 1) {
					int curN = newsTabRg.getCheckedRadioButtonId() - R.id.t11;
					if(newsPagedatas.get(curN).curPage<newsPagedatas.get(curN).totalPage){//还可以更多
						newsPagedatas.get(curN).curPage++;
						if (index != DRAG_INDEX) {
							GetNewsList(curN, newsPagedatas.get(curN).curPage);
						}
					}else{
						
						l1.onLoadMoreComplete(false);
					}

				} else if (pageflag == 2) {
					if(jpagedata.curPage < jpagedata.totalPage){//还可以更多
						jpagedata.curPage++;
						if (index != DRAG_INDEX) {
							GetJobList(jpagedata.curPage);
						}
					}else{
						
						l4.onLoadMoreComplete(false);
					}
				}
				else if (pageflag == 4) {
					int curN = lawTabRg.getCheckedRadioButtonId() - R.id.t41;
					if(lawPagedatas.get(curN).curPage<lawPagedatas.get(curN).totalPage){//还可以更多
						lawPagedatas.get(curN).curPage++;
						if (index != DRAG_INDEX) {
							GetLawList(curN, lawPagedatas.get(curN).curPage);
						}
					}else{
						
						l4.onLoadMoreComplete(false);
					}
				}

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
			// do adding things
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
			// 下拉函数
			if (index == DRAG_INDEX) {
				if (pageflag == 1) {
					l1.onRefreshComplete();
				}

				if (pageflag == 2) {
					l2.onRefreshComplete();
				}
				if (pageflag == 4) {
					l4.onRefreshComplete();
				}
			}
			// 点击more函数
			else if (index == LOADMORE_INDEX) {
				if (pageflag == 1) {
					int curN = newsTabRg.getCheckedRadioButtonId() - R.id.t11;
					if(newsPagedatas.get(curN).curPage<newsPagedatas.get(curN).totalPage){//还可以更多
						newslistshow(newsdatas.get(curN));
						l1.setSelection((newsPagedatas.get(curN).curPage - 1 ) * newsPagedatas.get(curN).pageSize - 11);
						Toast.makeText(getApplicationContext(), "加载完成", 5).show();
					}else
						Toast.makeText(getApplicationContext(), "加载已经到底了", 5).show();
					l1.onLoadMoreComplete(false);
				} else	if (pageflag == 2) {
					if(jpagedata.curPage < jpagedata.totalPage){//还可以更多
						//joblistshow(jobdatas);
						listItemAdapter2.notifyDataSetChanged();
						//l2.setSelection(jobdatas.size() - 8);
						Toast.makeText(getApplicationContext(), "加载完成", 5).show();
					}else
						Toast.makeText(getApplicationContext(), "加载已经到底了", 5).show();
					l2.onLoadMoreComplete(false);
				} else	if (pageflag == 4) {
					int curN = lawTabRg.getCheckedRadioButtonId() - R.id.t41;
					if(lawPagedatas.get(curN).curPage<lawPagedatas.get(curN).totalPage){//还可以更多
						lawlistshow(lawdatas.get(curN));
						l4.setSelection((lawPagedatas.get(curN).curPage - 1 ) * lawPagedatas.get(curN).pageSize - 7);
						Toast.makeText(getApplicationContext(), "加载完成", 5).show();
					}else
						Toast.makeText(getApplicationContext(), "加载已经到底了", 5).show();
					
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

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "登陆").setIcon(

		android.R.drawable.ic_menu_manage);

		//
		// menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(
		//
		// android.R.drawable.ic_menu_edit);
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
			// Toast.makeText(this, "1删除菜单被点击了", Toast.LENGTH_LONG).show();
			finish();
			break;

		// case Menu.FIRST + 2:
		//
		// Toast.makeText(this, "2保存菜单被点击了", Toast.LENGTH_LONG).show();
		//
		// break;
		}
		return false;
	}
	
	public final class ViewHolder{
		           public TextView t1;
		           public TextView t2;
		           public TextView t3;
		           public TextView t4;
		           public TextView t5;
		        }
	private final class JobListAdapter extends BaseAdapter {  
		private LayoutInflater mInflater;
        public JobListAdapter(Context context){
        	               this.mInflater = LayoutInflater.from(context);
        	          }

          
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
                 
        	 ViewHolder holder = null;
        	                if (convertView == null) {
        	                    holder=new ViewHolder(); 
        	                    convertView = mInflater.inflate(R.layout.jobitem, null);
        	                    holder.t1 = (TextView)convertView.findViewById(R.id.jobname);
        	                    holder.t2 = (TextView)convertView.findViewById(R.id.jobcity);
        	                    holder.t3 = (TextView)convertView.findViewById(R.id.jobcomp);
        	                    holder.t4 = (TextView)convertView.findViewById(R.id.jobweb);
        	                    holder.t5 = (TextView)convertView.findViewById(R.id.jobtime);
        	                    convertView.setTag(holder);
        	                }else {
        	                     holder = (ViewHolder)convertView.getTag();
        	                 }
        	                 holder.t1.setText((String)jobdatas.get(position).name);
        	                 holder.t2.setText((String)jobdatas.get(position).city);
        	                 holder.t3.setText((String)jobdatas.get(position).province);
        	                 holder.t4.setText((String)jobdatas.get(position).cmp);
        	                 holder.t5.setText((String)jobdatas.get(position).time);
        	                  
        	                 return convertView;
        }


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jobdatas.size();
		}


		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}  
 }  
}