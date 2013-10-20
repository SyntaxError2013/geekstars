package in.syntaxerror.jogging;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends Activity {

	
	private double weight;
	private GoogleMap googleMap;


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
		
        //LocateDirections(0,0,0,0,GMapV2Direction.MODE_DRIVING);
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
}
