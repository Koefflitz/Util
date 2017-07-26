package de.dk.util.javafxUtils.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Window;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 27.10.2016
 */
public class WindowEventSourceAdapter implements EventSource {
   private final Window window;

   public WindowEventSourceAdapter(Window window) {
      this.window = window;
   }

   @Override
   public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
      window.addEventHandler(eventType, eventHandler);
   }

   @Override
   public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
      window.removeEventHandler(eventType, eventHandler);
   }

   @Override
   public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
      window.addEventFilter(eventType, eventFilter);
   }

   @Override
   public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
      window.removeEventFilter(eventType, eventFilter);
   }

}