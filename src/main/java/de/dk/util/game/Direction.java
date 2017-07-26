package de.dk.util.game;

import java.util.function.BiFunction;

public enum Direction {
   TOP((a, b) -> a.topBorder() - b.bottomBorder()),
   BOTTOM((a, b) -> a.topBorder() - b.bottomBorder()),
   LEFT((a, b) -> a.leftBorder() - b.rightBorder()),
   RIGHT((a, b) -> a.leftBorder() - b.rightBorder());

	private BiFunction<GameObject, GameObject, Float> distanceCalculator;

	private Direction(BiFunction<GameObject, GameObject, Float> dc){
		this.distanceCalculator = dc;
	}

	public static Direction verticalDirection(GameObject obj0, GameObject obj1, Vector force){
		if (obj0.isUnder(obj1) && force.y < 0)
			return Direction.TOP;
		else if (obj0.isOver(obj1) && force.y > 0)
			return Direction.BOTTOM;
		else
			return null;
	}

	public static Direction horizontalDirection(GameObject obj0, GameObject obj1, Vector force){
		if (obj0.isLeftOf(obj1) && force.x > 0)
			return Direction.RIGHT;
		else if (obj0.isRightOf(obj1) && force.x < 0)
			return Direction.LEFT;
		else
			return null;
	}

	public float getDistance(GameObject obj0, GameObject obj1){
		return distanceCalculator.apply(obj0, obj1);
	}
}