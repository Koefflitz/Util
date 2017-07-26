package de.dk.util.game;

import java.util.function.UnaryOperator;

/**
 * Represents a 2-dimensional vector with an x and a y value.
 * Provides getters and setters for some of its properties, e.g. its magnitude or angle.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 14.11.2016
 */
public class Vector {
   public float x = 1;
   public float y = 1;

   public Vector(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public Vector() {
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
   public static Vector newVector(float angle, float magnitude) {
      Vector v = new Vector(1, 1);
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
   public static Vector up(float magnitude) {
      return new Vector(0, magnitude);
   }

   /**
    * Creates a vector which is pointing straight down. The vectors x-value is 0,
    * while the y-value gets the negative value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static Vector down(float magnitude) {
      return new Vector(0, -magnitude);
   }

   /**
    * Creates a vector which is pointing straight left. The vectors y-value is 0,
    * while the x-value gets the negative value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static Vector left(float magnitude) {
      return new Vector(-magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight right. The vectors y-value is 0,
    * while the x-value gets the value of the parameter.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return The created vector.
    */
   public static Vector right(float magnitude) {
      return new Vector(magnitude, 0);
   }

   /**
    * Creates a vector, which is rotated 180 degrees to the parameter-vector.
    *
    * @return The vector with the exact opposite direction of the parameter-vector.
    */
   public static Vector reverse(Vector v) {
      return new Vector(-v.x, -v.y);
   }

   /**
    * Creates a new vector, which is the sum of the parameter-vectors.
    *
    * @return The created vector.
    */
   public static Vector add(Vector a, Vector b) {
      if (a == null || b == null)
         return null;

      return new Vector(a.x + b.x, a.y + b.y);
   }

   /**
    * Creates a new vector, which is the difference of the parameter-vectors.
    *
    * @return The created vector.
    */
   public static Vector subtract(Vector a, Vector b) {
      if (a == null || b == null)
         return null;

      return new Vector(a.x - b.x, a.y - b.y);
   }

   /**
    * Creates a new vector, whose magnitude is the product of the parameter-vector and the amount.
    *
    * @return The created vector.
    */
   public static Vector multiply(Vector v, float amount) {
      if (v == null)
         return null;

      return new Vector(v.x * amount, v.y * amount);
   }

   /**
    * Creates a new vector, whose magnitude is the quotient of the parameter-vector and the amount.
    *
    * @return The created vector.
    */
   public static Vector divide(Vector v, float amount) {
      if (amount != 0)
         return new Vector(v.x / amount, v.y / amount);

      return null;
   }

   /**
    * Calculates the angle between two vectors. Always calculates the smaller angle.
    *
    * @return The angle between the two vectors.
    */
   public static float getAngleBetween(Vector v1, Vector v2) {
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
   public Vector add(Vector v) {
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
   public Vector subtract(Vector v) {
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
   public Vector multiply(float amount) {
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
   public Vector divide(float amount) {
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
   public Vector reverse() {
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
   public Vector setMagnitude(float magnitude) {
      if (magnitude == 0) {
         this.x = 0;
         this.y = 0;
      } else {
         float mag = getMagnitude();
         if (mag != 0) {
            this.x *= magnitude / mag;
            this.y *= magnitude / mag;
         }
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
   public Vector manipulateMagnitude(UnaryOperator<Float> op) {
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
   public Vector setAngle(float angle) {
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

      this.x = (float) (Math.cos(Math.toRadians(angle)) * magnitude * x);
      this.y = (float) (Math.sin(Math.toRadians(angle)) * magnitude * y);
      return this;
   }

   /**
    * Manipulates the angle of this vector.
    *
    * @param op The angle manipulation operator
    *
    * @return This vector
    */
   public Vector manipulateAngle(UnaryOperator<Float> op) {
      return setAngle(op.apply(getAngle()));
   }

   /**
    * Takes over the x- and the y-value of the given vector.
    * @param v The Vector of which the values are taken over.
    * @return this vector
    */
   public Vector setValue(Vector v) {
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
   public Vector set(float x, float y) {
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
   public Vector manipulate(UnaryOperator<Float> opX, UnaryOperator<Float> opY) {
      return set(opX.apply(x), opY.apply(y));
   }

   public Vector manipulate(UnaryOperator<Float> op) {
      return set(op.apply(x), op.apply(y));
   }

   @Override
   public Vector clone() {
      return new Vector(x, y);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits(this.x);
      result = prime * result + Float.floatToIntBits(this.y);
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
      Vector other = (Vector) obj;
      if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
         return false;
      if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Vector(" + x + ", " + y + ")";
   }

}