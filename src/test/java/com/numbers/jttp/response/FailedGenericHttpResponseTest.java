package com.numbers.jttp.response;

import com.numbers.jttp.mapper.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
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
public class FailedGenericHttpResponseTest {
    
    FailedGenericHttpResponse testResponse;
    
    @Mock CloseableHttpResponse mockClosable;
    @Mock JsonMapper mockMapper;
    @Mock HttpEntity mockEntity;
    
    private final Object jsonDummy = new Object();
    @Mock InputStream mockInput;
    
    @Before
    public void setUp() throws IOException{
        when(mockClosable.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getContent()).thenReturn(mockInput);
        
        when(mockMapper.readValue(mockInput, Object.class)).thenReturn(
                jsonDummy);
        
        testResponse = 
                new FailedGenericHttpResponse(mockClosable, mockMapper);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetResponseThrowsIllegalStateException()
    {
        testResponse.getResponse();
    }

    @Test
    public void testMapToErrorResponseUsesObjectMapper()
    {
        Object response = testResponse.mapToErrorResponse(Object.class);
        assertEquals(jsonDummy, response);
        
        verify(mockMapper, times(1)).readValue(mockInput, Object.class);
    }

    
    @Test
    public void testCloseCallsCloseIfOpen() throws IOException
    {
        testResponse.close();
        verify(mockInput, times(1)).close();
    }
    
    @Test
    public void testCloseDoesNotCallCloseIfClosed() throws IOException
    {
        testResponse.mapToErrorResponse(Object.class);
        testResponse.close();
        verify(mockInput, never()).close();
    }
    
}
