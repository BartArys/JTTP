package com.numbers.jttp.response;

import org.apache.http.*;
import org.apache.http.client.methods.*;

/**
 * a direct implementation of the
 * {@link com.numbers.jttp.response.HttpResponse HttpResponse} using a
 * org.apache.http.client.methods.CloseableHttpResponse
 *
 * @author Numbers
 */
class BasicHttpResponse implements HttpResponse {

    private final CloseableHttpResponse response;

    /**
     * creates a BasicHttpResponse based on the given CloseableHttpResponse
     * response
     *
     * @param response the underlying response
     */
    BasicHttpResponse(CloseableHttpResponse response)
    {
        this.response = response;
    }

    @Override
    public int getStatusCode()
    {
        return response.getStatusLine()
                .getStatusCode();
    }

    @Override
    public String getStatusText()
    {
        return response.getStatusLine()
                .getReasonPhrase();
    }

    @Override
    public Header[] getHeaderField(String key)
    {
        return response.getHeaders(key);
    }

    @Override
    public Header[] getAllHeaders()
    {
        return response.getAllHeaders();
    }

    /**
     *
     * @return the underlying CloseableHttpResponse
     */
    public CloseableHttpResponse getHttpResponse()
    {
        return response;
    }

}
