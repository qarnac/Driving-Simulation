package com.csusm.cs.earth.client;

import java.util.ArrayList;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationEndData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationInitialData;
import com.csusm.cs.earth.client.DrivingSimulator.SimulationListener;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.objetdirect.tatami.client.gfx.Color;
import com.objetdirect.tatami.client.gfx.Font;
import com.objetdirect.tatami.client.gfx.GraphicCanvas;
import com.objetdirect.tatami.client.gfx.GraphicObject;
import com.objetdirect.tatami.client.gfx.GraphicObjectListener;
import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Rect;
import com.objetdirect.tatami.client.gfx.Text;

	
/**
 * This class charts the remaining distance for the drivig simulation.
 * 
 * @author Curtis Jensen
 */
public class DSTChart extends GraphicCanvas implements SimulationListener 
{
	// logical variables
	private ArrayList<GraphicObject> legendLine = new ArrayList<GraphicObject>();
	private GraphicObject areaLineA;
	private GraphicObject areaLineB;
	private GraphicObject areaLineC;
	private GraphicObject targetSlopeLine;
	
	// used to save less important graphic objects
	private ArrayList<GraphicObject> graphicObjects = new ArrayList<GraphicObject>();
	
	private ChartPoint lastPnt = null; 
	
	int mode = Utils.VelocityTime;
	boolean showCase = true;
	boolean showFormula = true;
	boolean clickable = false;
	private boolean showFormulaAnswer = false;
	
	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
	public boolean isShowCase() {
		return showCase;
	}

	public void setShowCase(boolean showCase) {
		this.showCase = showCase;
	}

	public boolean isShowFormula() {
		return showFormula;
	}

	public void setShowFormula(boolean showFormula) {
		this.showFormula = showFormula;
	}
	
	public boolean isShowFormulaAnswer() {
		return showFormulaAnswer;
	}

	public void setShowFormulaAnswer(boolean showFormulaAnswer) {
		this.showFormulaAnswer = showFormulaAnswer;
	}

	// coordinates variables
	private static final int AXIS_PADDING = 5;
	private static final int AXIS_SHORT_LINE = 4;
	private static final int YUNIT_PADDING = 8;
	private static final int XUNIT_PADDING = 5;
	
	private int width;
	private int height;
	private int orgX;
	private int orgY;
	private int xAxisLength;
	private int yAxisLength;
	private int xTickLength;
	private int yTickLength;
	
	// real coordinates meanning
	private double xTickMin;
	private double xTickMax;
	private double yTickMin;
	private double yTickMax;
	
	private int xTickCount;
	private int yTickCount;
	
	// auxiliary decorations 
	String yTickUnitString;
	String xTickUnitString;
	private String xAxisLabelString;
	private String yAxisLabelString;
	
	// graphical objects
	static Color[] colors= new Color[]{Color.BLUE, Color.GREEN, Color.LIME, Color.YELLOW};
	private Color currentColor = colors[0];
	
	Text formulaText;
	Rect areaRect;
	Line[] slopeLines = new Line[4];
	
	Font labelFont = new Font("Arial", 10, Font.LIGHTER, Font.LIGHTER, Font.LIGHTER);
	Font tickFont = new Font("Arial", 8, Font.LIGHTER, Font.LIGHTER, Font.LIGHTER);
	int charLabelHeight;
	int charLabelWidth;
	int charTickHeight;
	int charTickWidth;
	
	public void setToMode(int newMode) {
		this.clearLines();
		if (newMode == mode) return;
		mode = newMode;
		this.clear();
		if (mode == Utils.VelocityTime)
			updateStatus(Utils.VelocityTime, "Time", "Speed", "sec", "m/s", 0.0, 20.0, 0.0, 40.0, 20, 8);
		else
			updateStatus(Utils.DistanceTime, "Time", "Distance", "sec", " m", 0.0, 20.0, 0.0, 200.0, 20, 8);
		this.drawBase();
	}
	
