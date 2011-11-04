package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QSimulationST extends QBaseTemplate {
	HTML htmlResult;
	Button btnDiagram1;
	Button btnDiagram2;
	VerticalPanel simulationPanel;
	
	public QSimulationST(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = true;
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
		time = selectedTime;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		distance = time*velocity;
		super.drawCase();
	}
	
	public void initQuestion() {
		simulationPanel = new VerticalPanel();
		simulationPanel.setWidth("430px");
		
	    HTML description = new HTML("In this simulation, you will be able to see " +
	    		"how the distance you car travel is affected by the number of " +
	    		"seconds you will drive as well as the speed of your car. As you ajust " +
	    		"the time(step 1) and speed(step 2), please watch how the chart is " +
	    		"ajusted accordingly. Hit the 'Drive' button to see your car move.");
	    simulationPanel.add(description);
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the period time to drive:");
	    simulationPanel.add(step1);
	    simulationPanel.add(this.getTimeSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 2.</span> Choose the current speed of your car:");
	    simulationPanel.add(step2);
	    simulationPanel.add(this.getSpeedSelector());
	    
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
		int formattedVelocityKM_H = Utils.SPEED_LIMITS[speedIndex];
		String formattedDistance = NumberFormat.getFormat(".0").format(distance);
		String formattedVelocity = NumberFormat.getFormat(".0").format(velocity);
	    htmlResult.setHTML("<span class='app-title3'></span> " +
	    		"In this run, you have set to drive your car at the speed of " +
				"<span class='app-emphasize'>" + formattedVelocity + "</span> m/s" +
				"(<span class='app-emphasize'>" + formattedVelocityKM_H + "</span> km/h) for " +
				"<span class='app-emphasize'>" + time + "</span> seconds. Your car has travelled " +
				"<span class='app-emphasize'>" + formattedDistance + "</span> meters. If you want to " +
				"see another run of driving, make adjustment for Steps 1 - 2 and hit the 'Drive' button again.");
	    htmlResult.setVisible(true);
	}
	
	public void hideResult() {
		htmlResult.setVisible(false);
	}

	@Override
	public void endAction(SimulationEndData endData) {
		showResult(endData.status);
		super.endAction(endData);
	}
	
	public void runAction() {
		hideResult();
		time = selectedTime;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		distance = time*velocity;
		
		simulator.setSpeed(velocity);
		simulator.setTime((long)(time*1000));
		simulator.setDistance(distance);
		
		simulator.setHasTraffic(false);
		simulator.moveDistanceFromDestination(-10);
		simulator.go();
	}
}
