/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security コンフィグレーションクラス.
 * <p>引数の型が異なる3つのconfigureメソッドでSpring Securityを定義する.</p>
 * <ul>
 * <li>引数の型WebSecurityクラス<ul>
 * <li>Spring Security 適用しないリソースの定義</li>
 * <li>Spring Security デバッグ</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author sano
 */
@Configuration
@EnableWebSecurity
public final class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/assets/**");
        web.debug(true);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/debug.jsp", "/permit_all.html").permitAll()
                .anyRequest().authenticated();
    }
}
