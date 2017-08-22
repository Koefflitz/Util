package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.control.Label;

/**
 * @author David Koettlitz
 * <br>Erstellt am 22.11.2016
 */
public class ObjectLabel<T> extends Label implements ObjectTextControl<T> {
   private ObjectControlAdapter<T> adapter;

   public ObjectLabel(T object, Function<? super T, String> toStringFunction) {
      this.adapter = new ObjectControlAdapter<>(this, object, toStringFunction);
   }

   public ObjectLabel(Function<? super T, String> toStringFunction) {
      this(null, toStringFunction);
   }

   public ObjectLabel(T object) {
      this(object, null);
   }

   public ObjectLabel() {
      this(null, null);
   }

   @Override
   public void update() {
      adapter.update();
   }

   @Override
   public Function<? super T, String> getAttributeMapper() {
      return adapter.getAttributeMapper();
   }

   @Override
   public void setAttributeMapper(Function<? super T, String> toStringFunction) {
      adapter.setAttributeMapper(toStringFunction);
   }

   @Override
   public T getObject() {
      return adapter.getObject();
   }

   @Override
   public void setObject(T object) {
      adapter.setObject(object);
   }
}