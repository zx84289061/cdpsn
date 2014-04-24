package com.example.cdpsn.tool;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class WrappedHttpClient {
	private DefaultHttpClient client = new DefaultHttpClient();
	private boolean EnableRedirect = false;
	private List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	private String encoding = "utf8";
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	private List<Cookie> cookies;
	private String host;
	private String protocol;
	private final String TAG = "WrappedHttpClient";

	// private
	/**
	 * @param args
	 */

	public String ResponseToString(HttpResponse response) {
		String responseBody = null;
		HttpEntity entity = response.getEntity();
		try {
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
			}
			if (entity != null)
				entity.consumeContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	public void cleanPostContent() {
		nvps.clear();
	}

	public String GetContent(String url) throws ClientProtocolException, IOException  {
		HttpResponse response = null;
		String responseBody = "";
		HttpGet httpget = null;
		boolean flag = false;
		httpget = new HttpGet(url);
		response = client.execute(httpget);
		responseBody = ResponseToString(response);
		httpget.abort();
		return responseBody;
	}

	public void AddToPostContent(String id, String cnt) {
		nvps.add(new BasicNameValuePair(id, cnt));
	} 

	public String PostContent(String url) throws Exception {
		String res = "General Failure";
		HttpResponse response;
		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
		response = client.execute(httpost);
		res = ResponseToString(response);
		cookies = client.getCookieStore().getCookies();
		httpost.abort();
		return res;
	}
}
