package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QSimulationDT extends QBaseTemplate {
	HTML htmlResult;
	Button btnDiagram1;
	Button btnDiagram2;
	VerticalPanel simulationPanel;
	
	public QSimulationDT(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = false;
		this.diagramEffect = true;
		this.chartMode = Utils.DistanceTime;
		simulator.setHasTraffic(false);
		initQuestion();
	}
	
	@Override
	public void syncDiagram() {
		chart.setToMode(chartMode);
		chart.setShowFormula(true);
		chart.setShowFormulaAnswer(false);
		hideResult();
		simulator.setHasTraffic(false);
		simulator.resetLoc();
		drawCase();
	}
	
	@Override
	public void drawCase() {
		distance = (double)selectedDistance;
		time = selectedTime;
		velocity = distance/time;
		super.drawCase();
	}
	
	public void initQuestion() {
		simulationPanel = new VerticalPanel();
		simulationPanel.setWidth("430px");
		
	    HTML description = new HTML("The Distance-Time chart uses a line to show the " +
	    		"speed of your car. Set the distance you would like to drive (step 1) " +
	    		"and the number of seconds you would like to drive your car (step 2). " +
	    		"Hit the 'Drive' button to see your car move.");
	    simulationPanel.add(description);
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the distance:");
	    simulationPanel.add(step1);
	    simulationPanel.add(this.getDistanceSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 1.</span> Choose the period time to drive:");
	    simulationPanel.add(step2);
	    simulationPanel.add(this.getTimeSelector());
	    
	    HTML step3 = new HTML("<span class='app-title3'>Step 3.</span> Hit the " +
	    		"<span class='app-emphasize'>'Drive'</span> button to start simulation:");
	    simulationPanel.add(step3);
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
		String formattedVelocity = NumberFormat.getFormat(".0").format(velocity);
		String formattedVelocityKM_H = NumberFormat.getFormat(".0").format(velocity*3.6);
	    htmlResult.setHTML("<span class='app-title3'></span> " +
	    		"In this run, you have set to drive your car for "+
				"<span class='app-emphasize'>" + distance + "</span> meters in "+
				"<span class='app-emphasize'>" + time + "</span> seconds. Your car was traveling at the speed of " +
				"<span class='app-emphasize'>" + formattedVelocity + "</span> m/s" +
				"(<span class='app-emphasize'>" + formattedVelocityKM_H + "</span> km/h). " +
				"If you want to see another run of driving, make adjustment for Steps " +
				"1 - 2 and hit the 'Drive' button again.");
	    		
	    htmlResult.setVisible(true);
	}
	
	public void hideResult() {
		htmlResult.setVisible(false);
	}

	@Override
	public void endAction(SimulationEndData endData) {
		showResult(endData.status);
	}
	
	public void runAction() {
		hideResult();
		distance = (double)selectedDistance;
		time = selectedTime;
		velocity = distance/time;
		
		simulator.setTime((long)(time*1000));
		simulator.setDistance(distance);
		simulator.setSpeed(velocity);
		
		simulator.setHasTraffic(false);
		simulator.moveDistanceFromDestination(-10);
		simulator.go();
	}

}
