package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QSimulationDrive extends QBaseTemplate {
	HTML htmlResult;
	Button btnDiagram1;
	Button btnDiagram2;
	VerticalPanel simulationPanel;
	int greenLightDuration = 5;
	HTML item;
	
	public QSimulationDrive(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = true;
		this.diagramEffect = true;
		this.chartMode = Utils.DistanceTime;
		simulator.setHasTraffic(true);
		initQuestion();
	}
	
	@Override
	public void syncDiagram() {
		chart.setToMode(chartMode);
		chart.setShowFormula(true);
		chart.setShowFormulaAnswer(false);
		hideResult();
		simulator.setHasTraffic(true);
		simulator.resetLoc();
		this.generateSpeed();
		this.drawCase();
	}
	
	@Override
	public void drawCase() {
		distance = selectedDistance;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		time = distance/velocity;
		super.drawCase();
	}
	
	public void generateSpeed() {
		speedIndex = Random.nextInt(Utils.SPEED_LIMITS.length);
	    item.setHTML("The speed limit for the road is <span style='background-color:yellow'> "
	    		+ Utils.SPEED_LIMITS[speedIndex] + " km/h</span>.");
	}
	
	public void initQuestion() {
		simulationPanel = new VerticalPanel();
		simulationPanel.setWidth("430px");
		
		item = new HTML("");
	    simulationPanel.add(item);
		this.generateSpeed();
	    
	    HTML description = new HTML("Please set the number of seconds before the green light will turn yellow (step 1) " +
	    		"and the starting point of your car away from the intersection (step 2). Hit the 'Drive' button " +
	    		"to see if your car will enter the intersection before or after the light turns yellow.");
	    simulationPanel.add(description);
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the duration time for green light:");
	    simulationPanel.add(step1);
	    simulationPanel.add(this.getTimeSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 2.</span> Choose the distance:");
	    simulationPanel.add(step2);
	    simulationPanel.add(this.getDistanceSelector());
	    
//	    HTML step3 = new HTML("<span class='app-title3'>Step 3.</span> Choose the chart:");
//	    simulationPanel.add(step3);
//	    AbsolutePanel diagramSelectionPanel = new AbsolutePanel();
//	    diagramSelectionPanel.setSize("430px", "35px");
//	    btnDiagram1 = new Button();
//	    btnDiagram2 = new Button();
//	    
//	    btnDiagram1.setHTML("<img src='images/icon16/chart.png'> Distance-Time</img>"); 
//	    btnDiagram1.setWidth("150px");
//	    diagramSelectionPanel.add(btnDiagram1, 50, 0);
//		btnDiagram1.addClickHandler(new ClickHandler() {  
//				public void onClick(ClickEvent event) {  
//					// TODO: show formula here
//				    btnDiagram1.setHTML("<img src='images/icon16/chart.png'> Distance-Time</img>"); 
//				    btnDiagram2.setHTML("Velocity-Time"); 
//				    chartMode = Utils.DistanceTime;
//				    chart.setToMode(chartMode);
//				    drawCase();
//				}  
//				});  
		
//	    btnDiagram2.setHTML("Velocity-Time"); 
//	    btnDiagram2.setWidth("150px");
//	    diagramSelectionPanel.add(btnDiagram2, 250, 0);
//		btnDiagram2.addClickHandler(new ClickHandler() {  
//				public void onClick(ClickEvent event) {  
//					// TODO: show formula here
//				    btnDiagram1.setHTML("Distance-Time"); 
//				    btnDiagram2.setHTML("<img src='images/icon16/chart.png'> Velocity-Time</img>"); 
//				    chartMode = Utils.VelocityTime;
//				    chart.setToMode(chartMode);
//				    drawCase();
//				}  
//				});  
//		simulationPanel.add(diagramSelectionPanel);
	    
	    HTML step4 = new HTML("<span class='app-title3'>Step 3.</span> Hit the " +
	    		"<span class='app-emphasize'>'Drive'</span> button to start simulation:");
	    simulationPanel.add(step4);
	    simulationPanel.add(this.getRunButtonPanel());
	    
	    htmlResult = new HTML("");
	    htmlResult.setVisible(false);
	    simulationPanel.add(htmlResult);
	    
	    simulationPanel.add(this.getFootage());
	}

	@Override
	public VerticalPanel getQuestionPanel() {
		return simulationPanel;
	}
	
	public void showResult(int status) {
		String stat = "";
		if (status == Utils.BEFORE)
			stat = "before";
		else if (status == Utils.WHEN)
			stat = "when";
		else
			stat = "after";
		
		String formattedVelocity = NumberFormat.getFormat("00.0").format(velocity);
		String formattedVelocityKM_H = NumberFormat.getFormat("00.0").format(velocity*3.6);
		String formattedTime = NumberFormat.getFormat(".0").format(time);
	    htmlResult.setHTML("In this run, you have set the car to be " +
	    		"<span class='app-emphasize'>"+distance+"</span> meters away from the light and driven the car at the speed of " +
	    		"<span class='app-emphasize'>"+formattedVelocity+"</span> m/s" +
	    		"(<span class='app-emphasize'>"+formattedVelocityKM_H+"</span> km/h). "+
	    		"Your car drived <span class='app-emphasize'>"+formattedTime+"</span> seconds and entered the intersection " + 
	    		"<span class='app-emphasize'>"+stat+"</span>" +
	    		" the green light turns yellow" +
	    		"(<span class='app-emphasize'>"+greenLightDuration+" s</span>). "+
	    		"If you want to see another run of " +
	    		"driving, make adjustment for Steps 1 - 3 and hit the 'Drive' button again.");
	    htmlResult.setVisible(true);
	    
	    this.generateSpeed();
		this.drawCase();
	}
	
	public void hideResult() {
		htmlResult.setVisible(false);
	}

	@Override
	public void endAction(SimulationEndData endData) {
		showResult(endData.status);
	}
	
	public void timeAction() {
		greenLightDuration = selectedTime;
	}
	
	public void runAction() {
		hideResult();
		distance = selectedDistance;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		time = distance/velocity;
		
		simulator.setSpeed(velocity);
		simulator.setTime((long)(time*1000));
		simulator.setDistance(distance);
		
		simulator.setHasTraffic(true);
		simulator.setGreenLightDurationTime((long)selectedTime*1000);
		simulator.moveDistanceFromDestination(distance);
		simulator.go();
	}
}
