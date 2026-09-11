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

import fr.recia.sacoche.ws.config.bean.GroupingProperties;
import fr.recia.sacoche.ws.service.IUaiGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UaiGroupServiceImpl implements IUaiGroupService {

    private final GroupingProperties groupingProperties;

    @Override
    public List<String> getGroupedUais(final String uai) {
        for (final List<String> group : this.groupingProperties.getUaiGroups()) {
            if (group.contains(uai)) {
                return group;
            }
        }
        return List.of(uai);
    }
}
