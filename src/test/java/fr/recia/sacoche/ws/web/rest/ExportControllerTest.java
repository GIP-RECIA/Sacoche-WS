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
package fr.recia.sacoche.ws.web.rest;

import fr.recia.sacoche.ws.config.security.AuthenticationFilter;
import fr.recia.sacoche.ws.model.Person;
import fr.recia.sacoche.ws.service.IExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ExportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IExportService exportService;

    @MockitoBean
    private AuthenticationFilter authenticationFilter;

    @Test
    void shouldReturnEportedPersonsAsJson() throws Exception{
        Person person = new Person();
        person.setLastName("Dupont");
        person.setFirstName("Michel");
        person.setProfile("Enseignant");
        person.setExternalId("ext-123");

        when(exportService.exportForUai("uai-test")).thenReturn(List.of(person));

        mockMvc.perform(get("/api/export/{uai}","uai-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].externalId").value("ext-123"))
                .andExpect(jsonPath("$[0].firstName").value("Michel"))
                .andExpect(jsonPath("$[0].lastName").value("Dupont"))
                .andExpect(jsonPath("$[0].profile").value("Enseignant"));
    }

}
