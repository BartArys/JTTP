package com.numbers.jttp.response;

import org.apache.http.client.methods.*;

/**
 * a {@link HttpResponse HttpResponse} containing a java type response mapped
 * from the supplied {@link java.io.InputStream}.
 *
 * @author Numbers
 * @param <T> The type response to which the JSON response is mapped to.
 */
class GenericHttpResponse<T> extends BasicHttpResponse implements JsonHttpResponse<T> {

    private final T response;

    /**
     *
     * @param chr      the underlying http response
     * @param response the mapped body response
     */
    GenericHttpResponse(CloseableHttpResponse chr, T response)
    {
        super(chr);
        this.response = response;
    }

    @Override
    public T getResponse()
    {
        return response;
    }

}
