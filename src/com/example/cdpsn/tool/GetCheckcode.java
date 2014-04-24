package com.example.cdpsn.tool;

//import java.awt.Color;
//import java.awt.image.Bitmap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.example.cdpsn.ContentActivity;
import com.example.cdpsn.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
//import javax.imageio.ImageIO;


public class GetCheckcode {
	
	 String url;
	 Bitmap buffImg=null;
	 String[] codes = new String[4];
	 Context context = null;
	public GetCheckcode(String u,Context c) throws IOException{
		url = u;
		context=c;
		Log.d("ret","getcheck");
	}
	
	
	
//	  public static void main(String args[]) throws Exception { 
//          
//		  GetCheckcode gt = new GetCheckcode("http://www.cdpsn.org.cn/register/image.htm?1376309636410");
//           gt.getRemoteFile();
//           gt.predo();
//           gt.find();
//           for(String s : gt.getcodes()){
//        	   System.out.println(s);
//           }
//           
//      } 
//	  private Resources getResources() {
//		// TODO Auto-generated method stub
//		Resources mResources = null;
//		mResources = getResources();
//		return mResources;
//		}
	  public String[] getcodes(){
		  return codes;
	  } 

	  
	  char getname(int j){
		  char ch = 0;
		  if(j-R.drawable.c2<=7)
		  
			  ch=(char) ('2'+j-R.drawable.c2);
		  
		  else if(j-R.drawable.ca<=7)//i
			  
			  ch=(char) ('a'+j-R.drawable.ca);
		  
		  else if(j-R.drawable.cj<=4)//o
			  
			  ch=(char) ('j'+j-R.drawable.cj);
		  
		  else if(j-R.drawable.cp<=10)//o
			  
			  ch=(char) ('p'+j-R.drawable.cp);
		  return ch;
	  }
	  
