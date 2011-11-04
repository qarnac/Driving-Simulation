/*
 * Author: Curtis Jensen
 */
package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlOrientation extends KmlObject {

	public KmlOrientation(JavaScriptObject impl) {
		super(impl);
	}
	
	public void set(double heading, double tilt, double roll) {
		setImpl(this.getImpl(), heading, tilt, roll);
	}
	
	public void setHeading(double heading) {
		setHeadingImpl(this.getImpl(), heading);
	}
	
	public double getHeading() {
		return getHeadingImpl(this.getImpl());
	}
	
	public void setTilt(double tilt) {
		setTiltImpl(this.getImpl(), tilt);
	}
	
	public double getTilt() {
		return getTiltImpl(this.getImpl());
	}
	
	public void setRoll(double roll) {
		setRollImpl(this.getImpl(), roll);
	}
	
	public double getRoll() {
		return getRollImpl(this.getImpl());
	}
	
	private native void setImpl(JavaScriptObject impl, double heading, double tilt, double roll) /*-{
		impl.set(heading, tilt, roll);
	}-*/;
	
	private native void setHeadingImpl(JavaScriptObject impl, double heading) /*-{
		impl.setHeading(heading);
	}-*/;
	
	private native double getHeadingImpl(JavaScriptObject impl) /*-{
		impl.getHeading();
	}-*/;
	
	private native void setTiltImpl(JavaScriptObject impl, double tilt) /*-{
		impl.setTilt(tilt);
	}-*/;
	
	private native double getTiltImpl(JavaScriptObject impl) /*-{
		impl.getTilt();
	}-*/;
	
	private native void setRollImpl(JavaScriptObject impl, double roll) /*-{
		impl.setRoll(roll);
	}-*/;
	
	private native double getRollImpl(JavaScriptObject impl) /*-{
		impl.getRoll();
	}-*/;
}
