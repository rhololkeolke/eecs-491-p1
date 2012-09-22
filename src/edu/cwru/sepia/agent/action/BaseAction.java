package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

public interface BaseAction {
	
	public Condition getPreConditions();
	
	public Condition getPostConditions();
	
	public int getDuration();
	
	public void updateDuration(int duration);
	
	public Action getAction();

}
