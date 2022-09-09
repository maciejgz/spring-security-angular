package pl.mg.authserver;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClusteredSessionStorageTest {

    private Jedis jedis;
    private TestRestTemplate testRestTemplate;
    private TestRestTemplate testRestTemplateWithAuth;
    private final String testUrl = "http://localhost:8082/";

    @BeforeEach
    public void clearRedisData() {
        testRestTemplate = new TestRestTemplate();
        testRestTemplateWithAuth = new TestRestTemplate("user", "password", null);

        jedis = new Jedis("localhost", 6379);
        jedis.flushAll();
    }

    @Test
    void testRedisIsEmpty() {
        Set<String> result = jedis.keys("*");
        assertEquals(0, result.size());
    }

    @Test
    void testUnauthenticatedCantAccess() {
        ResponseEntity<String> result = testRestTemplate.getForEntity(testUrl, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void testRedisControlsSession() {
        ResponseEntity<String> result = testRestTemplateWithAuth.getForEntity(testUrl, String.class);
        assertEquals("{\"password\":null,\"username\":\"user\",\"authorities\":[{\"authority\":\"ROLE_USER\"}],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true}", result.getBody()); //login worked

        Set<String> redisResult = jedis.keys("*");
        assertTrue(redisResult.size() > 0); //redis is populated with session data
        for (String s : redisResult) {
            System.out.println(s);
        }

        String sessionCookie = result.getHeaders().get("Set-Cookie").get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        result = testRestTemplate.exchange(testUrl, HttpMethod.GET, httpEntity, String.class);
        assertEquals("{\"password\":null,\"username\":\"user\",\"authorities\":[{\"authority\":\"ROLE_USER\"}],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true}", result.getBody()); //access with session working

        jedis.flushAll(); //clear all keys in redis


        result = testRestTemplate.exchange(testUrl, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        //access denied after sessions are removed in redis
    }

}
