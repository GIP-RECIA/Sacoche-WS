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
package fr.recia.sacoche.ws.dao.impl;

import fr.recia.sacoche.ws.model.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.NamingException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PersonAttributesMapperTest {

    static Stream<Arguments> externalIdsProvider() {
        return Stream.of(
                Arguments.of(new String[]{"DOC$999", "TEST$abc123"}, "abc123"),
                Arguments.of(new String[]{"DOC$999", "TEST$abd-3-8"}, "abd-3-8"),
                Arguments.of(new String[]{"DOC$999", "PALAPAPAPA$abc123"}, null),
                Arguments.of(new String[]{}, null)
        );
    }

    @ParameterizedTest
    @MethodSource("externalIdsProvider")
    void shouldExtractIdOrReturnNull() throws NamingException {
        DirContextAdapter context = mock(DirContextAdapter.class);
        when(context.getStringAttribute(LdapAttributes.UID)).thenReturn("jdupont");
        when(context.getStringAttribute(LdapAttributes.SN)).thenReturn("Dupont");
        when(context.getStringAttribute(LdapAttributes.GIVEN_NAME)).thenReturn("Jean");
        when(context.getStringAttributes(LdapAttributes.ESCO_PERSON_EXTERNAL_IDS)).thenReturn(new String[]{"DOC$999", "TEST$abc123"});

        PersonAttributesMapper mapper = new PersonAttributesMapper(Pattern.compile("^TEST\\$(.*)$"));

        Person person = mapper.mapFromContext(context);
        assertThat(person.getUid()).isEqualTo("jdupont");
        assertThat(person.getLastName()).isEqualTo("Dupont");
        assertThat(person.getFirstName()).isEqualTo("Jean");
        assertThat(person.getExternalId()).isEqualTo("abc123");

    }
}
