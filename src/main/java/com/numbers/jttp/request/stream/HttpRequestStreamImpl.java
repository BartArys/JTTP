package com.numbers.jttp.request.stream;

import com.numbers.jttp.*;
import com.numbers.jttp.mapper.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.supplier.*;
import com.numbers.jttp.response.HttpResponse;
import com.numbers.jttp.response.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.logging.*;
import org.apache.commons.io.*;
import org.apache.http.*;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;

/**
 * a direct implementation of the HttpRequestStream
 *
 * @author Numbers
 */
public class HttpRequestStreamImpl implements HttpRequestStream {

    /**
     * the base (no queries) url of the request
     */
    protected final String url;

    /**
     * an optional proxy to use during the execution of the request. no an empty
     * optional means no proxy.
     */
    protected Optional<HttpHost> proxy;

    /**
     * whether to execute the request async.
     */
    protected boolean async;

    /**
     * the headers to add to the request.
     */
    protected final List<Header> headers;

    /**
     * the executor to run the request on when {@code async} is set to true
     */
    protected final Executor executor;

    /**
     * the {@link com.numbers.jttp.mapper.JsonMapper JsonMapper} to use when
     * mapping {@link java.io.InputStream inputStreams} to POJO objects.
     */
    protected final JsonMapper mapper;

    /**
     * the HTTP request type used to generate an
     * org.​apache.​http.​client.​methods.HttpRequestBase from the
     * {@link RequestSupplier}.
     */
    protected final HttpRequestType requestType;

    /**
     * an integer representing the optional time in milliseconds to wait before
     * issuing a connection timeout. An empty value represents the highest
     * possible time.
     */
    protected Optional<Integer> connectionTimeout;

    /**
     * an optional {@link String} containing the Base64 encoded username +
     * password. An empty value represents no basicAuth.
     */
    protected Optional<String> basicAuth;

    /**
     * the queries to add to the URI.
     */
    protected final List<NameValuePair> queries;
    private final RequestSupplier<? extends HttpRequestBase> requestSupplier;
    private final CloseableHttpClient client;

    /**
     * creates a HttpRequestStream with the default supplied values, an empty
     * {@code basicAuth}, an empty {@code hearders} list, empty {@code queries}
     * list and an empty {@code connectionTimeout}.
     *
     * @param client      the client to execute the request with
     * @param requestType the HTTP request type
     * @param supplier    the HttpRequestBase supplier
     * @param url         the base url
     * @param config      the config containing user defined default values
     */
    HttpRequestStreamImpl(CloseableHttpClient client,
                                 HttpRequestType requestType,
                                 RequestSupplier<? extends HttpRequestBase> supplier,
                                 String url, Jttp.JTTPConfig config)
    {
        this.url = url;
        this.proxy = Optional.ofNullable(config.getDefaultProxy());
        this.async = config.isAsync();
        this.executor = config.getService();
        this.mapper = config.getMapper();
        this.requestType = requestType;
        this.requestSupplier = supplier;
        this.client = client;

        basicAuth = Optional.empty();
        headers = new ArrayList<>();
        queries = new ArrayList<>();
        connectionTimeout = Optional.empty();
    }

    @Override
    public HttpRequestStream async()
    {
        async = true;
        return this;
    }

