package edu.cwru.sepia.agent;

import java.util.LinkedList;
import java.util.List;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.agent.action.BuildFarmAction;
import edu.cwru.sepia.agent.action.BuildPeasantAction;
import edu.cwru.sepia.agent.action.CollectGoldAction;
import edu.cwru.sepia.agent.action.CollectWoodAction;
import edu.cwru.sepia.environment.model.history.History.HistoryView;
import edu.cwru.sepia.environment.model.state.ResourceNode.Type;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;

/*
 * Determines all of the actions necessary for each goal condition
 */

public class MEA 
{

	public static List<BaseAction> plan(StateView state, Condition goal, int playerNum)
	{
		List<BaseAction> actions = new LinkedList<BaseAction>();
		Condition current = getCurrentCondition(state, playerNum);
		
		while(true)
		{
			if (current.gold < goal.gold)
			{
				CollectGoldAction act = new CollectGoldAction();
				Condition pre = act.getPreConditions();
				Condition post = act.getPostConditions();
				
				int needs = (int) Math.ceil((goal.gold - current.gold)/100);
				for (int x = 0; x < needs; x++)
				{
					if (actions.size() == 0)
					{
						act.setStartTime(0);
						act.setEndTime(act.getDuration());
					}
					else
					{
						act.setStartTime(actions.get(actions.size()-1).getEndTime());
						act.setEndTime(act.getStartTime() + act.getDuration());
					}
					current.gold -= pre.gold;
					current.wood -= pre.wood;
					current.peasant -= pre.peasant;
					current.supply -= pre.supply;
					actions.add(act);
					current.gold += post.gold;
					current.wood += post.wood;
					current.peasant += post.peasant;
					current.supply += post.supply;
				}
				continue;
			}
			if (current.wood < goal.wood)
			{
				CollectWoodAction act = new CollectWoodAction();
				Condition pre = act.getPreConditions();
				Condition post = act.getPostConditions();
				
				int needs = (int) Math.ceil((goal.wood - current.wood)/100);
				for (int x = 0; x < needs; x++)
				{
					if (actions.size() == 0)
					{
						act.setStartTime(0);
						act.setEndTime(act.getDuration());
					}
					else
					{
						act.setStartTime(actions.get(actions.size()-1).getEndTime());
						act.setEndTime(act.getStartTime() + act.getDuration());
					}
					current.gold -= pre.gold;
					current.wood -= pre.wood;
					current.peasant -= pre.peasant;
					current.supply -= pre.supply;
					actions.add(act);
					current.gold += post.gold;
					current.wood += post.wood;
					current.peasant += post.peasant;
					current.supply += post.supply;
				}
				continue;
			}
			if (current.peasant < goal.peasant)
			{
				BuildPeasantAction act = new BuildPeasantAction();
				Condition pre = act.getPreConditions();
				Condition post = act.getPostConditions();
				
				int needs = goal.peasant - current.peasant;
				for (int x = 0; x < needs; x++)
				{
					if (actions.size() == 0)
					{
						act.setStartTime(0);
						act.setEndTime(act.getDuration());
					}
					else
					{
						act.setStartTime(actions.get(actions.size()-1).getEndTime());
						act.setEndTime(act.getStartTime() + act.getDuration());
					}
					current.gold -= pre.gold;
					current.wood -= pre.wood;
					current.peasant -= pre.peasant;
					current.supply -= pre.supply;
					actions.add(act);
					current.gold += post.gold;
					current.wood += post.wood;
					current.peasant += post.peasant;
					current.supply += post.supply;
				}
				continue;
			}
			if (current.supply < goal.supply)
			{
				BuildFarmAction act = new BuildFarmAction();
				Condition pre = act.getPreConditions();
				Condition post = act.getPostConditions();
				
				int needs = (int) Math.ceil((goal.supply - current.supply)/4);
				for (int x = 0; x < needs; x++)
				{
					if (actions.size() == 0)
					{
						act.setStartTime(0);
						act.setEndTime(act.getDuration());
					}
					else
					{
						act.setStartTime(actions.get(actions.size()-1).getEndTime());
						act.setEndTime(act.getStartTime() + act.getDuration());
					}
					current.gold -= pre.gold;
					current.wood -= pre.wood;
					current.peasant -= pre.peasant;
					current.supply -= pre.supply;
					actions.add(act);
					current.gold += post.gold;
					current.wood += post.wood;
					current.peasant += post.peasant;
					current.supply += post.supply;
				}
				continue;
			}
			break;
		}
		
		return actions;
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
					else if (act.getType() == ActionType.COMPOUNDBUILD)
					{
						current.supply += 4;
					}
				}
			}
			else if (unit.getTemplateView().getName().equalsIgnoreCase("townhall"))
			{
				current.supply += 1;
			}
			else if (unit.getTemplateView().getName().equalsIgnoreCase("farm"))
			{
				current.supply += 4;
			}
				
		}
		return current;
	}
}
