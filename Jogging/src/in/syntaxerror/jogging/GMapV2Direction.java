package in.syntaxerror.jogging;

import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;

import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.os.AsyncTask;

public class GMapV2Direction {

	public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList<LatLng>> {
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
	
		

	    Map<String, String> paramMap = params[0];
	    try{
	        LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)) , Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
	        LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
	        GMapV2Direction md = new GMapV2Direction();
	        
	        
	        Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
	        ArrayList<LatLng> directionPoints = md.getDirection(doc);
	        return directionPoints;

	    }
	    catch (Exception e) {
	        exception = e;
	        return null;
	    }
		
		
	}



	private void processException() {
	    
	}

	}
	private ArrayList<LatLng> getDirection(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	private Document getDocument(LatLng fromPosition, LatLng toPosition,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}


