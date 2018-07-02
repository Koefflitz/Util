package de.dk.util.game;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import de.dk.util.Vector;

/**
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public enum Direction {
   TOP((a, b) -> a.topBorder() - b.bottomBorder(), Vector::up),
   BOTTOM((a, b) -> b.topBorder() - a.bottomBorder(), Vector::down),
   LEFT((a, b) -> a.leftBorder() - b.rightBorder(), Vector::left),
   RIGHT((a, b) -> b.leftBorder() - a.rightBorder(), Vector::right);

	private final BiFunction<Collidable, Collidable, Float> distanceCalculator;
	private final Supplier<Vector> vector;

	private Direction(BiFunction<Collidable, Collidable, Float> dc, Supplier<Vector> vector){
		this.distanceCalculator = dc;
		this.vector = vector;
	}

	public static Direction vertical(Collidable objectA, Collidable objectB, Vector velocity){
		if (objectA.isUnder(objectB) && velocity.y() < 0)
			return Direction.TOP;
		else if (objectA.isOver(objectB) && velocity.y() > 0)
			return Direction.BOTTOM;
		else
			return null;
	}

	public static Direction horizontal(Collidable objectA, Collidable objectB, Vector velocity){
		if (objectA.isLeftOf(objectB) && velocity.x() > 0)
			return Direction.RIGHT;
		else if (objectA.isRightOf(objectB) && velocity.x() < 0)
			return Direction.LEFT;
		else
			return null;
	}

	public Vector vector() {
	   return vector.get();
	}

	public float getDistance(Collidable a, Collidable b){
		return distanceCalculator.apply(a, b);
	}
}
