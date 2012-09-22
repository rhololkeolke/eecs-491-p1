package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

public final class CollectWoodAction implements BaseAction {

	private final static Condition pre = new Condition(0,0,1,0);
	private final static Condition post = new Condition(0,100,1,0);
	
	// set to a default value, but this should probably be changed to a 
	// more reasonable estimate
	private static int duration = 10;
	
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
		return duration;
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDuration(int duration) throws Exception{
		if(duration > 0)
			CollectWoodAction.duration = duration;
		else
			throw new Exception("Duration out of bounds!!");
	}
	
}
