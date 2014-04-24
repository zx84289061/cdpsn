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
 * GPS������
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
	 * ��ȡGPSLocationHelperʵ��
	 */

	public static GPSLocationHelper getIntance() {
		return gpsLocationHelper;
	}

	/**
	 * ���GPS�ǿ���״̬����ʼʹ�ö�λ
	 * 
	 * @param context
	 * @param limitTime
	 *            GPS��λһ������ʱ��(��),����ʱ��ֹͣ��λ
	 */
	public void startGPSService(Context context, int limitTime) {
		// ��ȡLocationManagerʵ��
		gpsLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// GPS���ֻ�����û�б�������ֱ�ӷ���
		if (!(gpsLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)))
			return;
		// λ�ñ仯����,Ĭ��0��һ��,����0������
		gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, new MyLocationListener());
	}

	/**
	 * λ�ñ仯������,��ȡ��λ��ʱ������onLocationChanged
	 */
	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			// �˺��������У�������ȡ��λ��
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
	 * ֹͣ��ȡGPSλ��
	 */

	public void stopGPSService() {
		if (gpsLocationManager != null)
			gpsLocationManager.removeUpdates(mLocationListener);
	}

	/**
	 * �õ�λ����Ϣ
	 * 
	 * @return
	 */
	public Location getGPSLocation() {
		return location;
	}

}
