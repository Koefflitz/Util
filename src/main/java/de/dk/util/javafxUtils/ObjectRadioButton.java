package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.control.RadioButton;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 06.12.2016
 */
public class ObjectRadioButton<T> extends RadioButton implements ObjectControl<T, Boolean> {
   private T object;

   private Function<? super T, Boolean> booleanMapper;

   public ObjectRadioButton(T object, Function<T, Boolean> booleanMapper) {
      this.object = object;
      this.booleanMapper = booleanMapper;
   }

   public ObjectRadioButton(Function<T, Boolean> booleanMapper) {
      this(null, booleanMapper);
   }

   @Override
   public void update() {
      setSelected(booleanMapper.apply(object));
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

   @Override
   public Function<? super T, Boolean> getAttributeMapper() {
      return booleanMapper;
   }

   @Override
   public void setAttributeMapper(Function<? super T, Boolean> booleanMapper) {
      this.booleanMapper = booleanMapper;
      update();
   }

}