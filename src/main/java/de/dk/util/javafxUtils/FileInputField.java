package de.dk.util.javafxUtils;

import java.io.File;
import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

/**
 * @author David Koettlitz
 * <br>Erstellt am 12.12.2016
 */
public class FileInputField extends HBox {
   private TextField txtPath;
   private Button btnBrowse;

   private FileChooser chooser;
   private Consumer<File> fileListener;

   public FileInputField(Consumer<File> fileListener) {
      this.fileListener = fileListener;
      this.txtPath = new TextField();
      this.btnBrowse = new Button("Durchsuchen");
      btnBrowse.setOnAction(e -> browse());

      this.chooser = new FileChooser();
      chooser.setTitle("Datei auswählen");

      getChildren().addAll(txtPath, btnBrowse);
   }

   public FileInputField() {
      this(null);
   }

   private void browse() {
      FileChooser chooser = new FileChooser();
      File file = chooser.showOpenDialog(getScene().getWindow());

      if (file != null) {
         txtPath.setText(file.getAbsolutePath());
         fileListener.accept(file);
      }
   }

   public Consumer<File> getFileListener() {
      return this.fileListener;
   }

   public void setFileListener(Consumer<File> fileListener) {
      this.fileListener = fileListener;
   }

   public String getPath() {
      return txtPath.getText();
   }
}