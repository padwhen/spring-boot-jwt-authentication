package com.example.padwhen;

import com.example.padwhen.security.jwt.AuthEntryPointJwt;
import com.example.padwhen.security.jwt.AuthTokenFilter;
import com.example.padwhen.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;
    @Autowired
    private AuthTokenFilter authTokenFilter;

}
