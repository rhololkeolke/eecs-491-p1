package edu.cwru.sepia.agent;

import java.util.LinkedList;
import java.util.List;

import edu.cwru.sepia.agent.action.BaseAction;
import edu.cwru.sepia.agent.action.BuildFarmAction;
import edu.cwru.sepia.agent.action.BuildPeasantAction;
import edu.cwru.sepia.agent.action.CollectGoldAction;
import edu.cwru.sepia.agent.action.CollectWoodAction;

/*
 * Determines all of the actions necessary for each goal condition
 */

public class MEA 
{

	public static Pair<List<BaseAction>,Condition> plan(Condition start, Condition goal)
	{
		List<BaseAction> actions = new LinkedList<BaseAction>();
		Condition current = new Condition();
		current.gold = start.gold;
		current.peasant = start.peasant;
		current.supply = start.supply;
		current.townhall = start.townhall;
		current.wood = start.wood;
		
		if (current.gold < goal.gold)
		{
			CollectGoldAction act = new CollectGoldAction();
			Condition pre = act.getPreConditions();
			Condition post = act.getPostConditions();
			
			int needs = (int) Math.ceil((goal.gold - current.gold)/100.0);
			for (int x = 0; x < needs; x++)
			{
				act = new CollectGoldAction();
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
				current.townhall -= pre.townhall;
				actions.add(act);
				current.townhall += post.townhall;
				current.gold += post.gold;
				current.wood += post.wood;
				current.peasant += post.peasant;
				current.supply += post.supply;
			}
		}
		if (current.wood < goal.wood)
		{
			CollectWoodAction act = new CollectWoodAction();
			Condition pre = act.getPreConditions();
			Condition post = act.getPostConditions();
			
			int needs = (int) Math.ceil((goal.wood - current.wood)/100.0);
			for (int x = 0; x < needs; x++)
			{
				act = new CollectWoodAction();
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
				current.townhall -= pre.townhall;
				actions.add(act);
				current.townhall += post.townhall;
				current.gold += post.gold;
				current.wood += post.wood;
				current.peasant += post.peasant;
				current.supply += post.supply;
			}
		}
		if (current.supply < goal.supply)
		{
			BuildFarmAction act = new BuildFarmAction();
			Condition pre = act.getPreConditions();
			Condition post = act.getPostConditions();
			
			int needs = (int) Math.ceil((goal.supply - current.supply)/4.0);
			for (int x = 0; x < needs; x++)
			{
				act = new BuildFarmAction();
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
				current.townhall -= pre.townhall;
				actions.add(act);
				current.townhall += post.townhall;
				current.gold += post.gold;
				current.wood += post.wood;
				current.peasant += post.peasant;
				current.supply += post.supply;
			}
		}
		if (current.peasant < goal.peasant)
		{
			BuildPeasantAction act = new BuildPeasantAction();
			Condition pre = act.getPreConditions();
			Condition post = act.getPostConditions();
			
			int needs = goal.peasant - current.peasant;
			for (int x = 0; x < needs; x++)
			{
				act = new BuildPeasantAction();
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
				current.townhall -= pre.townhall;
				actions.add(act);
				current.townhall += post.townhall;
				current.gold += post.gold;
				current.wood += post.wood;
				current.peasant += post.peasant;
				current.supply += post.supply;
			}
		}
				
		if (actions.size() == 0)
		{
			return new Pair<List<BaseAction>,Condition>(actions, current);
		}
		else
		{
			Pair<List<BaseAction>,Condition> preconditionPlan = plan(current, goal);
			List<BaseAction> combined = concat(preconditionPlan.first, actions);
			return new Pair<List<BaseAction>,Condition>(combined, preconditionPlan.second);
		}
	}
	
	private static List<BaseAction> concat(List<BaseAction> plan1, List<BaseAction> plan2)
	{
		int endTime = 0;
		if (plan1.size() != 0)
		{
			endTime = plan1.get(plan1.size()-1).getEndTime();
		}
		
		for (BaseAction act : plan2)
		{
			act.setStartTime(act.getStartTime()+endTime);
			act.setEndTime(act.getEndTime()+endTime);
			plan1.add(act);
		}
		
		return plan1;
	}
}
