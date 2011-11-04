package com.csusm.cs.earth.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TrafficSchool implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GEWrapper gewrapper = new GEWrapper(450, 250);
		DrivingSimulator simulator = new DrivingSimulator(gewrapper);
		MainGUI mainGUI = new MainGUI(gewrapper, simulator);
		RootPanel.get().add(mainGUI);
		gewrapper.init();
		mainGUI.drawChart();
	}
}
