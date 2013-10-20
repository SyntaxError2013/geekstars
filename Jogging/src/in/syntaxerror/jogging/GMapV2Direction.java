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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

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
	
	private int getNodeIndex(NodeList nl, String nodename) {
	    for(int i = 0 ; i < nl.getLength() ; i++) {
	        if(nl.item(i).getNodeName().equals(nodename))
	            return i;
	    }
	    return -1;
	}
	
	
	
	public ArrayList<LatLng> getDirection (Document doc) {
	    NodeList nl1, nl2, nl3;
	    ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	    nl1 = doc.getElementsByTagName("step");
	    if (nl1.getLength() > 0) {
	        for (int i = 0; i < nl1.getLength(); i++) {
	            Node node1 = nl1.item(i);
	            nl2 = node1.getChildNodes();

	            Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
	            nl3 = locationNode.getChildNodes();
	            Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            double lat = Double.parseDouble(latNode.getTextContent());
	            Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            double lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));

	            locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "points"));
	            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
	            for(int j = 0 ; j < arr.size() ; j++) {
	                listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
	            }

	            locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            lat = Double.parseDouble(latNode.getTextContent());
	            lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	        }
	    }

	    return listGeopoints;
	}

	private ArrayList<LatLng> decodePoly(String encoded) {
		// TODO Auto-generated method stub
		 ArrayList<LatLng> poly = new ArrayList<LatLng>();
		    int index = 0, len = encoded.length();
		    int lat = 0, lng = 0;
		    while (index < len) {
		        int b, shift = 0, result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lat += dlat;
		        shift = 0;
		        result = 0;
		        do {
		            b = encoded.charAt(index++) - 63;
		            result |= (b & 0x1f) << shift;
		            shift += 5;
		        } while (b >= 0x20);
		        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		        lng += dlng;

		        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
		        poly.add(position);
		    }
		    return poly;
	}
	
	
	
}


