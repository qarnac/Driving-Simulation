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
 * Modified: Curtis Jensen - June '09 - Added getOptions method
 *                                    - SPEED_TELEPORT constant
 *                                    - Added fetchKml
 *                                    - Added LAYER_ROADS and LAYER_BORDERS
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import org.gwtopenmaps.googleearth.client.widget.GoogleEarthWidget;

import com.google.gwt.core.client.JavaScriptObject;

public class GEPlugin {
	public static final int UNITS_PIXELS = KmlXY.UNITS_PIXELS;
	public static final int UNITS_FRACTION = KmlXY.UNITS_FRACTION;
	public static final int UNITS_INSET_PIXELS = KmlXY.UNITS_INSET_PIXELS;
	
	public static final int ALTITUDE_CLAMP_TO_GROUND = 0;
	public static final int ALTITUDE_RELATIVE_TO_GROUND	= 1;
	public static final int ALTITUDE_ABSOLUTE = 2;
	public static final double SPEED_TELEPORT = 5.0;
	public static final String LAYER_ROADS = "4ddec456-c7b3-11dc-aaa5-dd553d8c9902";
	public static final String LAYER_BORDERS = "53004770-c7b3-11dc-92c2-dd553d8c9902";
	
	public interface CompletionCallback	{
		void completed(KmlContainer kml);
	}
	public class CompletionCallbackNative {
		CompletionCallback callback;
		
		public CompletionCallbackNative(CompletionCallback callback) {
			this.callback = callback;
		}
		
		public void completed(JavaScriptObject kml) {
			callback.completed(new KmlContainer(kml));
		}
	}
	/** 
	 * Never call this yourself. Use {@link GoogleEarthWidget}.getGEPlugin()
	 * 
	 * @param ge : the real javascript object
	 */
	public GEPlugin(JavaScriptObject ge) {
		this.ge = ge;
	}
	
	public GEView getView() {
		return new GEView(GEPluginNative.getView(ge));
	}
	
	public GEOptions getOptions() {
		return new GEOptions(GEPluginNative.getOptions(ge));
	}
	
	public KmlDocument getDocument() {
		return new KmlDocument(GEPluginNative.getDocument(ge));
	}
	
	public KmlPlacemark createPlacemark(String s) {
		return new KmlPlacemark(GEPluginNative.createPlacemark(ge, s));
	}
	
	public KmlPoint createPoint(String s) {
		return new KmlPoint(GEPluginNative.createPoint(ge, s));
	}
	
	public KmlIcon createIcon(String s) {
		return new KmlIcon(GEPluginNative.createIcon(ge, s));
	}
	
	public KmlLookAt createLookAt(String s) {
		return new KmlLookAt(GEPluginNative.createLookAt(ge, s));
	}
	
	public KmlScreenOverlay createScreenOverlay(String s) {
		return new KmlScreenOverlay(GEPluginNative.createScreenOverlay(ge, s));
	}
	
	public KmlFeature parseKml(String s) {
		return new KmlFeature(GEPluginNative.parseKml(ge, s));
	}
	
	/** Does not work ! */
	public String getEarthVersion() {
		return GEPluginNative.getEarthVersion(ge);
	}
	/** Does not work ! */
	public String getPluginVersion() {
		return GEPluginNative.getPluginVersion(ge);
	}
	
	public void fetchKml(String url, CompletionCallback callback) {
//		System.out.println("FETCHING");
		GEPluginNative.fetchKml(ge, url, new CompletionCallbackNative(callback));		
	}
	
	public KmlFeatures getFeatures() {
		return new KmlFeatures(GEPluginNative.getFeatures(ge));
	}

	public KmlLayerRoot getLayerRoot() {
		return new KmlLayerRoot(GEPluginNative.getLayerRoot(ge));
	}
	
	public String getLayerBuildings() {
		String tmp = getLayerBuildingsImpl(ge);
		return tmp;
	}
	private native String getLayerBuildingsImpl(JavaScriptObject ge) /*-{
		return ge.LAYER_BUILDINGS;
	}-*/;
	
	public JavaScriptObject getImpl() {
		return ge;
	}
	
	private JavaScriptObject ge;
}