	  public void find() throws Exception{ Log.d("ret","getcheck find()");
		  //图片分割成4块
          List<Bitmap> listImg = splitImage(buffImg) ;
		  HashMap<String,String> hm= new HashMap<String,String>(); Log.d("ret","getcheck find()1");
          File dir = new File("codes");   Log.d("ret","getcheck find()2");
          File[] files = dir.listFiles();   Log.d("ret","getcheck find()3");
          int rgb1,rgb2;
          int count = 0;
          double ok = 0;
          //Resources res=getResources();
          for(int i =0 ;i < 4; i++){
       	   hm.clear(); Log.d("ret","getcheck find()4");
	           for (int j=R.drawable.c2;j<=R.drawable.cz;j++) { 
	        	   Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), j);
	        	   
	        	   //Bitmap buffImgtmp=BitmapFactory.decodeResource(res, j);
	        	   count = 0;
	        	   ok = 0; Log.d("ret","getcheck find()5");
//	        	   FileInputStream fis = new FileInputStream(file);
//	        	   Bitmap buffImgtmp = BitmapFactory.decodeStream(fis); 
	        	   for (int y = 0; y < 14; y++) {
	              	    for (int x = 0; x < 14; x++) {
	              	    	count++;
	              	        rgb1 = listImg.get(i).getPixel(x, y);Log.d("ret","getcheck find() rgb1"+rgb1);
	              	        rgb2 = buffImg.getPixel(x, y);Log.d("ret","getcheck find() rgb2"+rgb2);
	              	        if(rgb1==rgb2 || rgb1<-10000000 && rgb2<-10000000)
	              	        	ok++;
	              	        } Log.d("ret","getcheck find()6");
	              	    }
	        	   double percent = ok/count;
	        	   System.out.println("tttpercent "+percent );
	        	   //把所有大于0.7的都放在map中
	        	   if(percent > 0.65)
	        		   hm.put(""+percent,""+getname(j));//file.getName().charAt(0)
	           }  
	           if(hm.size()==1) 
	           {
	        	   Iterator<String> it= hm.keySet().iterator();
	        	   while(it.hasNext()){
		        	   codes[i]=hm.get(it.next());}
	        	   System.out.println("ttt"+codes[i]);
	           }
	           else{//排序，取最大一个
	        	   Iterator<String> it= hm.keySet().iterator();
	        	   double dmax=0;
	        	   while(it.hasNext()){
	        		   double tmp = Double.parseDouble(it.next());
	        	    if(tmp>dmax)
	        	    	dmax = tmp;
	        	   }
	        	   codes[i]=hm.get(Double.toString(dmax)); Log.d("ret","getcheck find()7");
	           }  
          }
          Log.d("ret","cadddcadd");
          
		  
	  }
	  
	  public void predo() {  Log.d("ret","getcheckpredo()1");Log.d("ret","getcheckpredo()1"+buffImg);
          //首先灰度化，灰度值=0.3R+0.59G+0.11B： 
          int minY = 0;int minX = 0;
          int height = buffImg.getHeight();Log.d("ret","getcheckpredo()height"+height);
          int width = buffImg.getWidth();Log.d("ret","getcheckpredo()width"+width);
         // System.out.println("done"+width+"XXX"+height+"Y"+minY+"XX"+minX);
         // int average = buffImg.get
          for (int y = minY; y < height; y++) {Log.d("ret","getcheckpredo()y"+y);
              for (int x = minX; x < width; x++) {Log.d("ret","getcheckpredo()x"+x);
                  int pixel = buffImg.getPixel(x, y);Log.d("ret","dddxy");
                   // 根据rgb的int值分别取得r,g,b颜色。
                  int rgb1[]=new int[3]; Log.d("ret","dddxy3");
                  rgb1[0] = (pixel & 0xff0000 ) >> 16 ; 
       	    	  rgb1[1] = (pixel & 0xff00 ) >> 8 ; 
	              rgb1[2] = (pixel & 0xff ); Log.d("ret","dddxy4");
                  int pixel2 = (int) (0.3 * rgb1[0] + 0.59 * rgb1[1] + 0.11 * rgb1[2]);
                  int rgb2[]=new int[3];Log.d("ret","dddxy5");
                  rgb2[0] = (pixel2 & 0xff0000 ) >> 16 ; 
       	    	  rgb2[1] = (pixel2 & 0xff00 ) >> 8 ; 
	              rgb2[2] = (pixel2 & 0xff ); Log.d("ret","dddxy7"+rgb2);Log.d("ret","dddxy7"+rgb2[0]+""+rgb2[1]+""+rgb2[2]);
                  buffImg.setPixel(x, y, Color.argb(0, rgb2[0] , rgb2[1] , rgb2[2]));
                  Log.d("ret","dddxy8");
              }
          }
          
          Log.d("ret","getcheckpredo()2");
          //其次是灰度反转： 
          for (int y = minY; y < height; y++) {
       	    for (int x = minX; x < width; x++) {
       	        int pixel = buffImg.getPixel(x, y);
       	        Color color = new Color(); // 根据rgb的int值分别取得r,g,b颜色。
       	        Color newColor = new Color();
       	        int rgb1[]=new int[3];
       	        rgb1[0] = (pixel & 0xff0000 ) >> 16 ; 
       	    	rgb1[1] = (pixel & 0xff00 ) >> 8 ; 
              	rgb1[2] = (pixel & 0xff ); 
       	        buffImg.setPixel(x, y, Color.argb(0, 255-rgb1[0] , 255-rgb1[1] , 255-rgb1[2] ));
       	    }
       	}
          Log.d("ret","getcheckpredo()3");
          
          int average = 25;
          	//再次是二值化，取图片的平均灰度作为阈值，低于该值的全都为0，高于该值的全都为255：
          for (int y = minY; y < height; y++) {
       	    for (int x = minX; x < width; x++) {
       	        int rgb = buffImg.getPixel(x, y);
       	        Color color = new Color(); // 根据rgb的int值分别取得r,g,b颜色。
       	        int value = 255 - Color.BLUE;
       	        if (value > average) {
       	            //Color newColor = new Color();
       	            buffImg.setPixel(x, y, Color.BLACK);
       	        } else {
       	           // Color newColor = new Color();
       	            buffImg.setPixel(x, y, Color.WHITE);
       	        }
       	    }
       	}
          Log.d("ret","getcheckpredo()4");

	  }
	  
