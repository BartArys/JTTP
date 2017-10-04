package com.numbers.jttp.response;

import org.apache.http.client.methods.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

/**
 *
 * @author Numbers
 */
@RunWith(MockitoJUnitRunner.class)
public class GenericHttpResponseTest {
    
    @Mock CloseableHttpResponse mockClosable;
    
    private GenericHttpResponse testResponse;
    private Object response;
    
    @Before
    public void setUp(){
        testResponse = new GenericHttpResponse(mockClosable, response);
    }

    /**
     * Test of getResponse method, of class GenericHttpResponse.
     */
    @Test
    public void testGetResponse()
    {
        Assert.assertEquals(response, testResponse.getResponse());
    }
    
}
