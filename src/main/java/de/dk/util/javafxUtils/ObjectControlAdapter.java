package de.dk.util.javafxUtils;

import java.util.function.Function;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 21.11.2016
 */
public class ObjectControlAdapter<T> implements ObjectControl<T, String> {
   private TextControl control;
   private T object;
   private Function<? super T, String> toStringFunction;

   public ObjectControlAdapter(TextControl control, T object, Function<? super T, String> toStringFunction) {
      this.control = control;
      this.object = object;
      setAttributeMapper(toStringFunction);
   }

   public ObjectControlAdapter(TextControl control, T object) {
      this(control, object, null);
   }

   @Override
   public void update() {
      control.setText(object == null ? "" :
         toStringFunction == null ? object.toString() :
            toStringFunction.apply(object));
   }

   @Override
   public Function<? super T, String> getAttributeMapper() {
      return toStringFunction;
   }

   @Override
   public void setAttributeMapper(Function<? super T, String> toStringFunction) {
      this.toStringFunction = toStringFunction;
      update();
   }

   @Override
   public T getObject() {
      return object;
   }

   @Override
   public void setObject(T object) {
      this.object = object;
      update();
   }

}