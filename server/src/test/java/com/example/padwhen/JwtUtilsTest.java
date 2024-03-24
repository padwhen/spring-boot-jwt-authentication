package com.example.padwhen;


import com.example.padwhen.security.jwt.JwtUtils;
import com.example.padwhen.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtUtilsTest {
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private Authentication authentication;
    @Value("${padwhen.app.jwtSecret}")
    private String jwtSecret;
    @Value("${padwhen.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // initializes objects annotated with @Mock
        jwtUtils = new JwtUtils();
        // Sets the value of jwtSecret in jwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }
    // Testing function generateJwtToken() -> method is able to generate a JWT token
    // successfully based on the provided authentication information
    @Test
    public void testGenerateJwtToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
    }
    // Testing when a valid authentication object is used to generate JWT token,
    // the generated token is considered valid according to the 'validateJwtToken' method.
    @Test
    public void testValidateJwtToken_validToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        // token valid -> true
        assertTrue(jwtUtils.validateJwtToken(token));
    }
    @Test
    public void testValidateJwtToken_invalidToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String invalidToken = "invalid_t0k3n";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
    // Expired token by setting the time to be in the past
    @Test
    public void testValidateJwtToken_expiredToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        long expiredTimeMillis = System.currentTimeMillis() - 6000000;
        // Use reflection to access the private key() method
        Method keyMethod = JwtUtils.class.getDeclaredMethod("key");
        keyMethod.setAccessible(true); // Set the method accessible
        Key key = (Key) keyMethod.invoke(jwtUtils); // Invoke the method
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(expiredTimeMillis))
                .setExpiration(new Date(expiredTimeMillis))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }
}
