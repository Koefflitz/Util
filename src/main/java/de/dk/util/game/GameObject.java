package de.dk.util.game;

public interface GameObject {
   public Vector getPosition();
   public int getWidth();
   public int getHeight();

   public default boolean isOver(GameObject obj) {
      return this.bottomBorder() <= obj.topBorder();
   }

   public default boolean isUnder(GameObject obj) {
      return this.topBorder() >= obj.bottomBorder();
   }

   public default boolean isLeftOf(GameObject obj) {
      return this.rightBorder() <= obj.leftBorder();
   }

   public default boolean isRightOf(GameObject obj) {
      return this.leftBorder() >= obj.rightBorder();
   }

   public default boolean intersects(GameObject obj) {
      boolean vertical = this.topBorder() < obj.bottomBorder() && this.bottomBorder() > obj.topBorder();
      boolean horizontal = this.rightBorder() > obj.leftBorder() && this.leftBorder() < obj.rightBorder();
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