package de.dk.util.game;

import java.io.Serializable;

import de.dk.util.DMath;

/**
 * 15.10.2015<br>
 * Result of a collision detection of a {@link Collider}.
 */
public class Collision<O extends GameObject, C extends GameObject> implements Serializable {
   private static final long serialVersionUID = -5677118395649280621L;

   private O origin;
	private C collisionObj;
	private Direction direction;

	public Collision(O obj0, C obj1, Vector force) {
		this.origin = obj0;
		this.collisionObj = obj1;
		Direction vertical = Direction.verticalDirection(obj0, obj1, force);
		Direction horizontal = Direction.horizontalDirection(obj0, obj1, force);

		boolean isVertical;
		if (vertical == null)
			isVertical = false;
		else if (horizontal == null)
			isVertical = true;
		else
			isVertical = isVertical(force, horizontal.getDistance(obj0, obj1), vertical.getDistance(obj0, obj1));

		if (isVertical)
			this.direction = vertical;
		else
			this.direction = horizontal;
	}

   private static boolean isVertical(Vector force, float distX, float distY){
      return DMath.divideSafely(distY, force.y) > DMath.divideSafely(distX, force.x);
   }

	public Direction getDirection() {
		return direction;
	}

	public O getOrigin() {
		return origin;
	}

	public C getCollisionObj() {
		return collisionObj;
	}

}