package de.dk.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 10.11.2016
 */
public final class ReflectionUtils {
   private static final List<Primitive<?>> PRIMITIVES;

   static {
      PRIMITIVES = Arrays.asList(new Primitive<>(Byte.TYPE,
                                                 Byte.class,
                                                 Byte::parseByte,
                                                 (byte) 0),
                                 new Primitive<>(Short.TYPE,
                                                 Short.class,
                                                 Short::parseShort,
                                                 (short) 0),
                                 new Primitive<>(Integer.TYPE,
                                                 Integer.class,
                                                 Integer::parseInt,
                                                 0),
                                 new Primitive<>(Long.TYPE,
                                                 Long.class,
                                                 Long::parseLong,
                                                 0l),
                                 new Primitive<>(Float.TYPE,
                                                 Float.class,
                                                 Float::parseFloat,
                                                 0f),
                                 new Primitive<>(Double.TYPE,
                                                 Double.class,
                                                 Double::parseDouble,
                                                 0d),
                                 new Primitive<>(Character.TYPE,
                                                 Character.class,
                                                 s -> s.charAt(0),
                                                 ' '),
                                 new Primitive<>(Boolean.TYPE,
                                                 Boolean.class,
                                                 Boolean::parseBoolean,
                                                 false),
                                 new Primitive<>(String.class,
                                                 String.class,
                                                 s -> s,
                                                 ""));
   }

   private ReflectionUtils() {}

   /**
    * Finds out recursively whether the class of the <code>sub</code> parameter
    * is a subclass of the <code>sup</code> parameter class.
    *
    * @param sub the subclass
    * @param sup the superclass
    *
    * @return <code>true</code> if <code>sub</code> is the subclass of <code>sup</code>.
    */
   public static boolean isSubclass(Class<?> sub, Class<?> sup) {
      if (sub.equals(sup))
         return true;

      for (Class<?> i : sub.getInterfaces()) {
         if (isSubclass(i, sup))
            return true;
      }

      if (sub.getSuperclass() == null)
         return false;

      return isSubclass(sub.getSuperclass(), sup);
   }

   @SuppressWarnings("unchecked")
   public static <T> T invokeGetter(Object entity, Field field) throws NoSuchMethodException,
                                                                       SecurityException,
                                                                       IllegalAccessException,
                                                                       IllegalArgumentException,
                                                                       InvocationTargetException,
                                                                       ClassCastException {
      String prefix = "get";
      Primitive<?> primitive = Primitive.of(field.getType());
      if (primitive != null)
         prefix = primitive.equals(Primitive.of(Boolean.TYPE)) ? "is" : "get";

      String name = prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
      Method getter = getMethod(entity.getClass(), name);
      return (T) getter.invoke(entity);
   }

   public static void invokeSetter(Object target, Field field, Object value) throws NoSuchMethodException,
                                                                                    SecurityException,
                                                                                    IllegalAccessException,
                                                                                    IllegalArgumentException,
                                                                                    InvocationTargetException {
      String name = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
      Method setter = getMethod(target.getClass(), name, field.getType());
      setter.invoke(target, value);
   }

   public static void invokePrimitiveSetter(Object target,
                                            Field field,
                                            Object value,
                                            Class<?> primitiveClass) throws NoSuchMethodException,
                                                                            SecurityException,
                                                                            IllegalAccessException,
                                                                            IllegalArgumentException,
                                                                            InvocationTargetException {
      String name = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
      Method setter = getMethod(target.getClass(), name, field.getType());
      setter.invoke(target, value);
   }

   public static <E extends Enum<E>> Enum<E> parseEnumValue(Class<Enum<E>> type, String value) throws IllegalArgumentException {
      if (!type.isEnum())
         throw new IllegalArgumentException("Type " + type.getName() + " is not an enum class.");

      for (Enum<E> e : type.getEnumConstants()) {
         if (e.toString().equals(value))
            return e;
      }
      throw new IllegalArgumentException("No enum value found for value " + value + " in enum " + type.getName());
   }

