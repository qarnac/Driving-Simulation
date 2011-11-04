package com.csusm.cs.earth.client;

import java.util.HashMap;

/*
 * This class contains a set of utility classes and methods.
 */
public class Utils 
{
	//default settings
	public static final double SPEED = 60/3.6; // 60KPH
	public static final int[] SPEED_LIMITS = new int[] {30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130};
	public static final int SPEED_IDX = 3; // default speed sign picture
	
	public static final double DISTANCE = 100; // 100M
	public static final int SELECTED_DISTANCE = 100;
	
	public static final int TIME = 5; // 5 Seconds
	public static final int SELECTED_TIME = 5;
	
	public static final long GREEN_LIGHT_DURATION = 5000; //ms
	public static final long YELLOW_LIGHT_DURATION = 1000; //ms
	
	// Chart type
	public static final int VelocityTime = 1;
	public static final int DistanceTime = 2;
	
	// driving simulation result
	public static final int BEFORE = 1;
	public static final int AFTER = 2;
	public static final int WHEN = 3;
	public static final int NORMAL_STOP = 4;
	
	// traffic light status
	public static final int GREEN_LIGHT = 1;
	public static final int YELLOW_LIGHT = 2;
	public static final int RED_LIGHT = 3;
	
	// challenge result
	public static final int CORRECT = 1;
	public static final int WRONG = 0;
	
	// Universal constants for GE
	public static final int    MS_PER_HOUR = 1000 * 60 * 60;
	public static final double NAU_MILES_PER_STD_MILE = 0.868976242;
	public static final double KM_PER_STD_MILE = 1.609344;
	public static final double FEET_PER_M = 3.2808399;
	public static final double FEET_PER_KM = FEET_PER_M * 1000.0;
	public static final double INTERSECTION_DIST_FT = 150.0;
	public static final double INTERSECTION_DIST_M = INTERSECTION_DIST_FT / FEET_PER_M;
	public static final double FEET_PER_STD_MILE = 5280.0;
	public static final double EARTH_MEAN_RADIUS_KM = 6371.0;
	public static final double EARTH_MEAN_RADIUS_MILE = EARTH_MEAN_RADIUS_KM / KM_PER_STD_MILE;
	public static final double EARTH_MEAN_RADIAUS_FEET = EARTH_MEAN_RADIUS_KM * FEET_PER_KM;

	public static final Intersection[] INTERSECTIONS = new Intersection[] {
		new Intersection(new GeoLocation(-117.169224, 32.715768, 4.0), 270.0),  // W. Broadway & Ketner
		new Intersection(new GeoLocation(-117.915415, 33.809338, 8.0), 270.0),  // Disneyland entrance
		new Intersection(new GeoLocation(-77.015013, 38.891297, 5.0), 109.5),   // looking at capital bld
		new Intersection(new GeoLocation(-117.165541, 33.127276, 5.0), 90.5),  // Twin Oaks and Craven
		new Intersection(new GeoLocation(151.21255, -33.847409, 5.0), 165),  // Sydney Bridge
		new Intersection(new GeoLocation(-122.4783, 37.812, 5.0), 165),  // Golden Bridge
		new Intersection(new GeoLocation(151.21255, -33.847409, 10.0), 165)  // 
		};  // Stadium
	
	public static final HashMap<String, Integer> INTERSECTION_MAP = new HashMap<String, Integer>();
	static
	{
		INTERSECTION_MAP.put("San Diego", 0);
		INTERSECTION_MAP.put("Disneyland", 1);
		INTERSECTION_MAP.put("Washington DC", 2);
		INTERSECTION_MAP.put("CSUSM", 3);
		INTERSECTION_MAP.put("Sydney", 4);
		INTERSECTION_MAP.put("GoldenBridge", 5);
		INTERSECTION_MAP.put("Stadium", 6);
	}


	/**
	 * Converts degrees to radians.
	 * 
	 * @param deg - The degrees to convert.
	 * @return - The equivalent radiants.
	 */
	public static double deg2Rad(double deg)
	{
		return deg * (Math.PI / 180.0);
	}
	
