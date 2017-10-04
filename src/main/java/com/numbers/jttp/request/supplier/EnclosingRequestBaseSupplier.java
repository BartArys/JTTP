package com.numbers.jttp.request.supplier;

import com.numbers.jttp.request.*;
import java.net.*;
import org.apache.http.client.methods.*;

/**
 *
 * @author Numbers
 */
class EnclosingRequestBaseSupplier implements RequestSupplier<HttpEntityEnclosingRequestBase> {

    @Override
    public HttpEntityEnclosingRequestBase get(HttpRequestType type, URI uri)
    {
        if (type == HttpRequestType.POST) {
            return new HttpPost(uri);
        }
        if (type == HttpRequestType.PATCH) {
            return new HttpPatch(uri);
        }
        if (type == HttpRequestType.PUT) {
            return new HttpPut(uri);
        }
        throw new IllegalArgumentException("unsupported type");
    }

}
