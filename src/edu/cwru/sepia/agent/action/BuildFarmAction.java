package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;

public class BuildFarmAction implements BaseAction {
	
	private int startTime;
	private int endTime;
	
	private final static Condition pre = new Condition(500,250,1,0,1);
	private final static Condition post = new Condition(0,0,1,4,1);
	private static int farmLocX = 1;
	private static int farmLocY = 1;
	// set to a default value, but this should probably be changed to a 
	// more reasonable estimate

	private static int duration = 1;
	
	int unitid;

	@Override
	public Condition getPreConditions() {
		return pre;
	}

	@Override
	public Condition getPostConditions() {
		return post;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public Action getAction(int playernum, StateView state) {
		UnitView unit = state.getUnit(unitid);
		//int[] space = state.getClosestOpenPosition(unit.getXPosition(), unit.getYPosition());
		int[] space = state.getClosestOpenPosition(farmLocX, farmLocY);
		if (state.getYExtent() <= farmLocY)
		{
			farmLocX += 2;
			farmLocY = 1;
		}
		else
		{
			farmLocY++;
		}
		int templateID = state.getTemplate(playernum, "Farm").getID();
		return Action.createCompoundBuild(unitid, templateID, space[0], space[1]);
	}

	@Override
	public void updateDuration(int duration) throws Exception{
		if(duration > 0)
			BuildFarmAction.duration = duration;
		else
			throw new Exception("Duration out of bounds!!");
	}
	
	@Override
	public int getStartTime()
	{
		return startTime;
	}
	
	@Override
	public int getEndTime()
	{
		return endTime;
	}
	
	@Override
	public void setStartTime(int time)
	{
		startTime = time;
	}
	
	@Override
	public void setEndTime(int time)
	{
		endTime = time;
	}

	@Override
	public String getUnitType() {
		return "Peasant";
	}
	
	@Override
	public String toString(){
		return "Build a Farm with " + unitid;
	}

	@Override
	public void setUnitId(int unitid) {
		this.unitid = unitid;
	}

	@Override
	public int getUnitId() {
		return unitid;
	}
	
	@Override
	public int compareTo(BaseAction act) 
	{
		return this.getStartTime() - act.getStartTime();
	}
}
