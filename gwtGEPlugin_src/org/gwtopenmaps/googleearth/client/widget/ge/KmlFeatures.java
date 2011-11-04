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
 */
package org.gwtopenmaps.googleearth.client.widget.ge;

import com.google.gwt.core.client.JavaScriptObject;

public class KmlFeatures extends KmlObject {
	public KmlFeatures(JavaScriptObject impl) {
		super(impl);
	}
	
	public void appendChild(KmlFeature feature) {
		appendChildImpl(getImpl(), feature.getImpl());
	}

	private native void appendChildImpl(JavaScriptObject impl, JavaScriptObject featureImpl) /*-{
		impl.appendChild(featureImpl);		
	}-*/;
	
	public void removeChild(KmlFeature feature) {
		removeChildImpl(getImpl(), feature.getImpl());
	}

	private native void removeChildImpl(JavaScriptObject impl, JavaScriptObject featureImpl) /*-{
		impl.removeChild(featureImpl);		
	}-*/;
}
