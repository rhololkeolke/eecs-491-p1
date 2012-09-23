package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;
import edu.cwru.sepia.environment.model.state.State.StateView;

public final class BuildPeasantAction implements BaseAction {
	
	private int startTime;
	private int endTime;
	
	private final static Condition pre = new Condition(400,0,0,1);
	private final static Condition post = new Condition(0,0,1,0);
	
	// set to a default value, but this should probably be changed to a 
	// more reasonable estimate

	private static int duration = 1;
	
	private int unitid;

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
		int templateID = state.getTemplate(playernum, "Peasant").getID();
		return Action.createCompoundProduction(unitid, templateID);
	}

	@Override
	public void updateDuration(int duration) throws Exception{
		if(duration > 0)
			BuildPeasantAction.duration = duration;
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

	public String getUnitType() {
		return "TownHall";
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
	public String toString(){
		return "Build a Peasant with " + unitid;
	}
}
