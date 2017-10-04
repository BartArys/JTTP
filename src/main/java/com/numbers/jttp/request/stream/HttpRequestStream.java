package com.numbers.jttp.request.stream;

import com.numbers.jttp.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.supplier.*;
import com.numbers.jttp.response.HttpResponse;
import com.numbers.jttp.response.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * an interface representing an HTTP request that may return a response body
 * upon execution.
 *
 * @author Numbers
 */
public interface HttpRequestStream extends BodyResponselessHttpRequestStream {

    /**
     *
     * @param client
     * @param requestType
     * @param supplier
     * @param url
     * @param config
     * @return
     */
    public static HttpRequestStream of(CloseableHttpClient client,
                                 HttpRequestType requestType,
                                 RequestSupplier<? extends HttpRequestBase> supplier,
                                 String url, Jttp.JTTPConfig config){
    
        return new HttpRequestStreamImpl(client, requestType, supplier, url,
                config);
    }
    
    @Override
    HttpRequestStream async();

    @Override
    HttpRequestStream basicAuth(String username, String password);

    @Override
    HttpRequestStream header(String key, File file, String fileName);

    @Override
    HttpRequestStream header(String key, Path path, String pathName);

    @Override
    HttpRequestStream header(String key, InputStream stream,
                             String streamObjectName);

    @Override
    HttpRequestStream header(String key, byte[] bytes, String byteObjectName);

    @Override
    HttpRequestStream header(String key, String value);

    @Override
    HttpRequestStream header(String... keyValues);

    @Override
    HttpRequestStream queryString(String key, String value);

    @Override
    HttpRequestStream queryString(String... keyValues);

    @Override
    HttpRequestStream setConnectionTimeout(int ms);

    @Override
    HttpRequestStream setProxy(HttpHost proxy);

    @Override
    CompletableFuture<HttpResponse> execute();

    /**
     * executes the HTTP request and maps the response
     *
     * @return a JsonHttpResponse with the response body converted to a String
     */
    CompletableFuture<JsonHttpResponse<String>> asString();

    /**
     * executes the HTTP request and maps the response
     *
     * @param <T>   the type of the object
     * @param clazz the class of the object to instantiate
     * @return a JsonHttpResponse with the response body converted to an Object
     */
    <T> CompletableFuture<JsonHttpResponse<T>> asObject(Class<? extends T> clazz);

    /**
     * executes the HTTP request and maps the response to a list of objects
     *
     * @param <T>             the generic type of the list
     * @param <C>             the type of the collection
     * @param collectionClass the class of the collection to instantiate
     * @param clazz           the class of the list objects to instantiate
     * @return a JsonHttpResponse with the response body converted to a
     *         Collection of objects
     */
    <T, C extends Collection> CompletableFuture<JsonHttpResponse<C>> asObjects(
            Class<C> collectionClass, Class<T> clazz);

    /**
     * executes the HTTP request and maps the response to a map of key-value
     * objects
     *
     * @param <M>        the generic type of the map
     * @param <K>        the type of the map keys
     * @param <V>        the type of the map values
     * @param mapClass   the class of the map to instantiate
     * @param keyClass   the class of the map keys to instantiate
     * @param valueClass the class of the map values to instantiate
     * @return a JsonHttpResponse with the response body converted to map of
     *         key-value objects
     */
    <M extends Map<K, V>, K, V> CompletableFuture<JsonHttpResponse<Map<K, V>>> asMap(
            Class<M> mapClass, Class<K> keyClass, Class<V> valueClass);
}