	/**
	 * Constructor
	 * 
	 * @param width  - The desired width of the chart (pixels).
	 * @param height - The desired height of the chart (pixels).
	 */
	public DSTChart(int mode, int width, int height, String xAxisLabel, String yAxisLabel,  String xUnit, String yUnit, 
			final double xmin, final double xmax, final double ymin, final double ymax, int numXTicks, int numYTicks)
	{
		this.width = width;
		this.height = height;
		
		Text singleChar = new Text("W");
		singleChar.setFont(labelFont);
		charLabelHeight = (int)singleChar.getHeight();
		charLabelWidth = (int)singleChar.getWidth();
		
		singleChar.setFont(tickFont);
		charTickHeight = (int)singleChar.getHeight();
		charTickWidth = (int)singleChar.getWidth();
		
		this.setPixelSize(width, height);
		this.setSize(width + "px", height + "px");
		
		updateStatus(mode, xAxisLabel, yAxisLabel, xUnit, yUnit, xmin, xmax, ymin, ymax, numXTicks, numYTicks);
		this.drawBase();
		
		this.addGraphicObjectListener(new GraphicObjectListener() {

			@Override
			public void mouseClicked(GraphicObject graphicObject, Event event) {
				if (!clickable) return;
				int x = DOM.eventGetClientX(event);
				int y = DOM.eventGetClientY(event);
				x = x - DSTChart.this.getAbsoluteLeft()+Window.getScrollLeft();
				y = y - DSTChart.this.getAbsoluteTop() + Window.getScrollTop();
//				System.out.println("GEt x:"+x+" y:"+y);
				if (x >= orgX && x <= orgX+xAxisLength && y<=orgY && y>=orgY-yAxisLength) {
					
					// adjust coordinates to the top-right corner in the grid where mouse clicked 
					x = x - orgX;
					x = x - (x%DSTChart.this.xTickLength) + (x%DSTChart.this.xTickLength==0 ? 0 :DSTChart.this.xTickLength);
					y = orgY-y;
					y = y - y%DSTChart.this.yTickLength + (y%DSTChart.this.yTickLength==0 ? 0 : DSTChart.this.yTickLength);
					
					if (DSTChart.this.mode == Utils.VelocityTime){
						double time = x/(double)xAxisLength * (xmax-xmin);
						double speed = y/(double)yAxisLength * (ymax-ymin);
						double dist = time * speed;
						drawArea(dist, speed);
					} else {
						double time = x/(double)xAxisLength * (xmax-xmin);
						double dist = y/(double)yAxisLength * (ymax-ymin);
						double speed = dist/time;
						drawSlope(dist, speed);
					}
				}
					
			}

			@Override
			public void mouseDblClicked(GraphicObject graphicObject, Event event) {
//				System.out.println("dlb");
				
			}

			@Override
			public void mouseMoved(GraphicObject graphicObject, Event event) {
			}

			@Override
			public void mousePressed(GraphicObject graphicObject, Event event) {
//				System.out.println("mouse press");
				
			}

			@Override
			public void mouseReleased(GraphicObject graphicObject, Event event) {
//				System.out.println("mouse release");
				
			}
			
		});
		
	}
	
	public void updateStatus(int mode, String xAxisLabel, String yAxisLabel,  String xUnit, String yUnit, 
			final double xmin, final double xmax, final double ymin, final double ymax, int numXTicks, int numYTicks) {
		this.mode = mode;
		this.xAxisLabelString = xAxisLabel;
		this.yAxisLabelString = yAxisLabel;
		this.xTickUnitString = xUnit;
		this.yTickUnitString = yUnit;
		
		this.xTickMin = xmin;
		this.xTickMax = xmax;
		this.yTickMin = ymin;
		this.yTickMax = ymax;
		this.xTickCount = numXTicks;
		this.yTickCount = numYTicks;
		
		this.xAxisLength = (int) (this.width - AXIS_PADDING*2 - XUNIT_PADDING - charLabelWidth - charTickWidth*3 - AXIS_SHORT_LINE);
		this.yAxisLength = (int) (height - AXIS_PADDING*2 - YUNIT_PADDING - charLabelHeight - charTickHeight - AXIS_SHORT_LINE);
		
		this.orgX = (int) (AXIS_PADDING+charLabelWidth+charTickWidth*3+AXIS_SHORT_LINE);
		this.orgY = yAxisLength + AXIS_PADDING + YUNIT_PADDING - 2;
		
		this.xAxisLength = this.xAxisLength - this.xAxisLength % numXTicks;
		this.yAxisLength = this.yAxisLength - this.yAxisLength % numYTicks;
		this.xTickLength = this.xAxisLength/numXTicks;
		this.yTickLength = this.yAxisLength/numYTicks;
	}
	
