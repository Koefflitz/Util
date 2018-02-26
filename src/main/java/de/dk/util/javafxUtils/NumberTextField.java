package de.dk.util.javafxUtils;

import java.util.function.Consumer;

import de.dk.util.function.ConsumerChain;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A TextField that can only hold numbers, by preventing the user to enter
 * other characters than numbers.
 *
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
   private boolean forceDefaultString = true;

   private final ConsumerChain<String> changeListeners = new ConsumerChain<>();

   public NumberTextField() {
      this(null);
   }

   /**
    * Creates a new NumberTextField with the given <code>defaultString</code>.
    *
    * @param defaultString is displayed when there is no value set yet
    * or the user clears the textfield.
    */
   public NumberTextField(String defaultString) {
      this(0, Integer.MIN_VALUE, Integer.MAX_VALUE, defaultString);
   }

   /**
    * Creates a new NumberTextField with the <code>value</code> as the inital value,
    * a <code>min</code> and a <code>max</code>imum value.
    *
    * @param value The inital value.
    * @param min The minimum value that can be entered
    * @param max The maximum value that can be entered
    */
   public NumberTextField(int value, int min, int max) {
      this(value, min, max, null);
   }

   /**
    * Creates a new NumberTextField with the given initial <code>value</code>,
    * <code>min</code> and <code>max</code> values and a <code>defaultString</code>.
    * If the initial <code>value</code> equals the default value <i>(which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value)</i> the <code>defaultString</code> will be displayed.
    *
    * @param value The inital value.
    * @param min The minimum value that can be entered
    * @param max The maximum value that can be entered
    * @param defaultString is displayed when there is no value set yet
    * or the user clears the textfield.
    */
   public NumberTextField(int value, int min, int max, String defaultString) {
      this(value, min, max, defaultString, true);
   }

   /**
    * Creates a new NumberTextField with the given initial <code>value</code>,
    * <code>min</code> and <code>max</code> values, a <code>defaultString</code>
    * and the <code>arrowKeysEnabled</code> flag.
    * The <code>defaultString</code> is displayed when there is no value set yet
    * or the user clears the textfield.
    * If the initial <code>value</code> equals the default value <i>(which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value)</i> the <code>defaultString</code> will be displayed.
    *
    * @param value The inital value.
    * @param min The minimum value that can be entered
    * @param max The maximum value that can be entered
    * @param defaultString is displayed when there is no value set yet
    * or the user clears the textfield.
    * @param arrowKeysEnabled If set to <code>true</code> the user can increase/decrease the value
    * by using the arrowkeys
    */
   public NumberTextField(int value, int min, int max, String defaultString, boolean arrowKeysEnabled) {
      this.minValue = min;
      this.maxValue = max;
      this.defaultString = defaultString;
      setArrowKeysEnabled(arrowKeysEnabled);
      setValue(value);
      this.forceDefaultString = false;
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
         if (getValue() < Integer.MAX_VALUE)
            setText("" + (getValue() + 1));
      } else if (e.getCode() == KeyCode.DOWN) {
         e.consume();
         if (getValue() > Integer.MIN_VALUE)
            setText("" + (getValue() - 1));
      }
   }

   @Override
   public void changed(ObservableValue<? extends String> val,
                       String old,
                       String newVal) {
      if (getText().equals(defaultString)) {
         changeListeners.accept(defaultString);
         return;
      }

      long value;
      try {
         value = Long.parseLong(getText());
      } catch (NumberFormatException e) {
         setText(old);
         return;
      }
      if (value > maxValue)
         setValue(maxValue);
      else if (value < minValue)
         setValue(minValue);
      else
         changeListeners.accept(getText());
   }

   @Override
   public void replaceText(int start, int end, String text) {
      replacement(text,
                  t -> super.replaceText(start, end, t),
                  end - start == getText().length());
   }

   @Override
   public void replaceSelection(String text) {
      replacement(text,
                  super::replaceSelection,
                  getSelectedText().length() == getText().length());
   }

   private void replacement(String text, Consumer<String> superCall, boolean replaceAll) {
      if (text.isEmpty()) {
         if (replaceAll) {
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

   /**
    * Get the int value of this NumberTextField. If the <code>defaultString</code>
    * is currently displayed, the defaultValue will be returned, which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value.
    *
    * @return the int value represented by the text of this NumberTextField
    */
   public int getValue() {
      try {
         return Integer.parseInt(getText());
      } catch (NumberFormatException e) {
         return getDefaultValue();
      }
   }

   /**
    * Set the value for this NumberTextField. The value is gonna be displayed as text.
    * If the given <code>value</code> equals the defaultValue <i>(which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value)</i> and the <code>forceDefaultString</code> flag
    * is set to <code>true</code> the <code>defaultString</code> will be displayed.
    *
    * @param value The value for this NumberTextField
    */
   public void setValue(int value) {
      String text;
      if (forceDefaultString && value == getDefaultValue() && defaultString != null)
         setText(text = defaultString);
      else
         setText(text = "" + value);

      changeListeners.accept(text);
   }

   /**
    * Sets the <code>arrowKeysEnabled</code> flag. If set to <code>true</code>
    * the user will be able to increase/decrease the value by using the arrowkeys.
    *
    * @param enabled The flag value to set
    */
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

   /**
    * Get if the arrow keys are enabled or not. If set to <code>true</code>
    * the user will be able to increase/decrease the value by using the arrowkeys.
    *
    * @return The <code>arrowKeysEnabled</code> flag
    */
   public boolean isArrowKeysEnabled() {
      return arrowKeysEnabled;
   }

   /**
    * Add a listener to this NumberTextField which will be notified whenever the value changes.
    * This method is preferrable to using this:
    * {@link TextField#textProperty()} {@link StringProperty#addListener(ChangeListener)}
    *
    * @param listener The listener to be notified whenever the value changes.
    */
   public void addChangeListener(Consumer<String> listener) {
      changeListeners.add(listener);
   }

   /**
    * Remove a change listener.
    *
    * @param listener The listener to be removed.
    */
   public void removeChangeListener(Consumer<String> listener) {
      changeListeners.remove(listener);
   }

   /**
    * Get the minimum value, that can be entered into this NumberTextField.
    *
    * @return The minimum value
    */
   public int getMinValue() {
      return minValue;
   }

   /**
    * Set the minimum value, that can be entered into this NumberTextField.
    *
    * @param minValue The minimum value
    */
   public void setMinValue(int minValue) {
      this.minValue = minValue;
   }

   /**
    * Get the maximum value, that can be entered into this NumberTextField.
    *
    * @return The maximum value
    */
   public int getMaxValue() {
      return maxValue;
   }

   /**
    * Set the maximum value, that can be entered into this NumberTextField.
    *
    * @param maxValue The minimum value
    */
   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

   /**
    * Get the defaultString of this NumberTextField.
    * The defaultString is displayed when there is no value set yet
    * or the user clears the textfield.
    *
    * @return the defaultString
    */
   public String getDefaultString() {
      return defaultString;
   }

   /**
    * Set the defaultString of this NumberTextField.
    *
    * @param defaultString is displayed when there is no value set yet
    * or the user clears the textfield.
    */
   public void setDefaultString(String defaultString) {
      this.defaultString = defaultString;
      setValue(getValue());
   }

   /**
    * Get the flag if the set <code>defaultString</code> should always be displayed instead of
    * the defaultValue, which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value.
    *
    * @return the <codeforceDefaultString</code> flag
    */
   public boolean isForceDefaultString() {
      return forceDefaultString;
   }

   /**
    * Get the flag if the set <code>defaultString</code> should always be displayed instead of
    * the defaultValue, which will be 0
    * or in case 0 is out of the range defined by the <code>min</code> and <code>max</code>
    * values it is the <code>min</code> value.
    *
    * @param forceDefaultString The flag whether to force the <code>defaultString</code> to be
    * displayed rather than the <code>defaultValue</code>
    */
   public void setForceDefaultString(boolean forceDefaultString) {
      this.forceDefaultString = forceDefaultString;
   }
}