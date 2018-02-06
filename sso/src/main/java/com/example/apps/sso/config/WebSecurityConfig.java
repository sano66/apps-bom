/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config;

import com.example.apps.sso.config.security.MyAuthenticationProvider;
import com.example.apps.sso.config.security.MyFormLoginConfigurer;
import com.example.apps.sso.config.security.MyUserDetailsService;
import com.example.apps.sso.config.security.MyUserDetailsServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

/**
 * Spring Security コンフィグレーションクラス.
 * <h1>認証シナリオ</h1>
 * <ol>
 * <li>シングルサインオン経由でアクセスするウェブアプリケーションを想定</li>
 * <li>シングルサインオンはユーザ名、パスワードで認証済の前提</li>
 * <li>シングルサインオン認証が済んでいる場合はhttpヘッダーにSSO_USERが付与されている</li>
 * <li>ウェブアプリケーションを利用するにはシングルサインオンの認証とは別に、店識別とSSO_USERで指定したユーザ名による認証が必要</li>
 * <li>アプリケーションの認証ができなかった場合は、店識別とユーザ名による認証が必要</li>
 * <li>アプリケーションの認証が未済のとき、店識別をリクエストパラメータで指定した場合は、認証済のSSO_USERとあわせてログイン認証を実施する</li>
 * <li>店識別とはNYとかPARISとか店が存在する地名を表す任意の文字列</li>
 * <li>ログイン済のユーザは別の店識別のユーザへ切り替え可能</li>
 * </ol>
 * <h1>Spring Securityが具備すべきフィルタと対応するトークン</h1>
 * <table>
 * <caption>Spring Securityが具備すべきフィルタと対応するトークン</caption>
 * <thead><tr><th>フィルタ内容</th><th>フィルタ名</th><th>トークン名</th><th>適用順序</th></tr></thead>
 * <tbody>
 * <tr><td>リクエストヘッダーのSSO_USERの有無を判断し、ない場合はエラーとするフィルター</td>
 * <td>requestHeaderAuthenticationFilter</td>
 * <td>AnonymousToken</td><td>1</td></tr>
 * <tr><td>店識別とユーザ名によるフォームログインのフィルター</td>
 * <td>MyFormLoginFilter</td><td>MyAuthenticationToken</td><td>3</td></tr>
 * <tr><td>認証未済の場合、店識別が与えられた場合に認証を行うフィルター</td>
 * <td>MyPreAuthenticationFilter</td><td>MyAuthenticationToken</td><td>2</td></tr>
 * <tr><td>店識別とユーザ名によるユーザ切り替えのフィルター</td>
 * <td>MySwitchUserFilter</td><td>MyAuthenticationToken</td><td>4</td></tr>
 * </tbody>
 * </table>
 * <h1>Spring Securityの設定仕様</h1>
 * <ol>
 * <li>/assets, /staticはHTMLリソースのため認証対象外</li>
 * <li>デバッグ機能は利用可能とし、ロギング設定ファイル/src/resources/logback.xmlにより出力を制御する</li>
 * <li>DB認証のデータソースは認証専用のデータソースを利用する</li>
 * <li>ログアウトの際、セッションを初期化する</li>
 * <li>認証エラー、認可エラーに関してエラーページは用意しない</li>
 * </ol>
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
        http.apply(new MyFormLoginConfigurer<>()).loginPage("/login.jsp").permitAll();
        http.authorizeRequests()
                .antMatchers("/debug.jsp", "/permit_all.html").permitAll()
                .anyRequest().authenticated();
        http.logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
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
        auth.authenticationProvider(myAuthenticationProvider());
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
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider() {
            @Override
            public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
                Authentication auth = super.authenticate(authentication);
                return new AnonymousAuthenticationToken("ANONYMOUS", auth.getPrincipal(), auth.getAuthorities());
            }

        };
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

    /**
     *
     * @return DataSource
     */
    @Bean
    DataSource authDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addDefaultScripts()
                .build();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        JdbcDaoImpl service = new JdbcDaoImpl();
        service.setDataSource(authDataSource());
        return service;
    }

    @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider provider = new MyAuthenticationProvider();
        provider.setMyUserDetailsService(myUserDetailsService());
        return provider;
    }

    @Bean
    MyUserDetailsService myUserDetailsService() {
        MyUserDetailsServiceImpl service = new MyUserDetailsServiceImpl();
        service.setDataSource(authDataSource());
        return service;
    }
}
