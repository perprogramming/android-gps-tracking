package de.perprogramming.gpstracking;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GpsTrackingActivity extends Activity implements LocationListener {
	
	protected EditText totalDistanceValueField;
	protected EditText averageSpeedValueField;
	protected Button startButton;
	protected Button pauseButton;
	protected Button stopButton;
	
	protected LocationManager locationManager;
	protected TrackRepository trackRepository;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        this.totalDistanceValueField = (EditText) this.findViewById(R.id.totalDistanceValueField);
        this.averageSpeedValueField = (EditText) this.findViewById(R.id.averageSpeedValueField);
        this.startButton = (Button) this.findViewById(R.id.startButton);
        this.pauseButton = (Button) this.findViewById(R.id.pauseButton);
        this.stopButton = (Button) this.findViewById(R.id.stopButton);
        this.pauseButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        
        this.locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        this.trackRepository = new TrackRepository();
    }
    
    public void onStartButtonClicked(View v) {
    	this.startButton.setEnabled(false);
    	this.pauseButton.setEnabled(true);
    	this.stopButton.setEnabled(true);
    	
    	this.trackRepository.startTrack();
    	this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this); 
    }
    
    public void onPauseButtonClicked(View v) {
    	this.locationManager.removeUpdates(this);
    	this.trackRepository.finishTrack();
    	this.startButton.setEnabled(true);
    	this.pauseButton.setEnabled(false);
    }
    
    public void onStopButtonClicked(View v) throws Exception {
    	this.locationManager.removeUpdates(this);
    	String kml = this.trackRepository.toKml();
    	this.trackRepository.clearTracks();
    	
    	try {
	    	HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://perprogramming.de/pma-kml-repository/");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    nameValuePairs.add(new BasicNameValuePair("kml", kml));
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		    client.execute(post);
    	} catch (Exception e) {
    		throw e;
    	}
	    
    	this.startButton.setEnabled(true);
    	this.pauseButton.setEnabled(false);
    	this.stopButton.setEnabled(false);
    }
    
	public void onLocationChanged(Location location) {
		this.trackRepository.addToCurrentTrack(location);
		this.averageSpeedValueField.setText(String.valueOf(this.trackRepository.getAverageSpeed()).concat("m/s"));
    	this.totalDistanceValueField.setText(String.valueOf(this.trackRepository.getTotalDistance()).concat("m"));
	}
    
	public void onProviderDisabled(String provider) {}
	public void onProviderEnabled(String provider) {}
	public void onStatusChanged(String provider, int status, Bundle extras) {}

    
}