   public static Enum<?> parseUnknownEnumValue(Class<?> type, String value) throws IllegalArgumentException {
      if (!type.isEnum())
         throw new IllegalArgumentException("Type " + type.getName() + " is not an enum class.");

      for (Object e : type.getEnumConstants()) {
         if (e.toString().equals(value))
            return (Enum<?>) e;
      }
      throw new IllegalArgumentException("No enum value found for value " + value + " in enum " + type.getName());
   }

   public static <C extends Collection<Field>> C getAllFieldsOf(Class<?> type, C target) {
      target.addAll(Arrays.asList(type.getDeclaredFields()));
      if (type.getSuperclass() != null)
         return getAllFieldsOf(type.getSuperclass(), target);

      return target;
   }

   public static Field getField(Class<?> type, String fieldName) throws NoSuchFieldException, SecurityException {
      return getMetaObject(type, t -> t.getDeclaredField(fieldName));
   }

   public static Method getMethod(Class<?> type, String methodName, Class<?>... params) throws NoSuchMethodException {
      return getMetaObject(type, (t) -> t.getDeclaredMethod(methodName, params));
   }

   private static <T, E extends Exception> T getMetaObject(Class<?> type,
                                                           UnsafeFunction<Class<?>, ? extends T, E> metaObjectRetriever)
                                                                    throws E {
      try {
         return metaObjectRetriever.apply(type);
      } catch (Exception e) {
         if (type.getSuperclass() == null)
            throw e;

         return getMetaObject(type.getSuperclass(), metaObjectRetriever);
      }
   }

   /**
    * Checks whether the given type is a primitive type (or a String).
    *
    * @param type The type to be checked.
    *
    * @return <code>true</code> if the type is a primitve or a String.
    * <code>false</code> if it's a Type of Object except String.
    */
   public static boolean isPrimitive(Class<?> type) {
      return PRIMITIVES.stream()
                       .anyMatch(p -> p.type.equals(type) || p.boxType.equals(type));
   }

   /**
    * Represents a class of a primitive type (and String) and provides some methods
    * for handling reflective operations with primitive types and values.
    *
    * @author David Koettlitz
    * <br/>Erstellt am 16.12.2017
    */
   public static class Primitive<T> {
      private final Class<T> type;
      private final Class<T> boxType;
      private final Function<String, T> converter;
      private final T defaultValue;

      private Primitive(Class<T> type, Class<T> boxType, Function<String, T> converter, T defaultValue) {
         this.type = type;
         this.boxType = boxType;
         this.converter = converter;
         this.defaultValue = defaultValue;
      }

      /**
       * Provides an instance of a Primitive, that is matching the given primitive type (or String).
       * An object wrapper class of the primitives will also go for a result.
       *
       * @param type The primitive type, object wrapper class of a primitive type or String class
       * @return The primitive instance matching the given primitive type, object wrapper class or String class.
       *         Otherwise <code>null</code> will be returned.
       */
      public static <P> Primitive<P> of(Class<P> type) {
         for (Primitive<?> p : PRIMITIVES) {
            if (p.type.equals(type) || p.boxType.equals(type)) {
               @SuppressWarnings("unchecked")
               Primitive<P> returnVal = (Primitive<P>) p;
               return returnVal;
            }
         }
         return null;
      }

      /**
       * Applies the given value to the field of the target object by invoking the fields setter method.
       *
       * @param target The target object whose field value should be set
       * @param field The field whose value should be set
       * @param value The value that should be set to the field
       */
      public void applyValue(Object target, Field field, String value) throws NoSuchMethodException,
                                                                              SecurityException,
                                                                              IllegalAccessException,
                                                                              IllegalArgumentException,
                                                                              InvocationTargetException {
         invokePrimitiveSetter(target, field, converter.apply(value), type);
      }

      public T parse(String string) {
         return converter.apply(string);
      }

      public Class<T> getType() {
         return type;
      }

      public T getDefaultValue() {
         return defaultValue;
      }
   }

}