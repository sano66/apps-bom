/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Spring Security 初期設定クラス.
 * @author sano
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    /**
     * コンストラクタ.
    */
    public SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }

}
