package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.State.StateView;

/*
 * This agent uses an instance of the SRS class to do online planning for 
 * resource collection.
 */

public class ResourceCollectionAgent extends Agent {

	private static final long serialVersionUID = 1596635469778909387L;
	
	private SRS planner;
	private Map<Integer, List<BaseAction>> plan = null;
	
	// this number controls how often the agent will replan
	private int replanTime = 5;
	
	private Condition goal;
	
	/* Keeps track of actions in progress
	 * The first key is the list the action came from
	 * The second key is the total duration so far of that action
	 */
	private Map<Integer, Integer> inProgress;
	
	// stores the townhall's id
	private Integer townhall = null;
	
	// stores the peasants that are available for work
	private List<Integer> freePeasants;

	public ResourceCollectionAgent(int playernum) {
		super(playernum);
		planner = new SRS(this.playernum);
		// Extract the Goal conditions from the xml
		//Preferences prefs = Preferences.userRoot().node("edu").node("cwru").node("sepia").node("ModelParameters");
		goal = new Condition();
		// this will be used by the convertId's method
		inProgress = new HashMap<Integer, Integer>();
		
		freePeasants = new LinkedList<Integer>();
	}

	@Override
	public Map<Integer, Action> initialStep(StateView newstate,
			HistoryView statehistory) {
		System.out.println("In initial step");
		// get initial plan
		plan = SRS.getPlan(newstate, statehistory, goal);
		
		System.out.println("# of lists in plan: " + plan.size());
		
		// find out the townhall and add all of the peasants to the free list
		List<Integer> unitIds = newstate.getUnitIds(this.playernum);
		for(Integer id : unitIds)
		{
			String unitName = newstate.getUnit(id).getTemplateView().getName();
			if(unitName.equals("TownHall"))
			{
				townhall = id;
			}
			else if(unitName.equals("Peasant"))
			{
				freePeasants.add(id);
			}
		}

		// increment the number of steps;
		return convertPlan2Actions(newstate);
	}

	@Override
	public Map<Integer, Action> middleStep(StateView newstate,
			HistoryView statehistory) {
		if(newstate.getTurnNumber() % replanTime == 0)
		{
			plan = SRS.getPlan(newstate, statehistory, goal);
		}
		
		return convertPlan2Actions(newstate);
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
	
	/*
	 * This method takes the plan from the SRS and converts it to
	 * SEPIA's actions
	 */
	private Map<Integer, Action> convertPlan2Actions(StateView state)
	{
		System.out.println("In convertPlan2Actions");
		Map<Integer, Action> actionMap = new HashMap<Integer, Action>();
		// for each piece get the list of actions
		for(Integer i : plan.keySet())
		{
			// if this list's action is still being executed then
			// don't tell the peasants to do anything else
			if(inProgress.containsKey(i))
			{
				System.out.println("List " + i + " is in progress");
				continue;
			}
			

			List<BaseAction> actions = plan.get(i);
			// if there are actions and the preconditions are satisfied
			if(actions.size() > 0 && preSat(actions.get(0), state))
			{
				
				System.out.println("List " + i + "'s action has the preconditions satisfied");
				// integer 0 will always be the townhall's actions
				if(i == 0)
				{
					System.out.println("This is a townhall action");
					actions.get(0).setPeasant(townhall);
					actionMap.put(townhall, actions.get(0).getAction());
				}
				else // this is a peasant list
				{
					System.out.println("This is a peasant action");
					Integer id = getAvailPeasant();
					if(id != null) // if there are available peasants
					{
						// have them do the action
						actions.get(0).setPeasant(id);
						actionMap.put(id, actions.get(0).getAction());
					}
				}
			}
		}
		return actionMap;
	}
	
	private boolean preSat(BaseAction action, StateView state)
	{
		Condition pre = action.getPreConditions();
		boolean result = true;
		result = result && (state.getResourceAmount(playernum, ResourceType.GOLD) >= pre.gold); // is there enough gold?
		result = result && (state.getResourceAmount(playernum, ResourceType.WOOD) >= pre.wood); // is there enough wood?
		result = result && (freePeasants.size() >= pre.peasant); // are there enough peasants?
		// might have to subtract the number of peasants currently created
		result = result && (state.getSupplyCap(playernum) >= pre.supply); // are there enough farms?
		return result;
	}
	
	private Integer getAvailPeasant()
	{
		if(!freePeasants.isEmpty())
			return freePeasants.remove(0);
		return null;
	}
}
