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
package fr.recia.sacoche.ws.dto;

import fr.recia.sacoche.ws.model.Person;
import lombok.Data;

@Data
public class ExportResponse {
    private String externalId;
    private String profile;
    private String lastName;
    private String firstName;

    public ExportResponse(Person person){
        this.externalId = person.getExternalId();
        this.profile = person.getProfile();
        this.lastName = person.getLastName();
        this.firstName = person.getFirstName();
    }
}
