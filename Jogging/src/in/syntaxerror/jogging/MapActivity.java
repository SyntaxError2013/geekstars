package in.syntaxerror.jogging;

import in.syntaxerror.jogging.GMapV2Direction.GetDirectionsAsyncTask;

import java.util.ArrayList;
import java.util.Calendar;
import android.speech.tts.TextToSpeech;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MapActivity extends Activity implements TextToSpeech.OnInitListener {

	protected static final int REQUEST_CODE = 0;
	 double weight;
	private GoogleMap googleMap;
	 TextToSpeech tts;
	GMapV2Direction md;
	LocationManager locationManager;
	LocationListener locationListener;
	 GPSTracker gps;
	 double	d1,speed, calories;
	 GMapV2Direction md1 = new GMapV2Direction();
	 Date start,end;
	 ArrayList<Double> lat = new ArrayList<Double>();
 ArrayList<Double>  lng = new ArrayList<Double>();
	ArrayList<Double> dis = new ArrayList<Double>();
	double latitude,longitude;
	 Timer timer;
	 TimerTask doAsynchronousTask;
     final Handler handler = new Handler();

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		
		setUpActionBar();
		
		Bundle extras = getIntent().getExtras();
		String B = extras.getString("weight");
		weight = Double.parseDouble(B);
		
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
		
//        findDirections(29.869506,77.894973,
//        		29.863651,77.895522, GMapV2Direction.MODE_DRIVING );
        
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
	
	
	
	  @Override
	    protected void onPause() {
	    	// TODO Auto-generated method stub
	    	super.onPause();
	    	
	    	handler.removeCallbacks(doAsynchronousTask);
	    }
	    
	    @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	
	    	initilizeMap();
			handler.post(doAsynchronousTask);
	    }
	    

	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
	    map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
	    map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
	    map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
	    map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
	    map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

	    
	    GetDirectionsAsyncTask asyncTask = md1.new GetDirectionsAsyncTask(this);
	    asyncTask.execute(map); 
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
		          				   findDirections(lat.get(0),lng.get(0),
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	   

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.stop:
        	onPause();
        	tts = new TextToSpeech(this, this);
        	speakOut();
       
       Calendar c = Calendar.getInstance(); 
   		 end=c.getTime(); 	 
   		long millis = end.getTime() - start.getTime();
   		double Hours = millis/(1000 * 60 * 60);
   	 double t1 = (millis % (1000*60*60));
       for(int i=0; i<dis.size(); i++)
       {
    	   d1=d1+dis.get(i);
       }
       
       speed=d1/t1;
       
       calories= Hours*(weight*((3.5+speed*0.2)/3.5));
        	
        	new AlertDialog.Builder(this)
			.setTitle("Are you done with tracking to exit?")
			.setMessage("your distance travelled(in meters), mileage(in meter/minute) and calories burnt are "+ d1+", "+speed+", "+calories)
			.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								finish();
								Intent i=new Intent(MapActivity.this,HomeActivity.class);
								startActivity(i);
								tts.stop();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
						tts.stop();
                  onResume();
					}
				}).show();
    
            return true;
            
            
        case R.id.place:
        	
            return true;
       
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	 public void onDestroy() {
	        // Don't forget to shutdown tts!
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
				if (status == TextToSpeech.SUCCESS) {
					 
		            int result = tts.setLanguage(Locale.US);
		 
		            if (result == TextToSpeech.LANG_MISSING_DATA
		                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
		                Log.e("TTS", "This Language is not supported");
		            } else {
		                
		                speakOut();
		            }
		 
		        } else {
		            Log.e("TTS", "Initilization Failed!");
		        }
		
	}
	
	private void speakOut() {
		// TODO Auto-generated method stub
		String a=Double.toString(d1);
		String b=Double.toString(speed);
		String c=Double.toString(calories);
		String s = "your distance travelled mileage and calories burnt are respectively "+a+" meters "+b+" meters per minute "+c+" calories";
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}
}
