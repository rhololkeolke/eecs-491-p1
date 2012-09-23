package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.agent.action.CollectGoldAction;
import edu.cwru.sepia.agent.action.CollectWoodAction;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.State.StateView;

/*
 * This agent uses an instance of the SRS class to do online planning for 
 * resource collection.
 */

public class ResourceCollectionAgent extends Agent {

	private static final long serialVersionUID = 1596635469778909387L;
	
	private SRS planner;
	private List<BaseAction> plan = null;
	
	// this number controls how often the agent will replan
	private int replanTime = 10;
	
	private Condition goal;
	
	/* Keeps track of actions in progress
	 * The first key is the list the action came from
	 * The second key is the total duration so far of that action
	 */
	private List<Pair<BaseAction,Integer>> inProgress;
	
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
		inProgress = new ArrayList<Pair<BaseAction, Integer>>();
		
		freePeasants = new LinkedList<Integer>();
	}

	@Override
	public Map<Integer, Action> initialStep(StateView newstate,
			HistoryView statehistory) {
		System.out.println("In initial step");
		// get initial plan
		plan = SRS.getPlan(newstate, statehistory, goal);
		
		System.out.println("# of actions in plan: " + plan.size());
		
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
		
		// add the resources to the Collect Actions
		int[] spaces = new int[2];
		spaces[0] = newstate.getUnit(townhall).getXPosition();
		spaces[1] = newstate.getUnit(townhall).getYPosition();
		CollectGoldAction goldAction = new CollectGoldAction();
		CollectWoodAction woodAction = new CollectWoodAction();
		List<Integer> resourceIds = newstate.getAllResourceIds();
		for(Integer id : resourceIds)
		{
			ResourceView resource = newstate.getResourceNode(id);
			if(resource.getType() == ResourceNode.Type.GOLD_MINE)
			{
				goldAction.addResource(id, Math.max(Math.abs(resource.getXPosition()-spaces[0]), Math.abs(resource.getYPosition()-spaces[1])));
			}
			else
			{
				woodAction.addResource(id, Math.max(Math.abs(resource.getXPosition()-spaces[0]), Math.abs(resource.getYPosition()-spaces[1])));
			}
		}

		// increment the number of steps;
		return convertPlan2Actions(newstate);
	}

	@Override
	public Map<Integer, Action> middleStep(StateView newstate,
			HistoryView statehistory) {
		System.out.println("In middleStep");
		System.out.println("# of actions in plan: " + plan.size());
		System.out.println("\n");
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

		while(!plan.isEmpty())
		{
			System.out.println("Action " + plan.get(0).getClass().getName());
			System.out.println("\tPreconditions Met: " + preSat(plan.get(0), state));
			if(preSat(plan.get(0), state))
			{
				System.out.println("\tUnit Type: " + plan.get(0).getUnitType());
				if(plan.get(0).getUnitType().equals("Peasant"))
				{
					// grab the id of the peasant this will be assigned to
					Integer id = getAvailPeasant();
					// remove this action from the plan
					BaseAction action = plan.remove(0);
					// set the unitID in the action
					action.setUnitId(id);
					// add the action to the actions to be executed this turn
					actionMap.put(id, action.getAction(playernum, state));
					// add the action to the list of actions in progress
					inProgress.add(new Pair<BaseAction, Integer>(action, 0));
					System.out.println("actionMap: " + actionMap);
				}
				else
				{
					BaseAction action = plan.remove(0);
					action.setUnitId(townhall);
					actionMap.put(townhall, action.getAction(playernum, state));
					inProgress.add(new Pair<BaseAction, Integer>(action, 0));
					System.out.println("actionMap: " + actionMap);
				}
			}
			else // as soon as a single bad action is reached then quit trying to make a plan
			{
				break;
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
