package de.dk.util.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observer;
import java.util.function.Consumer;

import de.dk.util.DMath;

/**
 * Class to do collision detection for rectangles. Possible to add collision-{@link Observer}s to a collider.
 *
 * @param <G> Type of the object A that causes the collision.
 * Must be a subtype of {@link Collidable}.
 * @param <C> Type of the objects that the origin-object collides with.
 * Must be a subtype of {@link Collidable}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public class Collider<G extends Collidable, C extends Collidable> {
   private G origin;
   private Consumer<Collision<G, C>> listener;

   private ArrayList<C> ignoreList = new ArrayList<C>();

   /**
    * Creates a new collider for the given game object.
    *
    * @param origin The origin-object, that causes the collision(s).
    */
   public Collider(G origin) {
      this.origin = origin;
   }

   private static Direction verticalCollisionDirection(Collidable objectA,
                                                       Collidable objectB){
      if (objectA.isUnder(objectB) && objectA.getVelocity().y() < 0)
         return Direction.TOP;
      else if (objectA.isOver(objectB) && objectA.getVelocity().y() > 0)
         return Direction.BOTTOM;
      else
         return null;
   }

   private static Direction horizontalCollisionDirection(Collidable objectA,
                                                         Collidable objectB){
      if (objectA.isLeftOf(objectB) && objectA.getVelocity().x() > 0)
         return Direction.RIGHT;
      else if (objectA.isRightOf(objectB) && objectA.getVelocity().x() < 0)
         return Direction.LEFT;
      else
         return null;
   }

   /**
    * Looking for collisions with other game-objects.
    *
    * @param collisionObjects The game-objects which the origin-object could possibly collide with.
    * @param correctVelocity <code>true</code> if the force of the origin-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the origin-object is going straight through the other game-objects.
    */
   public void collisionDetection(Collection<? extends C> collisionObjects, boolean correctVelocity) {
      if (collisionObjects == null || (origin.getVelocity().getMagnitude() == 0 && correctVelocity))
         return;

      collisionObjects = getCollisions(origin.getVelocity(), collisionObjects);
      Iterator<? extends C> iterator = collisionObjects.iterator();
      while (iterator.hasNext()) {
         C current = iterator.next();
         try {
            collisionDetection(current, correctVelocity);
         } catch (IntersectionException e) {
            ignoreList.add(current);
         }
         if (!correctVelocity)
            continue;

         collisionObjects = getCollisions(origin.getVelocity(), collisionObjects);
         iterator = collisionObjects.iterator();
      }
      ignoreList.clear();
   }


   /**
    * Looking for collisions with an other game-object.
    *
    * @param obj The game-object which the origin-object could collide with.
    * @param correctVelocity <code>true</code> if the force of the origin-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the origin-objectis going straight through the other game-objects.
    *
    * @throws IntersectionException if the origin-object intersects another object
    * before collisiondetection and <code>correctForce</code> is true.
    * If <code>correctForce</code> is false the Exception will not be thrown.
    */
   protected void collisionDetection(C obj, boolean correctVelocity) throws IntersectionException {
      Direction vertical = verticalCollisionDirection(origin, obj);
      Direction horizontal = horizontalCollisionDirection(origin, obj);

      boolean isVertical;
      if (vertical == null)
         isVertical = false;
      else if (horizontal == null)
         isVertical = true;
      else
         isVertical = isVertical(origin.getVelocity(),
                                 horizontal.getDistance(origin, obj),
                                 vertical.getDistance(origin, obj));

      Direction direction = isVertical ? vertical : horizontal;

      Collision<G, C> collision = new Collision<G, C>(this.origin, obj, direction);

      if (correctVelocity)
         correctVelocity(collision);

      listener.accept(collision);
   }

   private static boolean isVertical(Vector velocity, float distX, float distY){
      return DMath.divideSafely(distY, velocity.y()) > DMath.divideSafely(distX, velocity.x());
   }

   protected void correctVelocity(Collision<G, C> collision) throws IntersectionException {
      Direction dir = collision.getDirection();
      if (dir == null)
         throw new IntersectionException(collision.getObjectA(), collision.getObjectB());

      float dist = dir.getDistance(collision.getObjectA(), collision.getObjectB());
      Vector velocity = origin.getVelocity();
      if (dir == Direction.BOTTOM || dir == Direction.TOP)
         velocity.y(velocity.y() > 0 ? dist : -dist);
      else
         velocity.x(velocity.x() > 0 ? dist : -dist);
   }

   protected ArrayList<? extends C> getCollisions(Vector force, Collection<? extends C> collisionObjects) {
      origin.getPosition().add(force);

      ArrayList<C> collisions = new ArrayList<C>();
      for (C obj : collisionObjects) {
         if (origin.intersects(obj) && !ignoreList.contains(obj))
            collisions.add(obj);
      }

      origin.getPosition().subtract(force);
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