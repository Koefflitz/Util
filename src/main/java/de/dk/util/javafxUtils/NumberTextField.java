package de.dk.util.javafxUtils;

import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author David Koettlitz
 * <br>Erstellt am 29.08.2016
 */
public class NumberTextField extends TextField
                             implements EventHandler<KeyEvent>,
                                        ChangeListener<String> {
   private int minValue;
   private int maxValue;

   private String defaultString;
   private boolean arrowKeysEnabled;

   public NumberTextField() {
      this(null);
   }

   public NumberTextField(String defaultString) {
      this(0, Integer.MIN_VALUE, Integer.MAX_VALUE, defaultString);
   }

   public NumberTextField(int value, int min, int max) {
      this(value, min, max, null);
   }

   public NumberTextField(int value, int min, int max, String defaultString) {
      this(value, min, max, defaultString, true);
   }

   public NumberTextField(int value, int min, int max, String defaultString, boolean arrowKeysEnabled) {
      this.minValue = min;
      this.maxValue = max;
      this.defaultString = defaultString;
      setArrowKeysEnabled(arrowKeysEnabled);
      setValue(value);
   }

   private static boolean isValid(String text) {
      if (text.isEmpty())
         return true;

      try {
         Integer.parseInt(text);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }

   @Override
   public void handle(KeyEvent e) {
      if (e.getCode() == KeyCode.UP) {
         e.consume();
         setText("" + (getValue() + 1));
      } else if (e.getCode() == KeyCode.DOWN) {
         e.consume();
         setText("" + (getValue() - 1));
      }
   }

   @Override
   public void changed(ObservableValue<? extends String> val,
                       String old,
                       String newVal) {
      long value;
      try {
         value = Long.parseLong(getText());
      } catch (NumberFormatException e) {
         value = getDefaultValue();
      }
      if (value > maxValue)
         setValue(maxValue);
      else if (value < minValue)
         setValue(minValue);
   }

   @Override
   public void replaceText(int start, int end, String text) {
      replacement(text, txt -> super.replaceText(start, end, txt));
   }

   @Override
   public void replaceSelection(String text) {
      replacement(text, super::replaceSelection);
   }

   private void replacement(String text, Consumer<String> superCall) {
      if (text.isEmpty()) {
         if (getText().length() < 2) {
            superCall.accept(defaultString);
            selectAll();
            return;
         }
      }
      if (isValid(text))
         superCall.accept(text);
   }

   private int getDefaultValue() {
      if (minValue > 0 || maxValue < 0)
         return minValue;

      return 0;
   }

   public int getValue() {
      try {
         return Integer.parseInt(getText());
      } catch (NumberFormatException e) {
         return getDefaultValue();
      }
   }

   public void setValue(int value) {
      if (value != getDefaultValue() || defaultString == null)
         setText("" + value);
      else
         setText(defaultString);
   }

   public void setArrowKeysEnabled(boolean enabled) {
      if (enabled == this.arrowKeysEnabled)
         return;

      if (enabled) {
         addEventFilter(KeyEvent.KEY_PRESSED, this);
         textProperty().addListener(this);
      } else {
         removeEventFilter(KeyEvent.KEY_PRESSED, this);
         textProperty().removeListener(this);
      }
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