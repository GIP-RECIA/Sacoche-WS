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

import fr.recia.sacoche.ws.config.bean.SecurityProperties;
import fr.recia.sacoche.ws.config.security.AuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfiguration {
    private final SecurityProperties securityProperties;

    public WebSecurityConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        StringBuilder hasIpAddress = new StringBuilder(
                "hasIpAddress('127.0.0.1') or hasIpAddress('::1')"
        );
        for (String ip : securityProperties.getAuthorizedIpAccess()) {
            hasIpAddress.append(" or hasIpAddress('").append(ip).append("')");
        }

        String accessExpression = "isAuthenticated() and (" + hasIpAddress + ")";

        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health-check").permitAll()
                        .requestMatchers("/api/**").access(new WebExpressionAuthorizationManager(accessExpression))
                        .anyRequest().denyAll())
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

}

