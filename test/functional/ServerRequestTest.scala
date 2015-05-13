package functional

import play.api._
import play.api.mvc._
import play.api.test.PlaySpecification
import play.api.test.FakeRequest
import play.api.libs.json.Json
import play.api.libs.ws._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import controllers.Application
import models.inandout._
import controllers.Version
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ServerRequestTest extends PlaySpecification with Results {
  "match endpoint" should {
    "refuse requests without versions" in {

      val request = FakeRequest(POST, "/").withJsonBody(Json.parse("""{ "fake": "fake" }"""))

      val result = call(Application.mmeMatch, request)

      status(result) mustEqual 406 // NotAcceptable
      contentAsString(result) mustEqual "Can't find version in header or incompatible version provided"
    }

    "refuse requests without features and genomicFeatures defined" in {
      val contact = Contact("contact", None, "href")
      val patient = Patient("patientId", contact = contact)
      val matchQuery = MatchQuery(patient)

      val request = FakeRequest(POST, "/")
        .withHeaders("Accept" -> Version.version)
        .withJsonBody(Json.parse("""
          {"patient" : {
            "contact": {
              "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
              "institution": "Children's Hospital of Eastern Ontario",
              "name": "Lijia Huang"
            },
            "id": "P0000079",
            "label": "178_M43377",
            "test": true
          }
          }
        """))

      val result = call(Application.mmeMatch, request)

      status(result) mustEqual 400 // BadRequest
      contentAsString(result) mustEqual "genomicFeatures and features are both undefined."
    }

    "refuse requests without features defined" in {
      val contact = Contact("contact", None, "href")
      val patient = Patient("patientId", contact = contact)
      val matchQuery = MatchQuery(patient)

      val request = FakeRequest(POST, "/")
        .withHeaders("Accept" -> Version.version)
        .withJsonBody(Json.parse("""
          {"patient" : {
            "contact": {
              "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
              "institution": "Children's Hospital of Eastern Ontario",
              "name": "Lijia Huang"
            },
            "id": "P0000079",
            "label": "178_M43377",
            "genomicFeatures":[],
            "test": true
          }
          }
        """))

      val result = call(Application.mmeMatch, request)

      status(result) mustEqual 501 // NotImplemented
      contentAsString(result) mustEqual "genomicFeatures not implemented on this server."
    }

    "refuse requests with empty features" in {
      val contact = Contact("contact", None, "href")
      val patient = Patient("patientId", contact = contact)
      val matchQuery = MatchQuery(patient)

      val request = FakeRequest(POST, "/")
        .withHeaders("Accept" -> Version.version)
        .withJsonBody(Json.parse("""
          {"patient" : {
            "contact": {
              "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
              "institution": "Children's Hospital of Eastern Ontario",
              "name": "Lijia Huang"
            },
            "id": "P0000079",
            "label": "178_M43377",
            "genomicFeatures":[],
            "features":[],
            "test": true
          }
          }
        """))

      val result = call(Application.mmeMatch, request)
      status(result) mustEqual 400 // BadRequest
      contentAsString(result) mustEqual "features can't be empty."
    }

  }

}