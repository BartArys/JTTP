package com.numbers.jttp.response;

import com.numbers.jttp.response.BasicHttpResponse;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.message.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.*;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.runners.*;

/**
 *
 * @author Numbers
 */
@RunWith(MockitoJUnitRunner.class)
public class BasicHttpResponseTest {
    
    @Mock CloseableHttpResponse closableMock;
    @Mock StatusLine statusLineMock;
    
    private final Header[] mockFilteredHeader = new Header[]{new BasicHeader("a", "b")};
    private final Header[] mockAllHeader = new Header[]{new BasicHeader("x","y")};
    
    private BasicHttpResponse testResponse;
    
    @Before
    public void setUp(){
        testResponse = new BasicHttpResponse(closableMock);
        when(closableMock.getHeaders("header")).thenReturn(mockFilteredHeader);
        when(closableMock.getAllHeaders()).thenReturn(mockAllHeader);
        when(closableMock.getStatusLine()).thenReturn(statusLineMock);
    }
    
    private void mockWithStatusText(String text){
        when(statusLineMock.getReasonPhrase()).thenReturn(text);
    }
    
    private void mockWithStatusCode(int code){
        when(statusLineMock.getStatusCode()).thenReturn(code);
    }
    
    /**
     * Test of getStatusCode method, of class BasicHttpResponse.
     */
    @Test
    public void testGetStatusCodeCallsUnderlying()
    {
        mockWithStatusCode(200);
        
        assertEquals(200, testResponse.getStatusCode());
        verify(closableMock, times(1)).getStatusLine();
    }

    /**
     * Test of getStatusText method, of class BasicHttpResponse.
     */
    @Test
    public void testGetStatusTextCallsUnderlying()
    {
        mockWithStatusText("response");
        
        assertEquals("response", testResponse.getStatusText());
        verify(closableMock, times(1)).getStatusLine();
    }

    /**
     * Test of getHeaderField method, of class BasicHttpResponse.
     */
    @Test
    public void testGetHeaderFieldCallsUnderlying()
    {
        assertArrayEquals(mockFilteredHeader, testResponse.getHeaderField("header"));
        verify(closableMock, times(1)).getHeaders("header");
    }

    /**
     * Test of getAllHeaders method, of class BasicHttpResponse.
     */
    @Test
    public void testGetAllHeadersCallsUnderlying()
    {
        assertArrayEquals(mockAllHeader, testResponse.getAllHeaders());
        verify(closableMock, times(1)).getAllHeaders();
    }

    /**
     * Test of getHttpResponse method, of class BasicHttpResponse.
     */
    @Test
    public void testGetHttpResponseCallsUnderlying()
    {
        assertEquals(closableMock, testResponse.getHttpResponse());
    }
    
}
