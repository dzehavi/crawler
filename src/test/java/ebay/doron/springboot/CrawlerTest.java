package ebay.doron.springboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Doron Zehavi
 */
@SpringBootTest
public class CrawlerTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CrawlerService service;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
    }

    @Test
    public void testGetRootResourceOnce() {
        mockServer.expect(once(), requestTo("http://from-root-1.com"))
            .andRespond(withSuccess("http://from-root-1-1.com\nhttp://from-root-1-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-1.com"))
            .andRespond(withSuccess("http://from-root-1-1-1.com\nhttp://from-root-1-1-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-1-1.com"))
            .andRespond(withSuccess("http://from-root-1-1-1-1.com\nhttp://from-root-1-1-1-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-1-2.com"))
            .andRespond(withSuccess("http://from-root-1-1-2-1.com\nhttp://from-root-1-1-2-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-2.com"))
            .andRespond(withSuccess("http://from-root-1-2-1.com\nhttp://from-root-1-2-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-2-1.com"))
            .andRespond(withSuccess("http://from-root-1-2-1-1.com\nhttp://from-root-1-2-1-2.com", MediaType.TEXT_PLAIN));

        mockServer.expect(once(), requestTo("http://from-root-1-2-2.com"))
            .andRespond(withSuccess("http://from-root-1-2-2-1.com\nhttp://from-root-1-2-2-2.com", MediaType.TEXT_PLAIN));

        Map result = service.crawl("http://from-root-1.com", 0, 2);
        System.out.println("testGetRootResourceOnce: " + result);

        mockServer.verify();
        assertEquals(200, result.get("http://from-root-1-1.com"));
        assertEquals(200, result.get("http://from-root-1-2.com"));
        assertEquals(200, result.get("http://from-root-1-1-1.com"));
        assertEquals(200, result.get("http://from-root-1-1-2.com"));
        assertEquals(200, result.get("http://from-root-1-2-1.com"));
        assertEquals(200, result.get("http://from-root-1-2-2.com"));
    }
}
