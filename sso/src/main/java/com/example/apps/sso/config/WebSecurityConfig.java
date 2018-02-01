/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

/**
 * Spring Security コンフィグレーションクラス.
 * <p>
 * 引数の型が異なる3つのconfigureメソッドでSpring Securityを定義する.</p>
 * <h2>configure(引数の型WebSecurityクラス)</h2>
 * <ul>
 * <li>Spring Security 適用しないリソースの定義</li>
 * <li>Spring Security デバッグ指示（ログの出力はlogback.xmlで制御可能）</li>
 * </ul>
 * <h2>configure(引数の型HttpSecurityクラス) </h2>
 * <ul>
 * <li>リクエストヘッダーの確認フィルタ定義</li>
 * <li>認証対象のページとアクセスするためのROLEの定義</li>
 * </ul>
 * <h2>configure(引数の型AuthenticationManagerBuilderクラス) </h2>
 * <ul>
 * <li>追加したフィルタが使用するtokenの型に応じた認証プロバイダ定義</li>
 * </ul>
 *
 * @author sano
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 認証を適用しないリソースの登録およびデバッグの指示.
     *
     * @param web WebSecurity
     * @throws Exception exception
     */
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/assets/**");
        web.debug(true);
    }

    /**
     * 認証の定義.
     *
     * @param http HttpSecurity
     * @throws Exception exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.addFilter(requestHeaderAuthenticationFilter());
        http.authorizeRequests()
                .antMatchers("/debug.jsp", "/permit_all.html").permitAll()
                .anyRequest().authenticated();
    }

    /**
     * フィルタが使用するtokenに応じた認証プロバイダの登録.
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception exception
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
    }

    /**
     * リクエストヘッダーに付与されたユーザ情報を取得するフィルタ.
     *
     * @return filter RequestHeaderAuthenticationFilter
     * @exception Exception exception
     */
    @Bean
    RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader("SSO_USER");
        filter.setAuthenticationDetailsSource(webAuthenticationDetailsSource());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    /**
     * RequestHeaderAuthenticationFilterが使用するToken
     * {@link org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider}
     * に対応した認証プロバイダ.
     *
     * @return provider PreAuthenticatedAuthenticationProvider
     */
    @Bean
    PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
        return provider;
    }

    /**
     * Filterで設定するtoken.detailsの内容を返却する.
     * token.detailはGrantedAuthorityを実装しなければならない。
     *
     * @return WebAuthenticationDetailsSource
     */
    @Bean
    WebAuthenticationDetailsSource webAuthenticationDetailsSource() {
        return new WebAuthenticationDetailsSource() {
            private final List<GrantedAuthority> authorities
                    = new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ANONYMOUS")));

            @Override
            public WebAuthenticationDetails buildDetails(final HttpServletRequest context) {
                return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, authorities);
            }
        };
    }
}
