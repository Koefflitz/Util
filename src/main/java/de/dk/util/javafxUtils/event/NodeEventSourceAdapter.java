package de.dk.util.javafxUtils.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 27.10.2016
 */
public class NodeEventSourceAdapter implements EventSource {
   private Node node;

   public NodeEventSourceAdapter(Node node) {
      this.node = node;
   }

   @Override
   public final <T extends Event> void addEventHandler(EventType<T> eventType,
                                                       EventHandler<? super T> eventHandler) {
      node.addEventHandler(eventType, eventHandler);
   }

   @Override
   public final <T extends Event> void removeEventHandler(EventType<T> eventType,
                                                          EventHandler<? super T> eventHandler) {
      node.removeEventHandler(eventType, eventHandler);
   }

   @Override
   public final <T extends Event> void addEventFilter(EventType<T> eventType,
                                                      EventHandler<? super T> eventFilter) {
      node.addEventFilter(eventType, eventFilter);
   }

   @Override
   public final <T extends Event> void removeEventFilter(EventType<T> eventType,
                                                         EventHandler<? super T> eventFilter) {
      node.removeEventFilter(eventType, eventFilter);
   }

}