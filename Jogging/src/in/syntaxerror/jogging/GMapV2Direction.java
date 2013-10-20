package in.syntaxerror.jogging;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.os.AsyncTask;

public class GMapV2Direction {
	
	public GMapV2Direction() 
	{
		
	    }

	public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList<LatLng>> 
	
	
	{
		
		
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

	private Document getDocument(LatLng start, LatLng end,
			String mode) {
		// TODO Auto-generated method stub
		 String url = "http://maps.googleapis.com/maps/api/directions/xml?" 
		            + "origin=" + start.latitude + "," + start.longitude  
		            + "&destination=" + end.latitude + "," + end.longitude 
		            + "&sensor=false&units=metric&mode="+ mode;

		    try {
		        HttpClient httpClient = new DefaultHttpClient();
		        HttpContext localContext = new BasicHttpContext();
		        HttpPost httpPost = new HttpPost(url);
		        HttpResponse response = httpClient.execute(httpPost, localContext);
		        InputStream in = response.getEntity().getContent();
		        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        Document doc = builder.parse(in);
		        return doc;
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return null;
	}
	
	
}


