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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.copilot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTerminalField;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.service.CertificatePdfFillService;

class PdfSpecificationCopilotHelperTest {

  /**
   * This class can be used to build PdfSpecification and PdfConfigurations for a certificate using
   * GitHub Copilot.
   *
   * <p>How to use: 1. Generate a file using this test for the new certificate type 2. Generate a
   * file using this test for a similar certificate type (one with overflow page if the new one
   * contains it for example) 3. Attach to context these two files and the already existing
   * PdfSpecification for the similar certificate 4. Use prompts down.
   *
   * <p>Prompt PdfSpecification: Generate a pdf specification for FK4727 following the previous
   * pattern in PdfSpecification file in your context, but not the values. The values like ids, page
   * indexes etc. you will get from certificate_type_structure. If overflow page is defined then set
   * those values. Remember that when values are indexes they start from 0 so it will not be the
   * page number but page index. If more than one page remember there should be more than one
   * patient id field in list, one per page.
   *
   * <p>Prompt PdfConfiguration for question: Generate a pdf configuration and tests for this
   * question using the attached Question as idea for structure but the pdf structure for the actual
   * values. If overflow sheet use constant from PdfSpecification.
   */
  private PDDocument documentWithAddress;

  private PDDocument documentWithoutAddress;
  private StringBuilder originalStructure;
  public static final int SIGNATURE_X_PADDING = 60;
  public static final int SIGNATURE_Y_PADDING = 7;
  private static final String FK_7427 = "fk7427";
  private static final String FK_7426 = "fk7426";
  private static final String FK_3221 = "fk3221";
  private static final String FK_7810 = "fk7810";
  private static final String FK_7804 = "fk7804";
  private static final String FK_7472 = "fk7472";
  private static final String FK_7809 = "fk7809";

  private static final Map<String, String> TYPE_TO_VERSION =
      Map.of(
          FK_7427, "v1",
          FK_7426, "v1",
          FK_3221, "v1",
          FK_7810, "v1",
          FK_7804, "v2",
          FK_7472, "v1",
          FK_7809, "v1");

  /**
   * Enable locally to print the AcroForm widget rectangle for the signed-date field and suggested
   * {@code SignatureOverlayDetails} (same offsets as {@link CertificatePdfFillService} for template
   * PDFs: {@link CertificatePdfFillService#SIGNATURE_X_PADDING} and {@link
   * CertificatePdfFillService#SIGNATURE_Y_PADDING}).
   *
   * <p>Adjust {@code signedDateFieldId} per certificate type, run the test, then copy the printed
   * {@code signatureTextX} / {@code signatureTextY} / {@code signaturePageIndex} into {@code
   * CustomPdfSpecification} for that type.
   */
  @Disabled("Enable locally to print signed-date field metrics for CustomPdf overlay coordinates")
  @Test
  void shouldPrintSignedDateFieldRectangleAndSuggestedOverlayCoordinates() throws IOException {
    final var certificateType = FK_7809;
    final var signedDateFieldId = "form1[0].#subform[3].flt_datUnderskrift[0]";
    final var classloader = getClass().getClassLoader();
    final var inputStream =
        classloader.getResourceAsStream(
            String.format(
                "%s/pdf/%s_%s.pdf",
                certificateType, certificateType, TYPE_TO_VERSION.get(certificateType)));
    Assumptions.assumeTrue(
        inputStream != null,
        "PDF not on classpath; run from a configuration that includes app resources.");
    try (var document = Loader.loadPDF(inputStream.readAllBytes())) {
      final var report = formatSignedDateOverlayReport(document, signedDateFieldId);
      System.out.println(report);
    }
  }

