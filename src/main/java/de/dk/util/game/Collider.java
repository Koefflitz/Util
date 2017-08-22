package de.dk.util.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * 15.10.2015 <br> Class to do collision detection for rectangles. Possible to add collision-{@link Observer}s to a collider.
 *
 * @param <G> Type of the origin-object that causes the collision. Must implement the {@link GameObject}-interface.
 * @param <C> Type of the objects that the origin-object collides with. Must implement the {@link GameObject}-interface too.
 */
public class Collider<G extends GameObject, C extends GameObject> {
   private G origin;
   private Consumer<Collision<? extends G, ? extends C>> observer;

   private ArrayList<C> ignoreList = new ArrayList<C>();

   /**
    * @param origin The origin-object, that causes the collision(s).
    */
   public Collider(G origin) {
      this.origin = origin;
   }

   /**
    * Looking for collisions with other game-objects.
    *
    * @param force The force-{@link Vector} of the origin-object.
    * @param collisionObjects The {@link Collection} of game-objects which the origin-object could collide with.
    * @param correctForce <code>true</code> if the force of the origin-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the origin-object is going straight through the other game-objects.
    */
   public void collisionDetection(Vector force, Collection<? extends C> collisionObjects, boolean correctForce) {
      if (collisionObjects == null || (force.getMagnitude() == 0 && correctForce))
         return;

      collisionObjects = getCollisions(force, collisionObjects);
      Iterator<? extends C> iterator = collisionObjects.iterator();
      while (iterator.hasNext()) {
         C current = iterator.next();
         try {
            collisionDetection(force, current, correctForce);
         } catch (IntersectionException e) {
            ignoreList.add(current);
         }
         if (!correctForce)
            continue;

         collisionObjects = getCollisions(force, collisionObjects);
         iterator = collisionObjects.iterator();
      }
      ignoreList.clear();
   }

   public void physicalCollisionDetection(Vector force, C obj) throws IntersectionException {
      collisionDetection(force, obj, true);
   }

   public void nonPhysicalCollisionDetection(Vector force, C obj) {
      try {
         collisionDetection(force, obj, false);
      } catch (IntersectionException e) {
      }
   }

   /**
    * Looking for collisions with an other game-object.
    *
    * @param force The force-{@link Vector} of the origin-object.
    * @param obj The game-object which the origin-object could collide with.
    * @param correctForce <code>true</code> if the force of the origin-object should be corrected
    * so that it is not intersecting any other game-object.<br>
    * <code>false</code> if the origin-objectis going straight through the other game-objects.
    *
    * @throws IntersectionException if the origin-object intersects another object
    * before collisiondetection and <code>correctForce</code> is true.
    * If <code>correctForce</code> is false the Exception will not be thrown.
    */
   protected void collisionDetection(Vector force, C obj, boolean correctForce) throws IntersectionException {
      Collision<G, C> collision = new Collision<G, C>(this.origin, obj, force);

      if (correctForce)
         correctForce(obj, force, collision);

      observer.accept(collision);
   }

   protected void correctForce(C obj, Vector force, Collision<G, C> collision) throws IntersectionException {
      Direction dir = collision.getDirection();
      if (dir == null)
         throw new IntersectionException();

      float dist = dir.getDistance(this.origin, obj);
      if (dir == Direction.BOTTOM || dir == Direction.TOP)
         force.y = force.y > 0 ? dist : -dist;
      else
         force.x = force.x > 0 ? dist : -dist;
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

   public void setObserver(Consumer<Collision<? extends G, ? extends C>> observer) {
      this.observer = observer;
   }

   public static class IntersectionException extends Exception {
      private static final long serialVersionUID = 1L;

      public IntersectionException() {
         super("The collision-object already intersected another gameobject before collisiondetection.");
      }
   }
}