package com.numbers.jttp.response;

import com.numbers.jttp.response.HttpResponse;
import org.apache.http.*;
import org.apache.http.client.methods.*;

/**
 * an interface representing a HTTP response without response body. This
 * interface allows the inspection of response headers, statuscode and status
 * text
 *
 * @author Numbers
 */
public interface HttpResponse {

    /**
     *
     * @param response
     * @return
     */
    public static HttpResponse of(CloseableHttpResponse response){
        return new BasicHttpResponse(response);
    }
    
    /**
     *
     * @param key the header key
     * @return a Header[] with headers matching the given key
     */
    Header[] getHeaderField(String key);

    /**
     *
     * @return a Header[] with all headers of this response
     */
    Header[] getAllHeaders();

    /**
     *
     * @return the response HTTP statuscode
     */
    int getStatusCode();

    /**
     *
     * @return the status text matching the HTTP statuscode
     */
    String getStatusText();

    /**
     * a utility method to quickly check if a response is considered successful
     * based on the HTTP statuscode
     *
     * @return true if {@code statuscode >= 200 && < 300}
     */
    public default boolean isSuccess()
    {
        final int status = getStatusCode();
        return status >= 200 && status < 300;
    }

}
