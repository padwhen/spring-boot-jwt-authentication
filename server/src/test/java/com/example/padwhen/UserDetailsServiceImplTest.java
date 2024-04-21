package com.example.padwhen;

import com.example.padwhen.models.ERole;
import com.example.padwhen.models.Role;
import com.example.padwhen.models.User;
import com.example.padwhen.repository.UserRepository;
import com.example.padwhen.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(new Role(ERole.ROLE_USER)));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Assert
        assertEquals(username, userDetails.getUsername());
    }
    @Test
    public void testLoadByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistingUser";
        // Act and assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );
        assertEquals("User Not Found with Username: " + username, exception.getMessage());
    }
}
