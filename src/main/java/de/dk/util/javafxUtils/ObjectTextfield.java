package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.control.TextField;

/**
 * @author David Koettlitz
 * <br>Erstellt am 21.11.2016
 */
public class ObjectTextfield<T> extends TextField implements ObjectTextControl<T> {
   private ObjectControlAdapter<T> adapter;

   public ObjectTextfield(T object, Function<? super T, String> toStringFunction) {
      this.adapter = new ObjectControlAdapter<>(this, object, toStringFunction);
   }

   public ObjectTextfield(Function<? super T, String> toStringFunction) {
      this(null, toStringFunction);
   }

   public ObjectTextfield(T object) {
      this(object, null);
   }

   public ObjectTextfield() {
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
   public void setAttributeMapper(Function<? super T, String> attributeMapper) {
      adapter.setAttributeMapper(attributeMapper);
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