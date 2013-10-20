package in.syntaxerror.jogging;

import android.app.Dialog;

public class GMapV2Direction {

	
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";

	public static final String USER_CURRENT_LAT = "user_current_lat";
	public static final String USER_CURRENT_LONG = "user_current_long";
	public static final String DESTINATION_LAT = "destination_lat";
	public static final String DESTINATION_LONG = "destination_long";
	public static final String DIRECTIONS_MODE = "directions_mode";
	private MapActivity activity;
	private String url;
	private Exception exception;

	private Dialog progressDialog;
	
	
	public GMapV2Direction() 
	{
		
	    }
	
	public GetDirectionsAsyncTask(MapActivity activity ) 
	{
	    super();
	    this.activity = activity;
	}
	
	
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(ArrayList<LatLng> result) {

	    if (exception == null) {
	        activity.handleGetDirectionsResult(result);
	    } else {
	        processException();
	    }
	}



	@Override
	protected ArrayList<LatLng> doInBackground(Map<String, String>... params) {
	
	}

	private void processException() {
	    
	}

}
