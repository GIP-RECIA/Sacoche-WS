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

import jakarta.servlet.http.HttpServletRequest;
import fr.recia.sacoche.ws.config.bean.SecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    private static final String AUTH_TOKEN_HEADER_NAME = "x-api-key";

    @Test
    void shouldAuthenticateWhenApiKeyMatches(){
        String apiKey = "good-api-key";
        SecurityProperties properties = new SecurityProperties();
        properties.setApiKey(apiKey);
        AuthenticationService service = new AuthenticationService(properties);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTH_TOKEN_HEADER_NAME)).thenReturn(apiKey);
        Authentication authentication = service.getAuthentication(request);
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(apiKey);

    }

    @Test
    void shouldThrowWhenApiKeyDoesNotMatch(){
        SecurityProperties properties = new SecurityProperties();
        properties.setApiKey("bad-api-key");
        AuthenticationService service = new AuthenticationService(properties);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTH_TOKEN_HEADER_NAME)).thenReturn("good-api-key");
        assertThatThrownBy(() -> service.getAuthentication(request)).isInstanceOf(BadCredentialsException.class);
    }
}
