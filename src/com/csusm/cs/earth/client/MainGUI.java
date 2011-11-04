package com.csusm.cs.earth.client;

import java.util.ArrayList;


import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainGUI extends Composite {
	private GEWrapper googleEarthPanel;
	private DrivingSimulator simulator;
	private DSTChart chart;
	private QuestionsPanel exercisePanel;
	private QuestionsPanel simulationPanel;

	public void drawChart() {
		simulationPanel.controller.drawCase();
	}
	
	public MainGUI(GEWrapper _gewrapper, DrivingSimulator _simulator) {
        super();
        simulator = _simulator;
		chart = new DSTChart(Utils.DistanceTime,435, 268, "Time", "Distance", "sec", " m", 0.0, 20.0, 0.0, 200.0, 20, 8);
        chart.getElement().setId("svtChart");
        simulator.addSimListener(chart);
        
        // base container
        AbsolutePanel baseAbsolutePanel = new AbsolutePanel();
        baseAbsolutePanel.setSize("900px", "600px");
        baseAbsolutePanel.getElement().setId("baseAbsolutePanel");
        baseAbsolutePanel.setStyleName("outline");
        initWidget(baseAbsolutePanel);
        
        HorizontalPanel baseHoriPanel = new HorizontalPanel();
        baseHoriPanel.setSize("900px", "600px"); 
        baseHoriPanel.getElement().setId("baseHoriPanel");
        baseHoriPanel.setStyleName("outline");
        
        // left
        VerticalPanel leftVertPanel = new VerticalPanel();
        leftVertPanel.setSize("450px", "600px");
        leftVertPanel.getElement().setId("leftVertPanel");
        leftVertPanel.setStyleName("outline");
        
        // first item in left
        //google earth size: 450px 250px
        googleEarthPanel = _gewrapper;
        googleEarthPanel.getElement().setId("googleEarthPanel");
        googleEarthPanel.setStyleName("outline");
        
        // last item in left : LOGO
        HTML logoHtml = new HTML("credits ... <br />" +
        		"<div id='logo'>" +
        		"<img src='images/logo/csusm.gif' weight='140px' height='50px' />" +
        		"<img src='images/logo/iquest.gif' weight='140px' height='50px' />" +
        		"<img src='images/logo/nsflogo.png' weight='140px' height='50px' />" +
        		"  TrafficSchool 2010"+
        		"</div>");
        
        // right - tab panel
        DecoratedTabPanel rightPartPanel = new DecoratedTabPanel();
        rightPartPanel.setSize("440px", "600px");
        rightPartPanel.getElement().setId("tabPanel");
        
        // first tab - simulation tests
        VerticalPanel simulationTab = new VerticalPanel();
        simulationTab.setHeight("500px");
        simulationTab.getElement().setId("simulationPanel");
        simulationTab.addStyleName("outline");
        
        // second tab - questions
        VerticalPanel questionsTab = new VerticalPanel();
        questionsTab.setHeight("500px");
        questionsTab.getElement().setId("simulationPanel");
        questionsTab.addStyleName("outline");
        
        ArrayList<QBaseTemplate> simulationList = new ArrayList<QBaseTemplate>();
        simulationList.add(new QSimulationST(simulator, chart, "Adjusting Speed and Time"));
        simulationList.add(new QSimulationDT(simulator, chart, "Adjusting Distance and Time"));
        simulationList.add(new QSimulationDrive(simulator, chart, "Driving through an intersection"));
        
        ArrayList<QBaseTemplate> challengeList = new ArrayList<QBaseTemplate>();
        challengeList.add(new QChallengeST(simulator, chart, "Adjusting Speed and Time"));
        challengeList.add(new QChallengeDT(simulator, chart, "Adjusting Distance and Time"));
        challengeList.add(new QChallengeDrive(simulator, chart, "Driving through an intersection"));
        
        simulationPanel = new QuestionsPanel(simulator, chart, simulationList);
        simulationTab.add(simulationPanel);
        simulator.addSimListener(simulationPanel);
        
        exercisePanel = new QuestionsPanel(simulator, chart, challengeList);
        questionsTab.add(exercisePanel);
        simulator.addSimListener(exercisePanel);
        
        // build panel hierarchy  
        baseAbsolutePanel.add(baseHoriPanel);
        baseHoriPanel.add(leftVertPanel);
//        leftVertPanel.add(dataDisplayPanel);
        leftVertPanel.add(googleEarthPanel);
        leftVertPanel.add(chart);
        leftVertPanel.add(logoHtml);
        
        
        rightPartPanel.add(simulationTab, "Simulations");
        rightPartPanel.add(questionsTab, "Challenges");
        rightPartPanel.add(new HTMLPanel("Study Guide"), "Reference");
        rightPartPanel.add(new HTMLPanel("Help"), "Help");
        rightPartPanel.selectTab(0);
        
        // right tabs selection handler
        rightPartPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int selectedItem = event.getSelectedItem();
//				System.out.println("Clicked tab #" + selectedItem);
				if (selectedItem == 0) { // selected simulation tab
					chart.setShowCase(true);
					simulationPanel.syncDiagram();
				} else if (selectedItem == 1) {
					chart.setShowCase(true);
					exercisePanel.syncDiagram();
				}
			}
        	
        });
        
        baseHoriPanel.add(rightPartPanel);
    }
}
