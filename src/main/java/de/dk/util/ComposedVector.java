package de.dk.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * A <code>ComposedVector</code> is an extension of {@link Vector} which consists of
 * multiple <code>Vector</code>s, that are added together.
 * All changes that are made to the part vectors, which the composed vector consists of
 * will affect the composed vector. The parts howerver are not affected by any changes to
 * the composed vector they are added to.
 * Use {@link #addPart(Vector)} to add a vector as a part to the composed vector and
 * {@link #removePart(Vector)} to remove it again.
 *
 * @author David Koettlitz
 * <br>Erstellt am 19.09.2018
 */
public class ComposedVector extends Vector {
   private static final long serialVersionUID = -5983503034347525112L;

   private static final BinaryOperator<Vector> AGGREGATOR = (a, b) -> Vector.add(a, b);

   private final Collection<Vector> parts;

   public ComposedVector(Collection<Vector> parts) {
      this.parts = new LinkedList<>(parts);
   }

   public ComposedVector(Vector... parts) {
      this(Arrays.asList(parts));
   }

   private Vector aggregate(Vector base) {
      return parts.stream()
                  .reduce(base,
                          AGGREGATOR,
                          AGGREGATOR);
   }

   private Vector parts() {
      return aggregate(new Vector());
   }

   private Vector offsets() {
      return new Vector(x, y);
   }

   /**
    * Adds the vector as a part of this composed vector.
    * If <code>part</code> changes, this composed vector will be affected
    * by that change too.
    *
    * @param part the Vector that is gonna be part of this composed vector
    *
    * @throws NullPointerException if <code>part</code> is <code>null</code>
    */
   public void addPart(Vector part) throws NullPointerException {
      parts.add(Objects.requireNonNull(part));
   }

   /**
    * Removes the <code>part</code> of this composed vector.
    *
    * @param part the part of this composed vector to remove
    */
   public void removePart(Vector part) {
      parts.remove(part);
   }

   /**
    * Get the number of part vectors,
    * which this composed vector consists of.
    *
    * @return the number of parts of this vector
    */
   public int partCount() {
      return parts.size();
   }

   @Override
   public float x() {
      return parts.stream()
                  .map(Vector::x)
                  .reduce(x, Float::sum);
   }

   @Override
   public float y() {
      return parts.stream()
                  .map(Vector::y)
                  .reduce(y, Float::sum);
   }

   @Override
   public ComposedVector x(float x) {
      float partsX = parts.stream()
                          .map(Vector::x)
                          .reduce(0f, Float::sum);

      this.x = x - partsX;
      return this;
   }

   @Override
   public ComposedVector y(float y) {
      float partsY = parts.stream()
                          .map(Vector::y)
                          .reduce(0f, Float::sum);

      this.y = y - partsY;
      return this;
   }

   @Override
   public ComposedVector multiply(float amount) {
      Vector parts = parts();
      Vector target = add(parts, offsets()).multiply(amount);
      this.x = target.x() - parts.x();
      this.y = target.y() - parts.y();

      return this;
   }

   @Override
   public ComposedVector divide(float divisor) {
      Vector parts = parts();
      Vector target = add(parts, offsets()).divide(divisor);
      this.x = target.x() - parts.x();
      this.y = target.y() - parts.y();

      return this;
   }

   @Override
   public float getMagnitude() {
      return aggregate(offsets()).getMagnitude();
   }

   @Override
   public ComposedVector setMagnitude(float magnitude) {
      Vector parts = parts();
      if (super.isZero()) {
         super.setValue(Vector.of(magnitude - parts.getMagnitude(), parts.getAngle()));
      } else {
         Vector aggregate = add(parts, offsets());
         Vector target = Vector.of(magnitude - parts.getMagnitude(), aggregate.getAngle());
         super.setValue(target);
      }

      return this;
   }

   @Override
   public ComposedVector manipulateMagnitude(UnaryOperator<Float> op) {
      Vector parts = parts();
      Vector aggregate = add(parts, offsets());
      float magnitude = aggregate.getMagnitude();
      float newMag = op.apply(magnitude);
      float partsMag = parts.getMagnitude();
      super.setValue(Vector.of(newMag - partsMag, aggregate.getAngle()));

      return this;
   }

   @Override
   public float getAngle() {
      return aggregate(offsets()).getAngle();
   }

   @Override
   public ComposedVector setAngle(float angle) {
      Vector parts = parts();
      Vector aggregate = Vector.add(parts, offsets());
      Vector target = Vector.of(aggregate.getMagnitude(), angle);
      super.setValue(target.subtract(parts));

      return this;
   }

   @Override
   public ComposedVector manipulateAngle(UnaryOperator<Float> op) {
      Vector parts = parts();
      Vector aggregate = add(parts, offsets());
      float targetAngle = op.apply(aggregate.getAngle());
      Vector target = Vector.of(aggregate.getMagnitude(), targetAngle);
      super.setValue(target.subtract(parts));

      return this;
   }

   @Override
   public ComposedVector setValue(Vector v) {
      super.setValue(Vector.subtract(v, parts()));

      return this;
   }

   @Override
   public ComposedVector set(float x, float y) {
      Vector parts = parts();
      super.set(x - parts.x, y - parts.y);

      return this;
   }

   @Override
   public ComposedVector manipulateX(UnaryOperator<Float> op) {
      float partsX = parts.stream()
                          .map(Vector::x)
                          .reduce(0f, Float::sum);

      float targetX = op.apply(partsX + this.x);
      this.x = targetX - partsX;

      return this;
   }

   @Override
   public ComposedVector manipulateY(UnaryOperator<Float> op) {
      float partsY = parts.stream()
                          .map(Vector::y)
                          .reduce(0f, Float::sum);

      float targetY = op.apply(partsY + this.y);
      this.y = targetY - partsY;

      return this;
   }

   @Override
   public ComposedVector manipulate(UnaryOperator<Float> opX, UnaryOperator<Float> opY) {
      Vector parts = parts();
      Vector aggregate = add(parts, offsets());

      float targetX = opX.apply(aggregate.x);
      float targetY = opY.apply(aggregate.y);
      this.x = targetX - parts.x;
      this.y = targetY - parts.y;

      return this;
   }

   @Override
   public ComposedVector manipulate(UnaryOperator<Float> op) {
      return manipulate(op, op);
   }

   @Override
   public ComposedVector reverse() {
      Vector parts = parts();
      Vector target = add(parts, offsets()).reverse();
      super.setValue(Vector.subtract(target, parts));

      return this;
   }

   @Override
   public boolean isZero() {
      return aggregate(offsets()).isZero();
   }

   @Override
   public ComposedVector clone() {
      ComposedVector clone = new ComposedVector(parts);
      clone.x = this.x;
      clone.y = this.y;
      return clone;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      Vector aggregate = aggregate(offsets());
      builder.append("ComposedVector(")
             .append(aggregate.x)
             .append(", ")
             .append(aggregate.y)
             .append(") xOffset=")
             .append(x)
             .append(", yOffset=")
             .append(y)
             .append(" parts[ ");

      Iterator<Vector> iter = parts.iterator();
      while (iter.hasNext()) {
         Vector part = iter.next();
         builder.append("(")
                .append(part.x)
                .append(", ")
                .append(part.y)
                .append(')');

         if (iter.hasNext())
            builder.append(',');
      }

      builder.append("]");

      return builder.toString();
   }
}
