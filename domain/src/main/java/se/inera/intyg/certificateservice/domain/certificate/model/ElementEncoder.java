package se.inera.intyg.certificateservice.domain.certificate.model;

import java.nio.charset.CharsetEncoder;

public class ElementEncoder {

  private ElementEncoder() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementEncoderResult canEncode(CharsetEncoder encoder, String value) {
    final var invalidChars =
        value
            .chars()
            .filter(c -> !encoder.canEncode((char) c))
            .distinct()
            .mapToObj(c -> String.valueOf((char) c))
            .toList();

    return new ElementEncoderResult(!invalidChars.isEmpty(), invalidChars);
  }
}