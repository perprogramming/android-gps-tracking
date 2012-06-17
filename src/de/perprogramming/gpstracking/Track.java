package de.perprogramming.gpstracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import android.location.Location;

public class Track {
	
	protected List<Location> locations;
	protected Date startTime;
	protected Date endTime;
	protected double totalDistance = 0.0;
	
	public Track(Date startTime) {
		this.startTime = startTime;
		this.locations = new ArrayList<Location>();
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void add(Location location) {
		Location lastLocation = this.locations.get(this.locations.size() - 1);
		this.totalDistance += lastLocation.distanceTo(location);
		this.locations.add(location);
	}
	
	public double getTotalDistance() {
		return this.totalDistance;
	}
	
	public double getAverageSpeed() {
		Date start = this.startTime;
		Date end = (this.endTime != null) ? this.endTime : new Date();
		double seconds = (end.getTime() - start.getTime()) / 1000; 
		return this.totalDistance / seconds;
	}
	
	public String toKml() {
		String kml = "";
		kml = kml.concat("        <Placemark>\n");
		kml = kml.concat("            <name>Track starting at ").concat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(this.startTime)).concat("</name>\n");
		kml = kml.concat("            <LineString>\n");
		kml = kml.concat("                <coordinates>\n");
		Iterator<Location> iterator = this.locations.iterator();
		while (iterator.hasNext()) {
			Location location = iterator.next();
			kml = kml.concat("                    ").concat(String.valueOf(location.getLongitude())).concat(",").concat(String.valueOf(location.getLatitude())).concat(",").concat(String.valueOf(location.getAltitude())).concat("\n");
		}
		kml = kml.concat("                </coordinates>\n");
		kml = kml.concat("            </LineString>\n");
		kml = kml.concat("        </Placemark>\n");
		return kml;
	}
	
}
