package de.dk.util.game;

import java.util.function.UnaryOperator;

/**
 * Represents a 2-dimensional vector with an x and a y value.
 * Provides getters and setters for some of its properties, e.g. its magnitude or angle.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 02.02.2017
 */
public class IntVector {
   public int x = 1;
   public int y = 1;

   public IntVector(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public IntVector() {
      this(0, 0);
   }

   /**
    * Creates a new vector by angle and magnitude.
    *
    * @param angle The angle-value goes from the plus-x-axis(0° or 360°) anti-clockwise over the minus-x-axis(180°).
    *           For example: If the Vector is parallel to the x-axis and has a positive x-value(Vector.right()),
    *           this method will return 0. If the Vector is parallel to the y-axis and has a positive y-value(Vector.up()),
    *           this method will return 90. If the Vector is parallel to the x-axis and has a negative x-value(Vector.left()),
    *           this method will return 180. If the Vector is parallel to the y-axis and has a negative y-value(Vector.down()),
    *           this method will return 270.
    * @param magnitude The magnitude (length) of the Vector.
    * @return The created Vector.
    */
   public static IntVector newVector(float angle, float magnitude) {
      IntVector v = new IntVector(1, 1);
      v.setAngle(angle);
      v.setMagnitude(magnitude);
      return v;
   }

   /**
    * Creates a vector which is pointing straight up. The vectors x-value is 0, while the y-value gets the value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static IntVector up(int magnitude) {
      return new IntVector(0, magnitude);
   }

   /**
    * Creates a vector which is pointing straight down. The vectors x-value is 0,
    * while the y-value gets the negative value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static IntVector down(int magnitude) {
      return new IntVector(0, -magnitude);
   }

   /**
    * Creates a vector which is pointing straight left. The vectors y-value is 0,
    * while the x-value gets the negative value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static IntVector left(int magnitude) {
      return new IntVector(-magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight right. The vectors y-value is 0,
    * while the x-value gets the value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static IntVector right(int magnitude) {
      return new IntVector(magnitude, 0);
   }

   /**
    * Creates a vector, which is rotated 180 degrees to the parameter-vector.
    *
    * @return The vector with the exact opposite direction of the parameter-vector.
    */
   public static IntVector reverse(IntVector v) {
      return new IntVector(-v.x, -v.y);
   }

   /**
    * Creates a new vector, which is the sum of the parameter-vectors.
    *
    * @return The created vector.
    */
   public static IntVector add(IntVector a, IntVector b) {
      if (a == null || b == null)
         return null;

      return new IntVector(a.x + b.x, a.y + b.y);
   }

   /**
    * Creates a new vector, which is the difference of the parameter-vectors.
    *
    * @return The created vector.
    */
   public static IntVector subtract(IntVector a, IntVector b) {
      if (a == null || b == null)
         return null;

      return new IntVector(a.x - b.x, a.y - b.y);
   }

   /**
    * Creates a new vector, whose magnitude is the product of the parameter-vector and the amount.
    *
    * @return The created vector.
    */
   public static IntVector multiply(IntVector v, float amount) {
      if (v == null)
         return null;

      return new IntVector(Math.round(v.x * amount), Math.round(v.y * amount));
   }

   /**
    * Creates a new vector, whose magnitude is the quotient of the parameter-vector and the amount.
    *
    * @return The created vector.
    */
   public static IntVector divide(IntVector v, float amount) {
      if (amount != 0)
         return new IntVector(Math.round(v.x / amount), Math.round(v.y / amount));

      return null;
   }

   /**
    * Calculates the angle between two vectors. Always calculates the smaller angle.
    *
    * @return The angle between the two vectors.
    */
   public static float getAngleBetween(IntVector v1, IntVector v2) {
      float a1 = v1.getAngle();
      float a2 = v2.getAngle();
      float angle = Math.abs(a1 - a2);
      if (angle <= 180)
         return angle;

      if (a1 > a2)
         return 360f - a1 + a2;
      else
         return 360f - a2 + a1;
   }

   /**
    * Adds a vector to this vector.
    *
    * @param v The vector that should be added to this vector.
    * @return this vector
    */
   public IntVector add(IntVector v) {
      if (v != null) {
         this.x += v.x;
         this.y += v.y;
      }
      return this;
   }

   /**
    * Subtracts a vector from this vector.
    *
    * @param v The vector that should be subtracted from this vector.
    * @return this vector
    */
   public IntVector subtract(IntVector v) {
      if (v != null) {
         this.x = this.x - v.x;
         this.y = this.y - v.y;
      }
      return this;
   }

   /**
    * Multiplies the magnitude of this vector by the specified amount.
    *
    * @param amount The amount by which the magnitude should be multiplied.
    * @return this vector
    */
   public IntVector multiply(float amount) {
      this.x *= amount;
      this.y *= amount;
      return this;
   }

   /**
    * Divides the magnitude of this vector by the specified amount.
    *
    * @param amount
    * @return this vector
    */
   public IntVector divide(float amount) {
      if (amount != 0) {
         this.x /= amount;
         this.y /= amount;
      }
      return this;
   }

   /**
    * Rotates this vector by 180 degrees. So that it points to the exact opposite direction.
    * @return this vector
    */
   public IntVector reverse() {
      this.x = -x;
      this.y = -y;
      return this;
   }

   /**
    * @return The magnitude (length) of this vector.
    */
   public float getMagnitude() {
      return (float) (Math.sqrt(x * x + y * y));
   }

   /**
    * Sets the magnitude (length) of this vector.
    *
    * @param magnitude The new magnitude of this vector.
    * @return this vector
    */
   public IntVector setMagnitude(float magnitude) {
      float mag = getMagnitude();
      if (mag != 0) {
         this.x *= magnitude / mag;
         this.y *= magnitude / mag;
      }
      return this;
   }

   /**
    * Manipulates the magnitude of this vector.
    *
    * @param op The magnitude manipulation operator
    *
    * @return This vector
    */
   public IntVector manipulateMagnitude(UnaryOperator<Float> op) {
      return setMagnitude(op.apply(getMagnitude()));
   }

   /**
    * @returns the angle from the +x-level to the Vector. The angle-value goes from the plus-x-axis(0° or 360°)
    *          anti-clockwise over the minus-x-axis(180°).
    *          For example: If the Vector is parallel to the x-axis and has a positive x-value(Vector.right()), this
    *          method will return 0. If the Vector is parallel to the y-axis and has a positive y-value(Vector.up()),
    *          this method will return 90. If the Vector is parallel to the x-axis and has a negative x-value(Vector.left()),
    *          this method will return 180. If the Vector is parallel to the y-axis and has a negative y-value(Vector.down()),
    *          this method will return 270.
    */
   public float getAngle() {
      if (getMagnitude() == 0)
         return 0;

      // quadrant I
      if (x >= 0 && y >= 0)
         return (float) (Math.toDegrees(Math.asin(y / getMagnitude())));
      // quadrant II
      else if (x <= 0 && y >= 0)
         return (float) (Math.toDegrees(Math.acos(x / getMagnitude())));
      // quadrant III
      else if (x <= 0 && y <= 0)
         return (float) (180 + Math.toDegrees(Math.abs(Math.asin(y / getMagnitude()))));
      // quadrant IV
      else if (x >= 0 && y <= 0)
         return (float) (270 + Math.toDegrees(Math.asin(x / getMagnitude())));

      return 0;
   }

   /**
    * The angle-value goes from the plus-x-axis(0°) anti-clockwise over the plus-y-axis(90°) and the minus-x-axis(180°)...
    *
    * @param angle Examples: If the Vector should be parallel to the x-axis and should have a positive x-value(Vector.right()),
    *        enter 0 as parameter to this method. If the Vector should be parallel to the y-axis and should have a positive
 *           y-value(Vector.up()), enter 90 as parameter to this method.
 *           If the Vector should be parallel to the x-axis and should have a negative x-value(Vector.left()),
 *           enter 180 as parameter to this method. If the Vector should be parallel to the
 *           y-axis and should have a negative y-value(Vector.down()), enter 270 as parameter to this method.
    * @return this vector
    */
   public IntVector setAngle(float angle) {
      float magnitude = getMagnitude();
      int x = 0;
      int y = 0;

      while (angle < 0)
         angle += 360;

      if (angle >= 360 || angle < 0)
         angle -= 360 * (int) (angle / 360);

      // quadrant I
      if (angle >= 0 && angle <= 90) {
         x = 1;
         y = 1;
      }
      // quadrant II
      else if (angle > 90 && angle <= 180) {
         angle = 180 - angle;
         x = -1;
         y = 1;
      }

      // quadrant III
      else if (angle > 180 && angle <= 270) {
         angle -= 180;
         x = -1;
         y = -1;
      }

      // quadrant IV
      else if (angle > 270 && angle <= 360) {
         angle = 360 - angle;
         x = 1;
         y = -1;
      }

      else
         return this;

      this.x = Math.round((float) (Math.cos(Math.toRadians(angle)) * magnitude * x));
      this.y = Math.round((float) (Math.sin(Math.toRadians(angle)) * magnitude * y));
      return this;
   }

   /**
    * Manipulates the angle of this vector.
    *
    * @param op The angle manipulation operator
    *
    * @return This vector
    */
   public IntVector manipulateAngle(UnaryOperator<Float> op) {
      return setAngle(op.apply(getAngle()));
   }

   /**
    * Takes over the x- and the y-value of the given vector.
    * @param v The Vector of which the values are taken over.
    * @return this vector
    */
   public IntVector setValue(IntVector v) {
      this.x = v.x;
      this.y = v.y;
      return this;
   }

   /**
    * Sets the x- and the y-value.
    *
    * @param x the x coordinate
    * @param y the y coordinate
    *
    * @return this vector
    */
   public IntVector set(int x, int y) {
      this.x = x;
      this.y = y;
      return this;
   }

   /**
    * Manupilates the x and the y value of this vector.
    *
    * @param opX The x manipulation
    * @param opY The y manipulation
    *
    * @return This vector
    */
   public IntVector manipulate(UnaryOperator<Integer> opX, UnaryOperator<Integer> opY) {
      return set(opX.apply(x), opY.apply(y));
   }

   @Override
   public IntVector clone() {
      return new IntVector(x, y);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.x;
      result = prime * result + this.y;
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      IntVector other = (IntVector) obj;
      if (this.x != other.x)
         return false;
      if (this.y != other.y)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "IntVector(" + x + ", " + y + ")";
   }

}