package com.example.cdpsn.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;


public class DetailsInfo {
	public String htmlcontent;//content
	public String comeFrom;
	public String metaDesc;//ժҪ
	public String author;
	public String createDate;
	
	@SuppressLint("SimpleDateFormat")
	public DetailsInfo(String s1,String s3,String s4,String s5,long time)
	{
		htmlcontent = s1;
		comeFrom = s3;
		metaDesc = s4;
		author = s5;
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");         
		Date date = new Date(time);     
		createDate = format.format(date);
	}
	
}
