package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804workshop;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import java.util.Arrays;
import org.hl7.fhir.r5.model.Questionnaire;
import org.springframework.stereotype.Component;

@Component
public class FHIRClient {

  FhirContext ctx = FhirContext.forR5();

  IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR5");

  /*
  43964
  81567
  898089
   */
  public Questionnaire getQuestionnaireForId(String id) {
    System.out.println("Fetching Questionnaire with ID: " + id);
    final var result =  client.read().resource(Questionnaire.class).withId(id).execute();
    System.out.println("###################################################");
    String string = ctx.newJsonParser().setPrettyPrint(true)
        .encodeResourceToString(result);
    System.out.println(string);
    System.out.println("###################################################");
    return result;
  }
}
