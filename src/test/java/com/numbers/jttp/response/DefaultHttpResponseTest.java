package com.numbers.jttp.response;

import com.numbers.jttp.response.HttpResponse;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHttpResponseTest {

    HttpResponse mockResponse;
    @Mock CloseableHttpResponse mockClosable;
    
    @Before
    public void setUp(){
    }

    private void mockWithStatusCode(int code){
        mockResponse = new HttpResponse() {
        @Override
        public Header[] getHeaderField(String key)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Header[] getAllHeaders()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getStatusCode()
        {
            return code;
        }

        @Override
        public String getStatusText()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };   
    }
    
    @Test
    public void testIsSuccessTrueWithStatusCode200(){
        mockWithStatusCode(200);
        Assert.assertTrue(mockResponse.isSuccess());
    }
    
    @Test
    public void testIsSuccessFalseWithStatusCode300(){
        mockWithStatusCode(300);
        Assert.assertFalse(mockResponse.isSuccess());
    }
    
    @Test
    public void testIsSuccessFalseWithStatusCode199(){
        mockWithStatusCode(199);
        Assert.assertFalse(mockResponse.isSuccess());
    }
    
    @Test
    public void testOfReturnsHttpResponse(){
        Assert.assertTrue(HttpResponse.of(mockClosable) != null);
    }
    
}
