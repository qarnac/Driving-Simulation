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
 * Modified: Curtis Jensen Jun '090 - Added KmlObject Constructor
 * 
 */
package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlFeature extends KmlObject {

	public KmlFeature(JavaScriptObject impl) {
		super(impl);
	}
	
	public KmlFeature(KmlObject obj) {
		this(obj.getImpl());
	}
	
	public void setStyleUrl(String s) {
		setStyleUrlImpl(getImpl(), s);
	}
	public native void setStyleUrlImpl(JavaScriptObject impl, String s) /*-{
		impl.setStyleUrl(s);
	}-*/;
	
	public void setName(String s) {
		setNameImpl(getImpl(), s);
	}
	public native void setNameImpl(JavaScriptObject impl, String s) /*-{
	impl.setName(s);
}-*/;
}