  /** Run to generate structure for the first time and save in resources/pdf folder. */
  @Disabled
  @Test
  void shouldCreateStructureFileForPdf() {
    final var certificateType = FK_7809;
    final var classloader = getClass().getClassLoader();
    final var inputStream =
        classloader.getResourceAsStream(
            String.format(
                "%s/pdf/%s_%s.pdf",
                certificateType, certificateType, TYPE_TO_VERSION.get(certificateType)));

    try {
      final var document = Loader.loadPDF(inputStream.readAllBytes());
      final var structure = getPdfStructure(document);
      writeToFile(certificateType, structure);
      assertFalse(structure.isEmpty());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {FK_7427, FK_7426, FK_3221, FK_7810, FK_7804, FK_7472, FK_7809})
  void shouldHaveSameStructureAsOriginalDocument(String certificateType) {
    setup(certificateType);

    final var contentNewStructure = getPdfStructure();

    final var normalizedOriginalStructure =
        originalStructure.toString().replaceAll("\r\n", "\n").replaceAll("\\s+", " ").trim();
    final var normalizedExpectedText =
        contentNewStructure.toString().replaceAll("\r\n", "\n").replaceAll("\\s+", " ").trim();

    assertEquals(normalizedExpectedText, normalizedOriginalStructure);
  }

  @ParameterizedTest
  @ValueSource(strings = {FK_7427, FK_7426, FK_3221, FK_7810, FK_7804, FK_7472, FK_7809})
  void shouldHaveSameIdsForTemplateWithAndWithoutAddress(String certificateType) {
    setup(certificateType);

    final var idsForTemplateWithAddress = getFieldIds(documentWithAddress);
    final var idsForTemplateWithoutAddress = getFieldIds(documentWithoutAddress);

    final var errors = new ArrayList<String>();
    final var minSize =
        Math.min(idsForTemplateWithAddress.size(), idsForTemplateWithoutAddress.size());
    for (int i = 0; i < minSize; i++) {
      final var idWithAddress = idsForTemplateWithAddress.get(i);
      final var idWithoutAddress = idsForTemplateWithoutAddress.get(i);

      if (!idWithAddress.equals(idWithoutAddress)) {
        errors.add(
            String.format(
                "Mismatch at index %d: '%s' vs '%s'", i, idWithAddress, idWithoutAddress));
      }
    }
    if (idsForTemplateWithAddress.size() != idsForTemplateWithoutAddress.size()) {
      errors.add(
          String.format(
              "Different number of fields: with address = %d, without address = %d",
              idsForTemplateWithAddress.size(), idsForTemplateWithoutAddress.size()));
    }

    assertTrue(errors.isEmpty(), String.join("\n", errors));
  }

  private void setup(String certificateType) {
    final var classloader = getClass().getClassLoader();
    final var inputStream =
        classloader.getResourceAsStream(
            String.format(
                "%s/pdf/%s_%s.pdf",
                certificateType, certificateType, TYPE_TO_VERSION.get(certificateType)));
    final var inputStreamWithoutAddress =
        classloader.getResourceAsStream(
            String.format(
                "%s/pdf/%s_%s_no_address.pdf",
                certificateType, certificateType, TYPE_TO_VERSION.get(certificateType)));

    try {
      documentWithAddress = Loader.loadPDF(inputStream.readAllBytes());
      documentWithoutAddress = Loader.loadPDF(inputStreamWithoutAddress.readAllBytes());
      originalStructure =
          readFileFromResources(
              String.format("%s/pdf/%s_structure.txt", certificateType, certificateType));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private StringBuilder getPdfStructure() {
    return getPdfStructure(documentWithAddress);
  }

  private StringBuilder getPdfStructure(PDDocument document) {
    final var acroForm = document.getDocumentCatalog().getAcroForm();
    final var parentField = (PDNonTerminalField) acroForm.getFields().getFirst();
    final var content = new StringBuilder();
    var count = 0;

    content.append(
        "General mapping for pdf specification per question type follows pattern ElementValueBoolean maps to PdfConfigurationBoolean and ElementValueDateList maps to PdfConfigurationDateList etc.\n");
    content.append(
        "This part of the file contains ids and names and types of fields extracted from the pdf.\n");
    for (PDField page : parentField.getChildren()) {
      content.append(String.format("//Page index %s\n", count++));
      for (PDField field : ((PDNonTerminalField) page).getChildren()) {
        if (field.getAlternateFieldName() != null
            && field.getAlternateFieldName().contains("Fortsättningsblad")) {
          content.append("// This is the overflow page\n");
        }

        final var extraText = new StringBuilder();
        if (field instanceof PDRadioButton radioButtonField) {
          extraText.append(
              "For radio boolean assume first option is true and second option is false. Use the options as field ids. Options:\n");
          for (String option : radioButtonField.getExportValues()) {
            extraText.append(option).append("\n");
          }
        }

        content.append(
            String.format(
                "Field ID: %s\nName: %s\nField Type: %s\n%s\n",
                field.getFullyQualifiedName(),
                field.getAlternateFieldName(),
                field.getClass(),
                extraText));
      }
    }
    return content;
  }

  private static Integer pageIndexOfWidget(PDDocument document, PDAnnotationWidget widget)
      throws IOException {
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      final var page = document.getPage(i);
      if (page.getAnnotations() == null) {
        continue;
      }
      for (var annotation : page.getAnnotations()) {
        if (annotation == widget) {
          return i;
        }
      }
    }
    final var page = widget.getPage();
    if (page != null) {
      for (int i = 0; i < document.getNumberOfPages(); i++) {
        if (document.getPage(i).getCOSObject() == page.getCOSObject()) {
          return i;
        }
      }
    }
    return null;
  }

  static String formatSignedDateOverlayReport(PDDocument document, String signedDateFieldId)
      throws IOException {
    final var acroForm = document.getDocumentCatalog().getAcroForm();
    if (acroForm == null) {
      return "No AcroForm in document.";
    }
    final var field = acroForm.getField(signedDateFieldId);
    if (field == null) {
      return "No field named: " + signedDateFieldId;
    }
    if (!(field instanceof PDTerminalField terminalField)) {
      return "Field is not a terminal field: " + signedDateFieldId;
    }
    final var widgets = terminalField.getWidgets();
    if (widgets == null || widgets.isEmpty()) {
      return "Field has no widgets: " + signedDateFieldId;
    }
    final var rect = widgets.getFirst().getRectangle();
    final var pageIndex = pageIndexOfWidget(document, widgets.getFirst());
    final var suggestedX = rect.getUpperRightX() + SIGNATURE_X_PADDING;
    final var suggestedY = rect.getLowerLeftY() + SIGNATURE_Y_PADDING;
    final var sw = new StringWriter();
    try (var out = new PrintWriter(sw)) {
      out.println("Signed-date field: " + signedDateFieldId);
      out.printf(
          "Rectangle: lowerLeft=(%.4f, %.4f) upperRight=(%.4f, %.4f)%n",
          rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getUpperRightY());
      out.println("Page index (0-based): " + (pageIndex == null ? "unknown" : pageIndex));
      out.println(
          "Suggested SignatureOverlayDetails (CertificatePdfFillService padding "
              + SIGNATURE_X_PADDING
              + " / "
              + SIGNATURE_Y_PADDING
              + "):");
      out.printf("  signatureTextX = %.4ff;%n", suggestedX);
      out.printf("  signatureTextY = %.4ff;%n", suggestedY);
      out.printf(
          "  signaturePageIndex = %s;%n", pageIndex == null ? "/* resolve manually */" : pageIndex);
    }
    return sw.toString();
  }

  private List<String> getFieldIds(PDDocument document) {
    final var acroForm = document.getDocumentCatalog().getAcroForm();
    final var parentField = (PDNonTerminalField) acroForm.getFields().getFirst();
    final var ids = new ArrayList<String>();

    for (PDField page : parentField.getChildren()) {
      for (PDField field : ((PDNonTerminalField) page).getChildren()) {
        if (field instanceof PDRadioButton radioButtonField) {
          ids.addAll(radioButtonField.getExportValues());
        }
        ids.add(field.getFullyQualifiedName());
      }
    }
    return ids;
  }

  private void writeToFile(String certificateType, StringBuilder content) {
    final var fileName = String.format("%s_structure.txt", certificateType);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      writer.write(content.toString());
    } catch (IOException e) {
      throw new RuntimeException("Failed to write content to file", e);
    }
  }

  private StringBuilder readFileFromResources(String fileName) throws IOException {
    final var classloader = getClass().getClassLoader();
    final var inputStream = classloader.getResourceAsStream(fileName);
    final var reader = new BufferedReader(new InputStreamReader(inputStream));
    final var fileContent = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      fileContent.append(line).append("\n");
    }
    return fileContent;
  }
}
