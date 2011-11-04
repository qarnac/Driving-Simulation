/*
 * Copyright 2009 CSUSM
 *
 * Author: Curtis Jensen
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlModel extends KmlObject {

	public KmlModel(JavaScriptObject impl) {
		super(impl);
	}
	
	public void setAltitudeMode(int mode) {
		setAltitudeModeImpl(getImpl(), mode);
	}
	
	public KmlLocation getLocation() {
		return new KmlLocation(getLocationImpl(this.getImpl()));
	}
	
	public void setLocation(KmlLocation loc) {
		setLocationImpl(this.getImpl(), loc.getImpl());
	}
	
	public KmlOrientation getOrientation() {
		return new KmlOrientation(getOrientationImpl(this.getImpl()));
	}
	
	public void setOrientation(KmlOrientation orientation) {
		setOrientationImpl(this.getImpl(), orientation.getImpl());
	}
	
	private native void setAltitudeModeImpl(JavaScriptObject impl, int mode) /*-{
		impl.setAltitudeMode(mode);
	}-*/;
	
	private native JavaScriptObject getLocationImpl(JavaScriptObject impl) /*-{
		return impl.getLocation();
	}-*/;
	
	private native void setLocationImpl(JavaScriptObject impl, JavaScriptObject loc) /*-{
		return impl.setLocation(loc);
	}-*/;
	
	private native JavaScriptObject getOrientationImpl(JavaScriptObject impl) /*-{
		return impl.getOrientation();
	}-*/;
	
	private native void setOrientationImpl(JavaScriptObject impl, JavaScriptObject orientation) /*-{
		return impl.setOrientation(orientation);
	}-*/;
}
