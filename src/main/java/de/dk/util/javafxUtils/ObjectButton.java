package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.control.Button;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 21.11.2016
 */
public class ObjectButton<T> extends Button implements ObjectTextControl<T> {
   private ObjectControlAdapter<T> adapter;

   public ObjectButton(T object, Function<? super T, String> toStringFunction) {
      this.adapter = new ObjectControlAdapter<>(this, object, toStringFunction);
   }

   public ObjectButton(Function<? super T, String> toStringFunction) {
      this(null, toStringFunction);
   }

   public ObjectButton(T object) {
      this(object, null);
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