package in.syntaxerror.jogging;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
	
	private final Context mContext ;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	 Location location; 
	    double latitude; 
	    double longitude;
	    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	    protected LocationManager locationManager;
	    
	    public GPSTracker(Context context) {
	        this.mContext = context;
	       getLocation();
	    }
	    
	    
	    public Location getLocation() {
	    	
			return null;
			// TODO Auto-generated method stub
			
		}


		public void stopUsingGPS(){
	        if(locationManager != null){
	            locationManager.removeUpdates(GPSTracker.this);
	        }       
	    }
	    
	    public double getLatitude(){
	        if(location != null){
	            latitude = location.getLatitude();
	        }
	        return latitude;
	    }
	    
	    
	    public double getLongitude(){
	        if(location != null){
	            longitude = location.getLongitude();
	        }
	        return longitude;
	    }
	    
	    public boolean canGetLocation() {
	        return this.canGetLocation;
	    }
	    
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	
}
