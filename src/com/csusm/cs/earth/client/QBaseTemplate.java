package com.csusm.cs.earth.client;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class QBaseTemplate {
	String title;
	
	//reference to google earth
	DrivingSimulator simulator;
	
	//reference the chart
	DSTChart chart;
	int chartMode;
	
	// distance selector
	int selectedDistance = Utils.SELECTED_DISTANCE;
	// speed selector
	int speedIndex = Utils.SPEED_IDX;
	// time selector
	int selectedTime = Utils.SELECTED_TIME;
	
	// basic 3 elements 
	double distance = Utils.DISTANCE; // m
	double velocity = Utils.SPEED; // m/s
	double time = Utils.TIME; // s
	
	boolean earthEffect = false;
	boolean diagramEffect = false;
	
	// Drive button. Since its status need to be changed by simulator, I set it as a field.
	Button btnRun;
	
	abstract public void syncDiagram();
	abstract public void initQuestion();
	abstract public VerticalPanel getQuestionPanel();
	
	/*
	 * the following methods needs to be override
	 */
	// **********************************************************************
	public void endAction(SimulationEndData endData) {
		btnRun.setEnabled(true);
	}
	
	public void runAction() {
		simulator.setDistance(distance);
		simulator.setSpeed(velocity);
		simulator.go();
	}
	
	public void distanceAction() {
		distance = selectedDistance;
		if (earthEffect && simulator!=null) {
			simulator.setDistance(distance);
			simulator.moveDistanceFromDestination(distance);
		}
		drawCase();
	}
	
	public void timeAction() {
		time = selectedTime;
		drawCase();
	}
	
	public void speedAction() {
		velocity = Utils.SPEED_LIMITS[speedIndex]/3.6;
		if (earthEffect)
			simulator.setSpeed(velocity);
		drawCase();
	}
	// **********************************************************************
	
	public void drawCase() {
		if (diagramEffect) {
			if (chartMode == Utils.VelocityTime)
				chart.drawDemo(velocity*time, velocity);
			else
				chart.drawDemo(distance, distance/time);
		}
	}
	
	public QBaseTemplate(DrivingSimulator simulator, DSTChart chart, String title) {
        super();
		this.simulator = simulator;
		this.chart = chart;
		this.title = title;
	}
	
	public AbsolutePanel getRunButtonPanel() {
		AbsolutePanel runButtonPanel = new AbsolutePanel();
		runButtonPanel.setSize("430px", "35px");
		// Drive button is not a local final variable
		btnRun = new Button();
		btnRun.setSize("100px", "30px");
		runButtonPanel.add(btnRun, (430-100)/2, 2);
		
		btnRun.setHTML("<img src='images/icon16/play.png' />Drive");
		
		btnRun.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				if (btnRun.isEnabled()) {
					btnRun.setEnabled(false);
					runAction();
				} 
			}
		});
		return runButtonPanel;
	}
	
	public AbsolutePanel getDistanceSelector() {
		AbsolutePanel distanceSelector = new AbsolutePanel();
	    distanceSelector.setSize("430px", "45px");
		Button btnAcc = new Button();
		btnAcc.setHTML("<img src='images/icon16/up.png' />1m");
		Button btnDec = new Button();
		btnDec.setHTML("<img src='images/icon16/down.png' />1m");
		Button btnAccTen = new Button();
		btnAccTen.setHTML("<img src='images/icon16/up.png' />10m");
		Button btnDecTen = new Button();
		btnDecTen.setHTML("<img src='images/icon16/down.png' />10m");
        
		final HTML distanceHtml = new HTML(selectedDistance+" Meters");
		distanceHtml.addStyleName("app-distance");
        distanceSelector.add(distanceHtml, 50, 5); 
        
        btnAccTen.setSize("70px", "30px");
        distanceSelector.add(btnAccTen, 150, 5);
        btnAcc.setSize("70px", "30px");
        distanceSelector.add(btnAcc, 220, 5);
        btnDec.setSize("70px", "30px");
        distanceSelector.add(btnDec, 290, 5);
        btnDecTen.setSize("70px", "30px");
        distanceSelector.add(btnDecTen, 360, 5);
        
		btnDec.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedDistance--;
					if (selectedDistance < 50) {
						selectedDistance = 50;
					}
					distanceHtml.setHTML(selectedDistance+" Meters");
					distanceAction();
				} 
				});  
		btnAcc.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedDistance++;
					if (selectedDistance >= 200) {
						selectedDistance = 200;
					}
					distanceHtml.setHTML(selectedDistance+" Meters");
					distanceAction();
				}  
				});  
		
		btnDecTen.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedDistance-=10;
					if (selectedDistance < 50) {
						selectedDistance = 50;
					}
					distanceHtml.setHTML(selectedDistance+" Meters");
					distanceAction();
				} 
				});  
		btnAccTen.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedDistance+=10;
					if (selectedDistance >= 200) {
						selectedDistance = 200;
					}
					distanceHtml.setHTML(selectedDistance+" Meters");
					distanceAction();
				}  
				});  
		
		return distanceSelector;
	}
	
	public AbsolutePanel getSpeedSelector() {
		AbsolutePanel speedSelector = new AbsolutePanel();
	    speedSelector.setSize("430px", "75px");
		Button btnAcc = new Button();
		btnAcc.setHTML("<img src='images/icon16/up.png' />Speed");
		Button btnDec = new Button();
		btnDec.setHTML("<img src='images/icon16/down.png' />Speed");
        
		final Image myImage = new Image();
        myImage.setSize("75px", "75px");
		myImage.setUrl("images/speedsigns/"+Utils.SPEED_LIMITS[speedIndex]+".jpg");  
        speedSelector.add(myImage, 100, 0); 
        
        btnAcc.setSize("75px", "30px");
        speedSelector.add(btnAcc, 200, 20);
        
        btnDec.setSize("75px", "30px");
        speedSelector.add(btnDec, 300, 20);
        
		btnDec.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					speedIndex--;
					if (speedIndex < 0) {
						speedIndex = 0;
					}
					myImage.setUrl("images/speedsigns/"+Utils.SPEED_LIMITS[speedIndex]+".jpg");  
					speedAction();
				}
				});  
		btnAcc.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					speedIndex++;
					if (speedIndex >= Utils.SPEED_LIMITS.length ) {
						speedIndex = Utils.SPEED_LIMITS.length-1;
					}
					myImage.setUrl("images/speedsigns/"+Utils.SPEED_LIMITS[speedIndex]+".jpg");  
					speedAction();
				}  
				});  
		
		return speedSelector;
	}
	
	public AbsolutePanel getTimeSelector() {
		AbsolutePanel timeSelector = new AbsolutePanel();
	    timeSelector.setSize("430px", "45px");
		Button btnAcc = new Button();
		btnAcc.setHTML("<img src='images/icon16/up.png' />Time");
		Button btnDec = new Button();
		btnDec.setHTML("<img src='images/icon16/down.png' />Time");
        
		final HTML timeHtml = new HTML(selectedTime+" Seconds");
		timeHtml.addStyleName("app-time");
        timeSelector.add(timeHtml, 80, 5); 
        
        btnAcc.setSize("75px", "30px");
        timeSelector.add(btnAcc, 200, 5);
        
        btnDec.setSize("75px", "30px");
        timeSelector.add(btnDec, 300, 5);
        
		btnDec.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedTime--;
					if (selectedTime < 1) {
						selectedTime = 1;
					}
					timeHtml.setHTML(selectedTime+" Seconds");
					timeAction();
				} 
				});  
		btnAcc.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					selectedTime++;
					if (selectedTime >= 10) {
						selectedTime = 10;
					}
					timeHtml.setHTML(selectedTime+" Seconds");
					timeAction();
				}  
				});  
		
		return timeSelector;
	}

	public HTML getFootage() {
//		String content = "<div style='align:right'><hr><span style='color:red'>Traffic School demo</span><br>iQuest 2009</div>";
		String content = "";
		return new HTML(content);
	}
	
	public String getTitle() {
		return title;
	}
	
}
