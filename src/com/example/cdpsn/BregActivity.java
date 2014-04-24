package com.example.cdpsn;

import java.net.URL;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cdpsn.tool.GenericTask;
import com.example.cdpsn.tool.NetInfoFetcher;
import com.example.cdpsn.tool.TaskAdapter;
import com.example.cdpsn.tool.TaskListener;
import com.example.cdpsn.tool.TaskParams;
import com.example.cdpsn.tool.TaskResult;

public class BregActivity extends Activity {
	private TextView tktv;
	private EditText emailt,pwdt,pwdt2,nickt,checkt;
	private CheckBox acceptBox;
	private Button RegBtn;
	private GenericTask _RegTask;
	String t1,t2,t3,t4,t5;
	private Dialog dialog;
	WebView wv ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_breg);
		findViews();
	}

	private void findViews() {
        wv = (WebView)findViewById(R.id.chkwv); 
		//chk = (ImageView) findViewById(R.id.checkimage);
		String url = "http://www.cdpsn.org.cn/register/image.htm?"+(new Date()).getTime();
		wv.loadUrl(url);
		
		RegBtn = (Button) findViewById(R.id.regbtnb);
		
		acceptBox = (CheckBox) findViewById(R.id.checkBoxb);
		checkt=(EditText) findViewById(R.id.checknumb);
		emailt=(EditText) findViewById(R.id.emailb);
		pwdt=(EditText) findViewById(R.id.pwdb);
		pwdt2=(EditText) findViewById(R.id.checkpwdb);
		nickt=(EditText) findViewById(R.id.nicknameb);
		tktv=(TextView) findViewById(R.id.regtktvb);
		tktv.setText(Html.fromHtml("<u>中国残疾人服务网用户服务条款</u>"));
		tktv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
	 			it.setClass(BregActivity.this,TextActivity.class);//跳转
	 			startActivity(it);
			}
		});
		wv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String urlt = "http://www.cdpsn.org.cn/register/image.htm?"+(new Date()).getTime();
				wv.loadUrl(urlt);
			}
		});
		RegBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!acceptBox.isChecked()){
					onMsg("请仔细查阅中国残疾人服务网用户服务条款");
					return;
				}
				else if(checkt.getText().length()==0||emailt.getText().length()==0||pwdt.getText().length()==0||pwdt2.length()==0||nickt.getText().length()==0){
					onMsg("还有有内容尚未填写！");
					return;
				}
				
				else if(pwdt.getText().length()<6 || pwdt.getText().length()>14){
					onMsg("密码长度不符合要求");
					return;
				}
				else if(!(pwdt.getText().toString().equals(pwdt2.getText().toString()))){
					onMsg("两次密码输入不一致");
					Log.d("ret",pwdt.getText()+"  "+pwdt2.getText());
					return;
				}
				
				else 
					doReg();
				
			}
		});
	}

	
	
	private class RegTask extends GenericTask {

		
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];

			try {
				String email = param.getString("email");
				String pwd = param.getString("password");
				String nick = param.getString("nick");
				String cknum = param.getString("checknum");
				return NetInfoFetcher.Regb(email, pwd, nick, cknum);
				
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.REG_SERVER_INTERNAL_ERROR;
			}
		}
	}
	private void doReg() {
		
		t1 = emailt.getText().toString();
		t2 = pwdt.getText().toString();
		t3 = nickt.getText().toString();
		t5 = checkt.getText().toString();
		if (_RegTask != null
				&& _RegTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			
				_RegTask = new RegTask(); 
				_RegTask.setListener(mRegTaskListener);
				
				TaskParams params = new TaskParams();
				params.put("email", t1);
				params.put("password", t2);
				params.put("nick", t3);
				params.put("checknum", t5);
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
			Toast.makeText(BregActivity.this, "注册成功", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.REG_SUCCESS) 
				onRegSuccess();
			 else if (result == TaskResult.REG_ED) {
				onRegFailure("用户名已存在");
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
		dialog = ProgressDialog.show(BregActivity.this, "", "正在提交信息,请稍后...", true);
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
		getMenuInflater().inflate(R.menu.breg, menu);
		return true;
	}

}
