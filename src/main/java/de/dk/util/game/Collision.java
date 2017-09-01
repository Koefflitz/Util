package de.dk.util.game;

import java.io.Serializable;

/**
 * Result of a collision detection of a {@link Collider}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public class Collision<O extends Collidable, C extends Collidable> implements Serializable {
   private static final long serialVersionUID = -5677118395649280621L;

   private O objectA;
	private C objectB;
	private Direction direction;

	public Collision(O objectA, C objectB, Direction direction) {
	   this.objectA = objectA;
	   this.objectB = objectB;
	   this.direction = direction;
   }

	public Direction getDirection() {
		return direction;
	}

	public O getObjectA() {
		return objectA;
	}

	public C getObjectB() {
		return objectB;
	}

}