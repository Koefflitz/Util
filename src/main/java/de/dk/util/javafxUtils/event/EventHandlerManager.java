package de.dk.util.javafxUtils.event;

import java.util.LinkedList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 04.10.2016
 */
public class EventHandlerManager {
   private final EventSource target;
   private final List<TypedEventHandler<?>> handlers = new LinkedList<>();

   public EventHandlerManager(EventSource target) {
      this.target = target;
   }

   public static EventHandlerManager instanceFor(Node node) {
      return new EventHandlerManager(new NodeEventSourceAdapter(node));
   }

   public static EventHandlerManager instanceFor(Window window) {
      return new EventHandlerManager(new WindowEventSourceAdapter(window));
   }

   public void cleanUp() {
      handlers.forEach(TypedEventHandler::cleanUp);
      handlers.clear();
   }

   public <T extends Event> void addEventHandler(EventType<T> eventType,
                                                 EventHandler<? super T> eventHandler) {

      TypedEventHandler<T> handler = new TypedEventHandler<>(eventType, eventHandler);
      handler.addTo(target);
      handlers.add(handler);
   }

   public <T extends Event> void addEventFilter(EventType<T> eventType,
                                                EventHandler<? super T> eventHandler) {

      TypedEventHandler<T> handler = new TypedEventHandler<>(eventType, eventHandler);
      handler.addAsFilterTo(target);
      handlers.add(handler);
   }

   public <T extends Event> void removeEventHandler(EventType<T> eventType,
                                                    EventHandler<? super T> eventHandler) {

      handlers.stream()
              .filter(h -> h.matches(eventType, eventHandler))
              .findAny()
              .ifPresent(h -> h.removeFrom(target));
   }

   public <T extends Event> void removeEventFilter(EventType<T> eventType,
                                                   EventHandler<? super T> eventHandler) {

      handlers.stream()
              .filter(h -> h.matches(eventType, eventHandler))
              .findAny()
              .ifPresent(h -> h.removeAsFilterFrom(target));
   }

}