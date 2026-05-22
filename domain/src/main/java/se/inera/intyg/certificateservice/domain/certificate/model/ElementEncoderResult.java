package se.inera.intyg.certificateservice.domain.certificate.model;

import java.util.List;

public record ElementEncoderResult(boolean canEncode, List<String> invalidChars) {}