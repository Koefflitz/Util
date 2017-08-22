package de.dk.util.javafxUtils.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * @author David Koettlitz
 * <br>Erstellt am 27.10.2016
 */
public interface EventSource {
   public <T extends Event> void addEventHandler(EventType<T> eventType,
                                                 EventHandler<? super T> eventHandler);

   public <T extends Event> void removeEventHandler(EventType<T> eventType,
                                                    EventHandler<? super T> eventHandler);

   public <T extends Event> void addEventFilter(EventType<T> eventType,
                                                EventHandler<? super T> eventFilter);

   public <T extends Event> void removeEventFilter(EventType<T> eventType,
                                                   EventHandler<? super T> eventFilter);
}