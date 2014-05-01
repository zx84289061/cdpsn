package com.example.cdpsn;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Home_Fragment extends Fragment{
private String str;
	
	public String getStr()
	{
		return str;
	}
	
	public void setStr(String str)
	{
		this.str = str;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.moodhome, null);
		//TextView textView = (TextView) view.findViewById(R.id.text_view);
		//textView.setText(str);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.vPager);
		List<FragmentChild> arrayList = new ArrayList<FragmentChild>();
		for (int i = 0; i < 2; i++)
		{
			FragmentChild item = new FragmentChild();
			item.setStr("child"+1);
			arrayList.add(item);
		}
		/**
		 * 此处用getChildFragmentManager()
		 */
		ChildAdapterDemo adapterDemo = new ChildAdapterDemo(getChildFragmentManager(), arrayList);
		viewPager.setAdapter(adapterDemo);
		return view;
	}
	
	private class ChildAdapterDemo extends FragmentPagerAdapter
	{
		private List<FragmentChild> arrayList;
		
		public ChildAdapterDemo(FragmentManager fm, List<FragmentChild> arrayList)
		{
			super(getChildFragmentManager());
			this.arrayList = arrayList;
		}
		
		public ChildAdapterDemo(FragmentManager fm)
		{
			super(getChildFragmentManager());
		}
		
		@Override
		public Fragment getItem(int arg0)
		{
			return arrayList.get(arg0);
		}
		
		@Override
		public int getCount()
		{
			return arrayList.size();
		}
	}
}
