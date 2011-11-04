package com.csusm.cs.earth.client;

import java.util.ArrayList;

import com.csusm.cs.earth.client.DrivingSimulator.SimulationData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationInitialData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationListener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuestionsPanel extends Composite implements SimulationListener{
	
	//reference to google earth
	DrivingSimulator simulator;
	//reference to the chart
	DSTChart chart;
	
	// question panels
	AbsolutePanel questionButtonPanel = new AbsolutePanel();
    Button btnPrev = new Button("Prev");
    Button btnNext = new Button("Next");
	HTML html = null;
        
	VerticalPanel base = new VerticalPanel();
	VerticalPanel panel;
	
	ArrayList<QBaseTemplate> questionList;
	QBaseTemplate controller;
	
	int questionIndex;
	
	public void syncDiagram() {
		controller.syncDiagram();
	}
	
	public QuestionsPanel(DrivingSimulator simulator, DSTChart chart, ArrayList<QBaseTemplate> list) {
        super();
		this.simulator = simulator;
		this.chart = chart;
		questionList = list;
		controller = questionList.get(0);
		
		initQuestionButtonPanel();
		base.add(questionButtonPanel);
		panel = controller.getQuestionPanel();
		base.add(panel);
		initWidget(base);
	}
	
	public void initQuestionButtonPanel() {
		questionButtonPanel.setWidth("435px");
		questionButtonPanel.setHeight("30px");
		
        btnPrev = new Button("Prev");
		btnPrev.setHTML("<img src='images/icon16/back.png' />Prev");
		btnPrev.setSize("75px", "28px");
		btnPrev.setEnabled(false);
		
        btnNext = new Button("Next");
		btnNext.setHTML("<img src='images/icon16/right.png' />Next");
		btnNext.setSize("75px", "28px");
		
		html = new HTML("<center><span class='app-title3'>" + controller.getTitle() + "</></center>");
		html.setWidth("285px");
		
        questionButtonPanel.add(btnPrev, 0, 2);
        questionButtonPanel.add(html, 75, 0);
        questionButtonPanel.add(btnNext, 360, 2);
        
		btnPrev.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					if (questionIndex>0 ) {
						questionIndex--;
						controller = questionList.get(questionIndex);
						
						String str = "<center><span class='app-title3'>"+controller.getTitle()+"</span></center>";
						html.setHTML(str);
						
						if (questionIndex==0) btnPrev.setEnabled(false);
						else btnPrev.setEnabled(true);
						if (questionIndex==questionList.size()-1) btnNext.setEnabled(false);
						else btnNext.setEnabled(true);
						base.remove(panel);
						panel = controller.getQuestionPanel();
						base.add(panel);
						controller.syncDiagram();
					}
				}  
				});  
        
		btnNext.addClickHandler(new ClickHandler() {  
				public void onClick(ClickEvent event) {  
					if (questionIndex<questionList.size()-1) {
						questionIndex++;
						controller = questionList.get(questionIndex);
						
						String str = "<center><span class='app-title3'>"+controller.getTitle()+"</span></center>";
						html.setHTML(str);
						
						if (questionIndex==questionList.size()-1) btnNext.setEnabled(false);
						else btnNext.setEnabled(true);
						if (questionIndex==0) btnPrev.setEnabled(false);
						else btnPrev.setEnabled(true);
						base.remove(panel);
						panel = controller.getQuestionPanel();
						base.add(panel);
						controller.syncDiagram();
					}
				}  
				});  
        
	}
	
	@Override
	public void simEndStatus(SimulationEndData simEndData) {
		// TODO Auto-generated method stub
		if (controller.btnRun != null) {
			controller.btnRun.setEnabled(true);
		}
		controller.endAction(simEndData);
	}

	@Override
	public void simInitalStatus(SimulationInitialData simInitData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simStatus(SimulationData simData) {
		// TODO Auto-generated method stub
		
	}
	
}
