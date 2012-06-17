package de.perprogramming.gpstracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import android.location.Location;

public class TrackRepository {
	
	protected String serverUrl;
	protected double totalDistance = 0.0;
	protected double averageSpeed = 0.0;
	protected List<Track> tracks;
	protected Track currentTrack;
	
	public TrackRepository() {
		this.tracks = new ArrayList<Track>();
	}
	
	public void startTrack() {
		this.currentTrack = new Track(new Date());
		this.tracks.add(currentTrack);
	}
	
	public void finishTrack() {
		this.currentTrack.setEndTime(new Date());
		this.currentTrack = null;
	}
	
	public String toKml() {
		if (this.currentTrack != null) {
			this.finishTrack();
		}
		
		String kml = "";
		kml = kml.concat("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		kml = kml.concat("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		kml = kml.concat("    <Document>\n");
		kml = kml.concat("        <name>Tracks submitted at ").concat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date())).concat("</name>\n");
		Iterator<Track> iterator = this.tracks.iterator();
		while (iterator.hasNext()) {
			kml = kml.concat(iterator.next().toKml());
		}
		kml = kml.concat("    </Document>\n");
		kml = kml.concat("</kml>\n");
		
		return kml;
	}
	
	public void clearTracks() {
		this.tracks.clear();
	}
	
	public void addToCurrentTrack(Location location) {
		if (this.currentTrack != null) {
			this.currentTrack.add(location);
		}
	}
	
	public double getTotalDistance() {
		double totalDistance = 0.0; 
		Iterator<Track> iterator = this.tracks.iterator();
		while (iterator.hasNext()) {
			totalDistance += iterator.next().getTotalDistance();
		}
		return totalDistance;
	}
	
	public double getAverageSpeed() {
		double averageSpeed = 0.0; 
		Iterator<Track> iterator = this.tracks.iterator();
		while (iterator.hasNext()) {
			averageSpeed += iterator.next().getAverageSpeed();
		}
		averageSpeed = averageSpeed / this.tracks.size();
		return averageSpeed;
	}

}
