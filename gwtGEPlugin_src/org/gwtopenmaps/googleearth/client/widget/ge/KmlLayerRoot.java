/*
 * Author: Curtis Jensen
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlLayerRoot extends GEJavaScriptObject {

	public KmlLayerRoot(JavaScriptObject impl) {
		super(impl);
	}

	public void enableLayerById(String id, boolean visibility) {
		enableLayerByIdImpl(getImpl(), id, visibility);
	}
	
	private native void enableLayerByIdImpl(JavaScriptObject impl, String id, boolean visibility) /*-{
		return impl.enableLayerById(id, visibility);	
	}-*/;
}
