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
