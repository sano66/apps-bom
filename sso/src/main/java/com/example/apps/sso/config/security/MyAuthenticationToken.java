/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author sano
 */
public class MyAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * token parameter.
     */
    @Getter
    private final String branchid;
    /**
     * token parameter.
     */
    @Getter
    private final String userid;

    /**
     *
     * @param branchid branchid
     * @param userid userid
     * @param principal principal
     * @param credentials credentials
     */
    public MyAuthenticationToken(final String branchid, final String userid, final Object principal,
            final Object credentials) {
        super(principal, credentials);
        this.branchid = branchid;
        this.userid = userid;
    }

    /**
     *
     * @param branchid branchid
     * @param userid userid
     * @param principal principal
     * @param credentials credentials
     * @param authorities authorities
     */
    public MyAuthenticationToken(final String branchid, final String userid, final Object principal,
            final Object credentials,
            final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.branchid = branchid;
        this.userid = userid;
    }

}