	public void drawDemo(double dist, double spd) {
		if (this.mode == Utils.VelocityTime)
			drawArea(dist, spd);
		else 
			drawSlope(dist, spd);
	}
	
	public void drawArea(double dist, double spd) {
		if (areaRect!=null)
			this.remove(areaRect);
		if (formulaText!= null)
			this.remove(formulaText);
		double x = dist/spd; 
		double y = spd; 
		int w = (int)(x / (xTickMax - xTickMin) * (double)xAxisLength); 
		int h = (int)(y / (yTickMax - yTickMin) * (double)yAxisLength);
		
//		Point[] rectangle = {new Point(0,0), new Point(0,-h), new Point(w,-h), new Point(w, 0), new Point(0, 0)};
		
		areaRect = new Rect(w-4, h);
		areaRect.setFillColor(new Color(175,238,238,64));
		areaRect.setStrokeColor(new Color(175,238,238,125));
//		areaRect.setStroke(new Color(175,238,238,125), 2);
		this.add(areaRect,orgX+2,(orgY-h-2));
		
		if (showFormula) {
			String formattedSpeed = NumberFormat.getFormat(".0").format(spd);
			String formattedTime = NumberFormat.getFormat(".0").format(dist/spd);
			String formattedDist = NumberFormat.getFormat(".0").format(dist);
			if (showFormulaAnswer)
				formulaText = new Text("D=S*T="+formattedSpeed+"(m/s)*"+formattedTime+"(s)="+formattedDist+"(m)");
			else
				formulaText = new Text("D=S*T");
				
			this.add(formulaText, (int) (orgX+(xAxisLength-formulaText.getWidth())/2), orgY-(h-2)/2);
		}
	}
	
	public void drawTargetSlope(double spd) {
		//clean existed slope lines
		if (targetSlopeLine!= null)
			this.remove(targetSlopeLine);
		
		double x = yTickMax/spd; 
		double y = yTickMax;
		int chartx = (int)(x / (xTickMax - xTickMin) * (double)xAxisLength) + orgX;
		int charty = (int)((yTickMax - y) / (yTickMax - yTickMin) * (double)yAxisLength) + AXIS_PADDING + YUNIT_PADDING;
		targetSlopeLine = new Line(orgX, orgY, chartx, charty);
		targetSlopeLine.setStrokeStyle("LONGDASH");
		targetSlopeLine.setStroke(Color.GRAY, 1);
		this.add(targetSlopeLine, 0, 0);
	}
	
	public void cleanTargetSlope() {
		//clean existed slope lines
		if (targetSlopeLine!= null)
			this.remove(targetSlopeLine);
	}
	
	public void drawSlope(double dist, double spd) {
		double time = dist/spd;
		//clean existed slope lines
		for (Line line : slopeLines) {
			if (line!=null)
				this.remove(line);
		}
		if (formulaText!= null)
			this.remove(formulaText);
		
		double x = time; 
		double y = dist;
		int chartx = (int)(x / (xTickMax - xTickMin) * (double)xAxisLength) + orgX;
		int charty = (int)((yTickMax - y) / (yTickMax - yTickMin) * (double)yAxisLength) + AXIS_PADDING + YUNIT_PADDING;
		slopeLines[1] = new Line(orgX, orgY, chartx, charty);
		slopeLines[1].setStroke(Color.GRAY, 1);
		this.add(slopeLines[1], 0, 0);
		
		slopeLines[2] = new Line(chartx, orgY, chartx, charty);
		slopeLines[2].setStrokeStyle("LONGDASH");
		slopeLines[2].setStroke(Color.RED, 1);
		this.add(slopeLines[2], 0, 0);
		
		slopeLines[3] = new Line(orgX, charty, chartx, charty);
		slopeLines[3].setStroke(Color.RED, 1);
		slopeLines[3].setStrokeStyle("LONGDASH");
		this.add(slopeLines[3], 0, 0);
		
		if (showFormula) {
			String formattedVelocity = NumberFormat.getFormat(".0").format(dist/time);
			String formattedTime = NumberFormat.getFormat(".0").format(time);
			String formattedDist = NumberFormat.getFormat(".0").format(dist);
			if (showFormulaAnswer)
				formulaText = new Text("S=D/T="+formattedDist+"(m)/"+formattedTime+"(s)="+formattedVelocity+"(m/s)");
			else
				formulaText = new Text("S=D/T");
				
	//		double posx = (10*xaxisLen/(xmax-xmin));
	//		double posy = ( spd*10 / (ymax - ymin) * (double)yaxisLen);
			chartx = Math.max((int)(chartx-formulaText.getWidth()), orgX+50);
			this.add(formulaText, (int)(chartx), (int)charty);
		}
		
	}
	
