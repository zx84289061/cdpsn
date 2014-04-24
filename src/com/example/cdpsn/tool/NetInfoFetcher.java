package com.example.cdpsn.tool;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;

   
public class NetInfoFetcher {
	/**
	 * @param args
	 */
	private static WrappedHttpClient whc = new WrappedHttpClient();
	public static String loginpostpage = "http://www.cdpsn.org.cn/j_spring_security_check";
	public static String regpostpageb ="http://www.cdpsn.org.cn/register/emailRegister.htm";
	public static String regpostpagea = "http://www.cdpsn.org.cn/register/blindRegister.htm";
	private static String index = "http://www.cdpsn.org.cn/login.htm?target=http://www.cdpsn.org.cn/#login";
	private static String _id, _ps;
	private static String _email, _pwd, _nick,_checknum,_name,_card,_usern;

//	public static void setId(String id) {
//		_id = id;
//	}
//
//	public static void setPs(String ps) {
//		_ps = ps;
//	}
//
//	public static String getId() {
//		return _id;
//	}
//(usern,name,card,email,pwd,cknum);
//	public static String getPs() {
//		return _ps;
//	}
	public static TaskResult Rega(String usern,String name,String card,String email,String pwd,String cknum)
			throws Exception {
		Log.d("ret","dorega");
		_email = email;
		_pwd = pwd;
		_checknum=cknum;
		_name = name;
		_card = card;
		_usern = usern;
		String ret = null;
		whc.cleanPostContent();
		whc.AddToPostContent("captcha_challenge", ""+true);
		whc.AddToPostContent("dcNumber", _card);
		whc.AddToPostContent("userName", _name);
		whc.AddToPostContent("loginId", _usern);
		whc.AddToPostContent("password", _pwd);
		whc.AddToPostContent("repassword", _pwd);
		whc.AddToPostContent("original", "1");
		whc.AddToPostContent("province", "");
		whc.AddToPostContent("city", "");
		whc.AddToPostContent("address", "");
		whc.AddToPostContent("mobile", "");
		whc.AddToPostContent("userEmail", _email);
		
		whc.AddToPostContent("txtVerifyCode", _checknum);
		
		ret = whc.PostContent(regpostpagea);
		//Log.d("ret",ret);
		if (ret.contains("出错"))
			return TaskResult.REG_SUCCESS;
		else if (ret.contains("用户名已存在")) 
				return TaskResult.REG_ED;
		else if (ret.contains("验证码不正确")) 
				return TaskResult.REG_CHKERROR;
		else if (ret.contains("请输入正确的残疾证")) 
			return TaskResult.REG_CARDERROR;
		else return TaskResult.REG_SERVER_INTERNAL_ERROR;
		
	}

	public static TaskResult Regb(String email, String ps, String nick,String cknum)
			throws Exception {
		Log.d("ret","doregb");
		_email = email;
		_pwd = ps;
		_nick = nick;
		_checknum=cknum;
		String indexpage;
		String ret = null;
		//indexpage = whc.GetContent(index);
		//Log.d("ret1",indexpage);
		
		
		whc.cleanPostContent();
		whc.AddToPostContent("userEmail", _email);
		whc.AddToPostContent("password", _pwd);
		whc.AddToPostContent("rePassword", _pwd);
		whc.AddToPostContent("diminutive", _nick);
		whc.AddToPostContent("txtVerifyCode", _checknum);
		ret = whc.PostContent(regpostpageb);
		//Log.d("ret",ret);
		if (ret.contains("出错"))
			return TaskResult.REG_SUCCESS;
		else if (ret.contains("用户名已存在")) 
				return TaskResult.REG_ED;
		else if (ret.contains("验证码不正确")) 
				return TaskResult.REG_CHKERROR;
		else return TaskResult.REG_SERVER_INTERNAL_ERROR;
		
	} 
//	
//	public static String getcheck()throws Exception {Log.d("ret","getcheck");
//		GetCheckcode gt = new GetCheckcode("http://www.cdpsn.org.cn/register/image.htm?1376309636410",this);
//        gt.getRemoteFile();Log.d("ret","getcheck1");
//        gt.predo();Log.d("ret","getcheck2");
//        gt.find();Log.d("ret","getcheck3");
//        String stmp = null;
//        for(String s : gt.getcodes()){
//     	   System.out.println(s);
//     	   stmp+=s;Log.d("ret","getcheck"+stmp);
//        }
//		
//		return stmp;
//	}
	
	public static TaskResult Login(String id, String ps)
			throws Exception {
		Log.d("ret","dologin");
		_id = id;
		_ps = ps;
		String indexpage;
		String ret = null;
		
		whc.cleanPostContent();
		whc.AddToPostContent("j_username", id);
		whc.AddToPostContent("j_password", ps);
		
		ret = whc.PostContent(loginpostpage);
		//getcheck();
		if (ret.contains("错误"))
			return TaskResult.LOGIN_PASSWORD_INCORRECT;
		else 
			return TaskResult.LOGIN_SUCCESS;
		 
	}

	public static String GetPageContent(String Url) throws Exception {
		String resString = whc.GetContent(Url);
		return resString;
	}
}
