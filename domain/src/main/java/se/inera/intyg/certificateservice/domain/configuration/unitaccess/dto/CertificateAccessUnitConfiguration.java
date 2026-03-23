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
package se.inera.intyg.certificateservice.domain.configuration.unitaccess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.configuration.unitaccess.dto.CertificateAccessUnitConfiguration.CertificateAccessUnitConfigurationBuilder;

@JsonDeserialize(builder = CertificateAccessUnitConfigurationBuilder.class)
@Builder
@Value
public class CertificateAccessUnitConfiguration {

  @JsonProperty("label")
  String label;

  @JsonProperty("type")
  String type;

  @JsonProperty("fromDateTime")
  LocalDateTime fromDateTime;

  @JsonProperty("toDateTime")
  LocalDateTime toDateTime;

  @JsonProperty("careProviders")
  @Builder.Default
  List<String> careProviders = Collections.emptyList();

  @JsonProperty("careUnit")
  @Builder.Default
  List<String> careUnit = Collections.emptyList();

  @JsonProperty("issuedOnUnit")
  @Builder.Default
  List<String> issuedOnUnit = Collections.emptyList();

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAccessUnitConfigurationBuilder {}
}
