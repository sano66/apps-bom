/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

/**
 *
 * @author sano
 */
public class MyAuthorities {
    public static final String AUTHORITY_ANONYMOUS = "anonymous";
    public static final String AUTHORITY_USER = "user";
    public static final String AUTHORITY_ADMIN = "admin";
    public static final String AUTHORITY_MANAGER = "manager";
    
    private MyAuthorities() {
        // NOP
    }
}
