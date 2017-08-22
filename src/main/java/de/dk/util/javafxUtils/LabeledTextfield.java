package de.dk.util.javafxUtils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * A {@link VBox} extension that contains a Label and a Textfield.
 *
 * @author David Koettlitz
 * <br>Erstellt am 29.08.2016
 */
public class LabeledTextfield extends VBox {
   private Label label;
   private TextField textfield;

   public LabeledTextfield(String text, String value) {
      setLabel(new Label(text));
      setTextfield(new TextField(value));
   }

   public LabeledTextfield(String text) {
      this(text, null);
   }

   public LabeledTextfield() {
      this(null, null);
   }

   public void setText(String text) {
      label.setText(text);
   }

   public String getText() {
      return label.getText();
   }

   public String getValue() {
      return textfield.getText();
   }

   public void setValue(String value) {
      textfield.setText(value);
   }

   public Label getLabel() {
      return label;
   }

   public void setLabel(Label label) {
      if (this.label != null)
         getChildren().remove(this.label);

      this.label = label;

      if (label != null)
         getChildren().add(label);
   }

   public TextField getTextfield() {
      return textfield;
   }

   public void setTextfield(TextField textfield) {
      if (this.textfield != null)
         getChildren().remove(this.textfield);

      this.textfield = textfield;

      if (textfield != null)
         getChildren().add(textfield);
   }

}