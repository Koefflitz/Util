package de.dk.util;

import java.io.Serializable;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * Represents a 2-dimensional vector with an x and a y value.
 * Provides getters and setters for some of its properties, e.g. its magnitude or angle.
 *
 * @author David Koettlitz
 * <br>Erstellt am 14.11.2016
 */
public class IntVector implements Cloneable, Serializable {
   private static final long serialVersionUID = -6572827884413220031L;

   protected int x = 1;
   protected int y = 1;


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
    * @param magnitude The magnitude (length) of the IntVector.
    * @param angle The angle-value goes from the plus-x-axis(0° or 360°) anti-clockwise over the minus-x-axis(180°).
    * For example: If the IntVector is parallel to the x-axis and has a positive x-value(IntVector.right()),
    * this method will return 0. If the IntVector is parallel to the y-axis and has a positive y-value(IntVector.up()),
    * this method will return 90. If the IntVector is parallel to the x-axis and has a negative x-value(IntVector.left()),
    * this method will return 180. If the IntVector is parallel to the y-axis and has a negative y-value(IntVector.down()),
    * this method will return 270.
    *
    * @return The new IntVector.
    */
   public static IntVector of(float magnitude, float angle) {
      IntVector v = new IntVector(1, 1);
      v.setAngle(angle);
      v.setMagnitude(magnitude);
      return v;
   }

   /**
    * Creates a new IntVector with the given x value and the given <code>angle</code>.
    *
    * @param x The x value of the vector
    * @param angle The angle of the vector
    *
    * @return The new created vector
    *
    * @throws ArithmeticException if a vector of the given values cannot be created,
    * e.g. a IntVector with an angle of 90° and an x value, that is != 0 is not possible.
    */
   public static IntVector ofX(int x, float angle) throws ArithmeticException {
      AngleCalculation tuple = angleCalculation(angle);
      if (tuple.x == 0)
         throw new ArithmeticException("Cannot create a IntVector of x=" + x + " and an angle of " + angle);

      int magnitude = x / (int) (Math.cos(tuple.angle) * tuple.x);
      int y = (int) (Math.sin(tuple.angle) * magnitude * tuple.y);
      return new IntVector(x, y);
   }

   /**
    * Creates a new IntVector with the given y value and the given <code>angle</code>.
    *
    * @param y The y value of the vector
    * @param angle The angle of the vector
    *
    * @return The new created vector
    *
    * @throws ArithmeticException if a vector of the given values cannot be created,
    * e.g. a IntVector with an angle of 0° and a y value, that is != 0 is not possible.
    */
   public static IntVector ofY(int y, float angle) throws ArithmeticException {
      AngleCalculation tuple = angleCalculation(angle);
      if (tuple.y == 0)
         throw new ArithmeticException("Cannot create a IntVector of y=" + y + " and an angle of " + angle);

      int magnitude = y / (int) (Math.sin(tuple.angle) * tuple.y);
      int x = (int) (Math.cos(tuple.angle) * magnitude * tuple.x);
      return new IntVector(x, y);
   }

   /**
    * Creates a vector which is pointing straight up <code>(0, magnitude)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return A vector pointing up.
    */
   public static IntVector up(int magnitude) {
      return new IntVector(0, magnitude);
   }

   /**
    * Creates a vector which is pointing straight down <code>(0, -magnitude)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing down.
    */
   public static IntVector down(int magnitude) {
      return new IntVector(0, -magnitude);
   }

