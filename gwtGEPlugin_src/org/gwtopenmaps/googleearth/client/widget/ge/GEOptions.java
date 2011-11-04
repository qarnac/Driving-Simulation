/*
 * Author: Curtis Jensen
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class GEOptions extends GEJavaScriptObject {

	public GEOptions(JavaScriptObject impl) {
		super(impl);
	}

	public double getFlyToSpeed() {
		return getFlyToSpeedImpl(getImpl());
	}
	
	public void setFlyToSpeed(double flyToSpeed) {
		setFlyToSpeedImpl(getImpl(), flyToSpeed);
	}
	
	public void setMouseNavigationEnabled(boolean enabled) {
		if (enabled)
			setMouseNavigationEnabledTrueImpl(getImpl());
		else
			setMouseNavigationEnabledFalseImpl(getImpl());
	}
		
	public boolean getMouseNavigationEnabled() {
		return getMouseNavigationEnabledImpl(getImpl());
	}
	
	private native double getFlyToSpeedImpl(JavaScriptObject impl) /*-{
		return impl.getFlyToSpeed();	
	}-*/;
	
	private native void setFlyToSpeedImpl(JavaScriptObject impl, double flyToSpeed) /*-{
		impl.setFlyToSpeed(flyToSpeed);	
	}-*/;
	
	private native void setMouseNavigationEnabledTrueImpl(JavaScriptObject impl) /*-{
		return impl.setMouseNavigationEnabled(true);	
	}-*/;
	
	private native void setMouseNavigationEnabledFalseImpl(JavaScriptObject impl) /*-{
		return impl.setMouseNavigationEnabled(false);	
	}-*/;
	
	private native boolean getMouseNavigationEnabledImpl(JavaScriptObject impl) /*-{
		return impl.getMouseNavigationEnabled();	
	}-*/;

}
