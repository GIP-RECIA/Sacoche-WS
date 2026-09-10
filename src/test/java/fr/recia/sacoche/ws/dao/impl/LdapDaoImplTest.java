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


import fr.recia.sacoche.ws.config.bean.LDAPProperties;
import fr.recia.sacoche.ws.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class LdapDaoImplTest {

    private LdapTemplate ldapTemplate;
    private LDAPProperties ldapProperties;
    private LdapDaoImpl dao;

    @BeforeEach
    void setUp() {
        ldapTemplate = mock(LdapTemplate.class);
        ldapProperties = new LDAPProperties();
        ldapProperties.setPeopleRootDn("ou=people");
        ldapProperties.setExportFilter("(&(uai=%s)(type=%s))");
        ldapProperties.setExternalIdPattern("^TEST\\$(.*)$");
        dao = new LdapDaoImpl(ldapTemplate, ldapProperties);
    }

    @Test
    void shouldMergeAndDeduplicateResultsAcrossPopulations() {
        Person teacherA = new Person();
        teacherA.setUid("A");
        teacherA.setProfile("Enseignant");

        Person teacherB = new Person();
        teacherB.setUid("B");
        teacherB.setProfile("Enseignant");

        Person staffC = new Person();
        staffC.setUid("C");
        staffC.setProfile("Personnel");

        Person studentA = new Person();
        studentA.setUid("A");
        studentA.setProfile("Élève");

        when(ldapTemplate.search(any(LdapQuery.class), any(ContextMapper.class)))
                .thenReturn(List.of(teacherA, teacherB))
                .thenReturn(List.of(staffC))
                .thenReturn(List.of(studentA))
                .thenReturn(List.of());

        List<Person> result = dao.findAllForUai("TEST-UAI");

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Person::getUid).containsExactlyInAnyOrder("A", "B", "C");

        Person keptA = result.stream().filter(p -> "A".equals(p.getUid())).findFirst().orElseThrow();
        assertThat(keptA.getProfile()).isEqualTo("Enseignant");
    }

}
