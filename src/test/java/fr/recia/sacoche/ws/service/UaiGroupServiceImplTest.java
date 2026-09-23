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

import fr.recia.sacoche.ws.config.bean.GroupingProperties;
import fr.recia.sacoche.ws.service.impl.UaiGroupServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UaiGroupServiceImplTest {

    @Test
    void shouldReturnUaiUnchangedWhenNoMappingAndNoGroup() {
        final GroupingProperties properties = new GroupingProperties();
        final UaiGroupServiceImpl service = new UaiGroupServiceImpl(properties);

        assertThat(service.getGroupedUais("uaiX")).containsExactly("uaiX");
    }

    @Test
    void shouldResolveFakeUaiWhenNotInAnyGroup() {
        final Map<String, String> fakeUai = new HashMap<>();
        fakeUai.put("fakeUai", "realUai");
        final GroupingProperties properties = new GroupingProperties();
        properties.setFakeMapping(fakeUai);
        final UaiGroupServiceImpl service = new UaiGroupServiceImpl(properties);

        assertThat(service.getGroupedUais("fakeUai")).containsExactly("realUai");
    }

    @Test
    void shouldResolveFakeUaiThenReturnItsGroup() {

        final Map<String, String> fakeUai = new HashMap<>();
        fakeUai.put("fakeUai", "realUai");

        final List<List<String>> uaiGroups = new ArrayList<>();
        uaiGroups.add(List.of("realUai", "other"));
        final GroupingProperties properties = new GroupingProperties();
        properties.setFakeMapping(fakeUai);
        properties.setUaiGroups(uaiGroups);
        final UaiGroupServiceImpl service = new UaiGroupServiceImpl(properties);

        assertThat(service.getGroupedUais("fakeUai")).containsExactly("realUai", "other");
    }

    @Test
    void shouldReturnGroupWhenUaiDirectlyInGroup() {

        final List<List<String>> uaiGroups = new ArrayList<>();
        uaiGroups.add(List.of("realUai", "other"));
        final GroupingProperties properties = new GroupingProperties();
        properties.setUaiGroups(uaiGroups);
        final UaiGroupServiceImpl service = new UaiGroupServiceImpl(properties);

        assertThat(service.getGroupedUais("realUai")).containsExactly("realUai", "other");
    }
}