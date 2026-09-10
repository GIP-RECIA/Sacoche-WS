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

import fr.recia.sacoche.ws.dto.ExportResponse;
import fr.recia.sacoche.ws.model.Person;
import fr.recia.sacoche.ws.service.IExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ExportController {
    private final IExportService exportService;

    @GetMapping(value = "/export/{uai}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExportResponse>> exportForUai(@PathVariable String uai) {
        List<Person> persons = exportService.exportForUai(uai);
        List<ExportResponse> response = persons.stream().map(ExportResponse::new).toList();
        return ResponseEntity.ok(response);
    }
}
