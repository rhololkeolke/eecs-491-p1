package edu.cwru.sepia.agent.action;

import java.util.Map;

/*
 * Contains extra information needed for actions that require
 * Peasants to collect resources
 * e.g. Collect Gold
 */

public abstract class CollectionAction extends BaseAction {

	Map<Integer, Integer> resourceDuration;
	
}
