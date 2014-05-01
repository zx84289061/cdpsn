package com.example.cdpsn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class FragmentChild extends ListFragment
{
	private String str;
	private AnimalListAdapter adapter = null;  
	public String getStr()
	{
		return str;
	}
	
	public void setStr(String str)
	{
		this.str = str;
	}
	
	 @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	          
	        adapter = new AnimalListAdapter (getActivity());  
	        setListAdapter(adapter);    
	    }  
	  
	    @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View v = inflater.inflate(R.layout.wall, container, false);  
	        return v;  
	    }  
	      
	    @Override  
	    public void onListItemClick(ListView l, View v, int position, long id) {  
	        System.out.println("Click On List Item!!!");  
	        super.onListItemClick(l, v, position, id);  
	    }  
}