	/** 
	 * Converts radians to degrees.
	 * 
	 * @param rad - The radians to convert.
	 * @return - The equivalent degrees.
	 */
	public static double rad2Deg(double rad)
	{
		return rad * (180.0 / Math.PI);
	}
	
	/**
	 * Get the distance between geodedic locations (in standard miles).
	 * 
	 * @param loc1 - The first location.
	 * @param loc2 - The second location.
	 * @return - The distance in miles between the two points.
	 */
	public static double getDistMiles(GeoLocation loc1, GeoLocation loc2)
	{
		return getDist(loc1, loc2, EARTH_MEAN_RADIUS_MILE);
	}
	
	/**
	 * Get the distance between geodedic locations (in standard miles).
	 * 
	 * @param loc1 - The first location.
	 * @param loc2 - The second location.
	 * @return - The distance in km between the two points.
	 */
	public static double getDistKM(GeoLocation loc1, GeoLocation loc2)
	{
		return getDist(loc1, loc2, EARTH_MEAN_RADIUS_KM);
	}
	
	public static double getDist(GeoLocation loc1, GeoLocation loc2, double meanRadius)
	{
		// Vincenty formula from http://www.movable-type.co.uk/scripts/latlong.html
		
		double lon1Rad = deg2Rad(loc1.lon); 
		double lat1Rad = deg2Rad(loc1.lat);
		double lon2Rad = deg2Rad(loc2.lon); 
		double lat2Rad = deg2Rad(loc2.lat);
		
		double d = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) +
				             Math.cos(lat1Rad) * Math.cos(lat2Rad) *
				             Math.cos(lon2Rad - lon1Rad)) * meanRadius;
		
		return d;
	}
	
	/**
	 * Calculate a location based on a starting location, a bearing and a distance (in miles).
	 * 
	 * @param startLoc - The starting location.
	 * @param bearingDeg - The bearing.
	 * @param distMiles - The distance (in miles).
	 * @return - The determined location.
	 */
	public static GeoLocation rangeBearingCalc(GeoLocation startLoc, double bearingDeg, double distKM)
	{
		// from http://www.movable-type.co.uk/scripts/latlong.html
		
		double bearingRad = deg2Rad(bearingDeg);
		double lonRad = deg2Rad(startLoc.lon);
		double latRad = deg2Rad(startLoc.lat);

		double newLatRad = Math.asin(Math.sin(latRad) * 
				                  Math.cos(distKM/EARTH_MEAN_RADIUS_KM) + 
				                  Math.cos(latRad) *
				                  Math.sin(distKM/EARTH_MEAN_RADIUS_KM) *
				                  Math.cos(bearingRad));
	
		double atan2y = Math.sin(bearingRad) * 
		                Math.sin(distKM/EARTH_MEAN_RADIUS_KM) *
		                Math.cos(latRad);
		double atan2x = Math.cos(distKM/EARTH_MEAN_RADIUS_KM) - 
		                Math.sin(latRad) *
		                Math.sin(newLatRad);
		
		double newLonRad = lonRad + Math.atan2(atan2y, atan2x);
		
		double newLonDeg = rad2Deg(newLonRad);
		double newLatDeg = rad2Deg(newLatRad);
		return new GeoLocation(newLonDeg, newLatDeg, startLoc.alt);
	}
	
	/*
	 * A simple class to represent a geodetic location.
	 */
	public static class GeoLocation
	{
		public double lon = 0.0;
		public double lat = 0.0;
		public double alt = 0.0;
		
		public GeoLocation(double lon, double lat, double alt)
		{
			this.lon = lon;
			this.lat = lat;
			this.alt = alt;
		}
		
		public String toString()
		{
			return "lon: " + lon + " lat: " + lat;
		}
	}	

	/**
	 * A simple class to describe an intersection.
	 */
	public static class Intersection 
	{
		// the location of the start of the intersection
		public GeoLocation loc;
		
		// the heading of a car that would be driving through the intersection
		public double drivingBearing;
		
		/**
		 * Constructor
		 * 
		 * @param loc     - Location of the start of the intersection.
		 * @param bearing - The heading needed to drive through the intersection.
		 */
		public Intersection(GeoLocation loc, double bearing)
		{
			this.loc = loc;
			this.drivingBearing = bearing;
		}
	}
}
