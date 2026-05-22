package se.inera.intyg.certificateservice.domain.certificate.model;

import java.nio.charset.CharsetEncoder;
import java.util.Collections;

public class ElementEncoder {

  private ElementEncoder() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementEncoderResult canEncode(CharsetEncoder encoder, String value) {
    if (value == null || value.isEmpty()) {
      return new ElementEncoderResult(true, Collections.emptyList());
    }

    final var invalidChars =
        value
            .chars()
            .filter(c -> !encoder.canEncode((char) c))
            .distinct()
            .mapToObj(c -> String.valueOf((char) c))
            .toList();

    return new ElementEncoderResult(invalidChars.isEmpty(), invalidChars);
  }
}