package com.numbers.jttp.request.supplier;

import com.numbers.jttp.request.*;
import java.net.*;
import org.apache.http.client.methods.*;

/**
 * a wrapper class to supply a correct generic response based on a given
 * {@link HttpRequestType HttpRequestType} and {@link java.net.URI URI}.
 *
 * @author Numbers
 * @param <T> the generic response type
 */
public interface RequestSupplier<T> {

    /**
     *
     * @return
     */
    public static RequestSupplier<HttpRequestBase> baseRequestSuppulier(){
        return new RequestBaseSupplier();
    }
    
    /**
     *
     * @return
     */
    public static RequestSupplier<HttpEntityEnclosingRequestBase> enclosingRequestSupplier(){
        return new EnclosingRequestBaseSupplier();
    }
    
    /**
     * supplies a {@code <T>} based on input request type and URI
     *
     * @param type the HTTP request type
     * @param uri  the request URI
     * @return a {@code <T>} based matching the given input
     */
    T get(HttpRequestType type, URI uri);

}
