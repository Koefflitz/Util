package de.dk.util.javafxUtils;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class CompletionTextField extends TextField implements TextControl {
   private final SortedSet<String> candidates;
   private ContextMenu popup;

   private boolean caseSensitive = false;

   public CompletionTextField(Set<String> candidates) {
      this.candidates = new TreeSet<>(candidates);
      textProperty().addListener(this::textChanged);
      this.popup = new ContextMenu();
   }

   private void textChanged(ObservableValue<?> val, String old, String newVal) {
      MenuItem[] candidates = this.candidates
                                  .stream()
                                  .filter(c -> match(c, newVal))
                                  .map(this::createEntry)
                                  .toArray(MenuItem[]::new);

      if (candidates.length == 0 || (candidates.length == 1 && candidates[0].equals(newVal))) {
         popup.hide();
         return;
      }

      popup.getItems()
           .setAll(candidates);

      if (getScene() != null && getScene().getWindow() != null)
         popup.show(this, Side.BOTTOM, 0, 0);
   }

   private boolean match(String candidate, String input) {
      if (!caseSensitive) {
         candidate = candidate.toLowerCase();
         input = input.toLowerCase();
      }

      return candidate.startsWith(input);
   }

   private MenuItem createEntry(String text) {
      MenuItem entry = new MenuItem(text);
      entry.setOnAction(e -> setText(text));
      return entry;
   }

   public boolean isCaseSensitive() {
      return this.caseSensitive;
   }

   public void setCaseSensitive(boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
   }

   public void setCandidates(Set<String> players) {
      this.candidates.clear();
      this.candidates.addAll(players);
   }

}
