package com.appsbee.shareveil.activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class TrackLocation implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	private static TrackLocation sInstance;
	private static Context sContext;
	private static LocationClient mLocationClient;
	private static LocationRequest mLocationRequest;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND* UPDATE_INTERVAL_IN_SECONDS;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND* FAST_CEILING_IN_SECONDS;
	
	
	private TrackLocation(final Context context) {
		sContext = context;		
	}	
	public static TrackLocation tackInstance(final Context context) {		
		if (sInstance == null) {
			sInstance = new TrackLocation(context);			
			open();
		}
		return sInstance;
	}
	private static void open() {		
		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(sContext, sInstance, sInstance);
		mLocationClient.connect();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(sInstance);
			mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);
		}		
	}	  
	public void removeLocationUpdate(){
		mLocationClient.removeLocationUpdates(this);		
	}	
	public void requestUpdate(){
		mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);
		
	}	
	public void onConnectionFailed(ConnectionResult result) {}	
	public void onConnected(Bundle connectionHint) {		
		mLocationClient.requestLocationUpdates(mLocationRequest,this);
	}	public void onDisconnected() {	}	
	public void onLocationChanged(Location location) {
		Constant.lat = ""+location.getLatitude();
		Constant.lon = ""+location.getLongitude();
	}
}
