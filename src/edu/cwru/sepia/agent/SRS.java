package edu.cwru.sepia.agent;

import java.util.Collections;
import java.util.List;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.environment.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.ResourceNode.Type;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;
import edu.cwru.sepia.util.Direction;

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
	public static List<BaseAction> getPlan(StateView state, Condition goal, int playerNum)
	{
		Condition end = new Condition();
		end.gold = goal.gold;
		end.peasant = goal.peasant;
		end.supply = goal.supply;
		end.townhall = goal.townhall;
		end.wood = goal.wood;
		
		Condition start = getCurrentCondition(state, playerNum);
		Pair<List<BaseAction>, Condition>  basePair = MEA.plan(start, end);
		List<BaseAction> basePlan = basePair.first;
		Scheduler.schedulePlan(basePlan, start);
		
		Condition intermediate = new Condition();
		intermediate.peasant = start.peasant + 1;
		Pair<List<BaseAction>,Condition> pair = MEA.plan(start, intermediate);
		List<BaseAction> intermediatePlan = pair.first;
		Condition afterIntermediate = pair.second;
		Scheduler.schedulePlan(intermediatePlan, start);
		
		end.peasant=start.peasant+1;
		List<BaseAction> afterIntermediatePlan = MEA.plan(afterIntermediate, end).first;
		Scheduler.schedulePlan(afterIntermediatePlan, afterIntermediate);
		
		List<BaseAction> peasantPlan = concat(intermediatePlan, afterIntermediatePlan);
		Scheduler.schedulePlan(peasantPlan, start);
		
		int baseTime = (int) (planTime(basePlan)*(1+0.1*(end.peasant-1)));
		int peasantTime = (int) (planTime(peasantPlan)*(1+0.1*end.peasant));
		
		Collections.sort(basePlan);
		Collections.sort(peasantPlan);
		
		if (baseTime < peasantTime)
		{
			Collections.sort(basePlan);
			return basePlan;
		}
		else
		{
			Collections.sort(peasantPlan);
			return peasantPlan;
		}
	}

	private static Condition getCurrentCondition(StateView state, int playerNum)
	{
		Condition current = new Condition();
		
		current.gold = state.getResourceAmount(playerNum, ResourceType.GOLD);
		current.wood = state.getResourceAmount(playerNum, ResourceType.WOOD);
		
		
		List<UnitView> units = state.getUnits(playerNum);
		for (UnitView unit: units)
		{
			if (unit.getTemplateView().getName().equalsIgnoreCase("peasant"))
			{
				current.peasant += 1;
				current.supply -= 1;
				Action act = unit.getCurrentDurativeAction();
				if (act != null)
				{
					if (act.getType() == ActionType.COMPOUNDGATHER)
					{
						TargetedAction targetAct = (TargetedAction) act;
						if (state.getResourceNode(targetAct.getTargetId()).getType() == Type.GOLD_MINE)
						{
							current.gold += 100;
						}
						else if (state.getResourceNode(targetAct.getTargetId()).getType() == Type.TREE)
						{
							current.wood += 100;
						}
					}
					else if(act.getType() == ActionType.COMPOUNDDEPOSIT)
					{
						if(unit.getCargoType() == ResourceType.GOLD)
						{
							current.gold += 100;
						}
						else
						{
							current.wood += 100;
						}
					}
					else if(act.getType() == ActionType.PRIMITIVEGATHER)
					{
						DirectedAction dAct = (DirectedAction) act;
						Type t = resourceAt(unit,dAct.getDirection(), state);
						if (t == Type.GOLD_MINE)
						{
							current.gold += 100;
						}
						else if (t == Type.TREE)
						{
							current.wood += 100;
						}
					}
					else if (act.getType() == ActionType.COMPOUNDBUILD || act.getType() == ActionType.PRIMITIVEBUILD)
					{
						current.supply += 4;
					}
				}
			}
			else if (unit.getTemplateView().getName().equalsIgnoreCase("townhall"))
			{
				current.supply += 3;
			}
			else if (unit.getTemplateView().getName().equalsIgnoreCase("farm"))
			{
				current.supply += 4;
			}
				
		}
		return current;
	}
	
	private static Type resourceAt(UnitView unit, Direction dir, StateView state)
	{
		int targetX = unit.getXPosition() + dir.xComponent();
		int targetY = unit.getYPosition() + dir.yComponent();
		
		List<ResourceView> gold = state.getResourceNodes(Type.GOLD_MINE);
		List<ResourceView> wood = state.getResourceNodes(Type.TREE);
		
		for (ResourceView g:gold)
		{
			if (g.getXPosition()==targetX && g.getYPosition()==targetY)
			{
				return Type.GOLD_MINE;
			}
		}
		
		for (ResourceView w:wood)
		{
			if (w.getXPosition()==targetX && w.getYPosition()==targetY)
			{
				return Type.TREE;
			}
		}
		
		return null;
	}
	
	private static int planTime(List<BaseAction> plan)
	{
		int time = 0;
		for (BaseAction act : plan)
		{
			if (act.getEndTime() > time)
			{
				time = act.getEndTime();
			}
		}
		return time;
	}
	
	private static List<BaseAction> concat(List<BaseAction> plan1, List<BaseAction> plan2)
	{
		int endTime = plan1.get(plan1.size()-1).getEndTime();
		
		for (BaseAction act : plan2)
		{
			act.setStartTime(act.getStartTime()+endTime);
			act.setEndTime(act.getEndTime()+endTime);
			plan1.add(act);
		}
		
		return plan1;
	}
}
