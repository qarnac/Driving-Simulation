package com.csusm.cs.earth.client;

import java.util.ArrayList;
import java.util.Date;
import com.csusm.cs.earth.client.Utils.GeoLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * This class handles the position of the car as it drives towards an intersection.
 */
public class DrivingSimulator 
{
	public enum State {READY, DRIVING, CROSSING, PAUSE, DONE};
	
	// handle to Google Earth wrapper
	private int doneState;
	private boolean hasTrafficLight = false;
	private int lightState;
	
	private GEWrapper gewrapper;
	private GeoLocation startLoc;
	private GeoLocation intersectionLoc;
	private GeoLocation lastLoc;
	private double bearing;
	
	private double speed;
	private double totalDistance;
	private double totalDistanceDrived;
	
	private long startTime;
	private long lastTime;
	private long totalTimeUsed;
	private long time;
	
	private long greenLightDurationTime;
	private long yellowLightDurationTime;
	
	private State simState;
	
	private double periodSinceLastUpdate;
	
	// list of objects interested in simluation status
	private ArrayList<SimulationListener> simListeners;

	/**
	 * Constructor.
	 * 
	 * @param gew - A handle to the GE wrapper.
	 * @param startLoc - The location to start from.
	 * @param endLoc - The destination location.
	 * @param bearing - Direction of travel. 
	 * @param speed - The speed of travel.
	 */
	public DrivingSimulator(GEWrapper gew)
	{
		this.gewrapper = gew;
		this.simListeners = new ArrayList<SimulationListener>();

		simState = State.DONE;
		hasTrafficLight = false;
		
		if (hasTrafficLight) {
			int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
			this.intersectionLoc = Utils.INTERSECTIONS[interIdx].loc;
			this.bearing = Utils.INTERSECTIONS[interIdx].drivingBearing;
		} else {
			int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
			this.intersectionLoc = Utils.INTERSECTIONS[interIdx].loc;
			this.bearing = Utils.INTERSECTIONS[interIdx].drivingBearing;
		}
		this.greenLightDurationTime = Utils.GREEN_LIGHT_DURATION;
		this.yellowLightDurationTime = Utils.YELLOW_LIGHT_DURATION;
		this.totalDistance = Utils.DISTANCE;
		this.speed = Utils.SPEED;
	}
	
	/**
	 * Add an object to be notified of status updates.
	 * 
	 * @param listener - The listener to add.
	 */
	public void addSimListener(SimulationListener listener)
	{
		simListeners.add(listener);
	}
	
	public void resetLoc() {
		// move camera 100m from intersection
		if (hasTrafficLight) {
			moveDistanceFromDestination(100);
			int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
			this.intersectionLoc = Utils.INTERSECTIONS[interIdx].loc;
			this.bearing = Utils.INTERSECTIONS[interIdx].drivingBearing;
		} else {
			moveDistanceFromDestination(-10);
			int interIdx = Utils.INTERSECTION_MAP.get("Washington DC");
			this.intersectionLoc = Utils.INTERSECTIONS[interIdx].loc;
			this.bearing = Utils.INTERSECTIONS[interIdx].drivingBearing;
		}
		gewrapper.setGreenLight();
		totalDistanceDrived = 0.0;
		totalTimeUsed = 0;
	}
	
	/**
	 * Simulate!
	 */
	public void go()
	{
		this.simState = State.READY;
		this.lightState = Utils.GREEN_LIGHT;
		this.lastLoc = startLoc;
		
		totalDistanceDrived = 0.0;
		totalTimeUsed = 0;
		
		// initial the state
		this.periodSinceLastUpdate = 0.0;
		for (SimulationListener listener : simListeners)
			listener.simInitalStatus(new SimulationInitialData(speed, totalDistance));

		startTime = new Date().getTime();
		lastTime = startTime;
		
		simState = State.DRIVING;
		recursiveGo();
	}
	
	/**
	 * This method is called internally to 
	 * move the car position to the proper 
	 * location at the current time.
	 */
	private void recursiveGo()
	{
		long deltaTimeMS = new Date().getTime() - lastTime;
		lastTime = new Date().getTime();
		
		if (simState == State.DRIVING)
		{
			drivingSim(deltaTimeMS); 
			totalTimeUsed += deltaTimeMS;
			
			this.periodSinceLastUpdate += deltaTimeMS;
			if (this.periodSinceLastUpdate >= 200.0) {
				// inquiring minds want to know
				for (SimulationListener listener : simListeners) {
					listener.simStatus(new SimulationData(this.lastLoc, this.simState, totalTimeUsed/1000.0, this.speed, this.totalDistanceDrived, this.lightState));
				}
				this.periodSinceLastUpdate = 0;
			}
			
			// keep going until we're done.
			DeferredCommand.addCommand(new Command() 
			{
				public void execute() 
				{
					recursiveGo();
				}
			});
			
		} else if (simState == State.DONE) {
			// end notify 
			//System.out.println("Once?"); //anser is right
			for (SimulationListener listener : simListeners)
				listener.simEndStatus(new SimulationEndData(doneState));
		} else {
		}
	}
	
