package de.dk.util.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

import de.dk.util.DMath;

/**
 * Class to do collision detection for rectangles. Possible to add collision-listeners to a collider.
 *
 * @param <G> Type of the object A that causes the collision.
 * Must be a subtype of {@link Collidable}.
 * @param <C> Type of the objects that the collidable-object collides with.
 * Must be a subtype of {@link Collidable}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public class Collider<G extends Collidable, C extends Collidable> {
   private final G collidable;
   private Consumer<Collision<G, C>> listener;

   private ArrayList<C> ignoreList = new ArrayList<C>();

   /**
    * Creates a new collider for the given game object.
    *
    * @param collidable The collidable-object, that causes the collision(s).
    */
   public Collider(G collidable) {
      this.collidable = collidable;
   }

   private static Direction verticalCollisionDirection(Collidable objectA,
                                                       Collidable objectB,
                                                       Vector velocity){
      if (objectA.isUnder(objectB) && velocity.y() < 0)
         return Direction.TOP;
      else if (objectA.isOver(objectB) && velocity.y() > 0)
         return Direction.BOTTOM;
      else
         return null;
   }

   private static Direction horizontalCollisionDirection(Collidable objectA,
                                                         Collidable objectB, Vector velocity){
      if (objectA.isLeftOf(objectB) && velocity.x() > 0)
         return Direction.RIGHT;
      else if (objectA.isRightOf(objectB) && velocity.x() < 0)
         return Direction.LEFT;
      else
         return null;
   }

   /**
    * Looking for collisions with other game-objects.
    *
    * @param collisionObjects The game-objects which the collidable-object could possibly collide with.
    * @param velocity The velocity of this colliders collidable.
    * @param correctVelocity <code>true</code> if the force of the collidable-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the collidable-object is going straight through the other game-objects.
    */
   public void collisionDetection(Iterable<? extends C> collisionObjects,
                                  Vector velocity,
                                  boolean correctVelocity) {
      if (collisionObjects == null || (velocity.getMagnitude() == 0 && correctVelocity))
         return;

      collisionObjects = getCollisions(velocity, collisionObjects);
      Iterator<? extends C> iterator = collisionObjects.iterator();
      while (iterator.hasNext()) {
         C current = iterator.next();
         try {
            collisionDetection(current, velocity, correctVelocity);
         } catch (IntersectionException e) {
            ignoreList.add(current);
         }
         if (!correctVelocity)
            continue;

         collisionObjects = getCollisions(velocity, collisionObjects);
         iterator = collisionObjects.iterator();
      }
      ignoreList.clear();
   }


   /**
    * Looking for collisions with an other game-object.
    *
    * @param obj The game-object which the collidable-object could collide with.
    * @param velocity The velocity of this colliders collidable
    * @param correctVelocity <code>true</code> if the force of the collidable-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the collidable-objectis going straight through the other game-objects.
    *
    * @throws IntersectionException if the collidable-object intersects another object
    * before collisiondetection and <code>correctForce</code> is true.
    * If <code>correctForce</code> is false the Exception will not be thrown.
    */
   protected void collisionDetection(C obj, Vector velocity, boolean correctVelocity) throws IntersectionException {
      Direction vertical = verticalCollisionDirection(collidable, obj, velocity);
      Direction horizontal = horizontalCollisionDirection(collidable, obj, velocity);

      boolean isVertical;
      if (vertical == null)
         isVertical = false;
      else if (horizontal == null)
         isVertical = true;
      else
         isVertical = isVertical(velocity,
                                 horizontal.getDistance(collidable, obj),
                                 vertical.getDistance(collidable, obj));

      Direction direction = isVertical ? vertical : horizontal;

      Collision<G, C> collision = new Collision<G, C>(this.collidable, obj, direction);

      if (correctVelocity)
         correctVelocity(collision, velocity);

      if (listener != null)
         listener.accept(collision);
   }

   private static boolean isVertical(Vector velocity, float distX, float distY){
      return DMath.divideSafely(distY, velocity.y()) > DMath.divideSafely(distX, velocity.x());
   }

   protected void correctVelocity(Collision<G, C> collision, Vector velocity) throws IntersectionException {
      Direction dir = collision.getDirection();
      if (dir == null)
         throw new IntersectionException(collision.getObjectA(), collision.getObjectB());

      float dist = dir.getDistance(collision.getObjectA(), collision.getObjectB());
      if (dir == Direction.BOTTOM || dir == Direction.TOP)
         velocity.y(velocity.y() > 0 ? dist : -dist);
      else
         velocity.x(velocity.x() > 0 ? dist : -dist);
   }

   protected Iterable<? extends C> getCollisions(Vector force, Iterable<? extends C> collisionObjects) {
      collidable.getPosition().add(force);

      ArrayList<C> collisions = new ArrayList<C>();
      for (C obj : collisionObjects) {
         if (collidable.intersects(obj) && !ignoreList.contains(obj))
            collisions.add(obj);
      }

      collidable.getPosition().subtract(force);
      return collisions;
   }

   public void setListener(Consumer<Collision<G, C>> listener) {
      this.listener = listener;
   }

   public static class IntersectionException extends Exception {
      private static final long serialVersionUID = 1L;

      public IntersectionException(Collidable a, Collidable b) {
         super("The collision-object " + a + " already intersected object " + b + " before collisiondetection.");
      }
   }
}