/*
 * Copyright 2018 Pivotal Software, Inc..
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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 *
 * @author sano
 */
public class MyUserDetailsServiceImpl extends JdbcDaoSupport
        implements MyUserDetailsService, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public UserDetails loadUserByArgs(String... args)
            throws UsernameNotFoundException {
        Assert.notEmpty(args, "invarid args not empty");
        Assert.state(args.length == 2, "require two args");

        // authentication
        List<UserDetails> users = loadUsersByArgs(args);
        String branchid = args[0];
        String userid = args[1];
        if (users.isEmpty()) {
            this.logger.debug("Query returned no results for branch: '" + branchid + "' user: '" + userid + "'");

            throw new UsernameNotFoundException(
                    this.messages.getMessage("JdbcDaoImpl.notFound",
                            new Object[]{branchid, userid}, "Branchname {0} Username {1} not found"));
        }
        UserDetails user = users.get(0); // contains no GrantedAuthority[]

        // authorization
        List<GrantedAuthority> dbAuths = new ArrayList<>();
        dbAuths.add(new SimpleGrantedAuthority(MyAuthorities.AUTHORITY_USER));

        // return 
        MyAccount myAccount = ((MyUser) user).getMyAccount();
        return new MyUser(myAccount, userid, user.getPassword(), dbAuths);
    }

    private static final String DEF_USERS_BY_ARGS_QUERY
            = "select b.branchid, b.branchname, b.country, "
            + "u.userid, u.email, u.firstname, u.lastname from my_signon s "
            + "inner join my_branches b on b.branchid = s.branchid "
            + "inner join my_users u on u.userid = s.userid "
            + "where s.branchid = ? "
            + "and s.userid = ?";

    /**
     * Executes the SQL <tt>usersByUsernameQuery</tt> and returns a list of
     * UserDetails objects. There should normally only be one matching user.
     *
     * @param args args
     * @return List&lt;UserDetails&gt;
     */
    protected List<UserDetails> loadUsersByArgs(String... args) {
        String branchid = args[0];
        String userid = args[1];
        return getJdbcTemplate().query(DEF_USERS_BY_ARGS_QUERY,
                new String[]{branchid, userid}, (ResultSet rs, int rowNum) -> {
                    String resultBranchid = rs.getString(1);
                    String resultBranchname = rs.getString(2);
                    String resultCountry = rs.getString(3);
                    String resultUserid = rs.getString(4);
                    String resultEmail = rs.getString(4);
                    String resultFirstname = rs.getString(5);
                    String resultLastname = rs.getString(6);
                    MyAccount myAccount = new MyAccount(resultBranchid, resultBranchname, resultCountry,
                            resultUserid, resultEmail, resultFirstname, resultLastname);
                    return new MyUser(myAccount, userid, "no_password", AuthorityUtils.NO_AUTHORITIES);
                });
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
