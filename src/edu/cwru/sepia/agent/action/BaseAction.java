package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

/*
 * Base Action Class
 */

public abstract class BaseAction {
	Condition precondtion;
	Condition postcondition;
	
	// smallest duration is 1
	int duration;
	
	public abstract Action getAction();
}
