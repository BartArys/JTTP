package com.numbers.jttp.request.stream;

import com.numbers.jttp.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.supplier.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;

/**
 * an implementation of the {@link HttpBodyRequestStream HttpBodyRequestStream},
 * extending the {@link HttpRequestStreamImpl HttpRequestStreamImpl} for shared
 * methods.
 *
 * @author Numbers
 */
class HttpBodyRequestStreamImpl extends HttpRequestStreamImpl implements HttpBodyRequestStream {

    /**
     * the body request entities
     */
    protected List<HttpEntity> requestEntites;

    /**
     * creates a HttpBodyRequestStream with the default supplied values, an
     * empty {@code basicAuth}, an empty {@code hearders} list, empty
     * {@code queries} list, an empty {@code connectionTimeout} and an empty
     * {@code requestEntitities} list.
     *
     * @param client      the client to execute the request with
     * @param requestType the HTTP request type
     * @param supplier    the HttpRequestBase supplier
     * @param url         the base url
     * @param config      the config containing user defined default values
     */
    HttpBodyRequestStreamImpl(CloseableHttpClient client,
                                     HttpRequestType requestType,
                                     RequestSupplier<? extends HttpEntityEnclosingRequestBase> supplier,
                                     String url, Jttp.JTTPConfig config)
    {
        super(client, requestType, supplier, url, config);
        requestEntites = new ArrayList<>();
    }

    @Override
    public HttpBodyRequestStream body(String... keyValues)
    {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("keyvalues must be paired");
        }
        Map<String, List<String>> kvPairs = new HashMap<>();
        for (int i = 0; i < keyValues.length; i++) {
            if (i % 2 == 0 && kvPairs.get(keyValues[i]) == null) { // i uneven and key not present
                kvPairs.put(keyValues[i], new ArrayList<>());
            } else {
                kvPairs.get(keyValues[i - 1])
                        .add(keyValues[i]);
            }
        }

        String append = kvPairs.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()
                        .stream()
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";", "", ";"));
        requestEntites
                .add(new StringEntity(append, ContentType.APPLICATION_JSON));

        return this;
    }

    @Override
    public HttpBodyRequestStream body(Object value)
    {
        StringEntity entity = new StringEntity(
                mapper.writeValue(value),
                ContentType.APPLICATION_JSON);

        requestEntites.add(entity);
        return this;
    }

    /*
     * already implemented methods that need to return this type of
     * HttpRequestStream
     */
    
    @Override
    public HttpBodyRequestStream async()
    {
        super.async();
        return this;
    }

    @Override
    public HttpBodyRequestStream basicAuth(String username, String password)
    {
        super.basicAuth(username, password);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String key, File file, String fileName)
    {
        super.header(key, file, fileName);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String key, InputStream stream,
                                        String streamObjectName)
    {
        super.header(key, stream, streamObjectName);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String key, Path path, String pathName)
    {
        super.header(key, path, pathName);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String key, byte[] bytes,
                                        String byteObjectName)
    {
        super.header(key, bytes, byteObjectName);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String... keyValues)
    {
        super.header(keyValues);
        return this;
    }

    @Override
    public HttpBodyRequestStream header(String key, String value)
    {
        super.header(key, value);
        return this;
    }

    @Override
    public HttpBodyRequestStream queryString(String... keyValues)
    {
        super.queryString(keyValues);
        return this;
    }

    @Override
    public HttpBodyRequestStream queryString(String key, String value)
    {
        super.queryString(key, value);
        return this;
    }

    @Override
    public HttpBodyRequestStream setConnectionTimeout(int ms)
    {
        super.setConnectionTimeout(ms);
        return this;
    }

    @Override
    public HttpBodyRequestStream setProxy(HttpHost proxy)
    {
        super.setProxy(proxy);
        return this;
    }

}
