package ebay.doron.springboot;

/**
 * @author Doron Zehavi
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class CrawlerController {

    private final CrawlerService service;

    public CrawlerController(CrawlerService service) {
        this.service = service;
    }

    @GetMapping("/")
    public Map endpoint(HttpServletRequest request){
        String url = request.getParameter("url");
        int depth = Integer.parseInt(request.getParameter("depth"));
        return service.crawl(url, 0, depth);
    }

}