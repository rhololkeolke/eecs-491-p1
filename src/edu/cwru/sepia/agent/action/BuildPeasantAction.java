package edu.cwru.sepia.agent.action;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Condition;

public final class BuildPeasantAction implements BaseAction {
	
	private final static Condition pre = new Condition(400,0,0,1);
	private final static Condition post = new Condition(0,0,1,0);
	
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDuration(int duration) {
		// TODO Auto-generated method stub
		
	}

}
