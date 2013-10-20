package in.syntaxerror.jogging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity {

	
	private double weight;
	private GoogleMap googleMap;
	private GPSTracker gps;
	private Date start;
	private ArrayList<Double> lat = new ArrayList<Double>();
	private ArrayList<Double>  lng = new ArrayList<Double>();
	private ArrayList<Double> dis = new ArrayList<Double>();
	private Timer timer;
	private TimerTask doAsynchronousTask;
    private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		
		setUpActionBar();
		
		weight = Double.parseDouble(getIntent().getExtras().getString(Constants.WEIGHT));
		
		try {
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
		
        LocateDirections(0,0,0,0,GMapV2Direction.MODE_DRIVING);
        

		 gps = new GPSTracker(MapActivity.this);
		// check if GPS enabled     
        if(gps.canGetLocation()){
       	
       
       	 Calendar c = Calendar.getInstance(); 
       		 start=c.getTime();    	 
            
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
             
            lat.add(latitude);
				lng.add(longitude);
		
				Log.d("check", ""+latitude);
				Log.d("check", ""+longitude);
				
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title("yup :)");
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
				googleMap.addMarker(marker);
            
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude,  longitude)).zoom(19).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
   
        }else{
           
            gps.showSettingsAlert();
        }
        
        callAsynchronousTask();
	}

	
	private void callAsynchronousTask() {
		  timer = new Timer();
		    doAsynchronousTask = new TimerTask() {       


				@Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() {       
		                    try {           

	               	         if(gps.canGetLocation()){
		               	             
	               	        	 int count= lat.size();
		               	        	 
		               	        	 double prev_lat= lat.get(count-1);
		               	        	double prev_lng= lng.get(count-1);
		               	        	
		               	        	LatLng sourcePosition= new LatLng(prev_lat,prev_lng);
		               	        	 
		               	             double latitude1= gps.getLatitude();
		               	             double longitude1 = gps.getLongitude();
		               	              
		               	          LatLng destPosition= new LatLng(latitude1,longitude1);
		               	             
		               	             lat.add(latitude1);
		               				lng.add(longitude1);
		               					
		               				Location locationA = new Location("point A");

		               				locationA.setLatitude(prev_lat);
		               				locationA.setLongitude(prev_lng);

		               				Location locationB = new Location("point B");

		               				locationB.setLatitude(latitude1);
		               				locationB.setLongitude(longitude1);

		               				double distance = locationA.distanceTo(locationB);
		          				     dis.add(distance);
		               				 findDirections(prev_lat,prev_lng,
		               						latitude1,longitude1, GMapV2Direction.MODE_DRIVING );
		               			    
		               	         }else{
		               	            
		               	         }
		                          
		                        
		                    } catch (Exception e) {
		                        // TODO Auto-generated catch block
		                    }
		                }
		            });
		        }
		    };
		    timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
		
	}


	@SuppressLint("NewApi")
	private void initilizeMap() {
		
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), 
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
		
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setUpActionBar() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
		}
	}
	
	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints)
	{
		 Polyline newPolyline;
	     
		    PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

		    for(int i = 0 ; i < directionPoints.size() ; i++) 
		    {          
		        rectLine.add(directionPoints.get(i));
		    }
		    newPolyline = googleMap.addPolyline(rectLine);
	}
}
