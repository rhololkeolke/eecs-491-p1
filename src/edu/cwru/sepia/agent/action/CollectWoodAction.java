package edu.cwru.sepia.agent.action;

import java.util.HashMap;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;
import edu.cwru.sepia.environment.model.state.State.StateView;

public final class CollectWoodAction implements BaseAction, CollectAction {

	private int startTime;
	private int endTime;
	
	private final static Condition pre = new Condition(0,0,1,0);
	private final static Condition post = new Condition(0,100,1,0);
	
	// Keeps track of duration per mine
	private static Map<Integer, Integer> durations = new HashMap<Integer, Integer>();
	
	private static Integer shortestDuration = null;
	private static Integer shortestID = null;
	
	// This is this instances duration and the associated resource ID
	private int duration = 0;
	private int resourceID;

	@Override
	public Condition getPreConditions() {
		return pre;
	}

	@Override
	public Condition getPostConditions() {
		return post;
	}

	@Override
	public int getDuration() {
		return shortestDuration;
	}
	
	@Override
	public Action getAction(int playernum, int unitid, StateView state) {
		return Action.createCompoundGather(unitid, resourceID);
	}

	@Override
	public void updateDuration(int duration) throws Exception{
		// if the duration is valid
		if(duration > 0)
		{
			// update the action's resource ID with a new duration
			durations.put(resourceID, duration);
			
			// if this new duration is the shortest
			if(shortestDuration == null || duration < shortestDuration)
			{
				shortestDuration = duration;
				shortestID = resourceID;
			}
		}
		else
			throw new Exception("Duration out of bounds!!");
	}

	@Override
	public String getUnitType() {
		return "Peasant";
	}
	
	@Override
	public int getStartTime()
	{
		return startTime;
	}
	
	@Override
	public int getEndTime()
	{
		return endTime;
	}
	
	@Override
	public void setStartTime(int time)
	{
		startTime = time;
	}
	
	@Override
	public void setEndTime(int time)
	{
		endTime = time;
	}

	@Override
	public void addResource(int resourceId, int distance) {
		durations.put(resourceID, 2*distance);
		if(shortestDuration == null || 2*distance < shortestDuration)
		{
			shortestDuration = 2*distance;
			shortestID = resourceId;
		}
	}

	@Override
	public void deleteResource(int resourceId) {
		durations.remove(resourceId);
		// if the one to be removed is the shortest then a new shortest must be found
		if(resourceId == shortestID)
		{
			shortestDuration = Integer.MAX_VALUE;
			for(Integer key : durations.keySet())
			{
				if(durations.get(key) < shortestDuration)
				{
					shortestDuration = durations.get(key);
					shortestID = key;
				}
			}
		}
	}

}
