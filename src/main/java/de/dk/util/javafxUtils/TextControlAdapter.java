package de.dk.util.javafxUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.layout.Region;

/**
 * @author David Koettlitz
 * <br>Erstellt am 01.12.2016
 */
public class TextControlAdapter<R extends Region> implements TextControl {
   private static final Map<String, Class<?>[]> METHODS = new HashMap<>();

   protected R region;

   static {
      METHODS.put("setText", new Class[] {String.class});
      METHODS.put("getText", null);
   }

   public TextControlAdapter(R region) throws NoSuchMethodException, SecurityException {
      setControl(region);
   }

   @Override
   public void setText(String text) {
      try {
         region.getClass()
               .getMethod("setText", String.class)
               .invoke(region, text);
      } catch (IllegalAccessException
               | IllegalArgumentException
               | InvocationTargetException
               | NoSuchMethodException
               | SecurityException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public String getText() {
      try {
         return (String) region.getClass()
                               .getMethod("getText")
                               .invoke(region);
      } catch (IllegalAccessException
               | IllegalArgumentException
               | InvocationTargetException
               | NoSuchMethodException
               | SecurityException e) {
         throw new RuntimeException(e);
      }
   }

   public R getControl() {
      return region;
   }

   public void setControl(R region) throws NoSuchMethodException, SecurityException {
      this.region = region;
      for (Entry<String, Class<?>[]> e : METHODS.entrySet())
         region.getClass().getMethod(e.getKey(), e.getValue());
   }

}