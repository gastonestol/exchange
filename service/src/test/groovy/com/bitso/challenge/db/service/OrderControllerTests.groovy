package com.bitso.challenge.db.service

import com.bitso.challenge.model.OrderModel
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import javax.inject.Inject

/** 
 * Test the OrderController.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application-test.properties"])
class OrderControllerTests extends Specification {
    private static final String TOKEN_PREFIX = "Bearer";

    @Inject
    OrderModel model
    @Value('${local.server.port}')
    int port
    RestTemplate rest = new RestTemplate()


    Map<String, Object> submit(String token,long userId, String major, String minor, BigDecimal amount, BigDecimal price, boolean buy) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.setBearerAuth(transformToken(token))
        JSONObject body = new JSONObject();
        body.put('userId', Long.toString(userId))
        body.put('major', major)
        body.put('minor', minor)
        body.put('amount', amount.toPlainString())
        body.put('price', price.toPlainString())
        body.put('buy', Boolean.toString(buy))
        return rest.postForObject("http://localhost:${ port }/submit", new HttpEntity(body.toString(), headers), Map)
    }

    ResponseEntity login(String email, String password) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        JSONObject body = new JSONObject();
        body.put("email", email)
        body.put("password", password)
        return rest.exchange("http://localhost:${ port }/login", HttpMethod.POST, new HttpEntity(body.toString(), headers), Map)
    }

    void "given an order data when submitting and getting then order is retrieved"() {
        given:
            String email = "user1@bitso.com"
            String password = "password1"
            ResponseEntity loginResp = login(email, password)
            String token = loginResp.getHeaders().get("Authorization")
            Long id = (Long) loginResp.getBody().get("id")
        when:
            Map<String, Object> resp = submit(token,id, 'btc', 'mxn', 1.0, 380_000.00, true)
        then:
            resp != null
            resp.id != null
            Long.parseLong(resp.id.toString()) > 0
            resp.status == 'active'
            resp.created != null
        when:
            HttpHeaders headers = new HttpHeaders()
            headers.setBearerAuth(transformToken(token))
            Map<String, Object> resp2 = rest.exchange("http://localhost:${ port }/get/${ resp.id }",HttpMethod.GET,new HttpEntity(headers), Map).body
        then:
            resp2 != null
            resp2 == resp
    }

    void "given major and minor when quering books in public then books are retrieved"() {
        when:
            List<Map<String, Object>> resp = rest.getForEntity("http://localhost:${ port }/book/btc/mxn", List).body
        then:
            resp != null
            resp.size() >= 2
    }

    void "other queries"() {
        when:
            List<Map<String, Object>> resp = rest.getForEntity("http://localhost:${ port }/query/1/active/btc/mxn", List).body
        then:
            resp != null
            resp.size() >= 2
    }

    private static String transformToken(String token){
        return token.replace(TOKEN_PREFIX, "").replace("[","").replace("]","")
    }

}
