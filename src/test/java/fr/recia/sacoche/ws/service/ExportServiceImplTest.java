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
package fr.recia.sacoche.ws.service;

import fr.recia.sacoche.ws.dao.ILdapDao;
import fr.recia.sacoche.ws.model.Person;
import fr.recia.sacoche.ws.service.impl.ExportServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExportServiceImplTest {

    @Test
    void shouldMergeAndDeduplicateAcrossGroupedUais() {
        final ILdapDao ldapDao = mock(ILdapDao.class);
        final IUaiGroupService uaiGroupService = mock(IUaiGroupService.class);
        final ExportServiceImpl service = new ExportServiceImpl(ldapDao, uaiGroupService);

        final Person shared = new Person();
        shared.setUid("shared");
        final Person onlyInB = new Person();
        onlyInB.setUid("onlyInB");

        when(uaiGroupService.getGroupedUais("uaiA")).thenReturn(List.of("uaiA", "uaiB"));
        when(ldapDao.findAllForUai("uaiA")).thenReturn(List.of(shared));
        when(ldapDao.findAllForUai("uaiB")).thenReturn(List.of(shared, onlyInB));

        final List<Person> result = service.exportForUai("uaiA");

        assertThat(result).extracting(Person::getUid).containsExactlyInAnyOrder("shared", "onlyInB");
    }

    @Test
    void shouldQueryOnlyTheGivenUaiWhenNotGrouped() {
        final ILdapDao ldapDao = mock(ILdapDao.class);
        final IUaiGroupService uaiGroupService = mock(IUaiGroupService.class);
        final ExportServiceImpl service = new ExportServiceImpl(ldapDao, uaiGroupService);

        final Person person = new Person();
        person.setUid("solo");

        when(uaiGroupService.getGroupedUais("uaiSolo")).thenReturn(List.of("uaiSolo"));
        when(ldapDao.findAllForUai("uaiSolo")).thenReturn(List.of(person));

        final List<Person> result = service.exportForUai("uaiSolo");

        assertThat(result).containsExactly(person);
    }
}
