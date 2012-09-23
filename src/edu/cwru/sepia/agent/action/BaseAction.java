package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;
import edu.cwru.sepia.environment.model.state.State.StateView;

public interface BaseAction {
	
	public Condition getPreConditions();
	
	public Condition getPostConditions();
	
	public int getDuration();
	
	public void updateDuration(int duration) throws Exception;
	
	public Action getAction(int playernum, StateView state);
	
	public int getStartTime();
	
	public int getEndTime();
	
	public void setStartTime(int time);
	
	public void setEndTime(int time);

	public String getUnitType();
	
	public void setUnitId(int unitid);
	
	public int getUnitId();

}
