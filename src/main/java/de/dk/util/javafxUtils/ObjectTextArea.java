package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.control.TextArea;

/**
 * @author David Koettlitz
 * <br>Erstellt am 05.12.2016
 */
public class ObjectTextArea<T> extends TextArea implements ObjectTextControl<T> {
   private ObjectControlAdapter<T> adapter;

   public ObjectTextArea(T object, Function<? super T, String> toStringFunction) {
      this.adapter = new ObjectControlAdapter<>(this, object, toStringFunction);
   }

   public ObjectTextArea(Function<? super T, String> toStringFunction) {
      this(null, toStringFunction);
   }

   public ObjectTextArea(T object) {
      this(object, null);
   }

   public ObjectTextArea() {
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