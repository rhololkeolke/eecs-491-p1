package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

public class BuildFarmAction implements BaseAction {
	
	private final static Condition pre = new Condition(500,250,1,0);
	private final static Condition post = new Condition(0,0,1,4);

	// set to a default value, but this should probably be changed to a 
	// more reasonable estimate
	private static int duration = 10;
	
	// used to keep track of which peasant is executing this action
	private Integer peasant = null;
	
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
			BuildFarmAction.duration = duration;
		else
			throw new Exception("Duration out of bounds!!");
	}

	@Override
	public void setPeasant(Integer i) {
		peasant = i;
	}
	
	@Override
	public Integer getPeasant(){
		return peasant;
	}

}
