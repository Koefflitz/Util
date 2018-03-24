package de.dk.util.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ColliderTest {
   private Collider<CollisionObject, CollisionObject> collider;
   private CollisionObject object;
   private CollisionObject other;

   private Collection<Collision<CollisionObject, CollisionObject>> collisions = new LinkedList<>();

   public ColliderTest() {

   }

   @BeforeEach
   public void init() {
      this.object = new CollisionObject();
      this.collider = new Collider<>(object);
      collider.setListener(collisions::add);
      this.other = new CollisionObject();
   }

   @Test
   public void testTopCollision() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(2);
      object.getVelocity().y(-2);

      other.getPosition().x(object.getPosition().x());
      other.getPosition().y(1);
      other.setWidth(2);
      other.setHeight(2);

      collider.collisionDetection(Arrays.asList(other), object.getVelocity(), true);

      assertEquals(1, collisions.size());
      Collision<CollisionObject, CollisionObject> collision = collisions.iterator()
                                                                        .next();
      assertEquals(Direction.TOP, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other, collision.getObjectB());
      assertEquals(-1, object.getVelocity().y(), 0.0001f);
      assertEquals(0, object.getVelocity().x());
   }

   @Test
   public void testBottomCollision() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(2);
      object.getVelocity().y(2);

      other.getPosition().x(object.getPosition().x());
      other.getPosition().y(7);
      other.setWidth(2);
      other.setHeight(2);

      collider.collisionDetection(Arrays.asList(other), object.getVelocity(), true);

      assertEquals(1, collisions.size());
      Collision<CollisionObject, CollisionObject> collision = collisions.iterator()
                                                                        .next();
      assertEquals(Direction.BOTTOM, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other, collision.getObjectB());
      assertEquals(1, object.getVelocity().y(), 0.0001f);
      assertEquals(0, object.getVelocity().x());
   }

   @Test
   public void testLeftCollision() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(2);
      object.getVelocity().x(-2);

      other.getPosition().x(1);
      other.getPosition().y(object.getPosition().y());
      other.setWidth(2);
      other.setHeight(2);

      collider.collisionDetection(Arrays.asList(other), object.getVelocity(), true);

      assertEquals(1, collisions.size());
      Collision<CollisionObject, CollisionObject> collision = collisions.iterator()
                                                                        .next();
      assertEquals(Direction.LEFT, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other, collision.getObjectB());
      assertEquals(0, object.getVelocity().y());
      assertEquals(-1, object.getVelocity().x(), 0.0001f);
   }

   @Test
   public void testRightCollision() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(2);
      object.getVelocity().x(2);

      other.getPosition().x(7);
      other.getPosition().y(object.getPosition().y());
      other.setWidth(2);
      other.setHeight(2);

      collider.collisionDetection(Arrays.asList(other), object.getVelocity(), true);

      assertEquals(1, collisions.size());
      Collision<CollisionObject, CollisionObject> collision = collisions.iterator()
                                                                        .next();
      assertEquals(Direction.RIGHT, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other, collision.getObjectB());
      assertEquals(0, object.getVelocity().y());
      assertEquals(1, object.getVelocity().x(), 0.0001f);
   }

   @Test
   public void testMultipleCollisions() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(8);
      object.getVelocity().x(2);

      other.getPosition().x(7);
      other.getPosition().y(1);
      other.setWidth(2);
      other.setHeight(2);

      CollisionObject other2 = new CollisionObject();
      other2.getPosition().x(7);
      other2.getPosition().y(7);
      other2.setWidth(2);
      other2.setHeight(2);

      collider.collisionDetection(Arrays.asList(other, other2), object.getVelocity(), false);

      assertEquals(2, collisions.size());
      Iterator<Collision<CollisionObject, CollisionObject>> iter = collisions.iterator();
      Collision<CollisionObject, CollisionObject> collision = iter.next();

      assertEquals(Direction.RIGHT, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other, collision.getObjectB());
      assertEquals(2, object.getVelocity().x(), 0.0001f);
      assertEquals(0, object.getVelocity().y());

      collision = iter.next();
      assertEquals(Direction.RIGHT, collision.getDirection());
      assertEquals(object, collision.getObjectA());
      assertEquals(other2, collision.getObjectB());
   }

   @Test
   public void testNoCollision() {
      object.getPosition().x(4);
      object.getPosition().y(4);
      object.setWidth(2);
      object.setHeight(2);
      object.getVelocity().x(2);

      other.getPosition().x(128);
      other.getPosition().y(256);
      other.setWidth(2);
      other.setHeight(2);

      collider.collisionDetection(Arrays.asList(other), object.getVelocity(), true);

      assertTrue(collisions.isEmpty());
   }

   @AfterEach
   public void cleanUp() {
      collisions.clear();
   }

}
