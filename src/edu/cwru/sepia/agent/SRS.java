package edu.cwru.sepia.agent;

import java.util.List;
import java.util.Map;

import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.State.StateView;

/*
 * Calls the MEA with the original goal conditions 
 * and with added renewable and borrowable resources
 */

public final class SRS {
	
	private SRS()
	{
		
	}
	
	/* Given information about the game state this will return a mapping of peasant IDs to list of actions
	 * Townhall will be ID 0
	 * The agent will execute each action so long as the precondtions are met
	 */
	public static Map<Integer, List<BaseAction>> getPlan(StateView state, HistoryView statehistory, Condition Goal)
	{
		return null;
	}

}
