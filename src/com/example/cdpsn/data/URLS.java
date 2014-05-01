package com.example.cdpsn.data;

public class URLS {
	static public String URL_PREFIX = "http://www.cdpsn.org.cn/phone/";
	static public String NEWS_URL = URL_PREFIX + "newslist.htm?";
	static public String DETAIL_NEWS_URL = URL_PREFIX + "newsdetail.htm?id={}"; 
	static public String LAWS_URL = URL_PREFIX + "policylist.htm?"; 
	static public String DETAIL_LAW_URL = URL_PREFIX + "policydetail.htm?key={1}&id={2}"; 
	static public String SERVICES_URL = URL_PREFIX + "directlist.htm?"; 
	static public String DETAIL_SERVICE_URL = URL_PREFIX + "directdetail.htm?id={1}"; 
	static public String EMPLOYMENT_URL = URL_PREFIX +"joblist.htm?"; 
	static public String DETAIL_EMPLOYMENT_URL = URL_PREFIX +"jobdetail.htm?id={1}"; 
	static public String SIGN_IN_URL = URL_PREFIX +"login?username={1}&password={2}"; 
	static public String SIGN_OUT_URL = "http://www.cdpsn.org.cn/j_spring_security_logout?logoutSuccessUrl={1};"; 
}
