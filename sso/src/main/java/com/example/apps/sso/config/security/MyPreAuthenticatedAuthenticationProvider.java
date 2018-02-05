/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 *
 * @author sano
 */
public class MyPreAuthenticatedAuthenticationProvider extends PreAuthenticatedAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        List<GrantedAuthority> dbAuths = new ArrayList<>();
        dbAuths.add(new SimpleGrantedAuthority(MyAuthorities.AUTHORITY_ANONYMOUS));
        return new AnonymousAuthenticationToken(authentication.getName(), authentication.getPrincipal(), dbAuths);
    }

}
