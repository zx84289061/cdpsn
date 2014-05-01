package com.example.cdpsn;

import android.content.Context;  
import android.view.LayoutInflater;  
import android.view.View;   
import android.view.ViewGroup;  
import android.widget.BaseAdapter;   
import android.widget.TextView;  

class ViewHolder {    
    public TextView cn_word;   
}    
  
public class AnimalListAdapter extends BaseAdapter {    
    private LayoutInflater mInflater = null;  
    public AnimalListAdapter(Context context){  
        super();  
        mInflater = (LayoutInflater) context  
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return 50;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder = null;    
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.jobitem, null);      
            holder.cn_word = (TextView) convertView.findViewById(R.id.jobname);     
            convertView.setTag(holder);    
        } else {    
            holder = (ViewHolder) convertView.getTag();    
        }  
        holder.cn_word.setText("xxxxx");  
        return convertView;  
    }    
  
}  