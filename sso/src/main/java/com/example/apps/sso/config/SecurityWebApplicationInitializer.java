/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Spring Security サーブレットコンテナ初期化処理.
 * <ul>
 * <li>ContextLoaderListenerをサーブレットコンテナに登録する</li>
 * <li>Spring Securityのサーブレットフィルタクラスをサーブレットコンテナに登録する処理</li>
 * </ul>
 *
 * @author sano
 */
public final class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    /**
     * コンストラクタ.
     */
    public SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }

}
