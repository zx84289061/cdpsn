package com.example.cdpsn.data;

public class JobInfo {
	public String city;
	public String name;
	public String cmp;
	public String url;
	public String province;
	public String source;
	public String time;
	
	public JobInfo(String s1,String s2,String s3,String s4,String s5,String s6,String s7)
	{
		city = s1;
		name = s2;
		cmp = s3;
		url = s4;
		province = s5;
		time = s7;
		if(time.length() <= 1)
			time = "";
		switch(Integer.parseInt(s6))
		{
			case 127:
				source = "������Ƹ";
				break;
			case 128:
				source = "�ϼ���";
				break;
			case 138:
				source = "�й��м��˷�����";
				break;
			case 139:
				source = "58ͬ��";
				break;
		}
	}
	
}
