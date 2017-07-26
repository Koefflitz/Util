package de.dk.util.javafxUtils.event;

import java.util.HashSet;
import java.util.Set;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 04.10.2016
 */
public class TypedEventHandler<T extends Event> {
   private EventType<T> type;
   private EventHandler<? super T> handler;

   private Set<EventSource> added = new HashSet<>();
   private Set<EventSource> addedAsFilter = new HashSet<>();

   public TypedEventHandler(EventType<T> type, EventHandler<? super T> handler) {
      this.type = type;
      this.handler = handler;
   }

   public void cleanUp() {
      added.forEach(n -> n.removeEventHandler(type, handler));
      addedAsFilter.forEach(n -> n.removeEventFilter(type, handler));
      added.clear();
      addedAsFilter.clear();
   }

   public void addTo(EventSource target) {
      target.addEventHandler(type, handler);
      added.add(target);
   }

   public void addAsFilterTo(EventSource target) {
      target.addEventFilter(type, handler);
      addedAsFilter.add(target);
   }

   public void removeFrom(EventSource target) {
      target.removeEventHandler(type, handler);
      added.remove(target);
   }

   public void removeAsFilterFrom(EventSource target) {
      target.removeEventFilter(type, handler);
      addedAsFilter.remove(target);
   }

   public EventType<T> getType() {
      return type;
   }

   public void setType(EventType<T> type) {
      this.type = type;
   }

   public EventHandler<? super T> getHandler() {
      return handler;
   }

   public void setHandler(EventHandler<? super T> handler) {
      this.handler = handler;
   }

   public <E extends Event> boolean matches(EventType<? extends E> eventType, EventHandler<? super E> eventHandler) {
      return eventType.equals(type) && eventHandler == handler;
   }

}