/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author sano
 */
public class MyUser extends User {

    @Getter
    private final MyAccount myAccount;

    public MyUser(MyAccount myAccount, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.myAccount = myAccount;
    }

    public MyUser(MyAccount myAccount, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.myAccount = myAccount;
    }

}
