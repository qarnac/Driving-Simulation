/*
 * Author: Curtis Jensen
 * 
 */
package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlObjectList extends GEJavaScriptObject {

	public KmlObjectList(JavaScriptObject impl) {
		super(impl);
	}
	
	public KmlObject item(int index) {
		return new KmlObject(itemImpl(this.getImpl(), index));
	}
	
	public int getLength() {
		return getLengthImpl(this.getImpl());
	}
	
	private native JavaScriptObject itemImpl(JavaScriptObject impl, int index) /*-{
		return impl.item(index);		
	}-*/;
	
	private native int getLengthImpl(JavaScriptObject impl) /*-{
		return impl.getLength();		
	}-*/;
}
