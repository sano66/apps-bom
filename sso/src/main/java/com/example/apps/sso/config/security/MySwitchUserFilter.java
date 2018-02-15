/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserAuthorityChanger;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.util.StringUtils;

/**
 *
 * @author sano
 */
public class MySwitchUserFilter extends SwitchUserFilter {

    @Setter
    private ApplicationEventPublisher eventPublisher;
    @Setter
    private MyUserDetailsService myUserDetailsService;
    @Setter
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    @Setter
    private String usernameParameter = SwitchUserFilter.SPRING_SECURITY_SWITCH_USERNAME_KEY;
    @Setter
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    @Setter
    private String switchAuthorityRole = SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR;
    @Setter
    private SwitchUserAuthorityChanger switchUserAuthorityChanger;

    /**
     *
     */
    @Setter
    private String branchidParameter = MyAuthentication.MY_SECURITY_FORM_BRANCHID_KEY;
    /**
     *
     */
    @Setter
    private String useridParameter = MyAuthentication.MY_SECURITY_FORM_USERID_KEY;

    @Override
    protected Authentication attemptSwitchUser(final HttpServletRequest request) throws AuthenticationException {
        MyAuthenticationToken targetUserRequest;

        String branchid = obtainBranchid(request);
        if (StringUtils.isEmpty(branchid)) {
            throw new UsernameNotFoundException("branchid: " + branchid + " is empty");
        }
        String userid = obtainUserid(request);
        if (StringUtils.isEmpty(userid)) {
            throw new UsernameNotFoundException("userid: " + userid + " is empty");
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Attempt to switch to user [" + userid + "]");
        }

        UserDetails targetUser = this.myUserDetailsService.loadUserByArgs(branchid, userid);
        this.userDetailsChecker.check(targetUser);

//         OK, create the switch user token
        targetUserRequest = createSwitchUserToken(request, targetUser);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Switch User Token [" + targetUserRequest + "]");
        }
//         publish event
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
                    SecurityContextHolder.getContext().getAuthentication(), targetUser));
        }

        return targetUserRequest;
    }

    private MyAuthenticationToken createSwitchUserToken(
            final HttpServletRequest request, final UserDetails targetUser) {

        MyAuthenticationToken targetUserRequest;

        // grant an additional authority that contains the original Authentication object
        // which will be used to 'exit' from the current switched user.
        Authentication currentAuth;

        try {
            // SEC-1763. Check first if we are already switched.
            currentAuth = attemptExitUser(request);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            currentAuth = SecurityContextHolder.getContext().getAuthentication();
        }

        GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(
                this.switchAuthorityRole, currentAuth);

        // get the original authorities
        Collection<? extends GrantedAuthority> orig = targetUser.getAuthorities();

        // Allow subclasses to change the authorities to be granted
        if (this.switchUserAuthorityChanger != null) {
            orig = this.switchUserAuthorityChanger.modifyGrantedAuthorities(targetUser,
                    currentAuth, orig);
        }

        // add the new switch user authority
        List<GrantedAuthority> newAuths = new ArrayList<>(orig);
        newAuths.add(switchAuthority);

        // create the new authentication token
        MyUser myUser = (MyUser) targetUser;
        MyAccount myAccount = myUser.getMyAccount();
        targetUserRequest = new MyAuthenticationToken(myAccount.getBranchid(), myAccount.getUserid(), targetUser,
                targetUser.getPassword(), newAuths);

        // set details
        targetUserRequest
                .setDetails(this.authenticationDetailsSource.buildDetails(request));

        return targetUserRequest;
    }

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
