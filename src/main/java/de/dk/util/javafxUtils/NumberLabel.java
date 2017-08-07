package de.dk.util.javafxUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class NumberLabel extends Label implements ChangeListener<String> {
   private int minValue;
   private int maxValue;

   private String defaultString;

   {
      textProperty().addListener(this);
   }

   public NumberLabel() {
      this(null);
   }

   public NumberLabel(String defaultString) {
      this(0, Integer.MIN_VALUE, Integer.MAX_VALUE, defaultString);
   }

   public NumberLabel(int value, int min, int max) {
      this(value, min, max, null);
   }

   public NumberLabel(int value, int min, int max, String defaultString) {
      super("" + value);
      this.minValue = min;
      this.maxValue = max;
      this.defaultString = defaultString;
   }

   @Override
   public void changed(ObservableValue<? extends String> val,
                       String old,
                       String newVal) {
      if (newVal.isEmpty())
         return;

      long value;
      try {
         value = Long.parseLong(newVal);
      } catch (NumberFormatException e) {
         try {
            value = Long.parseLong(old);
         } catch (NumberFormatException ex) {
            value = getDefaultValue();
         }
      }
      if (value > maxValue)
         setValue(maxValue);
      else if (value < minValue)
         setValue(minValue);
      else
         setValue((int) value);
   }

   private int getDefaultValue() {
      return minValue > 0 || maxValue < 0 ? minValue : 0;
   }

   public int getValue() {
      try {
         return Integer.parseInt(getText());
      } catch (NumberFormatException e) {
         return getDefaultValue();
      }
   }

   public void setValue(int value) {
      if (defaultString == null || value != getDefaultValue())
         setText("" + value);
      else
         setText(defaultString);
   }

   public int getMinValue() {
      return minValue;
   }

   public void setMinValue(int minValue) {
      this.minValue = minValue;
   }

   public int getMaxValue() {
      return maxValue;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

   public String getDefaultString() {
      return defaultString;
   }

   public void setDefaultString(String defaultString) {
      this.defaultString = defaultString;
      setValue(getValue());
   }

}
