package com.example.cdpsn;

import java.util.Date;

import com.example.cdpsn.tool.GenericTask;
import com.example.cdpsn.tool.NetInfoFetcher;
import com.example.cdpsn.tool.TaskAdapter;
import com.example.cdpsn.tool.TaskListener;
import com.example.cdpsn.tool.TaskParams;
import com.example.cdpsn.tool.TaskResult;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AregActivity extends Activity {
	private TextView tv;
	private Button RegBtn;
	private EditText emailt,pwdt,pwdt2,checkt,namet,cardt,usernamet;
	private CheckBox acceptBox;
	private GenericTask _RegTask;
	String t1,t2,t3,t4,t5,t6,t7;
	private Dialog dialog;
	WebView wv ;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_areg);
		findViews();
	}

	private void findViews() {
		wv = (WebView)findViewById(R.id.chkwv); 
		//chk = (ImageView) findViewById(R.id.checkimage);
		String url = "http://www.cdpsn.org.cn/register/image.htm?"+(new Date()).getTime();
		wv.loadUrl(url);
		RegBtn = (Button) findViewById(R.id.regbtna);
		acceptBox = (CheckBox) findViewById(R.id.checkBox);
		checkt=(EditText) findViewById(R.id.checknum);
		emailt=(EditText) findViewById(R.id.email);
		pwdt=(EditText) findViewById(R.id.pwd);
		pwdt2=(EditText) findViewById(R.id.checkpwd);
		namet=(EditText) findViewById(R.id.name);
		cardt=(EditText) findViewById(R.id.card);
		usernamet=(EditText) findViewById(R.id.username);
		tv=(TextView) findViewById(R.id.regtktv);
		tv.setText(Html.fromHtml("<u>中国残疾人服务网用户服务条款</u>"));
		
		wv.setOnClickListener(new OnClickListener() {
			
			@Override	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String urlt = "http://www.cdpsn.org.cn/register/image.htm?"+(new Date()).getTime();
				wv.loadUrl(urlt);
				wv.reload();
				
			}
		});
		RegBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!acceptBox.isChecked()){
					onMsg("请仔细查阅中国残疾人服务网用户服务条款");
					return;
				}else if(cardt.getText().length()<20){
					onMsg("残疾证号码输入有误");
					return;
					
				}else if(namet.getText().length()==0||checkt.getText().length()==0||emailt.getText().length()==0||pwdt.getText().length()==0||pwdt2.length()==0){
					onMsg("还有有内容尚未填写！");
					return;
				}else if(usernamet.getText().length()<3){
					onMsg("用户名过短");
					return;
				}
				else if(pwdt.getText().length()<6 || pwdt.getText().length()>14){
					onMsg("密码长度不符合要求");
					return;
				}else if(!(pwdt.getText().toString().equals(pwdt2.getText().toString()))){
					onMsg("两次密码输入不一致");
					Log.d("ret",pwdt.getText()+"  "+pwdt2.getText());
					return;
				}else 
					doReg();
			}
		});
		
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
	 			it.setClass(AregActivity.this,TextActivity.class);//跳转
	 			startActivity(it);
			}
		});
		
	}
private class RegTask extends GenericTask {

		
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];

			try {
				String usern = param.getString("username");
				String name = param.getString("name");				
				String card = param.getString("card");
				String email = param.getString("email");
				String pwd = param.getString("password");
				String cknum = param.getString("checknum");
				return NetInfoFetcher.Rega(usern,name,card,email,pwd,cknum);
				
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.REG_SERVER_INTERNAL_ERROR;
			}
		}
	}
	private void doReg() {
		
		
		t1 = namet.getText().toString();
		t2 = cardt.getText().toString();
		t3 = usernamet.getText().toString();
		t4 = pwdt.getText().toString();
		t6 = emailt.getText().toString();
		t7 = checkt.getText().toString();
		if (_RegTask != null
				&& _RegTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			
				_RegTask = new RegTask(); 
				_RegTask.setListener(mRegTaskListener);
				
				TaskParams params = new TaskParams();
				params.put("name", t1);
				params.put("card", t2);
				params.put("username", t3);
				params.put("password", t4);
				params.put("email", t6);
				params.put("checknum", t7);
				_RegTask.execute(params);Log.d("ret","execute");
			
		}
	}
	
	private TaskListener mRegTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onRegBegin();
		}

		private void onRegSuccess() {
			dialog.dismiss();
			Toast.makeText(AregActivity.this, "注册成功", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.REG_SUCCESS) 
				onRegSuccess();
			 else if (result == TaskResult.REG_ED) {
				onRegFailure("用户名已存在");
			}else if (result == TaskResult.REG_CARDERROR) {
				onRegFailure("残疾证号码输入有误");
			} else if (result == TaskResult.REG_CHKERROR) {
				onRegFailure("验证码错误或已过期，请检查");
			} else if (result == TaskResult.REG_SERVER_INTERNAL_ERROR) {
				onRegFailure("信息有误，请重新注册");
			}
			else {
				onRegFailure("未知网络错误");
			}
		}

		public String getName() {
			return "RegTask";
		}
	};
	private void onRegFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	private void onRegBegin() {
		dialog = ProgressDialog.show(AregActivity.this, "", "正在提交信息,请稍后...", true);
		dialog.setCancelable(false);
	}
	
	
	
	private void onMsg(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.areg, menu);
		return true;
	}

}
