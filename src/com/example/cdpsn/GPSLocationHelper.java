package com.example.cdpsn;


import java.util.Iterator;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * GPS助手类
 * 
 */
public class GPSLocationHelper {
	public Location location;
	public LocationManager gpsLocationManager;
	public MyLocationListener mLocationListener = new MyLocationListener();
	public static GPSLocationHelper gpsLocationHelper = new GPSLocationHelper();
	public static final String tag = "GPSLocationHelper";

	public GPSLocationHelper() {
	}

	/**
	 * 获取GPSLocationHelper实例
	 */

	public static GPSLocationHelper getIntance() {
		return gpsLocationHelper;
	}

	/**
	 * 如果GPS是开启状态，则开始使用定位
	 * 
	 * @param context
	 * @param limitTime
	 *            GPS定位一次限制时间(秒),超过时间停止定位
	 */
	public void startGPSService(Context context, int limitTime) {
		// 获取LocationManager实例
		gpsLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// GPS在手机里面没有被开启，直接返回
		if (!(gpsLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)))
			return;
		// 位置变化监听,默认0秒一次,距离0米以上
		gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, new MyLocationListener());
	}

	/**
	 * 位置变化监听类,获取到位置时将调用onLocationChanged
	 */
	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			// 此函数被运行，表明获取到位置
			GPSLocationHelper.this.location = location;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	/**
	 * 停止获取GPS位置
	 */

	public void stopGPSService() {
		if (gpsLocationManager != null)
			gpsLocationManager.removeUpdates(mLocationListener);
	}

	/**
	 * 得到位置信息
	 * 
	 * @return
	 */
	public Location getGPSLocation() {
		return location;
	}

}
