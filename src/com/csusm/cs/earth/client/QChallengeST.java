package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QChallengeST extends QBaseTemplate {
	int destDistance = 0;

	HTML htmlResult;
	HTML description;
	int goalDistance;
	VerticalPanel questionPanel = new VerticalPanel();
	
	public QChallengeST(DrivingSimulator simulator, DSTChart chart, String title) {
		super(simulator, chart, title);
		this.earthEffect = true;
		this.diagramEffect = true;
		this.chartMode = Utils.DistanceTime;
		simulator.setHasTraffic(false);
		initQuestion();
	}
	
	@Override
	public void syncDiagram() {
		chart.cleanTargetSlope();
		chart.setToMode(chartMode);
		chart.setShowFormula(true);
		chart.setShowFormulaAnswer(false);
		simulator.setHasTraffic(false);
		simulator.resetLoc();
		this.drawCase();
		
		int randomTime = Random.nextInt(10)+1;
		double randomSpeed = Utils.SPEED_LIMITS[Random.nextInt(Utils.SPEED_LIMITS.length)]/3.6;
		destDistance = (int)(randomTime*randomSpeed);
	    description.setHTML("Your goal is to travel " +
				"<span class='app-emphasize'>" + destDistance + " meters</span> " +
	    		"with your car. " +
	    		"Set the number of seconds you would like to drive your car (step 1) " +
	    		"and the speed of your car (step 2). Hit the 'Drive' button to see " +
	    		"if your car moves exactly " +
				"<span class='app-emphasize'>" + destDistance + " meters</span>. ");
	    hideResult();
	}
	@Override
	public void drawCase() {
		time = selectedTime;
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		distance = time*velocity;
		super.drawCase();
	}
	
	public void initQuestion() {
	    description = new HTML("");
	    questionPanel.add(description);
		int randomTime = Random.nextInt(10)+1;
		double randomSpeed = Utils.SPEED_LIMITS[Random.nextInt(Utils.SPEED_LIMITS.length)]/3.6;
		destDistance = (int)(randomTime*randomSpeed);
	    description.setHTML("Your goal is to travel " +
				"<span class='app-emphasize'>" + destDistance + " meters</span> " +
	    		"with your car. " +
	    		"Set the number of seconds you would like to drive your car (step 1) " +
	    		"and the speed of your car (step 2). Hit the 'Drive' button to see " +
	    		"if your car moves exactly " +
				"<span class='app-emphasize'>" + destDistance + " meters</span>. ");
	    
	    HTML step1 = new HTML("<span class='app-title3'>Step 1.</span> Choose the time:");
	    questionPanel.add(step1);
	    questionPanel.add(this.getTimeSelector());
	    
	    HTML step2 = new HTML("<span class='app-title3'>Step 2.</span> Choose the limit speed of the road:");
	    questionPanel.add(step2);
	    questionPanel.add(this.getSpeedSelector());
	    
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
		String formattedVelocityKM_H = NumberFormat.getFormat(".0").format(Utils.SPEED_LIMITS[speedIndex]);
		String formattedVelocity = NumberFormat.getFormat(".0").format(Utils.SPEED_LIMITS[speedIndex]/3.6);
		
		if ( status == Utils.CORRECT ) {
			htmlResult.setHTML("Congratulations, you have achieved the goal of driving " +
					"<span class='app-emphasize'>" + destDistance + "</span> meters " +
					"by driving your car at the speed of " +
					"<span class='app-emphasize'>" + formattedVelocityKM_H + "</span> km/h" +
					"(<span class='app-emphasize'>" + formattedVelocity + "</span> m/s) for " +
					"<span class='app-emphasize'>" + time + "</span> " +
					" seconds. A new challenge is set for you. " +
					"Repeat Steps 1 - 3 to see if you could solve the challenge again.");
			
			// reset question
			int randomTime = Random.nextInt(10)+1;
			double randomSpeed = Utils.SPEED_LIMITS[Random.nextInt(Utils.SPEED_LIMITS.length)]/3.6;
			destDistance = (int)(randomTime*randomSpeed);
		    description.setHTML("Your goal is to travel " +
					"<span class='app-emphasize'>" + destDistance + "</span> " +
		    		"meters with your car. " +
		    		"Set the number of seconds you would like to drive your car (step 1) " +
		    		"and the speed of your car (step 2). Hit the 'Drive' button to see " +
		    		"if your car moves exactly " +
					"<span class='app-emphasize'>" + destDistance + "</span> " +
		    		"meters.");
		} else {
			htmlResult.setHTML("Sorry, the distance of your drive " +
					"(<span class='app-emphasize'>" + (int)(Math.round(velocity*time)) + "m</span>)" +
					" did not match the goal" +
					"(<span class='app-emphasize'>" + destDistance + "m</span>). " +
					"Please adjust your velocity and/or time before hitting the 'Drive' button " +
					"to see if your adjustment makes the trick.");
		}
		
	    htmlResult.setVisible(true);
	}
	
	public void hideResult() {
		htmlResult.setVisible(false);
	}

	@Override
	public void endAction(SimulationEndData endData) {
		if ((int)(Math.round(velocity * time)) == destDistance)
			showResult(Utils.CORRECT);
		else
			showResult(Utils.WRONG);
	}
	
	@Override
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
