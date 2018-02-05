/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author sano
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     *
     */
    private static final String EMPTY = "";

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
            final HttpServletResponse response) throws AuthenticationException {
        String branchid = obtainBranchid(request);
        String userid = obtainUserid(request);

        MyAuthenticationToken authRequest = new MyAuthenticationToken(branchid, userid, userid, EMPTY);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     *
     */
    public static final String MY_SECURITY_FORM_BRANCHID_KEY = "branchid";
    /**
     *
     */
    public static final String MY_SECURITY_FORM_USERID_KEY = "userid";

    /**
     *
     */
    @Setter
    private String branchidParameter = MY_SECURITY_FORM_BRANCHID_KEY;
    /**
     *
     */
    @Setter
    private String useridParameter = MY_SECURITY_FORM_USERID_KEY;

    /**
     *
     * @param request HttpServletRequest
     * @return String
     */
    protected String obtainBranchid(final HttpServletRequest request) {
        return request.getParameter(branchidParameter);
    }

    /**
     *
     * @param request HttpServletRequest
     * @return String
     */
    protected String obtainUserid(final HttpServletRequest request) {
        return request.getParameter(useridParameter);
    }

}
