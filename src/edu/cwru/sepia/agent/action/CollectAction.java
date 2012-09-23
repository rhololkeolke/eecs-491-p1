package edu.cwru.sepia.agent.action;

public interface CollectAction {
	public void addResource(int resourceId, int distance);
	
	public void deleteResource(int resourceId);
}
