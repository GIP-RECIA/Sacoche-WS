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
package fr.recia.sacoche.ws.config.security;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import fr.recia.sacoche.ws.config.bean.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "x-api-key";

    private static SecurityProperties securityProperties;

    private final SecurityProperties config;

    @PostConstruct
    private void setUp() {
        securityProperties = this.config;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        Assert.notNull(securityProperties, "You have a misconfiguration of the class with the injected bean appConfProperties !");

        final String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (!securityProperties.getApiKey().equals(apiKey)) {
            log.warn("Rejected API call from IP '{}' - invalid API key" , request.getRemoteAddr());
            throw new BadCredentialsException("Invalid API Key");
        }
        final String clientId = securityProperties.getApiKey();
        return new ApiKeyAuthentication(clientId, AuthorityUtils.NO_AUTHORITIES);
    }
}