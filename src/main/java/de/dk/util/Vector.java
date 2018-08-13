package de.dk.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Represents a 2-dimensional vector with an x and a y value.
 * Provides getters and setters for some of its properties, e.g. its magnitude or angle.
 *
 * @author David Koettlitz
 * <br>Erstellt am 14.11.2016
 */
public class Vector implements Cloneable, Serializable {
   private static final long serialVersionUID = -6572827884413220031L;
   private static float equalsDelta = 0.001f;

   protected float x = 1;
   protected float y = 1;


   public Vector(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public Vector() {
      this(0, 0);
   }

   /**
    * Get the tolerance delta to be used for the floating point value
    * comparison of x and y by the {@link #equals(Object)} method.
    *
    * @return The delta for float comparisons.
    */
   public static float getEqualsDelta() {
      return equalsDelta;
   }

   /**
    * Set the tolerance delta to be used for the floating point value
    * comparison of x and y by the {@link #equals(Object)} method.
    *
    * @param floatDelta the delta for float comparisons.
    */
   public static void setEqualsDelta(float floatDelta) {
      Vector.equalsDelta = floatDelta;
   }

   /**
    * Creates a new vector by angle and magnitude.
    *
    * @param magnitude The magnitude (length) of the Vector.
    * @param angle The angle-value goes from the plus-x-axis(0° or 360°) anti-clockwise over the minus-x-axis(180°).
    * For example: If the Vector is parallel to the x-axis and has a positive x-value(Vector.right()),
    * this method will return 0. If the Vector is parallel to the y-axis and has a positive y-value(Vector.up()),
    * this method will return 90. If the Vector is parallel to the x-axis and has a negative x-value(Vector.left()),
    * this method will return 180. If the Vector is parallel to the y-axis and has a negative y-value(Vector.down()),
    * this method will return 270.
    *
    * @return The new Vector.
    */
   public static Vector of(float magnitude, float angle) {
      Vector v = new Vector(1, 1);
      v.setAngle(angle);
      v.setMagnitude(magnitude);
      return v;
   }

   /**
    * Creates a new Vector with the given x value and the given <code>angle</code>.
    *
    * @param x The x value of the vector
    * @param angle The angle of the vector
    *
    * @return The new created vector
    *
    * @throws ArithmeticException if a vector of the given values cannot be created,
    * e.g. a Vector with an angle of 90° and an x value, that is != 0 is not possible.
    */
   public static Vector ofX(float x, float angle) throws ArithmeticException {
      AngleCalculation tuple = angleCalculation(angle);
      if (tuple.x == 0)
         throw new ArithmeticException("Cannot create a Vector of x=" + x + " and an angle of " + angle);

      float magnitude = x / (float) (Math.cos(tuple.angle) * tuple.x);
      float y = (float) (Math.sin(tuple.angle) * magnitude * tuple.y);
      return new Vector(x, y);
   }

   /**
    * Creates a new Vector with the given y value and the given <code>angle</code>.
    *
    * @param y The y value of the vector
    * @param angle The angle of the vector
    *
    * @return The new created vector
    *
    * @throws ArithmeticException if a vector of the given values cannot be created,
    * e.g. a Vector with an angle of 0° and a y value, that is != 0 is not possible.
    */
   public static Vector ofY(float y, float angle) throws ArithmeticException {
      AngleCalculation tuple = angleCalculation(angle);
      if (tuple.y == 0)
         throw new ArithmeticException("Cannot create a Vector of y=" + y + " and an angle of " + angle);

      float magnitude = y / (float) (Math.sin(tuple.angle) * tuple.y);
      float x = (float) (Math.cos(tuple.angle) * magnitude * tuple.x);
      return new Vector(x, y);
   }

   /**
    * Creates a vector which is pointing straight up <code>(0, magnitude)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    * @return A vector pointing up.
    */
   public static Vector up(float magnitude) {
      return new Vector(0, magnitude);
   }

   /**
    * Creates a vector which is pointing straight down <code>(0, -magnitude)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing down.
    */
   public static Vector down(float magnitude) {
      return new Vector(0, -magnitude);
   }

   /**
    * Creates a vector which is pointing straight left <code>(-magnitude, 0)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing left.
    */
   public static Vector left(float magnitude) {
      return new Vector(-magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight right <code>(magnitude, 0)</code>.
    *
    * @param magnitude The magnitude (length) of the vector.
    *
    * @return A vector pointing right.
    */
   public static Vector right(float magnitude) {
      return new Vector(magnitude, 0);
   }

   /**
    * Creates a vector which is pointing straight up <code>(0, 1)</code>.
    *
    * @return A vector pointing up.
    */
   public static Vector up() {
      return up(1);
   }

   /**
    * Creates a vector which is pointing straight down <code>(0, -1)</code>.
    *
    * @return A vector pointing down.
    */
   public static Vector down() {
      return down(1);
   }

   /**
    * Creates a vector which is pointing straight left <code>(-1, 0)</code>.
    *
    * @return A vector pointing left.
    */
   public static Vector left() {
      return left(1);
   }

   /**
    * Creates a vector which is pointing straight right <code>(1, 0)</code>.
    *
    * @return A vector pointing right.
    */
   public static Vector right() {
      return right(1);
   }

   /**
    * Determines the vector with the less magnitude.
    *
    * @param a one vector
    * @param b another vector
    *
    * @return the vector with the less magnitude
    *
    * @throws NullPointerException if one of the arguments is <code>null</code>.
    */
   public static Vector min(Vector a, Vector b) throws NullPointerException {
      Objects.requireNonNull(a);
      Objects.requireNonNull(b);
      return a.getMagnitude() < b.getMagnitude() ? a : b;
   }

   /**
    * Determines the vector with the greater magnitude.
    *
    * @param a one vector
    * @param b another vector
    *
    * @return the vector with the greater magnitude
    *
    * @throws NullPointerException if one of the arguments is <code>null</code>.
    */
   public static Vector max(Vector a, Vector b) {
      Objects.requireNonNull(a);
      Objects.requireNonNull(b);
      return a.getMagnitude() > b.getMagnitude() ? a : b;
   }

   /**
    * Creates a vector, which is rotated 180 degrees to the parameter-vector.
    *
    * @param v The vector to reverse
    *
    * @return A vector with the exact opposite direction of <code>v</code>
    */
   public static Vector reverse(Vector v) {
      return new Vector(-v.x, -v.y);
   }

   /**
    * Creates a new vector, which is the sum of the parameter-vectors.
    *
    * @param a The vector to be added to <code>b</code>
    * @param b The vector to be added to <code>a</code>
    *
    * @return The new vector.
    */
   public static Vector add(Vector a, Vector b) {
      if (a == null || b == null)
         return null;

      return new Vector(a.x + b.x, a.y + b.y);
   }

   /**
    * Creates a new vector, which is the difference of the parameter-vectors.
    *
    * @param a The vector to subtract <code>b</code> from
    * @param b The vector to be subtracted from <code>a</code>
    *
    * @return The new vector.
    */
   public static Vector subtract(Vector a, Vector b) {
      if (a == null || b == null)
         return null;

      return new Vector(a.x - b.x, a.y - b.y);
   }

   /**
    * Creates a new vector, whose magnitude is the product of the parameter-vector and the amount.
    *
    * @param v The vector to be multiplied
    * @param amount The amount to multiply with
    *
    * @return The new vector.
    */
   public static Vector multiply(Vector v, float amount) {
      if (v == null)
         return null;

      return new Vector(v.x * amount, v.y * amount);
   }

   /**
    * Creates a new vector, whose magnitude is the quotient of the parameter-vector and the amount.
    *
    * @param v The vector to be divided
    * @param amount the amount to divide through
    *
    * @return The new vector.
    */
   public static Vector divide(Vector v, float amount) {
      if (amount == 0)
         throw new ArithmeticException("Division by zero");

      return new Vector(v.x / amount, v.y / amount);
   }

   /**
    * Calculates the angle between two vectors. Always calculates the smaller angle.
    *
    * @param a The first vector
    * @param b The second vector
    *
    * @return The angle between the two vectors.
    */
   public static float getAngleBetween(Vector a, Vector b) {
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
    *
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
    *
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
    * @param divisor The amount to divide through
    *
    * @return this vector to go on
    */
   public Vector divide(float divisor) {
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
   public Vector reverse() {
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
    * @return This vector to go on
    */
   public Vector manipulateMagnitude(UnaryOperator<Float> op) {
      return setMagnitude(op.apply(getMagnitude()));
   }

   /**
    * Determines wether this vector has a magnitude or not
    *
    * @return <code>false</code> if x and y are both 0,
    * <code>true</code> otherwise
    */
   public boolean isZero() {
      return x == 0 && y == 0;
   }

   /**
    * Calculates the angle from the +x-level to the Vector. The angle-value goes from the plus-x-axis(0° or 360°)
    * anti-clockwise over the minus-x-axis(180°).
    * For example: If the Vector is parallel to the x-axis and has a positive x-value(Vector.right()), this
    * method will return 0. If the Vector is parallel to the y-axis and has a positive y-value(Vector.up()),
    * this method will return 90. If the Vector is parallel to the x-axis and has a negative x-value(Vector.left()),
    * this method will return 180. If the Vector is parallel to the y-axis and has a negative y-value(Vector.down()),
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
    * @param angle Examples: If the Vector should be parallel to the x-axis and should have a positive x-value(Vector.right()),
    * enter 0 as parameter to this method. If the Vector should be parallel to the y-axis and should have a positive
    * y-value(Vector.up()), enter 90 as parameter to this method.
    * If the Vector should be parallel to the x-axis and should have a negative x-value(Vector.left()),
    * enter 180 as parameter to this method. If the Vector should be parallel to the
    * y-axis and should have a negative y-value(Vector.down()), enter 270 as parameter to this method.
    *
    * @return this vector to go on
    */
   public Vector setAngle(float angle) {
      float magnitude = getMagnitude();
      if (magnitude == 0)
         return this;

      AngleCalculation tuple = angleCalculation(angle);
      this.x = (float) (Math.cos(tuple.angle) * magnitude * tuple.x);
      this.y = (float) (Math.sin(tuple.angle) * magnitude * tuple.y);

      return this;
   }

   /**
    * Manipulates the angle of this vector.
    *
    * @param op The angle manipulation operator
    *
    * @return This vector to go on
    */
   public Vector manipulateAngle(UnaryOperator<Float> op) {
      return setAngle(op.apply(getAngle()));
   }

   /**
    * Takes over the x- and the y-value of the given vector.
    *
    * @param v The Vector of which the values are taken over.
    *
    * @return this vector to go on
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
    * @return this vector to go on
    */
   public Vector set(float x, float y) {
      this.x = x;
      this.y = y;
      return this;
   }

   /**
    * Get the x value of this vector.
    *
    * @return The x value
    */
   public float x() {
      return x;
   }

   /**
    * Get the y value of this vector.
    *
    * @return The y value
    */
   public float y() {
      return y;
   }

   /**
    * Set the x value of this vector.
    *
    * @param x The x value to set
    *
    * @return This Vector to go on
    */
   public Vector x(float x) {
      this.x = x;
      return this;
   }

   /**
    * Set the y value of this vector.
    *
    * @param y The y value to set
    *
    * @return This Vector to go on
    */
   public Vector y(float y) {
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
   public Vector manipulate(UnaryOperator<Float> opX, UnaryOperator<Float> opY) {
      return set(opX.apply(x), opY.apply(y));
   }

   /**
    * Manupilates the x value of this vector.
    *
    * @param op The x manipulation
    *
    * @return This vector to go on
    */
   public Vector manipulateX(UnaryOperator<Float> op) {
      return x(op.apply(x()));
   }

   /**
    * Manupilates the y value of this vector.
    *
    * @param op The y manipulation
    *
    * @return This vector to go on
    */
   public Vector manipulateY(UnaryOperator<Float> op) {
      return y(op.apply(y()));
   }

   /**
    * Manipulates the x and the y value with the same function.
    *
    * @param op The operator to manipulate the x and y value
    *
    * @return this vector to go on
    */
   public Vector manipulate(UnaryOperator<Float> op) {
      return set(op.apply(x), op.apply(y));
   }

   @Override
   public Vector clone() {
      try {
         return (Vector) super.clone();
      } catch (CloneNotSupportedException e) {
         String msg = "Error cloning this vector. "
                      + "This error should never occur.";
         throw new Error(msg, e);
      }
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits(this.x);
      result = prime * result + Float.floatToIntBits(this.y);
      return result;
   }

   /**
    * Checks if the x and y value are equal.
    * The comparison of the floating point values
    * is done by tolerating a delta that is accessible by
    * {@link #setEqualsDelta(float)} and {@link #getEqualsDelta()}.
    *
    * @see #equals(Vector, float)
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;

      Vector other = (Vector) obj;
      return equals(other, equalsDelta);
   }

   /**
    * This method is equal to the {@link #equals(Object)} method,
    * except that the <code>delta</code> of the comparisons of x and y
    * is customly provided by parameter.
    *
    * @param other The other vector to test whether it is equal to this one
    * @param delta The delta to use for the floating point value comparison
    * of x and y.
    *
    * @return <code>true</code> if this vector is equal to <code>other</code>
    */
   public boolean equals(Vector other, float delta) {
      if (this == other)
         return true;
      if (other == null)
         return false;
      if (Math.abs(x - other.x) > equalsDelta)
         return false;
      if (Math.abs(y - other.y) > equalsDelta)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Vector{" + x + ", " + y + "}";
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