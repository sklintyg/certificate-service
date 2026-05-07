package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804workshop;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import java.io.IOException;
import java.net.URISyntaxException;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;

public class Main {

  static void main() throws URISyntaxException, IOException {
    FhirContext ctx = FhirContext.forR5();

    IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR5");

    // 43964
    // 81567
    // 898089
    Questionnaire questionnaire = client.read().resource(Questionnaire.class).withId("898089")
        .execute();

    QuestionnaireResponse questionnaireResponse = client.read()
        .resource(QuestionnaireResponse.class).withId("81661").execute();

    // MethodOutcome response = client.create().resource(Files.readString(
//        Path.of(Main.class.getResource("/fk7804.json").getPath()))).execute();

    // QuestionnaireItemComponent questionnaireItemComponent = questionnaire.getItem().get(0);
    // System.out.println(questionnaireItemComponent.getText());
    // System.out.println(questionnaireItemComponent.getType());

    questionnaireResponse.getItem().forEach(item -> {
      System.out.println(item.getAnswer().getFirst().getValue());
    });

    String string = ctx.newJsonParser().setPrettyPrint(true)
        .encodeResourceToString(questionnaire);
    System.out.println(string);
  }
}
