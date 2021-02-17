package com.bitso.challenge.db.service


import com.bitso.challenge.model.UserModel
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.*
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import javax.inject.Inject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application-test.properties"])
class UserControllerTests extends Specification {

    @Inject
    UserModel model
    @Value('${local.server.port}')
    int port
    RestTemplate rest = new RestTemplate()

    ResponseEntity login(String email, String password) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        JSONObject body = new JSONObject();
        body.put("email", email)
        body.put("password", password)
        return rest.exchange("http://localhost:${ port }/login", HttpMethod.POST, new HttpEntity(body.toString(), headers), Map)
    }

    void "given an existing user when login then return 200 and Authorization header"() {
        given:
        def email = "user1@bitso.com"
        def password = "password1"
        when:
        ResponseEntity resp = login(email, password)
        then:
        resp != null
        resp.getBody().get("id") != null
        resp.statusCode == HttpStatus.OK
        resp.headers.get("Authorization") != null
    }

    void "given an non existing user when login then return forbidden"() {
        given:
        def email = "nonexiting@bitso.com"
        def password = "password1"
        when:
        login(email, password)
        then:
        thrown HttpClientErrorException.Forbidden
    }

    void "given an existing user and incorrect password when login then return forbidden"() {
        given:
        def email = "user1@bitso.com"
        def password = "password12"
        when:
        login(email, password)
        then:
        thrown HttpClientErrorException.Forbidden

    }
}
