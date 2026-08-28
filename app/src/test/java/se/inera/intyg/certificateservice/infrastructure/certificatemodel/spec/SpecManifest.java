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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.spec;

import java.util.Collections;
import java.util.List;

/**
 * Machine-readable form of a certificate specification ("intygsspecifikation").
 *
 * <p>A manifest is authored once from the specification document, reviewed by a human against that
 * document, and then treated as the contract for the implementation. {@code
 * CertificateSpecConformanceTest} compares the built {@link
 * se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel} against it.
 */
public record SpecManifest(Certificate certificate, List<Category> categories) {

  public SpecManifest {
    categories = categories == null ? Collections.emptyList() : categories;
  }

  /** Metadata from the specification header and the "Texter" section. */
  public record Certificate(
      String type,
      String version,
      String externalCode,
      String name,
      String recipient,
      String description,
      String detailedDescription) {}

  /** A "KAT" row and everything nested under it. */
  public record Category(String id, String name, String description, List<Element> elements) {

    public Category {
      elements = elements == null ? Collections.emptyList() : elements;
    }
  }

  /**
   * One "FRG", "DFR" or message row of the specification table.
   *
   * @param id the specification question number, used verbatim as the {@code ElementId}
   * @param kind FRG, DFR or MSG
   * @param component the SK code, optionally qualified with the datatype, e.g. {@code SK-002
   *     (kodverk)}
   * @param label "Rubriktext"
   * @param helpText "Hjälptext"
   * @param checkboxLabel "Label för checkbox" / "Label för fritextfält"
   * @param header the "Rubrik:" prefix of a Rubriktext cell, when present
   * @param parent the element this one is nested under, i.e. the "FRG" a "DFR" row belongs to
   * @param xmlParent the element id an explicit {@code ElementMapping} must point at, when the XML
   *     structure differs from the question structure
   * @param fieldId the XML-mapping id (TextId, BooleskId, KodId, DatumId, ...)
   * @param maxLength "Antal tecken"
   * @param codeSystem "Kodverk", e.g. KV_FKMU_0002
   * @param options "Urval", in specification order
   * @param rules the SR codes listed in the "Regel" column
   * @param status {@code pending} until the element is implemented, then {@code implemented}
   */
  public record Element(
      String id,
      String kind,
      String component,
      String multiplicity,
      String label,
      String helpText,
      String checkboxLabel,
      String header,
      String parent,
      String xmlParent,
      String fieldId,
      Integer maxLength,
      String codeSystem,
      List<String> options,
      List<String> rules,
      String status) {

    public static final String STATUS_IMPLEMENTED = "implemented";

    public Element {
      options = options == null ? Collections.emptyList() : options;
      rules = rules == null ? Collections.emptyList() : rules;
      status = status == null ? "pending" : status;
    }

    public boolean isImplemented() {
      return STATUS_IMPLEMENTED.equals(status);
    }
  }
}
