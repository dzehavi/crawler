package ebay.doron.springboot;

/**
 * @author Doron Zehavi
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class CrawlerService {
    private RestTemplate template;

    @Autowired
    public CrawlerService(RestTemplate template) {
        this.template = template;
    }

    public Map crawl(String url, int depth, int maxDepth) {
        Map result = new HashMap();
        crawl(url, depth, maxDepth, result);
        return result;
    }

    private void crawl(String url, int depth, int maxDepth, Map result) {
        if (depth > maxDepth) {
            return;
        }
        ResponseEntity<String> resp =
            template.getForEntity(url, String.class);
        if (depth > 0) {
            result.putIfAbsent(url, resp.getStatusCodeValue());
        }
        if (resp.getStatusCodeValue() == 200) {
            String response = resp.getBody();
            String[] urls = response.split("\n");
            int depth1 = ++depth;
            for (String line : urls) {
                crawl(line, depth1, maxDepth, result);
            }
        }
    }

}

