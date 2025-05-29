package com.dux.challenge.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String username;
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        // devuelve una lista vacía para satisfacer el contrato de UserDetails
        return Collections.emptyList();
    }
    
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
