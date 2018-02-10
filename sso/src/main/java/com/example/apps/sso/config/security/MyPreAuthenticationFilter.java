/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

/**
 *
 * @author sano
 */
public class MyPreAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 
     */
    public MyPreAuthenticationFilter(final String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        super.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(defaultFilterProcessesUrl));
    }

    /**
     * 
     */
    private static final String EMPTY = "";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String branchid = obtainBranchid(request);
        String userid = obtainUserid(request);

        MyAuthenticationToken authRequest = new MyAuthenticationToken(branchid, userid,
                userid, EMPTY);

        // Allow subclasses to set the "details" property
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        if (auth instanceof MyAuthenticationToken) {
            return false;
        }
        if (super.requiresAuthentication(request, response)) {
            return false;
        }
        String branchid = obtainBranchid(request);
        if (StringUtils.isEmpty(branchid)) {
            return false;
        }
        String userid = obtainUserid(request);
        return !StringUtils.isEmpty(userid);
    }

    private String obtainBranchid(HttpServletRequest request) {
        return request.getParameter("branchid");
    }

    private String obtainUserid(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        if (StringUtils.isEmpty(userid)) {
            AnonymousAuthenticationToken token = (AnonymousAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetails user = (UserDetails) token.getPrincipal();
            userid = user.getUsername();
        }
        return userid;
    }

}
