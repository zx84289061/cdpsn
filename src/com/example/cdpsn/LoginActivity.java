package com.example.cdpsn;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.cdpsn.tool.GenericTask;
import com.example.cdpsn.tool.NetInfoFetcher;
import com.example.cdpsn.tool.TaskAdapter;
import com.example.cdpsn.tool.TaskListener;
import com.example.cdpsn.tool.TaskParams;
import com.example.cdpsn.tool.TaskResult;

public class LoginActivity extends Activity {
	private EditText _UsernameEdit;
	private EditText _PasswordEdit;
	private Button _SigninButton;
	private Button _RegButton,youkebtn;
	private CheckBox _rememberPasswordBox;
	private SharedPreferences setting;
	private boolean hasNetwork = false;
	private GenericTask _LoginTask;
	private Dialog dialog;
	private String _Password;
	private String _Username;
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String USERINFO = "USERINFO";
	public static final String AUTOLOGIN = "AUTOLOGIN";
	public static final String REMEMBERPWD = "REMEMBERPWD";
	private final static String FIRST_LOGIN = "FIRST_LOGIN";
	Intent intent = new Intent();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setting = getSharedPreferences(USERINFO, 0);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		findViews();
		checkNetworkState();
		
		if (setting.getBoolean(REMEMBERPWD, false)) {
			_UsernameEdit.setText(setting.getString(USERNAME, ""));
			_PasswordEdit.setText(setting.getString(PASSWORD, ""));
			_rememberPasswordBox.setChecked(true);
		}
		
		intent.setClass(LoginActivity.this, ContentActivity.class);

	}
	
	private void findViews() {
		_UsernameEdit = (EditText) findViewById(R.id.username_edit);
		_PasswordEdit = (EditText) findViewById(R.id.password_edit);
		_SigninButton = (Button) findViewById(R.id.signin_button);
		_RegButton = (Button) findViewById(R.id.reg_button);
		youkebtn = (Button) findViewById(R.id.yke);
		_rememberPasswordBox = (CheckBox) findViewById(R.id.rememberBox);
		
		_SigninButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				Log.d("ret","click");
				new Thread(){
					@Override
					public void run(){
						try {
							Thread.sleep(25000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (dialog != null) {
							dialog.dismiss();
						}
					}
					}.start(); 
				doLogin();
				
//				Intent it = new Intent();
//	 			it.setClass(LoginActivity.this,ContentActivity.class);//跳转
//	 			startActivity(it);
//	 			finish();
			}
		});
		
		youkebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("_id", "游客");
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
				
			}
		});
		
		_RegButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
	 			it.setClass(LoginActivity.this,RegisterActivity.class);//跳转
	 			startActivity(it);
			}
		});
	}

	
	private void onMsg(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	


	private TaskListener mLoginTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onLoginBegin();
		}


		
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.LOGIN_SUCCESS) {
				onLoginSuccess();
			} else if (result == TaskResult.LOGIN_PASSWORD_INCORRECT) {
				onLoginFailure("密码错误");
			} else if (result == TaskResult.LOGIN_SERVER_INTERNAL_ERROR) {
				onLoginFailure("服务器状态异常");
			} else if (result == TaskResult.LOGIN_NETWORK_ERROR) {
				onLoginFailure("联网错误");
			}
			else {
				onLoginFailure("未知网络错误");
			}
		}

		public String getName() {
			return "LoginTask";
		}
	};
	private void onLoginFailure(String reason) {
		Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	private void onLoginBegin() {
		dialog = ProgressDialog.show(LoginActivity.this, "", "正在登陆，请稍后...", true);
		dialog.setCancelable(false);
	}

	private void doLogin() {
			
		_Username = _UsernameEdit.getText().toString();
		_Password = _PasswordEdit.getText().toString();Log.d("ret",_Password);
		if (_LoginTask != null
				&& _LoginTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			if (!(_Username.equals("")) & !(_Password.equals(""))) {Log.d("ret","not empty");
				_LoginTask = new LoginTask(); 
				_LoginTask.setListener(mLoginTaskListener);
				
				TaskParams params = new TaskParams();
				params.put("username", _Username);
				params.put("password", _Password);
				_LoginTask.execute(params);Log.d("ret","execute");
			} else {
				onLoginFailure("账号和密码不能为空！");
			}
		}
	}
	private void checkNetworkState() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (wifi == State.CONNECTED || mobile == State.CONNECTED) {
			//有网络
			this.hasNetwork = true;
			
		}
		else {
			onMsg("请检查网络连接!");
		}
	}
	private class LoginTask extends GenericTask {

	
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];

			try {
				String username = param.getString("username");
				String password = param.getString("password");
				//先确保联网
				if (hasNetwork)
					return NetInfoFetcher.Login(username, password);
				else {
					SharedPreferences setting = getSharedPreferences(USERINFO,0);
					if (username.equals(setting.getString(USERNAME, ""))
						&& password.equals(setting.getString(PASSWORD, "")))
					return TaskResult.LOGIN_SUCCESS;
					else
					return TaskResult.LOGIN_PASSWORD_INCORRECT;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.LOGIN_NETWORK_ERROR;
			}
		}
	}

	private void onLoginSuccess() {
		
		Editor editor = getSharedPreferences(USERINFO, 0).edit();
		if (_rememberPasswordBox.isChecked()) {

			editor.putString(USERNAME, _Username)
					.putString(PASSWORD, _Password).putBoolean(REMEMBERPWD,
							true);

		} else {
			editor.putString(USERNAME, "").putString(PASSWORD, "").putBoolean(
					REMEMBERPWD, false);
		}
		editor.putBoolean(FIRST_LOGIN, false);
		editor.commit();
		Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG)
				.show();
		Bundle bundle = new Bundle();
		bundle.putString("_id", _Username);
		intent.putExtras(bundle);
		dialog.dismiss();
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
		finish();
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(this)
					.setMessage("确定退出？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							}).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