	public void drawBase() {
		Text xtext = new Text(xAxisLabelString);
		xtext.setFont(labelFont);
		xtext.setStrokeColor(Color.RED);
		
		Text ytext = new Text(yAxisLabelString);
		ytext.setFont(labelFont);
		ytext.setStrokeColor(Color.RED);
		

		Line xaxis = new Line(orgX-AXIS_SHORT_LINE, orgY, orgX+xAxisLength, orgY);
		Line yaxis = new Line(orgX, orgY+AXIS_SHORT_LINE, orgX, orgY-yAxisLength);
		
		this.add(yaxis, 0, 0);
		this.add(xaxis, 0, 0);
		
		Text xunitText = new Text("("+xTickUnitString+")");
		xunitText.setFont(tickFont);
		xunitText.setStrokeColor(Color.RED);
		this.add(xunitText, orgX+xAxisLength-20, orgY+AXIS_SHORT_LINE+charTickHeight*2);
		
		Text yunitText = new Text("("+yTickUnitString+")");
		yunitText.setFont(tickFont);
		yunitText.setStrokeColor(Color.RED);
		this.add(yunitText, (int) (orgX-xunitText.getWidth()+20), AXIS_PADDING+YUNIT_PADDING-3);
		
		this.add(xtext, (int)(orgX + (this.xAxisLength-xtext.getWidth())/2), (int)(orgY+AXIS_SHORT_LINE+charLabelHeight+charTickHeight));
		this.addYLegend(yAxisLabelString, AXIS_PADDING, (int)(height-charLabelHeight*yAxisLabelString.length())/2);
		
		// ticks start from left and go right
		int tickDist = xAxisLength / xTickCount;
		
		for (int i = 1; i <= xTickCount; i++)
		{
			int xPos = tickDist * i + orgX;
			Line tickLine = new Line(xPos, orgY +  AXIS_SHORT_LINE,
					                 xPos, AXIS_PADDING+YUNIT_PADDING);
			tickLine.setStrokeColor(Color.GRAY);
			this.add(tickLine, 0, 0);
			//String strNum = NumberFormat.getFormat(".0s").format((xmin+(xmax-xmin)/numXTicks*i));
			//Text tickNum = new Text(strNum);
			
			Text tickNum = new Text(""+(int)(xTickMin+(xTickMax-xTickMin)/xTickCount*i));
			tickNum.setFont(tickFont);
			this.add(tickNum, (int)(xPos-tickNum.getWidth()/2), (int)(orgY+AXIS_SHORT_LINE+tickNum.getHeight()));
			
		}
		
		
		// ticks start from top and go down
		tickDist = yAxisLength / yTickCount;
		for (int i = 1; i <= xTickCount; i++)
		{
			int yPos = orgY - (tickDist * i);
			Line  tickLine = new Line(orgX - AXIS_SHORT_LINE, yPos,
					                  orgX + xAxisLength, yPos);
			tickLine.setStrokeColor(Color.GRAY);
			this.add(tickLine, 0, 0);
			Text tickNum = new Text(""+(int)((yTickMin+(yTickMax-yTickMin)/yTickCount*i)));
			tickNum.setFont(tickFont);
			this.add(tickNum, (int)(orgX-AXIS_SHORT_LINE-tickNum.getWidth()+5), (int)(yPos+(tickNum.getHeight()/2)+2) );
		}
		
	}
	
	public void addYLegend(String yAxisLabel, int x, int y) {
		for (int i = 0; i < yAxisLabel.length(); i++) {
			Text piece = new Text(yAxisLabel.substring(i, i+1));
			piece.setFont(labelFont);
			piece.setStrokeColor(Color.RED);
			this.add(piece, x, (int)(y+i*piece.getHeight()));
		}
	}
	
	/**
	 * Start a new simulation graph
	 */
	public void beginSimulation(String str) {
		lastPnt = null;
		this.clearLines();
	}
	