   /**
    * Creates a vector which is pointing straight left <code>(-magnitude, 0)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing left.
    */
   public static IntVector left(int magnitude) {
      return new IntVector(-magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight right <code>(magnitude, 0)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing right.
    */
   public static IntVector right(int magnitude) {
      return new IntVector(magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight up <code>(0, 1)</code>.
    *
    * @return A vector pointing up.
    */
   public static IntVector up() {
      return up(1);
   }

   /**
    * Creates a vector which is pointing straight down <code>(0, -1)</code>.
    *
    * @return A vector pointing down.
    */
   public static IntVector down() {
      return down(1);
   }

   /**
    * Creates a vector which is pointing straight left <code>(-1, 0)</code>.
    *
    * @return A vector pointing left.
    */
   public static IntVector left() {
      return left(1);
   }

   /**
    * Creates a vector which is pointing straight right <code>(1, 0)</code>.
    *
    * @return A vector pointing right.
    */
   public static IntVector right() {
      return right(1);
   }

   /**
    * Creates a vector, which is rotated 180 degrees to the parameter-vector.
    *
    * @param v The vector to reverse
    *
    * @return A vector with the exact opposite direction of <code>v</code>
    */
   public static IntVector reverse(IntVector v) {
      return new IntVector(-v.x, -v.y);
   }

   /**
    * Creates a new vector, which is the sum of the parameter-vectors.
    *
    * @param a The vector to be added to <code>b</code>
    * @param b The vector to be added to <code>a</code>
    *
    * @return The new vector.
    */
   public static IntVector add(IntVector a, IntVector b) {
      if (a == null || b == null)
         return null;

      return new IntVector(a.x + b.x, a.y + b.y);
   }

   /**
    * Creates a new vector, which is the difference of the parameter-vectors.
    *
    * @param a The vector to subtract <code>b</code> from
    * @param b The vector to be subtracted from <code>a</code>
    *
    * @return The new vector.
    */
   public static IntVector subtract(IntVector a, IntVector b) {
      if (a == null || b == null)
         return null;

      return new IntVector(a.x - b.x, a.y - b.y);
   }

   /**
    * Creates a new vector, whose magnitude is the product of the parameter-vector and the amount.
    *
    * @param v The vector to be multiplied
    * @param amount The amount to multiply with
    *
    * @return The new vector.
    */
   public static IntVector multiply(IntVector v, int amount) {
      if (v == null)
         return null;

      return new IntVector(v.x * amount, v.y * amount);
   }

   /**
    * Creates a new vector, whose magnitude is the quotient of the parameter-vector and the amount.
    *
    * @param v The vector to be divided
    * @param amount the amount to divide through
    *
    * @return The new vector.
    */
   public static IntVector divide(IntVector v, int amount) {
      if (amount == 0)
         throw new ArithmeticException("Division by zero");

      return new IntVector(v.x / amount, v.y / amount);
   }

   /**
    * Calculates the angle between two vectors. Always calculates the smaller angle.
    *
    * @param a The first vector
    * @param b The second vector
    *
    * @return The angle between the two vectors.
    */
   public static float getAngleBetween(IntVector a, IntVector b) {
      float a1 = a.getAngle();
      float a2 = b.getAngle();
      float angle = Math.abs(a1 - a2);
      if (angle <= 180)
         return angle;

      if (a1 > a2)
         return 360f - a1 + a2;
      else
         return 360f - a2 + a1;
   }

   private static AngleCalculation angleCalculation(float angle) {
      // Make sure angle is >= 0 && < 360
      angle = angle % 360;
      if (angle < 0)
         angle += 360;

      int x;
      int y;

      // quadrant I
      if (angle == 0) {
         x = 1;
         y = 0;
      } else if (angle < 90) {
         x = 1;
         y = 1;
      } else if (angle == 90) {
         x = 0;
         y = 1;
      }

      // quadrant II
      else if (angle <= 180) {
         x = -1;
         y = angle == 180 ? 0 : 1;
         angle = 180 - angle;
      }

      // quadrant III
      else if (angle <= 270) {
         x = angle == 270 ? 0 : -1;
         y = -1;
         angle -= 180;
      }

      // quadrant IV
      else if (angle < 360) {
         x = 1;
         y = -1;
         angle = 360 - angle;
      }

      // this method asured, that angle is never < 0 || >= 360
      // so that exception should never be thrown and is just here to satisfy the compiler
      else throw new IllegalStateException("Angle should be >= 0 && < 360, but was: " + angle);

      return new AngleCalculation(x, y, Math.toRadians(angle));
   }

   /**
    * Adds a vector to this vector.
    *
    * @param v The vector that should be added to this vector.
    *
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
    *
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
    *
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
    * @param divisor The amount to divide through
    *
    * @return this vector to go on
    */
   public IntVector divide(float divisor) {
      if (divisor != 0) {
         this.x /= divisor;
         this.y /= divisor;
      }
      return this;
   }

   /**
    * Rotates this vector by 180 degrees,
    * so that it points to the exact opposite direction.
    *
    * @return this vector to go on
    */
   public IntVector reverse() {
      this.x = -x;
      this.y = -y;
      return this;
   }

   /**
    * Get the magnitude (length) of this vector.
    *
    * @return The magnitude (length) of this vector.
    */
   public float getMagnitude() {
      return (float) (Math.sqrt(x * x + y * y));
   }

   /**
    * Set the magnitude (length) of this vector.
    *
    * @param magnitude The new magnitude of this vector.
    *
    * @return this vector to go on
    */
   public IntVector setMagnitude(float magnitude) {
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
    * @return This vector to go on
    */
   public IntVector manipulateMagnitude(UnaryOperator<Float> op) {
      return setMagnitude(op.apply(getMagnitude()));
   }

   /**
    * Calculates the angle from the +x-level to the IntVector. The angle-value goes from the plus-x-axis(0° or 360°)
    * anti-clockwise over the minus-x-axis(180°).
    * For example: If the IntVector is parallel to the x-axis and has a positive x-value(IntVector.right()), this
    * method will return 0. If the IntVector is parallel to the y-axis and has a positive y-value(IntVector.up()),
    * this method will return 90. If the IntVector is parallel to the x-axis and has a negative x-value(IntVector.left()),
    * this method will return 180. If the IntVector is parallel to the y-axis and has a negative y-value(IntVector.down()),
    * this method will return 270.
    *
    * @return The angle of this vector
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
    * @param angle Examples: If the IntVector should be parallel to the x-axis and should have a positive x-value(IntVector.right()),
    * enter 0 as parameter to this method. If the IntVector should be parallel to the y-axis and should have a positive
    * y-value(IntVector.up()), enter 90 as parameter to this method.
    * If the IntVector should be parallel to the x-axis and should have a negative x-value(IntVector.left()),
    * enter 180 as parameter to this method. If the IntVector should be parallel to the
    * y-axis and should have a negative y-value(IntVector.down()), enter 270 as parameter to this method.
    *
    * @return this vector to go on
    */
   public IntVector setAngle(float angle) {
      float magnitude = getMagnitude();
      if (magnitude == 0)
         return this;

      AngleCalculation tuple = angleCalculation(angle);
      this.x = (int) (Math.cos(tuple.angle) * magnitude * tuple.x);
      this.y = (int) (Math.sin(tuple.angle) * magnitude * tuple.y);

      return this;
   }

   /**
    * Manipulates the angle of this vector.
    *
    * @param op The angle manipulation operator
    *
    * @return This vector to go on
    */
   public IntVector manipulateAngle(UnaryOperator<Float> op) {
      return setAngle(op.apply(getAngle()));
   }

   /**
    * Takes over the x- and the y-value of the given vector.
    *
    * @param v The IntVector of which the values are taken over.
    *
    * @return this vector to go on
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
    * @return this vector to go on
    */
   public IntVector set(int x, int y) {
      this.x = x;
      this.y = y;
      return this;
   }

   /**
    * Get the x value of this vector.
    *
    * @return The x value
    */
   public int x() {
      return x;
   }

   /**
    * Get the y value of this vector.
    *
    * @return The y value
    */
   public int y() {
      return y;
   }

   /**
    * Set the x value of this vector.
    *
    * @param x The x value to set
    *
    * @return This IntVector to go on
    */
   public IntVector x(int x) {
      this.x = x;
      return this;
   }

   /**
    * Set the y value of this vector.
    *
    * @param y The y value to set
    *
    * @return This IntVector to go on
    */
   public IntVector y(int y) {
      this.y = y;
      return this;
   }

   /**
    * Manupilates the x and the y value of this vector.
    *
    * @param opX The x manipulation
    * @param opY The y manipulation
    *
    * @return This vector to go on
    */
   public IntVector manipulate(IntUnaryOperator opX, IntUnaryOperator opY) {
      return set(opX.applyAsInt(x), opY.applyAsInt(y));
   }

   /**
    * Manupilates the x value of this vector.
    *
    * @param op The x manipulation
    *
    * @return This vector to go on
    */
   public IntVector manipulateX(IntUnaryOperator op) {
      return x(op.applyAsInt(x()));
   }

   /**
    * Manupilates the y value of this vector.
    *
    * @param op The y manipulation
    *
    * @return This vector to go on
    */
   public IntVector manipulateY(IntUnaryOperator op) {
      return y(op.applyAsInt(y()));
   }

   /**
    * Manipulates the x and the y value with the same function.
    *
    * @param op The operator to manipulate the x and y value
    *
    * @return this vector to go on
    */
   public IntVector manipulate(IntUnaryOperator op) {
      return set(op.applyAsInt(x), op.applyAsInt(y));
   }

   @Override
   public IntVector clone() {
      try {
         return (IntVector) super.clone();
      } catch (CloneNotSupportedException e) {
         String msg = "Error calling super.clone() in IntVector " + this;
         throw new Error(msg, e);
      }
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
      return "IntVector{" + x + ", " + y + "}";
   }

   private static class AngleCalculation {
      int x;
      int y;
      double angle;

      AngleCalculation(int x, int y, double angle) {
         this.x = x;
         this.y = y;
         this.angle = angle;
      }
   }

}