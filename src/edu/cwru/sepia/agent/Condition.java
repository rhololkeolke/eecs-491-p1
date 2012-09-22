package edu.cwru.sepia.agent;

/*
 * Keeps track of Goal conditions, preconditions and postconditions
 */
public class Condition {

	int gold;
	int wood;
	int peasant;
	int supply; // number of peasants that can be supported
	
	// Initialize everything to 0
	public Condition()
	{
		gold = 0;
		wood = 0;
		peasant = 0;
		supply = 0;
	}
	
	// Initialize everything to the values provided
	// if a condition is not needed set to 0
	public Condition(int gold, int wood, int peasant, int supply)
	{
		this.gold = gold;
		this.wood = wood;
		this.peasant = peasant;
		this.supply = supply;
	}
}