    private <T> void checkArgumentsPaired(T[] objects)
    {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("arguments must be in pairs");
        }
    }

    @Override
    public HttpRequestStream header(String key, String value)
    {
        headers.add(new BasicHeader(key, value));
        return this;
    }

    @Override
    public HttpRequestStream header(String... keyValues)
    {
        checkArgumentsPaired(keyValues);

        for (int i = 0; i < keyValues.length - 1; i += 2) {
            header(keyValues[i], keyValues[i + 1]);
        }

        return this;
    }

    @Override
    public HttpRequestStream queryString(String key, String value)
    {
        queries.add(new BasicNameValuePair(key, value));
        return this;
    }

    @Override
    public HttpRequestStream queryString(String... keyValues)
    {
        checkArgumentsPaired(keyValues);

        for (int i = 0; i < keyValues.length - 1; i += 2) {
            queryString(keyValues[i], keyValues[i + 1]);
        }

        return this;
    }

    @Override
    public HttpRequestStream basicAuth(String username, String password)
    {
        basicAuth = Optional.of(Base64.getEncoder()
                .encodeToString((username + ":" + password)
                        .getBytes(StandardCharsets.UTF_8)));
        return this;
    }

    @Override
    public HttpRequestStream setProxy(HttpHost proxy)
    {
        this.proxy = Optional.ofNullable(proxy);
        return this;
    }

    @Override
    public HttpRequestStream setConnectionTimeout(int ms)
    {
        this.connectionTimeout = Optional.of(ms);
        return this;
    }

    @Override
    public HttpRequestStream header(String key, File file, String fileName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpRequestStream header(String key, Path path, String pathName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpRequestStream header(String key, InputStream stream,
                                    String streamObjectName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpRequestStream header(String key, byte[] bytes,
                                    String byteObjectName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * builds the URI for the request
     *
     * @return a URI build given the url and queries of this object
     * @throws URISyntaxException if the URI contains bad syntax
     */
    public URI buildUri() throws URISyntaxException
    {
        return new URIBuilder(url).addParameters(queries)
                .build();
    }

    /**
     * configures the request fields
     *
     * @param request the request to configure
     */
    protected void configRequest(HttpRequestBase request)
    {
        basicAuth.ifPresent(auth -> request
                .addHeader("Authorization", "Basic " + auth));
        headers.forEach(request::addHeader);
    }

    /**
     * builds the requestconfig for the HttpRequest
     *
     * @param request the base request to use as default values
     * @return the new requestconfig
     */
    protected RequestConfig buildRequestConfig(HttpRequestBase request)
    {
        RequestConfig.Builder builder = RequestConfig
                .copy(Optional.ofNullable(request.getConfig()).orElse(RequestConfig.DEFAULT));

        proxy.ifPresent(host -> builder.setProxy(host));
        connectionTimeout.ifPresent(duration -> builder
                .setConnectionRequestTimeout(duration));

        return builder.build();
    }

    /**
     * executes the response and returns the ClosableHttpResponse response of
     * the request
     *
     * @return the ClosableHttpResponse generated by this request and the
     *         ClosableHttpClient
     */
    protected CloseableHttpResponse executeRequest()
    {
        try {
            URI uri = buildUri();
            HttpRequestBase request = requestSupplier.get(requestType, uri);
            configRequest(request);

            request.setConfig(buildRequestConfig(request));

            return client.execute(request);

        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(this.getClass()
                    .getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     *
     * @param <T>      the type of completableFuture
     * @param supplier the type instance supplier
     * @return a completableFuture that is either completing on the executor in
     *         the case of {@code async == true}, or is already completed if
     *         not.
     *
     */
    protected final <T> CompletableFuture<T> wrapInCompletableFuture(
            Supplier<T> supplier)
    {
        return async
               ? CompletableFuture.supplyAsync(supplier, executor)
               : CompletableFuture.completedFuture(supplier.get());
    }

    /**
     *
     * @param response the response of which to get the inputStream from
     * @return the response's inputstream
     */
    protected final InputStream getStreamQuietly(CloseableHttpResponse response)
    {
        try {
            return response.getEntity()
                    .getContent();
        } catch (IOException | UnsupportedOperationException ex) {
            Logger.getLogger(HttpRequestStreamImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param response the response to give the success of
     * @return {@code true} if the response status code is
     *         {@code >= 200 && < 300}
     */
    protected final boolean isSuccess(CloseableHttpResponse response)
    {
        int statusCode = response.getStatusLine()
                .getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public <T> CompletableFuture<JsonHttpResponse<T>> asObject(
            Class<? extends T> clazz)
    {
        Supplier<JsonHttpResponse<T>> supplier = () -> {
            CloseableHttpResponse response = executeRequest();
            if (isSuccess(response)) {
                return JsonHttpResponse.ofSuccess(response, mapper
                        .readValue(getStreamQuietly(response), clazz));
            } else {
                return JsonHttpResponse.ofFailure(response, mapper);
            }
        };

        return wrapInCompletableFuture(supplier);
    }

    @Override
    public CompletableFuture<JsonHttpResponse<String>> asString()
    {
        Supplier<JsonHttpResponse<String>> supplier = () -> {
            try {
                CloseableHttpResponse response = executeRequest();
                return JsonHttpResponse.ofSuccess(response, IOUtils
                        .toString(new InputStreamReader(getStreamQuietly(
                                response))));
            } catch (IOException ex) {
                Logger.getLogger(HttpRequestStreamImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
                throw new CompletionException(ex);
            }
        };

        return wrapInCompletableFuture(supplier);
    }

    @Override
    public <T, C extends Collection> CompletableFuture<JsonHttpResponse<C>> asObjects(
            Class<C> collectionClass, Class<T> clazz)
    {
        Supplier<JsonHttpResponse<C>> supplier = () -> {
            CloseableHttpResponse response = executeRequest();
            if (isSuccess(response)) {
                return JsonHttpResponse.ofSuccess(response, mapper
                        .readValues(getStreamQuietly(response), collectionClass,
                                clazz));
            } else {
                return JsonHttpResponse.ofFailure(response, mapper);
            }
        };

        return wrapInCompletableFuture(supplier);
    }

    @Override
    public <M extends Map<K, V>, K, V> CompletableFuture<JsonHttpResponse<Map<K, V>>> asMap(
            Class<M> mapClass, Class<K> keyClass, Class<V> valueClass)
    {
        Supplier<JsonHttpResponse<Map<K, V>>> supplier = () -> {
            CloseableHttpResponse response = executeRequest();
            if (isSuccess(response)) {
                return JsonHttpResponse.ofSuccess(response, mapper
                        .readValues(getStreamQuietly(response), mapClass,
                                keyClass, valueClass));
            } else {
                return JsonHttpResponse.ofFailure(response, mapper);
            }
        };

        return wrapInCompletableFuture(supplier);
    }

    @Override
    public CompletableFuture<HttpResponse> execute()
    {
        return wrapInCompletableFuture(() -> {
            CloseableHttpResponse resp = executeRequest();
            try {
                resp.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpRequestStreamImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            return HttpResponse.of(resp);
        });
    }

    /**
     *
     * @return the requestSupplier that supplies the correct HttpRequestBase
     *         depending on the {@link HttpRequestType HttpRequestType} of this
     *         request
     */
    protected RequestSupplier<? extends HttpRequestBase> getRequestSupplier()
    {
        return requestSupplier;
    }

    /**
     *
     * @return the Base64 encoded username + password, or an empty optional if
     *         no basicauth was used
     */
    protected Optional<String> getBasicAuth()
    {
        return basicAuth;
    }

    /**
     *
     * @return the maximum connection timeout, or an empty optional if
     *         indefinite
     */
    protected Optional<Integer> getConnectionTimeout()
    {
        return connectionTimeout;
    }

    /**
     *
     * @return the client used to execute the HTTP request
     */
    protected CloseableHttpClient getClient()
    {
        return client;
    }

    /**
     *
     * @return the executor used to run the request in async
     */
    protected Executor getExecutor()
    {
        return executor;
    }

    /**
     *
     * @return an unmodifiable list of headers
     */
    protected List<Header> getHeaders()
    {
        return Collections.unmodifiableList(headers);
    }

    /**
     *
     * @return the JSON object mapper to map the inputstream
     */
    protected JsonMapper getMapper()
    {
        return mapper;
    }

    /**
     *
     * @return the optional proxy used for this query
     */
    protected Optional<HttpHost> getProxy()
    {
        return proxy;
    }

    /**
     *
     * @return an unmodifiable list of the queries
     */
    protected List<NameValuePair> getQueries()
    {
        return Collections.unmodifiableList(queries);
    }

    /**
     *
     * @return the requestType of the request
     */
    protected HttpRequestType getRequestType()
    {
        return requestType;
    }

    /**
     *
     * @return the url of the request
     */
    protected String getUrl()
    {
        return url;
    }

}
