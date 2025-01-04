package looqbox.challenge.microservicepokeapi.infra;

import looqbox.challenge.microservicepokeapi.exception.ApiRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpRequestComponet {

    private final RestTemplate restTemplate;


    public HttpRequestComponet(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> sendGetRequest(String uri, Class<T> responseType) {
        try {
            return restTemplate.getForEntity(uri, responseType);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to fetch data from URI: " + uri);
        }
    }

}
