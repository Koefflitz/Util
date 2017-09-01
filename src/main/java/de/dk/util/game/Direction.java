package de.dk.util.game;

import java.util.function.BiFunction;

/**
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public enum Direction {
   TOP((a, b) -> a.topBorder() - b.bottomBorder()),
   BOTTOM((a, b) -> b.topBorder() - a.bottomBorder()),
   LEFT((a, b) -> a.leftBorder() - b.rightBorder()),
   RIGHT((a, b) -> b.leftBorder() - a.rightBorder());

	private BiFunction<Collidable, Collidable, Float> distanceCalculator;

	private Direction(BiFunction<Collidable, Collidable, Float> dc){
		this.distanceCalculator = dc;
	}

	public static Direction vertical(Collidable objectA, Collidable objectB, Vector velocity){
		if (objectA.isUnder(objectB) && velocity.y < 0)
			return Direction.TOP;
		else if (objectA.isOver(objectB) && velocity.y > 0)
			return Direction.BOTTOM;
		else
			return null;
	}

	public static Direction horizontal(Collidable objectA, Collidable objectB, Vector velocity){
		if (objectA.isLeftOf(objectB) && velocity.x > 0)
			return Direction.RIGHT;
		else if (objectA.isRightOf(objectB) && velocity.x < 0)
			return Direction.LEFT;
		else
			return null;
	}

	public float getDistance(Collidable a, Collidable b){
		return distanceCalculator.apply(a, b);
	}
}