	/**
	 * This is the normal simulation routine.
	 * 
	 * @param deltaTimeMS - The amount of time that has passed since the start of the simulation.
	 * @return - The distance remaining.
	 */
	private double drivingSim(long deltaTimeMS)
	{
//		this.speed += this.accSpeed * deltaTimeMS/1000.0;
		//System.out.println("Speed:" + this.speed);
		double distTraveled = ((double)deltaTimeMS / 1000.0) * speed;
		lastLoc = Utils.rangeBearingCalc(lastLoc, bearing, distTraveled/1000.0);
		gewrapper.moveToLoc(lastLoc, bearing);
		totalDistanceDrived += distTraveled;
		
		if (hasTrafficLight) {
			if ( totalTimeUsed > greenLightDurationTime) {
				if ( totalTimeUsed <= greenLightDurationTime+yellowLightDurationTime) {
					if (!gewrapper.isYellowLight()) {
						this.lightState = Utils.YELLOW_LIGHT;
						gewrapper.setYellowLight();
					}
				} else {
					if (!gewrapper.isRedLight()) {
						this.lightState = Utils.RED_LIGHT;
						gewrapper.setRedLight();
					}
				}
			} else {
				if (!gewrapper.isGreenLight()) {
					this.lightState = Utils.GREEN_LIGHT;
					gewrapper.setGreenLight();
				}
			}
			
			if (totalDistanceDrived >= totalDistance)
			{
				simState = State.DONE;
				if ( totalTimeUsed <= greenLightDurationTime) {
					doneState = Utils.BEFORE;
				} else if ( totalTimeUsed <= greenLightDurationTime + yellowLightDurationTime ){
					doneState = Utils.WHEN;
				} else {
					doneState = Utils.AFTER;
				}
			}
		} else {
			if (totalDistanceDrived >= totalDistance)
			{
				simState = State.DONE;
				doneState = Utils.NORMAL_STOP;
			}
		}
			
		// return remaining distance
		return distTraveled;
//		return Math.max(0.0, (totalDistKM - distTraveled));
	}
	
	/*
	 * return speed
	 * unit: m/s
	 */
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/*
	 * return distance
	 * unit: m
	 */
	public double getDistance() {
		return totalDistance;
	}
	
	public void setDistance(double distance) {
		this.totalDistance = distance;
	}
	
	public void moveDistanceFromDestination(double distance) {
		this.startLoc = Utils.rangeBearingCalc(this.intersectionLoc, bearing, -distance/1000.0);
		this.moveToLoc(this.startLoc, this.bearing);
	}

	public void moveToLoc(GeoLocation loc, double bearing) {
		simState = State.DONE;
		if (gewrapper.isEnable())
			gewrapper.moveToLoc(loc, bearing);
	}
	
	/*
	 * time
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	public long getTime() {
		return this.time; 
	}
	
	public void setGreenLightDurationTime(long time) {
		this.greenLightDurationTime = time;
	}
	
	public long getGreenLightDurationTime() {
		return this.greenLightDurationTime;
	}
	
	public void setHasTraffic(boolean b) {
		this.hasTrafficLight = b;
	}
	
	public void stop() {
		simState = State.DONE;
	}

	public boolean isPaused() {
		return simState == State.PAUSE;
	}
	
	public void pause() {
		simState = State.PAUSE;
	}
	
	public void resume() {
		if ( simState == State.PAUSE)
			simState = State.DRIVING;
	}
	
	public boolean isIdle() {
		return simState == State.DONE;
	}
	
	public boolean isRunning() {
		return simState == State.DRIVING;
	}

	public State getSimState() {
		return simState;
	}

	public void setSimState(State simState) {
		this.simState = simState;
	}

	public class SimulationData {
		GeoLocation location;
		State simState;
		double curTime; // sec
		double curSpeed; // M/S
		double curTraveledDistance; // unit: M
		int lightState;
		
		public SimulationData(GeoLocation location, State simState, double curTime, 
							  double curSpeed, double curTraveledDistance, int lightState) {
			this.location = location;
			this.simState = simState;
			this.curTime = curTime;
			this.curSpeed = curSpeed;
			this.lightState = lightState;
			this.curTraveledDistance = curTraveledDistance;
		}
		public void print() {
			System.out.println("curTime:"+curTime + " curSpeed:"+curSpeed + " curDist:" + curTraveledDistance);
		}

	}
	
	public class SimulationInitialData {
		double speedLimited; // M/S
		double totalDistance; // M
		
		public SimulationInitialData(double speedLimited, double totalDistance) {
			this.speedLimited = speedLimited;
			this.totalDistance = totalDistance;
		}
		public void print() {
			System.out.println("SpeedLimit:"+speedLimited + " totalDistance:"+totalDistance);
		}
	}
	
	public class SimulationEndData {
		int status;
		public SimulationEndData(int status) {
			this.status = status;
		}
	}
	
	/**
	 * An interface to define a callback for intermediate simulation events.
	 */
	public interface SimulationListener
	{
		void simInitalStatus(SimulationInitialData simInitData);
		void simEndStatus(SimulationEndData simEndData);
		void simStatus(SimulationData simData);
	}
	
}
