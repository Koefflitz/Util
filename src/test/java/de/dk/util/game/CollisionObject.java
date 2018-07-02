package de.dk.util.game;

import de.dk.util.Vector;

public class CollisionObject implements Collidable {
   private final Vector position = new Vector();
   private int width;
   private int height;

   private final Vector velocity = new Vector();

   public CollisionObject() {

   }

   @Override
   public Vector getPosition() {
      return position;
   }

   @Override
   public int getWidth() {
      return width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   @Override
   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public Vector getVelocity() {
      return velocity;
   }

}
