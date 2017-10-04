package com.numbers.jttp.request.supplier;

import com.numbers.jttp.request.*;
import java.net.*;
import org.apache.http.client.methods.*;

/**
 *
 * @author Numbers
 */
class RequestBaseSupplier implements RequestSupplier<HttpRequestBase> {

    @Override
    public HttpRequestBase get(HttpRequestType type, URI uri)
    {
        if (type == HttpRequestType.GET) {
            return new HttpGet(uri);
        }
        if (type == HttpRequestType.HEAD) {
            return new HttpHead(uri);
        }
        if (type == HttpRequestType.DELETE) {
            return new HttpDelete(uri);
        }
        throw new IllegalArgumentException("unsupported type");
    }

}
