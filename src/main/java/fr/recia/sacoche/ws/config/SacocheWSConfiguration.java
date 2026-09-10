/*
 * Copyright © 2026 GIP-RECIA (https://www.recia.fr/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.recia.sacoche.ws.config;


import fr.recia.sacoche.ws.config.bean.LDAPProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SacocheWSConfiguration {


    private final LDAPProperties ldapProperties;

    @Bean
    public LdapTemplate ldapTemplate(){

        final LdapTemplate ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(contextSource());
        ldapTemplate.setDefaultCountLimit(ldapProperties.getCountLimit());
        ldapTemplate.setDefaultTimeLimit(ldapProperties.getTimeout());
        return  ldapTemplate;
    }

    @Bean
    public LdapContextSource contextSource(){
        final LdapContextSource ldapContextSource = new LdapContextSource();

        ldapContextSource.setAnonymousReadOnly(ldapProperties.isAnonymousReadOnly());
        ldapContextSource.setBase(ldapProperties.getBase());
        ldapContextSource.setUrl(ldapProperties.getUrl());
        ldapContextSource.setUserDn(ldapProperties.getUserDn());
        ldapContextSource.setPassword(ldapProperties.getPassword());
        ldapContextSource.setPooled(ldapProperties.isPooled());

        return  ldapContextSource;
    }
}
