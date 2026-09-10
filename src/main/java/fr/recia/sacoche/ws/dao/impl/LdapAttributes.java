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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LdapAttributes {
    public static final String UID = "uid";
    public static final String SN = "sn";
    public static final String GIVEN_NAME = "givenName";
    public static final String ESCO_PERSON_EXTERNAL_IDS = "ESCOPersonExternalIds";

    public static final Set<String> PERSON_ATTRS =
            Stream.of(
                    UID,
                    SN,
                    GIVEN_NAME,
                    ESCO_PERSON_EXTERNAL_IDS
                    ).collect(Collectors.toUnmodifiableSet());

    private LdapAttributes() {
    }
}
