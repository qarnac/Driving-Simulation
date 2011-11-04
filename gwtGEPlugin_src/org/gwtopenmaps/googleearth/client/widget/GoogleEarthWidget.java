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
 * Modified: Curtis Jensen - Removed this as a ready listener (other listeners were being called first)
 */
package org.gwtopenmaps.googleearth.client.widget;

import java.util.ArrayList;

import org.gwtopenmaps.googleearth.client.widget.ge.GEPlugin;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class GoogleEarthWidget extends Widget //implements GEPluginReadyListener 
{
	static int id = 0;
	private JavaScriptObject ge;
	private GEPlugin gePlugin;
	private ArrayList<GEPluginReadyListener> pluginReadyListeners = new ArrayList<GEPluginReadyListener>();
	
	public GoogleEarthWidget(int width, int height) {
		HTML html = new HTML(
				"<div class='map3dcontainer' id='map3dcontainer" + id + "'>" + 
				"<div class='map3d' id='map3d" + id + "'style='height: " + height + "px; width: "+ width + "px;'></div></div>");
		setElement(html.getElement());
	}
	
	public void init() {
//		addPluginReadyListener(this);
		jsInitGE(id);
	}
	
//	public void pluginReady(JavaScriptObject ge) {
//		System.out.println("READY");
//		this.ge = ge;
//		gePlugin = new GEPlugin(ge);
//		id++;
//	}
	
	public void addPluginReadyListener(GEPluginReadyListener listener) {
		pluginReadyListeners.add(listener);
	}
	
	public GEPlugin getGEPlugin() {
		return gePlugin;
	}
	
	public void ready(JavaScriptObject ge) {
		this.setGe(ge);
		gePlugin = new GEPlugin(ge);
		id++;

		for (int i = 0; i < pluginReadyListeners.size(); ++i) {
			((GEPluginReadyListener)pluginReadyListeners.get(i)).pluginReady(ge);
		}
	}
	
	private native void jsInitGE(int id) /*-{
		var ge;
		var instance = this;
		function initCB(obj) {
  			ge = obj;
  			ge.getWindow().setVisibility(true);
		  	instance.@org.gwtopenmaps.googleearth.client.widget.GoogleEarthWidget::ready(Lcom/google/gwt/core/client/JavaScriptObject;)(ge);
		}
		function failureCB(object) {
  			alert('GE load failed: ' + object);
		}
  		$wnd.google.earth.createInstance($doc.getElementById("map3d" + id), initCB, failureCB);
	}-*/;

	public void setGe(JavaScriptObject ge) {
		this.ge = ge;
	}

	public JavaScriptObject getGe() {
		return ge;
	}
	
}
