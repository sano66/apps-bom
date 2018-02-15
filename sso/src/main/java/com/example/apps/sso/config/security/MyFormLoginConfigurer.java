/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class MyFormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, MyFormLoginConfigurer<H>, MyAuthenticationFilter> {

    public MyFormLoginConfigurer() {
        this(new MyAuthenticationFilter(), null);
    }

    public MyFormLoginConfigurer(MyAuthenticationFilter authenticationFilter, String defaultLoginProcessingUrl) {
        super(authenticationFilter, defaultLoginProcessingUrl);
        branchidParameter(MyAuthentication.MY_SECURITY_FORM_BRANCHID_KEY);
        useridParameter(MyAuthentication.MY_SECURITY_FORM_USERID_KEY);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public MyFormLoginConfigurer<H> branchidParameter(String branchidParameter) {
        getAuthenticationFilter().setBranchidParameter(branchidParameter);
        return this;
    }

    public MyFormLoginConfigurer<H> useridParameter(String useridParameter) {
        getAuthenticationFilter().setUseridParameter(useridParameter);
        return this;
    }

    @Override
    public MyFormLoginConfigurer<H> loginPage(String loginPage) {
        return (MyFormLoginConfigurer) super.loginPage(loginPage);
    }

}
