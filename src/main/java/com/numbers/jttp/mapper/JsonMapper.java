package com.numbers.jttp.mapper;

import java.io.*;
import java.util.*;

/**
 * a wrapper interface intended to be implemented by other frameworks that
 * specialize in JSON parsing and object mapping. An instance of this object is
 * required to use the JSON mapping available in JTTP.
 *
 * @author Numbers
 */
public interface JsonMapper {

    /**
     * maps the given inputStream to an object of the given class
     *
     * @param <T>        the type of the return object
     * @param json       the body inputstream
     * @param valueClass the class to which the inputstream has to be mapped to
     * @return an instance of the given valueClass with properties bound from
     *         the JSON
     */
    <T> T readValue(InputStream json, Class<T> valueClass);

    /**
     * maps the given inputstream to a {@link java.util.Collection Collection}
     * class of objects given as parameters
     *
     * @param <T>             the type of generic for the returned collection
     * @param <C>             the collection instanced from the supplied
     *                        collectionClass
     * @param json            the body inputstream
     * @param collectionClass the collection class to instantiate
     * @param valueClass      the class to which the inputstream has to be
     *                        mapped to
     * @return a collection of instances of the given valueClass with properties
     *         bound from the JSON
     */
    <T, C extends Collection> C readValues(InputStream json,
                                           Class<C> collectionClass,
                                           Class<T> valueClass);

    /**
     * maps the given inputstream to a {@link java.util.Map Map} class of
     * key-value given as parameters
     *
     * @param <M>        the Map class returned
     * @param <K>        the Key class of the Map
     * @param <V>        the Value class of the Map
     * @param json       the body inputstream
     * @param mapClass   the Map class to instantiate
     * @param keyClass   the Key class to instantiate
     * @param valueClass the Value class to instantiate
     * @return a map of instances of the given keyClass and valueClass with
     *         properties bound from the JSON
     */
    <M extends Map<K, V>, K, V> Map<K, V> readValues(InputStream json,
                                                     Class<M> mapClass,
                                                     Class<K> keyClass,
                                                     Class<V> valueClass);

    /**
     * converts the given object to a valid JSON String
     *
     * @param o the object o convert
     * @return a valid JSON String representing the given object's state
     */
    String writeValue(Object o);
}
