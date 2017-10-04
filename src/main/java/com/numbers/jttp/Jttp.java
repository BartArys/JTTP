package com.numbers.jttp;

import com.numbers.jttp.mapper.*;
import com.numbers.jttp.request.*;
import com.numbers.jttp.request.stream.*;
import com.numbers.jttp.request.supplier.*;
import java.io.*;
import java.util.concurrent.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * the central class of JTTP, all requests will be routed through an instance of
 * this class
 *
 * @author Numbers
 */
public final class Jttp implements Closeable {

    /**
     * creates a new JTTP instance without object mapping.
     * <p>
     * Note: JTTP instances are expensive to instantiate and creation of these
     * objects should therefore be limited
     *
     * @return a new instance of JTTP
     */
    public static Jttp newDefault()
    {
        return new Jttp(
                RequestSupplier.baseRequestSuppulier(),
                RequestSupplier.enclosingRequestSupplier(),
                new JTTPConfig()
        );
    }

    /**
     * creates a new JTTP instance with a given config.
     * <p>
     * Note: JTTP instances are expensive to instantiate and creation of these
     * objects should therefore be limited
     *
     * @param config the config with default values
     * @return a new instance of JTTP
     */
    public static Jttp fromConfig(JTTPConfig config)
    {
        return new Jttp(
                RequestSupplier.baseRequestSuppulier(),
                RequestSupplier.enclosingRequestSupplier(),
                config
        );
    }

    private final CloseableHttpClient client = HttpClients.createSystem();
    private final RequestSupplier<HttpRequestBase> defaultRequestSupplier;
    private final RequestSupplier<HttpEntityEnclosingRequestBase> bodyRequestSupplier;
    private final JTTPConfig config;

    /**
     * constructor used for static instantiating and testing
     *
     * @param defaultRequestSupplier the supplier used for HTTP requests without
     *                               body
     * @param bodyRequestSupplier    the supplier used for HTTP requests with
     *                               body
     * @param config                 the config with defaults for new requests
     */
    protected Jttp(
            RequestSupplier<HttpRequestBase> defaultRequestSupplier,
            RequestSupplier<HttpEntityEnclosingRequestBase> bodyRequestSupplier,
            JTTPConfig config)
    {
        this.defaultRequestSupplier = defaultRequestSupplier;
        this.bodyRequestSupplier = bodyRequestSupplier;
        this.config = config;
    }

    /**
     * creates a HTTP GET request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream without body options
     */
    public HttpRequestStream get(String url)
    {
        return HttpRequestStream.of(client, HttpRequestType.GET,
                defaultRequestSupplier, url, config);
    }

    /**
     * creates a HTTP POST request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream with body options
     */
    public HttpBodyRequestStream post(String url)
    {
        return HttpBodyRequestStream.of(client, HttpRequestType.POST,
                bodyRequestSupplier, url, config);
    }

    /**
     * creates a HTTP PATCH request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream with body options
     */
    public HttpRequestStream patch(String url)
    {
        return HttpRequestStream.of(client, HttpRequestType.PATCH,
                defaultRequestSupplier, url, config);
    }

    /**
     * creates a HTTP PUT request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream with body options
     */
    public HttpBodyRequestStream put(String url)
    {
        return HttpBodyRequestStream.of(client, HttpRequestType.POST,
                bodyRequestSupplier, url, config);
    }

    /**
     * creates a HTTP DELETE request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream without body options
     */
    public HttpRequestStream delete(String url)
    {
        return HttpRequestStream.of(client, HttpRequestType.DELETE,
                defaultRequestSupplier, url, config);
    }

    /**
     * creates a HTTP HEAD request stream with the given url.
     *
     * @param url the url to use in the request
     * @return a request stream without body response
     */
    public BodyResponselessHttpRequestStream head(String url)
    {
        return HttpRequestStream.of(client, HttpRequestType.PATCH,
                defaultRequestSupplier, url, config);
    }

    /**
     * shuts down the currently running request threads if necessary. This will
     * stop the JTTP from issuing any further async tasks.
     */
    public void shutdown()
    {
        config.service.shutdown();
    }

    /**
     * permanently closes the I/O requests if any are still open.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException
    {
        client.close();
    }

    /**
     * the config for newly generated JTTP requests
     */
    public static class JTTPConfig {

        private ExecutorService service = Executors.newCachedThreadPool();
        private boolean async = false;
        private HttpHost defaultProxy;
        private JsonMapper mapper;

        /**
         *
         * @param proxy the default proxy to use for new requests
         */
        public void setDefaultProxy(HttpHost proxy)
        {
            this.defaultProxy = proxy;
        }

        /**
         *
         * @param mapper the object mapper to use for new requests with response
         *               body
         */
        public void setJsonMapper(JsonMapper mapper)
        {
            this.mapper = mapper;
        }

        /**
         *
         * @param async whether requests should run asynchronously by default.
         */
        public void setDefaultAsync(boolean async)
        {
            this.async = async;
        }

        /**
         *
         * @param value whether or not async threads should be run as deamon or
         *              not.
         */
        public void setDeamon(boolean value)
        {
            if (!value) {
                service = Executors.newFixedThreadPool(Runtime.getRuntime()
                        .availableProcessors());
            } else {
                service.shutdown();
                service = Executors.newWorkStealingPool();
            }
        }

        /**
         *
         * @return the default Proxy used for requests
         */
        public HttpHost getDefaultProxy()
        {
            return defaultProxy;
        }

        /**
         *
         * @return the default mapper used for requests
         */
        public JsonMapper getMapper()
        {
            return mapper;
        }

        /**
         *
         * @return the default executor used for async requests
         */
        public ExecutorService getService()
        {
            return service;
        }

        /**
         *
         * @return true if requests are async by default
         */
        public boolean isAsync()
        {
            return async;
        }

    }

}
