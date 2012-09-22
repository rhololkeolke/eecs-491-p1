package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.State.StateView;

/*
 * This agent uses an instance of the SRS class to do online planning for 
 * resource collection.
 */

public class ResourceCollectionAgent extends Agent {

	private static final long serialVersionUID = 1596635469778909387L;

	public ResourceCollectionAgent(int playernum) {
		super(playernum);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Integer, Action> initialStep(StateView newstate,
			HistoryView statehistory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Action> middleStep(StateView newstate,
			HistoryView statehistory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void terminalStep(StateView newstate, HistoryView statehistory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePlayerData(OutputStream os) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadPlayerData(InputStream is) {
		// TODO Auto-generated method stub

	}

}
