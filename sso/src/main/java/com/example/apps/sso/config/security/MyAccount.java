/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.apps.sso.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author sano
 */
@Getter
@AllArgsConstructor
public class MyAccount {
    
    //
    private String branchid;
    private String branchname;
    private String country;
    
    //
    private String userid;
    private String email;
    private String firstname;
    private String lastname;
    
}