//	  void getpic() throws IOException{Log.d("ret","getcheckgetpic()");
//		    getRemoteFile(url,"codes\\int.jpg");
//	        //File buffImgFile = new File("codes\\int.jpg");
//	        FileInputStream fis = new FileInputStream("codes\\int.jpg");
//	        buffImg = BitmapFactory.decodeStream(fis);
//	       // buffImg = ImageIO.read(buffImgFile); 
//	  }
	  
	   List<Bitmap> splitImage(Bitmap img)  
	            throws Exception {  Log.d("ret","getcheck splitImage");
	        List<Bitmap> subImgs = new ArrayList<Bitmap>();  
	        subImgs.add(Bitmap.createBitmap(img,1, 5, 14, 14));  
	        subImgs.add(Bitmap.createBitmap(img,23, 5, 14, 14));  
	        subImgs.add(Bitmap.createBitmap(img,46, 5, 14, 14));  
	        subImgs.add(Bitmap.createBitmap(img,68, 5, 14, 14));   Log.d("ret","splitImagedone");
	        return subImgs;  
	    }  
	  
	   public void getRemoteFile() throws IOException { Log.d("ret","getcheck getRemoteFile");
	   String imagePath  = url;
	   
	   	File cache_file = new File(new File(Environment.getExternalStorageDirectory(), "xxxx"),"cachebitmap");

	   	cache_file = new File(cache_file,getMD5(imagePath));

	   

	   try {

	   URL url = new URL(imagePath);

	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	   conn.setConnectTimeout(5000);

	   if (conn.getResponseCode() == 200) {

	   InputStream inStream = conn.getInputStream();

	   File file = new File(new File(Environment.getExternalStorageDirectory(), "xxxx"),"cachebitmap");

	   if(!file.exists())file.mkdirs();

	   file = new File(file,getMD5(imagePath));

	   FileOutputStream out = new FileOutputStream(file);

	   byte buff[] = new byte[1024];

	   int len = 0;

	   while((len = inStream.read(buff))!=-1){

	   out.write(buff,0,len);

	   }

	   out.close();

	   inStream.close();

	   buffImg = BitmapFactory.decodeFile(getBitmapCache(imagePath)).copy(Bitmap.Config.ARGB_8888, true);  ;

	   }

	   } catch (Exception e) {}

	   }
	   
	   
	   
	   
	   public static String getBitmapCache(String url){

		   File file = new File(new File(Environment.getExternalStorageDirectory(), "xxxx"),"cachebitmap");

		   file = new File(file,getMD5(url));

		   if(file.exists()){

		   return file.getAbsolutePath();

		   }

		   return null;

		   }

		   //加密为MD5

		   public static String getMD5(String content) {

		   try {

		   MessageDigest digest = MessageDigest.getInstance("MD5");

		   digest.update(content.getBytes());

		   return getHashString(digest);



		   } catch (Exception e) {

		   }

		   return null;

		   }



		   private static String getHashString(MessageDigest digest) {

		           StringBuilder builder = new StringBuilder();

		           for (byte b : digest.digest()) {

		               builder.append(Integer.toHexString((b >> 4) & 0xf));

		               builder.append(Integer.toHexString(b & 0xf));

		           }

		           

		           return builder.toString().toLowerCase();

		       }
	   
	   
	  // }
//		     URL url = new URL(strUrl); 
//		     HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
//		     DataInputStream input = new DataInputStream(conn.getInputStream()); 
//		     DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName)); 
//		    byte[] buffer = new byte[1024 * 8]; 
//		    int count = 0; 
//		    while ((count = input.read(buffer)) > 0) { 
//		       output.write(buffer, 0, count); 
//		     } 
//		     output.close(); 
//		     input.close(); 
//		    return true; 
//		   }
	  
}