	/**
	 * Add a piont to the graph.
	 * 
	 * @param x - x location.
	 * @param y - y location.
	 */
	public void addPoint(double x, double y)
	{
		int chartx = (int)(x / (xTickMax - xTickMin) * (double)xAxisLength) + orgX;
		int charty = (int)((yTickMax - y) / (yTickMax - yTickMin) * (double)yAxisLength) + AXIS_PADDING + YUNIT_PADDING;
		
		ChartPoint newPnt = new ChartPoint(chartx, charty);
		
		if (lastPnt != null)
		{
			Line chartLine = new Line(lastPnt.x, lastPnt.y, newPnt.x, newPnt.y);
			chartLine.setStroke(currentColor, 3);
			this.add(chartLine, 0, 0);
			legendLine.add(chartLine);
		}
	
		lastPnt = newPnt;
	}
	
	/**
	 * Add a area to the graph.
	 * 
	 * @param x - x location.
	 * @param y - y location.
	 */
	public void addArea(double x, double y)
	{
		int chartx = (int)(x / (xTickMax - xTickMin) * (double)xAxisLength) + orgX;
		int charty = (int)((yTickMax - y) / (yTickMax - yTickMin) * (double)yAxisLength) + AXIS_PADDING + YUNIT_PADDING;
		
		ChartPoint newPnt = new ChartPoint(chartx, charty);
		
		if (lastPnt != null)
		{
			Line chartLine = new Line(lastPnt.x, lastPnt.y, newPnt.x, newPnt.y);
			chartLine.setStroke(currentColor, 3);
			this.add(chartLine, 0, 0);
			
			if (areaLineA!=null) this.remove(areaLineA);
			areaLineA = new Line(newPnt.x, newPnt.y, newPnt.x, orgY);
			areaLineA.setStroke(currentColor, 3);
			this.add(areaLineA, 0, 0);
			
			if (areaLineB!=null) this.remove(areaLineB);
			areaLineB = new Line(orgX, orgY, newPnt.x, orgY);
			areaLineB.setStroke(currentColor, 3);
			this.add(areaLineB, 0, 0);
			
			if (areaLineC==null) {
				areaLineC = new Line(orgX, newPnt.y, orgX, orgY);
				areaLineC.setStroke(currentColor, 3);
				this.add(areaLineC, 0, 0);
			}
			
			legendLine.add(chartLine);
		}
	
		lastPnt = newPnt;
	}
	
	/**
	 * Internal class to easily pass around plot point information.
	 */
	private class ChartPoint
	{
		public int x;
		public int y;
		
		public ChartPoint(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}

	@Override
	public void simEndStatus(SimulationEndData simEndData) {
		if (!this.isVisible()) return;
	}

	@Override
	public void simInitalStatus(SimulationInitialData simInitData) {
		if (!this.isVisible()) return;
		String formattedVelocity = NumberFormat.getFormat("00.0").format(simInitData.speedLimited);
		String str = "S: "+(int)simInitData.totalDistance+"(m) V:"+formattedVelocity+"(m/s)";
		beginSimulation(str);
		
		if (this.mode == Utils.VelocityTime){
			//drawArea(simInitData.totalDistance, simInitData.speedLimited);
			addPoint(0, simInitData.speedLimited);
		} else {
			//drawSlope(simInitData.totalDistance, simInitData.speedLimited);
			addPoint(0.0, 0.0);
		}
		
	}

	@Override
	public void simStatus(SimulationData simData) {
		if (!this.isVisible()) return;
		
		if (simData.lightState == Utils.GREEN_LIGHT)
			this.currentColor = Color.GREEN;
		else if (simData.lightState == Utils.YELLOW_LIGHT)
			this.currentColor = Color.YELLOW;
		else if (simData.lightState == Utils.RED_LIGHT)
			this.currentColor = Color.RED;
		
		if (this.mode == Utils.VelocityTime){
			addArea((double)simData.curTime, simData.curSpeed);
		} else {
			addPoint((double)simData.curTime, simData.curTraveledDistance);
		}
	}
	
	public void clearLines() {
		for (GraphicObject obj : graphicObjects) {
			this.remove(obj);
		}
		graphicObjects.clear();
		for (GraphicObject obj : legendLine) {
			this.remove(obj);
		}
		legendLine.clear();
		
		if (areaLineA!=null)
			this.remove(areaLineA);
		areaLineA = null;
		if (areaLineB!=null)
			this.remove(areaLineB);
		areaLineB = null;
		if (areaLineC!=null)
			this.remove(areaLineC);
		areaLineC = null;
		
		lastPnt = null;
	}
	
}
