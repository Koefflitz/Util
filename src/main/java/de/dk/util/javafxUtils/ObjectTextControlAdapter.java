package de.dk.util.javafxUtils;

import java.util.function.Function;

import javafx.scene.layout.Region;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 01.12.2016
 */
public class ObjectTextControlAdapter<C extends Region
                                                & ObjectTextControl<O>
                                                & TextControl, O> extends TextControlAdapter<C>
                                                                  implements ObjectTextControl<O> {

   public ObjectTextControlAdapter(C control) throws NoSuchMethodException, SecurityException {
      super(control);
   }

   @Override
   public void update() {
      region.update();
   }

   @Override
   public Function<? super O, String> getAttributeMapper() {
      return region.getAttributeMapper();
   }

   @Override
   public void setAttributeMapper(Function<? super O, String> toStringFunction) {
      region.setAttributeMapper(toStringFunction);
   }

   @Override
   public O getObject() {
      return region.getObject();
   }

   @Override
   public void setObject(O object) {
      region.setObject(object);
   }

}