package edu.cwru.sepia.agent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.cwru.sepia.agent.action.BaseAction;

/*
 * Determine the concurrency of actions from the various plans
 */

public class Scheduler {

	
	
	public static void schedulePlan(List<BaseAction> actions, Condition start)
	{
		if (actions.isEmpty())
		{
			return;
		}
		List<Integer> decisionTimes = new LinkedList<Integer>();
		decisionTimes.add(0);
		decisionTimes.add(actions.get(0).getEndTime());
		for(int x = 1; x < actions.size(); x++)
		{
			Collections.sort(decisionTimes);
			BaseAction act = actions.get(x);
			int y = decisionTimes.size()-1;
			while (y >= 0)
			{
				Condition current = getCondition(start, decisionTimes.get(y), x, actions);
				if (satisfy(current, act.getPreConditions()))
				{
					act.setStartTime(decisionTimes.get(y));
					act.setEndTime(act.getStartTime()+act.getDuration());
					y--;
				}
				else 
				{
					break;
				}
			}
			if (!decisionTimes.contains(act.getStartTime()))
			{
				decisionTimes.add(act.getStartTime());
			}
			if (!decisionTimes.contains(act.getEndTime()))
			{
				decisionTimes.add(act.getEndTime());
			}
		}
	}
	
	private static Condition getCondition(Condition start, int time, int maxAction, List<BaseAction> actions)
	{
		Condition cond = new Condition();
		cond.gold = start.gold;
		cond.peasant = start.peasant;
		cond.supply = start.supply;
		cond.wood = start.wood;
		
		for (int x = 0; x < maxAction; x++)
		{
			BaseAction act = actions.get(x);
			if (act.getStartTime() <= time)
			{
				Condition pre = act.getPreConditions();
				cond.gold -= pre.gold;
				cond.peasant -= pre.peasant;
				cond.supply -= pre.supply;
				cond.wood -= pre.wood;
			}
			if (act.getEndTime() <= time)
			{
				Condition post = act.getPostConditions();
				cond.gold += post.gold;
				cond.peasant += post.peasant;
				cond.supply += post.supply;
				cond.wood += post.wood;
			}
		}
		return cond;
	}
	
	private static boolean satisfy(Condition current, Condition pre)
	{
		if (pre.gold > current.gold) 
		{
			return false;
		}
		else if (pre.peasant > current.peasant)
		{
			return false;
		}
		else if (pre.supply > current.supply)
		{
			return false;
		}
		else if (pre.wood > current.wood)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
}

