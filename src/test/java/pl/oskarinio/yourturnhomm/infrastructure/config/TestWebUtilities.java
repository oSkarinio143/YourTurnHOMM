package pl.oskarinio.yourturnhomm.infrastructure.config;

import jakarta.servlet.FilterChain;
import lombok.Data;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.mock;

@Data
public class TestWebUtilities {
    private MockHttpServletRequest request = new MockHttpServletRequest();;
    private MockHttpServletResponse response = new MockHttpServletResponse();;
    private FilterChain filterChain = mock(FilterChain.class);;

    public void resetUtilities(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }
}
