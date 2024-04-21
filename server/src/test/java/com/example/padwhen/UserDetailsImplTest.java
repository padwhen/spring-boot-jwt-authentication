package com.example.padwhen;

import com.example.padwhen.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {
    private final Long id = 1L;
    private final String username = "testUser";
    private final String password = "password";
    private final String email = "testuser@gmail.com";
    private final Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    @Test
    public void testUserDetailsCreation() {
        // Collection hold objects implementing the GrantedAuthority interface.
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);
        assertNotNull(userDetails);
        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(email, userDetails.getEmail());
        assertEquals(authorities, userDetails.getAuthorities());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
    @Test
    public void testEmptyAuthorities() {
        Collection<? extends GrantedAuthority> emptyAuthorities = Collections.emptyList();
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email,  password, emptyAuthorities);
        assertNotNull(userDetails);
        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(email, userDetails.getEmail());
        assertEquals(emptyAuthorities, userDetails.getAuthorities());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
    // Testing the behaviour of the constructor of UserDetailsImpl when certain
    // parameters are null.
    @Test
    public void testNullId() {
        assertThrows(IllegalArgumentException.class, () -> new UserDetailsImpl(null, username, email, password, authorities));
    }
    @Test
    public void testNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new UserDetailsImpl(id, null, email, password, authorities));
    }
    @Test
    public void testNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> new UserDetailsImpl(id, username, null, password, authorities));
    }
    @Test
    public void testNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserDetailsImpl(id, username, email, null, authorities));
    }
    @Test
    public void testNullAuthorities() {
        assertThrows(IllegalArgumentException.class, () -> new UserDetailsImpl(id, username, email, password, null));
    }
}
