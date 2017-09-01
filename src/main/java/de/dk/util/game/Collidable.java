package de.dk.util.game;

/**
 * Objects that can be handled by a {@link Collider}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.10.2015
 */
public interface Collidable {
   public Vector getPosition();
   public Vector getVelocity();
   public int getWidth();
   public int getHeight();

   public default boolean isOver(Collidable c) {
      return this.bottomBorder() <= c.topBorder();
   }

   public default boolean isUnder(Collidable c) {
      return this.topBorder() >= c.bottomBorder();
   }

   public default boolean isLeftOf(Collidable c) {
      return this.rightBorder() <= c.leftBorder();
   }

   public default boolean isRightOf(Collidable c) {
      return this.leftBorder() >= c.rightBorder();
   }

   public default boolean intersects(Collidable c) {
      boolean vertical = this.topBorder() < c.bottomBorder() && this.bottomBorder() > c.topBorder();
      boolean horizontal = this.rightBorder() > c.leftBorder() && this.leftBorder() < c.rightBorder();
      return vertical && horizontal;
   }

   public default float topBorder() {
      return getPosition().y - (float) getHeight() / 2;
   }

   public default float bottomBorder() {
      return getPosition().y + (float) getHeight() / 2;
   }

   public default float leftBorder() {
      return getPosition().x - (float) getWidth() / 2;
   }

   public default float rightBorder() {
      return getPosition().x + (float) getWidth() / 2;
   }
}
