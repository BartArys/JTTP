package com.numbers.jttp.response;

import com.numbers.jttp.mapper.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.apache.http.client.methods.*;

/**
 * a {@link HttpResponse HttpResponse} containing a java type response mapped
 * from the supplied {@link java.io.InputStream}.
 *
 * @author Numbers
 * @param <T> The type response to which the JSON response is mapped to.
 */
public interface JsonHttpResponse<T> extends HttpResponse, Closeable {

    /**
     *
     * @param <T>
     * @param response
     * @param value
     * @return
     */
    public static<T> JsonHttpResponse<T> ofSuccess(CloseableHttpResponse response,
                                     T value){
        return new GenericHttpResponse<>(response,value);
    }
    
    /**
     *
     * @param <T>
     * @param response
     * @param mapper
     * @return
     */
    public static<T> JsonHttpResponse<T> ofFailure(CloseableHttpResponse response, JsonMapper mapper){
        return new FailedGenericHttpResponse<>(response, mapper);
    }
    
    /**
     * return an instance of a java object which contains the values of the
     * supplied JSON response, or an IllegalStateException when the response
     * returned a HTTP error code.
     *
     * @return a java instance mapped from the supplied JSON response
     * @throws IllegalStateException if {@link #isSuccess() isSuccess()} returns
     *                               <code>false</code>
     */
    T getResponse() throws IllegalStateException;

    /**
     * optional method that maps the JSON response to an instance of the
     * supplied class if the {@link #isSuccess() isSuccess()} method returns
     * <code>true</code>.
     *
     * @param <U>        the type to map the error response to
     * @param errorClass the class to map the error response to
     * @return an instance of the error class with values supplied by the HTTP
     *         response.
     * @throws UnsupportedOperationException if error mapping is not implemented
     * @throws IllegalStateException         if {@link #isSuccess() isSuccess()}
     *                                       returns <code>true</code>
     */
    public default <U> U mapToErrorResponse(Class<U> errorClass) throws
            UnsupportedOperationException, IllegalStateException
    {
        if (!isSuccess()) {
            throw new UnsupportedOperationException();
        } else {
            throw new IllegalStateException(
                    "response has no error response code");
        }
    }

    /**
     * convenience method to map the response from
     * {@link #getResponse() getResponse()} to another instance with the
     * supplied Function.
     *
     * @param <U>      the instance to map the current response to
     * @param function the function that will map the current value to the new
     *                 value
     * @return an instance of U mapped by the supplied function
     * @throws NullPointerException  if either the response or the supplied
     *                               function are null
     * @throws IllegalStateException if {@link #getResponse() getResponse()}
     *                               throws an IllegalStatException
     */
    public default <U> U map(Function<T, U> function) throws
            NullPointerException, IllegalStateException
    {
        Objects.requireNonNull(getResponse(), "can't map null");
        Objects.requireNonNull(function, "supplied function can't be null");
        return function.apply(getResponse());
    }
    
    public default<U> Stream<U> stream(Function<T,U> function) throws NullPointerException{
        Objects.requireNonNull(function, "supplied function can't be null");
    
        return Optional.of(function.apply(getResponse())).map(Stream::of).orElse(Stream.empty());
    }
    
    public default<F, U extends Collection<F>> Stream<F> flatStream(Function<T,U> function) throws NullPointerException{
        Objects.requireNonNull(function, "supplied function can't be null");
    
        return Optional.of(function.apply(getResponse())).map(U::stream).orElse(Stream.empty());
    }

    /**
     * optional method that closes the underlying HTTP
     * {@link java.io.InputStream} if not already closed. This should only be
     * called if neither {@link #getResponse() getResponse()},
     * {@link #mapToErrorResponse(Class) mapToErrorResponse(Class)} or
     * {@link #map(Function) map(Function)} methods are not called.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public default void close() throws IOException
    {
    }

}
