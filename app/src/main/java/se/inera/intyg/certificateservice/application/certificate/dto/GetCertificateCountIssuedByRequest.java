package se.inera.intyg.certificateservice.application.certificate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(
    builder = GetCertificateCountIssuedByRequest.GetCertificateCountIssuedByRequestBuilder.class)
@Value
@Builder
public class GetCertificateCountIssuedByRequest {

  String issuedByHsaId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GetCertificateCountIssuedByRequestBuilder {}
}
