package com.example.cdpsn;



import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class RegisterActivity extends ActivityGroup {  
    /** Called when the activity is first created. */  
    private TabHost tabHost;  
    private TabWidget tabWidget;

    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reg);
        Intent BatteryS_Intent=new Intent(this,AregActivity.class);
        Intent SVEMode_Intent=new Intent(this,BregActivity.class);
        try{  
            tabHost = (TabHost) this.findViewById(R.id.TabHost01);  
            tabHost.setup(RegisterActivity.this.getLocalActivityManager());
             TextView TABbtn1=(TextView) this.getLayoutInflater().inflate(R.layout.tabs_button, null);
             TABbtn1.setText("残障人士会员注册");
             TABbtn1.setTextSize(15);
             TextView TABbtn2=(TextView) this.getLayoutInflater().inflate(R.layout.tabs_button,null);
             TABbtn2.setText("个人会员注册");
             TABbtn2.setTextSize(15);
             //添加tab标签
             tabHost.addTab(tabHost.newTabSpec("0").setContent(BatteryS_Intent).setIndicator(TABbtn1));
             tabHost.addTab(tabHost.newTabSpec("1").setContent(SVEMode_Intent).setIndicator(TABbtn2));
             tabWidget=tabHost.getTabWidget();
             ((LinearLayout.LayoutParams)TABbtn1.getLayoutParams()).setMargins(2, 0, 8,0);
             ((LinearLayout.LayoutParams)TABbtn2.getLayoutParams()).setMargins(0, 0, 2,0);
            
          
            tabHost.setCurrentTab(0);
        }catch(Exception ex){  
            ex.printStackTrace();  
            Log.d("EXCEPTION", ex.getMessage());  
        }  
    }  
}  