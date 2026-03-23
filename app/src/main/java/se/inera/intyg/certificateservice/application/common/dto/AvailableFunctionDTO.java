/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateservice.application.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.application.common.dto.AvailableFunctionDTO.AvailableFunctionDTOBuilder;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CitizenAvailableFunction;

@JsonDeserialize(builder = AvailableFunctionDTOBuilder.class)
@Value
@Builder
public class AvailableFunctionDTO {

  AvailableFunctionType type;
  String name;
  String description;
  String title;
  String body;
  List<InformationDTO> information;
  boolean enabled;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AvailableFunctionDTOBuilder {}

  public static AvailableFunctionDTO toDTO(CitizenAvailableFunction function) {
    return AvailableFunctionDTO.builder()
        .title(function.title())
        .name(function.name())
        .type(AvailableFunctionType.valueOf(function.type().name()))
        .body(function.body())
        .enabled(function.enabled())
        .description(function.description())
        .information(
            function.information() != null
                ? function.information().stream()
                    .map(
                        info ->
                            InformationDTO.builder()
                                .id(info.id() != null ? info.id().id() : "")
                                .type(InformationType.valueOf(info.type().name()))
                                .text(info.text())
                                .build())
                    .toList()
                : List.of())
        .build();
  }
}
