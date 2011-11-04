/*
 * Copyright 2009 CSUSM
 *
 * Author: Curtis Jensen
 */

package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class GEFeatureContainer extends KmlObject {
	public GEFeatureContainer(JavaScriptObject impl) {
		super(impl);
	}
	
	public KmlObject getFirstChild() {
		return new KmlObject(getFirstChildImpl(this.getImpl()));
	}

	public KmlObjectList getChildNodes() {
		return new KmlObjectList(getChildNodesImpl(this.getImpl()));
	}
	
	public KmlObject getLastChild() {
		return new KmlObject(getLastChildImpl(this.getImpl()));
	}

	public boolean hasChildNodes() {
		return (hasChildNodesImpl(this.getImpl()) != 0);
	}
	
	private native int hasChildNodesImpl(JavaScriptObject impl) /*-{
		return impl.hasChildNodes();		
	}-*/;

	private native JavaScriptObject getFirstChildImpl(JavaScriptObject impl) /*-{
		return impl.getFirstChild();		
	}-*/;

	private native JavaScriptObject getLastChildImpl(JavaScriptObject impl) /*-{
		return impl.getLastChild();		
	}-*/;

	private native JavaScriptObject getChildNodesImpl(JavaScriptObject impl) /*-{
		return impl.getChildNodes();		
	}-*/;
}
