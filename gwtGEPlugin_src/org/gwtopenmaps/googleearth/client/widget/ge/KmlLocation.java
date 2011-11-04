/*
 * Copyright 2009 CSUSM
 *
 * Author: Curtis Jensen
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlLocation extends KmlObject {

	public KmlLocation(JavaScriptObject impl) {
		super(impl);
	}
	
	public void setLatLngAlt(double lat, double lng, double alt) {
		setLatLngAltImpl(getImpl(), lat, lng, alt);
	}
	
	public double getLatitude() {
		return getLatitudeImpl(getImpl());
	}
	
	public double getLongitude() {
		return getLongitudeImpl(getImpl());
	}
	
	public double getAltitude() {
		return getAltitudeImpl(getImpl());
	}
	
	private native void setLatLngAltImpl(JavaScriptObject impl, double lat, double lng, double alt) /*-{
		impl.setLatLngAlt(lat, lng, alt);
	}-*/;
	
	private native double getLatitudeImpl(JavaScriptObject impl) /*-{
		return impl.getLatitude();
	}-*/;

	private native double getLongitudeImpl(JavaScriptObject impl) /*-{
		return impl.getLongitude();
	}-*/;

	private native double getAltitudeImpl(JavaScriptObject impl) /*-{
		return impl.getAltitude();
	}-*/;
}
