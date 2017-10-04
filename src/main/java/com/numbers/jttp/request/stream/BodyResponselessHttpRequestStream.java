package com.numbers.jttp.request.stream;

import com.numbers.jttp.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.supplier.*;
import com.numbers.jttp.response.HttpResponse;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * The base interface for all HttpRequestStreams. This interface is meant for
 * HTTP methods that do not intend to return any response body, as such it does
 * not include response mapping.
 *
 * @author Numbers
 */
public interface BodyResponselessHttpRequestStream {

    /**
     *
     * @param client
     * @param requestType
     * @param supplier
     * @param url
     * @param config
     * @return
     */
    public static BodyResponselessHttpRequestStream of(CloseableHttpClient client,
                                 HttpRequestType requestType,
                                 RequestSupplier<? extends HttpRequestBase> supplier,
                                 String url, Jttp.JTTPConfig config){
    
        return new HttpRequestStreamImpl(client, requestType, supplier, url,
                config);
    }
    
    /**
     * enable async response handling on a separate thread
     *
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream async();

    /**
     * sets the username and password for a basicauth header. Given username and
     * password will be converted to a Base64 variant.
     *
     * @param username the username
     * @param password the password
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream basicAuth(String username, String password);

    /**
     * adds a file to the request with the given header key and filename in the
     * header
     *
     * @param key      the header key
     * @param file     the file to upload
     * @param fileName the filename for the header
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream header(String key, File file,
                                             String fileName);

    /**
     * adds a file to the request with the given header key and filename in the
     * header
     *
     * @param key      the header key
     * @param path     the Path from which to get the file
     * @param fileName the filename for the header
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream header(String key, Path path,
                                             String fileName);

    /**
     * adds a file to the request with the given header key and filename in the
     * header
     *
     * @param key              the header key
     * @param stream           the file inputstream
     * @param streamObjectName the filename for the header
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream header(String key, InputStream stream,
                                             String streamObjectName);

    /**
     * adds a file to the request with the given header key and filename in the
     * header
     *
     * @param key            the header key
     * @param bytes          the file byte input
     * @param byteObjectName the filename for the header
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream header(String key, byte[] bytes,
                                             String byteObjectName);

    /**
     * adds a single key-value header to the request
     *
     * @param key   the request key
     * @param value the request header
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream header(String key, String value);

    /**
     * adds a series of key-value headers to the request
     *
     * @param keyValues the key-values to enter as headers to the request. Odd
     *                  entries will be used as keys, even entries as values.
     * @return this requestStream
     * @throws IllegalArgumentException if the array size is not even
     */
    BodyResponselessHttpRequestStream header(String... keyValues) throws
            IllegalArgumentException;

    /**
     * adds a single key-value query to the request
     *
     * @param key   the query key
     * @param value the query value
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream queryString(String key, String value);

    /**
     * adds a series of key-value queries to the request
     *
     * @param keyValues the key-values to enter as queries to the request. Odd
     *                  entries will be used as keys, even entries as values.
     * @return this requestStream
     * @throws IllegalArgumentException if the array size is not even
     */
    BodyResponselessHttpRequestStream queryString(String... keyValues) throws
            IllegalArgumentException;

    /**
     * sets the maximum amount of time to wait (in milliseconds) for a
     * connection to initialize before issuing a connection timeout.
     *
     * @param ms the length of time (in milliseconds) to wait before issuing a
     *           connection timeout.
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream setConnectionTimeout(int ms);

    /**
     * sets the proxy to use for this request.
     *
     * @param proxy the proxy to use for this request, or {@code null} if no
     *              proxy should be used.
     * @return this requestStream
     */
    BodyResponselessHttpRequestStream setProxy(HttpHost proxy);

    /**
     * parses the {@link java.io.InputStream InputStream} inputstream to an
     * {@link com.numbers.jttp.response.HttpResponse HttpResponse} and closes
     * the underlying stream.
     *
     * @return a
     *         {@link java.util.concurrent.CompletableFuture CompletableFuture}
     *         with the HttpResponse of the executed request.
     */
    CompletableFuture<HttpResponse> execute();

}
