package se.inera.intyg.certificateservice.application.certificate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(
    builder = GetCertificateCountIssuedByResponse.GetCertificateCountIssuedByResponseBuilder.class)
@Value
@Builder
public class GetCertificateCountIssuedByResponse {

  Long numberOfCertificates;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GetCertificateCountIssuedByResponseBuilder {}
}
