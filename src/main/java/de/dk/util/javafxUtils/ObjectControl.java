package de.dk.util.javafxUtils;

import java.util.function.Function;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 21.11.2016
 */
public interface ObjectControl<T, V> {
   public void update();
   public Function<? super T, V> getAttributeMapper();
   public void setAttributeMapper(Function<? super T, V> attributeMapper);
   public T getObject();
   public void setObject(T object);
}