package com.numbers.jttp.request.stream;

import com.numbers.jttp.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.supplier.*;
import java.io.*;
import java.nio.file.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * an interface representing an HTTP request that may have a body and may return
 * a response body upon execution.
 *
 * @author Numbers
 */
public interface HttpBodyRequestStream extends HttpRequestStream {

    /**
     *
     * @param client
     * @param requestType
     * @param supplier
     * @param url
     * @param config
     * @return
     */
    public static HttpBodyRequestStream of(CloseableHttpClient client,
                                 HttpRequestType requestType,
                                 RequestSupplier<? extends HttpEntityEnclosingRequestBase> supplier,
                                 String url, Jttp.JTTPConfig config){
    
        return new HttpBodyRequestStreamImpl(client, requestType, supplier, url,
                config);
    }
    
    /**
     * sets an array of key-value strings as a JSON body
     *
     * @param values they key-value body
     * @return this requestStream
     */
    HttpBodyRequestStream body(String... values);

    /**
     * sets an object as a JSON body
     *
     * @param value the object body
     * @return this requestStream
     */
    HttpBodyRequestStream body(Object value);

    @Override
    HttpBodyRequestStream async();

    @Override
    HttpBodyRequestStream basicAuth(String username, String password);

    @Override
    HttpBodyRequestStream header(String key, File file, String fileName);

    @Override
    HttpBodyRequestStream header(String key, Path path, String pathName);

    @Override
    HttpBodyRequestStream header(String key, InputStream stream,
                                 String streamObjectName);

    @Override
    HttpBodyRequestStream header(String key, byte[] bytes, String byteObjectName);

    @Override
    HttpBodyRequestStream header(String key, String value);

    @Override
    HttpBodyRequestStream header(String... keyValues);

    @Override
    HttpBodyRequestStream queryString(String key, String value);

    @Override
    HttpBodyRequestStream queryString(String... keyValues);

    @Override
    HttpBodyRequestStream setConnectionTimeout(int ms);

    @Override
    HttpBodyRequestStream setProxy(HttpHost proxy);

}
