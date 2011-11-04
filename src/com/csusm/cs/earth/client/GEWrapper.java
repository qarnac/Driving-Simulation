/*
 * Copyright 2008 Google Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * Author: Samuel Charron
 * Modified: Curtis Jensen
 * 
 */

package com.csusm.cs.earth.client;

import org.gwtopenmaps.googleearth.client.widget.GEPluginReadyListener;
import org.gwtopenmaps.googleearth.client.widget.GoogleEarthWidget;
import org.gwtopenmaps.googleearth.client.widget.ge.GEPlugin;
import org.gwtopenmaps.googleearth.client.widget.ge.KmlContainer;
import org.gwtopenmaps.googleearth.client.widget.ge.KmlFeature;
import org.gwtopenmaps.googleearth.client.widget.ge.KmlLookAt;
import org.gwtopenmaps.googleearth.client.widget.ge.KmlModel;

import com.csusm.cs.earth.client.Utils.GeoLocation;
import com.csusm.cs.earth.client.Utils.Intersection;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class GEWrapper extends Composite implements GEPluginReadyListener, GEPlugin.CompletionCallback {
	
	public GoogleEarthWidget gew;
	private Intersection curIntersection = null;
	private boolean enable;
	private int fetchSequence = 0;
	private KmlFeature lightPlacemark;
	private GeoLocation startLoc;
	
	public GEWrapper(int width, int height) {
		enable = false;
		AbsolutePanel base = new AbsolutePanel();
		base.setSize(String.valueOf(width), String.valueOf(height));
		initWidget(base);
		
		gew = new GoogleEarthWidget(width-10, height-10);
		gew.addPluginReadyListener(this);
		base.add(gew, 5, 5);
		
		//TODO hardcode fly to Washington DC
//		int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
//		curIntersection = Utils.INTERSECTIONS[interIdx];
	}
	
	public void setCurrentIntersection(Intersection inter){
		curIntersection = inter;
	}
	
	public void pluginReady(JavaScriptObject g) {
		// load buildings
		String buildingsLayer = gew.getGEPlugin().getLayerBuildings();
		gew.getGEPlugin().getLayerRoot().enableLayerById(buildingsLayer, true);
		// setup GE for driving simulation
		gew.getGEPlugin().getOptions().setFlyToSpeed(GEPlugin.SPEED_TELEPORT);
		gew.getGEPlugin().getOptions().setMouseNavigationEnabled(false);
		// load the traffic light board
		setStreetWithLight();
//		setStreet();
	}
	
	public void init() {
		gew.init();
	}
	
	/**
	 * Called when the kmz file is loaded
	 */
	public void completed(KmlContainer kml) 
	{
		// load street 
		if ( fetchSequence == -2) {
			
			int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
			curIntersection = Utils.INTERSECTIONS[interIdx];
			
			KmlFeature boardPlacemark = new KmlFeature(kml.getFeatures().getChildNodes().item(1));
			KmlModel model = boardPlacemark.getGeometry();
			
			model.getLocation().setLatLngAlt(curIntersection.loc.lat, curIntersection.loc.lon, curIntersection.loc.alt);
			model.getOrientation().setHeading(curIntersection.drivingBearing);
	
			model.setAltitudeMode(GEPlugin.ALTITUDE_ABSOLUTE);
			gew.getGEPlugin().getFeatures().appendChild(boardPlacemark);
			enable= true;
			
			moveDistanceFromDestination(-10);
//			moveDistanceFromDestination(100);
//			setStreet();
			
		} else if( fetchSequence == -1) {
			int interIdx = Utils.INTERSECTION_MAP.get("Disneyland");
			curIntersection = Utils.INTERSECTIONS[interIdx];
			
			KmlFeature boardPlacemark = new KmlFeature(kml.getFeatures().getChildNodes().item(1));
			KmlModel model = boardPlacemark.getGeometry();
			
			model.getLocation().setLatLngAlt(curIntersection.loc.lat, curIntersection.loc.lon, curIntersection.loc.alt+2.0);
			model.getOrientation().setHeading(curIntersection.drivingBearing);
	
			model.setAltitudeMode(GEPlugin.ALTITUDE_ABSOLUTE);
			gew.getGEPlugin().getFeatures().appendChild(boardPlacemark);
			enable= true;
			
			// move camera 100m from intersection
//			moveDistanceFromDestination(100);
		} else {
			if (lightPlacemark!=null) {
				gew.getGEPlugin().getFeatures().removeChild(lightPlacemark);
			}
			lightPlacemark = new KmlFeature(kml.getFeatures().getChildNodes().item(1));
			KmlModel model = lightPlacemark.getGeometry();
			
			model.getLocation().setLatLngAlt(curIntersection.loc.lat, curIntersection.loc.lon, curIntersection.loc.alt);
			model.getOrientation().setHeading(curIntersection.drivingBearing);
	
			model.setAltitudeMode(GEPlugin.ALTITUDE_ABSOLUTE);
			gew.getGEPlugin().getFeatures().appendChild(lightPlacemark);
		}
	}
	
	/**
	 * Controls the camera position in the Google Earth plugin.
	 * 
	 * @param newLoc - The geodetic location to position the camera.
	 * @param heading - The heading of the camera.
	 * @param gewrapper - The handle to the GE wrapper.
	 */
	public void moveToLoc(GeoLocation newLoc, double heading) 
	{
		final KmlLookAt la = gew.getGEPlugin().createLookAt("");
		la.set(newLoc.lat, newLoc.lon,
               newLoc.alt+0.5,       // altitude
//               GEPlugin.ALTITUDE_RELATIVE_TO_GROUND,
               GEPlugin.ALTITUDE_ABSOLUTE,
               heading, // heading
               90,      // tilt
               2);     // range (inverse of zoom)
		
		gew.getGEPlugin().getView().setAbstractView(la);
	}
	
	public void setStreetWithLight() {
		fetchSequence = -2;
		gew.getGEPlugin().fetchKml("http://www.csusmcsu.com/sketchup/street_light.kmz", this);
	}
	
	public void setStreet() {
		fetchSequence = -1;
		gew.getGEPlugin().fetchKml("http://www.csusmcsu.com/sketchup/street_light.kmz", this);
	}
	
	public void setGreenLight() {
		fetchSequence = 1;
		gew.getGEPlugin().fetchKml("http://www.csusmcsu.com/sketchup/street_light_green.kmz", this);
	}
	public boolean isGreenLight() {
		return (fetchSequence==1);
	}
	public void setYellowLight() {
		fetchSequence = 2;
		gew.getGEPlugin().fetchKml("http://www.csusmcsu.com/sketchup/street_light_yellow.kmz", this);
	}
	public boolean isYellowLight() {
		return (fetchSequence==2);
	}
	public void setRedLight() {
		fetchSequence = 3;
		gew.getGEPlugin().fetchKml("http://www.csusmcsu.com/sketchup/street_light_red.kmz", this);
	}
	public boolean isRedLight() {
		return (fetchSequence==3);
	}
	
	public void moveDistanceFromDestination(double distance) {
		this.startLoc = Utils.rangeBearingCalc(curIntersection.loc, curIntersection.drivingBearing, -distance/1000.0);
		this.moveToLoc(this.startLoc, curIntersection.drivingBearing);
	}
	public boolean isEnable() {
		return this.enable;
	}
	
	
}
