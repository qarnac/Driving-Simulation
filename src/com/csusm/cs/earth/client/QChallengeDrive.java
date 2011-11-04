package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QChallengeDrive extends QBaseTemplate {
	HTML htmlResult;
	HTML description;
	int goalDistance;
	VerticalPanel questionPanel = new VerticalPanel();
	
	public QChallengeDrive(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = true;
		this.diagramEffect = true;
		this.chartMode = Utils.DistanceTime;
		simulator.setHasTraffic(true);
		initQuestion();
	}
	
	public void syncDiagram() {
		chart.cleanTargetSlope();
		chart.setToMode(chartMode);
		chart.setShowFormula(true);
		chart.setShowFormulaAnswer(false);
		simulator.setHasTraffic(true);
		simulator.resetLoc();
		this.drawCase();
		hideResult();
	}
	
	@Override
	public void drawCase() {
		distance = selectedDistance;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		time = distance/velocity;
		super.drawCase();
	}
	
	public void initQuestion() {
	    description = new HTML("Your goal is to enter the intersection when the green light turns yellow. There are many factors you may adjust: duration of the green light (step 1), the starting point of your car away from the intersection (step 2) and the speed limit of the street (step 3). When you are done, hit the 'Drive' button to see your car move toward the traffic light.");
	    questionPanel.add(description);
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the duration time for green light:");
	    questionPanel.add(step1);
	    questionPanel.add(this.getTimeSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 2.</span> Choose the distance:");
	    questionPanel.add(step2);
	    questionPanel.add(this.getDistanceSelector());
	    
	    HTML step3 = new HTML("<span class='app-title3'>Step 3.</span> Choose the speed limit of the road:");
	    questionPanel.add(step3);
	    questionPanel.add(this.getSpeedSelector());
	    
	    HTML step4 = new HTML("<span class='app-title3'>Step 4.</span> Hit the " +
	    		"<span class='app-emphasize'>'Drive'</span> button to start challenge:");
	    questionPanel.add(step4);
	    questionPanel.add(this.getRunButtonPanel());
	    
	    htmlResult = new HTML("");
	    htmlResult.setVisible(false);
	    questionPanel.add(htmlResult);
	    
	    questionPanel.add(this.getFootage());
	    
	}

	@Override
	public VerticalPanel getQuestionPanel() {
		return questionPanel;
	}
	
	public void showResult(int status) {
		String stat = "";
		if (status == Utils.BEFORE)
			stat = "before";
		else if (status == Utils.WHEN)
			stat = "when";
		else
			stat = "after";
		
	    htmlResult.setHTML("Your car entered the intersection " + 
	    		"<span class='app-emphasize'>"+stat+"</span>" +
	    		" the green light turns yellow. If you want to see another run of " +
	    		"driving, make adjustment for Steps 1 - 3 and hit the 'Drive' button again.");
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
		distance = selectedDistance;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		time = distance/velocity;
		
		simulator.setDistance(distance);
		simulator.setSpeed(velocity);
		simulator.setTime((long)(time*1000));
		
		simulator.setHasTraffic(true);
		simulator.setGreenLightDurationTime((long)selectedTime*1000);
		simulator.moveDistanceFromDestination(distance);
		simulator.go();
	}
	
	public void timeAction() {
	}
	
}
