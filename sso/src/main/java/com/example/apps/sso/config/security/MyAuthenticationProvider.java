/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.apps.sso.config.security;

import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * An {@link AuthenticationProvider} implementation that retrieves user details
 * from a {@link UserDetailsService}.
 *
 * @author sano
 */
public class MyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Setter
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.myUserDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(final String username,
            final UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;
        String branchid = token.getBranchid();
        String userid = token.getUserid();
        UserDetails loadedUser;

        try {
            loadedUser = myUserDetailsService.loadUserByArgs(branchid, userid);
        }
        catch (UsernameNotFoundException notFound) {
            throw notFound;
        }
        catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // NOP
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (MyAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;
        MyAuthenticationToken result = new MyAuthenticationToken(
                token.getBranchid(), token.getUserid(), principal, authentication.getCredentials(),
                user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

}
