package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QChallengeDT extends QBaseTemplate {
	HTML htmlResult;
	HTML description;
	VerticalPanel questionPanel = new VerticalPanel();
	
	public QChallengeDT(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = false;
		this.diagramEffect = true;
		this.chartMode = Utils.DistanceTime;
		simulator.setHasTraffic(false);
		initQuestion();
	}

	public void syncDiagram() {
		chart.setToMode(chartMode);
		chart.setShowFormula(true);
		chart.setShowFormulaAnswer(false);
		simulator.setHasTraffic(false);
		simulator.resetLoc();
		this.drawCase();
		
		speedIndex = Random.nextInt(Utils.SPEED_LIMITS.length);
		chart.drawTargetSlope(Utils.SPEED_LIMITS[speedIndex]/3.6);
		description.setHTML("Your goal is to travel at the speed of " +
				"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span> " +
				"with your car. Set the distance (step 1) and the number of " +
				"seconds (step 2) you would like to drive your car. Hit the 'Drive' " +
				"button to see if your car moves exactly  at the speed of " +
				"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span> " +
				".");
		hideResult();
	}
	@Override
	public void drawCase() {
		time = selectedTime;
		distance = selectedDistance;
		velocity = distance/time;
		super.drawCase();
	}
	
	public void initQuestion() {
	    description = new HTML("");
	    questionPanel.add(description);
		speedIndex = Random.nextInt(Utils.SPEED_LIMITS.length-1);
		description.setHTML("Your goal is to travel at the speed of " +
				"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span> " +
				"with your car. Set the distance (step 1) and the number of " +
				"seconds (step 2) you would like to drive your car. Hit the 'Drive' " +
				"button to see if your car moves exactly  at the speed of " +
				"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span>" +
				".");
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the period time to drive:");
	    questionPanel.add(step1);
	    questionPanel.add(this.getTimeSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 2.</span> Choose the distance:");
	    questionPanel.add(step2);
	    questionPanel.add(this.getDistanceSelector());
	    
	    HTML step3 = new HTML("<span class='app-title3'>Step 3.</span> Hit the " +
	    		"<span class='app-emphasize'>'Drive'</span> button to start challenge:");
	    questionPanel.add(step3);
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
		String formattedVelocityKM_H = NumberFormat.getFormat(".0").format(Math.round(distance/time*3.6));
		String formattedVelocity = NumberFormat.getFormat(".0").format(distance/time);
		if ( status == Utils.CORRECT ) {
			htmlResult.setHTML("Congratulations, you have achieved the goal of driving at " +
					"<span class='app-emphasize'>" + formattedVelocityKM_H + "km/h</span>" +
					"(<span class='app-emphasize'>" + formattedVelocity + "m/s</span>) " +
					"by driving your car for <span class='app-emphasize'>" + distance + " meters</span> " +
					"in <span class='app-emphasize'>" + time + " seconds</span>. " +
					"A new challenge is set for you. Repeat Steps 1 - 3 to see if " +
					"you could solve the challenge again.");
			
			// reset question
			speedIndex = Random.nextInt(Utils.SPEED_LIMITS.length);
			chart.drawTargetSlope(Utils.SPEED_LIMITS[speedIndex]/3.6);
			description.setHTML("Your goal is to travel at the speed of " +
					"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span> " +
					"with your car. Set the distance (step 1) and the number of " +
					"seconds (step 2) you would like to drive your car. Hit the 'Drive' " +
					"button to see if your car moves exactly  at the speed of " +
					"<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span>" +
					".");
			
		} else {
			htmlResult.setHTML("Sorry, the speed of your drive " +
					"(<span class='app-emphasize'>" + formattedVelocity + "m/s</span> or " +
					"<span class='app-emphasize'>" + formattedVelocityKM_H + "km/h</span>)" +
					"did not match the goal" +
					"(<span class='app-emphasize'>" + Utils.SPEED_LIMITS[speedIndex] + "km/h</span>)." +
					"Please adjust your distance and/or time before hitting the 'Drive' " +
					"button to see if your adjustment makes the trick.");
		}
	    htmlResult.setVisible(true);
	}
	
	public void hideResult() {
		htmlResult.setVisible(false);
	}

	@Override
	public void endAction(SimulationEndData endData) {
//		System.out.println((Math.round(distance / time*3.6)));
//		System.out.println((Math.round(speedArray[speedIndex])));
		if (Math.round(distance / time*3.6) == Math.round(Utils.SPEED_LIMITS[speedIndex]))
			showResult(Utils.CORRECT);
		else
			showResult(Utils.WRONG);
	}
	
	@Override
	public void runAction() {
		hideResult();
		time = selectedTime;
		distance = selectedDistance;
		velocity = distance/time;
		
		simulator.setDistance(distance);
		simulator.setSpeed(velocity);
		simulator.setTime((long)(time*1000));
		
		simulator.setHasTraffic(false);
		simulator.moveDistanceFromDestination(-10);
		simulator.go();
	}
}
