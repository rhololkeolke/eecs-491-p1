package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

public interface BaseAction {
	
	public Condition getPreConditions();
	
	public Condition getPostConditions();
	
	public int getDuration();
	
	public void updateDuration(int duration) throws Exception;
	
	public Action getAction();
	
	public int getStartTime();
	
	public int getEndTime();
	
	public void setStartTime(int time);
	
	public void setEndTime(int time);

	public String getUnitType();

}
