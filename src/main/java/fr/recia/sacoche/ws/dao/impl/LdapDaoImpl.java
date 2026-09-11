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
import fr.recia.sacoche.ws.dao.ILdapDao;
import fr.recia.sacoche.ws.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class LdapDaoImpl implements ILdapDao {
    private final LdapTemplate ldapTemplate;
    private final LDAPProperties ldapProperties;

    @Override
    public List<Person> findAllForUai(final String uai) {
        final Map<String, Person> result = new LinkedHashMap<>();
        final Pattern externalIdPattern = Pattern.compile(ldapProperties.getExternalIdPattern());
        for (final Population population : Population.values()) {
            final String filter = String.format(ldapProperties.getExportFilter(), uai, population.getObjectClass());
            final LdapQuery query = LdapQueryBuilder
                    .query()
                    .base(ldapProperties.getPeopleRootDn())
                    .attributes(LdapAttributes.PERSON_ATTRS.toArray(new String[0]))
                    .filter(new HardcodedFilter(filter));

            final PersonAttributesMapper mapper = new PersonAttributesMapper(population.getLabel(), externalIdPattern);
            final List<Person> found = ldapTemplate.search(query, mapper);
            for (final Person person : found) {
                if (null != person) {
                    result.putIfAbsent(person.getUid(), person);
                }
            }
        }

        return new ArrayList<>(result.values());
    }
}
