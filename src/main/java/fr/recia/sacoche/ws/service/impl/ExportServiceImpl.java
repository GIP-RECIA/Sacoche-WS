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
package fr.recia.sacoche.ws.service.impl;

import fr.recia.sacoche.ws.dao.ILdapDao;
import fr.recia.sacoche.ws.model.Person;
import fr.recia.sacoche.ws.service.IExportService;
import fr.recia.sacoche.ws.service.IUaiGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements IExportService {
    private final ILdapDao ldapDao;
    private final IUaiGroupService uaiGroupService;

    public List<Person> exportForUai(final String uai) {
        final List<String> uais = uaiGroupService.getGroupedUais(uai);
        if (uais.size() > 1) {
            log.debug("uai='{}' belongs to a group, querying {} uais: {}", uai, uais.size(), uais);
        }
        final Map<String, Person> result = new LinkedHashMap<>();
        for (final String uaiToQuery : uais) {
            for (final Person person : ldapDao.findAllForUai(uaiToQuery)) {
                result.putIfAbsent(person.getUid(), person);
            }
        }
        return new ArrayList<>(result.values());
    }
}
