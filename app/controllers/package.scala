import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json._
import akka.actor.Props
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Akka
import models.inandout._

package object controllers {

  implicit val ResponseFeatureWrites = new Writes[ResponseFeature] {
    def writes(responseFeature: ResponseFeature) = Json.obj(
      "id" -> responseFeature.id,
      "label" -> responseFeature.label)
  }

  implicit val mmeResultWrites = new Writes[MmeResult] {
    def writes(mmeResult: MmeResult) = Json.obj(
      "id" -> mmeResult.id,
      "label" -> mmeResult.label,
      "features" -> mmeResult.features)
  }

  implicit val mmeResponseWrites = new Writes[MmeResponse] {
    def writes(mmeResponse: MmeResponse) = Json.obj(
      "queryId" -> mmeResponse.queryId,
      "results" -> mmeResponse.mmeResults)
  }

  implicit val typeInfoReads: Reads[TypeInfo] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "label").read[Option[String]])(TypeInfo.apply _)

  implicit val variantReads: Reads[Variant] = (
    (JsPath \ "assembly").read[String] and
    (JsPath \ "referenceName").read[String] and
    (JsPath \ "start").read[Int] and
    (JsPath \ "end").read[Option[Int]] and
    (JsPath \ "referenceBases").read[Option[String]] and
    (JsPath \ "alternateBases").read[Option[String]])(Variant.apply _)

  implicit val geneReads: Reads[Gene] = (
    (JsPath \ "id").read[String]).map(Gene.apply _)

  implicit val genomicFeatureReads: Reads[GenomicFeature] = (
    (JsPath \ "gene").read[Gene] and
    (JsPath \ "variant").read[Option[Variant]] and
    (JsPath \ "zygosity").read[Option[Int]] and
    (JsPath \ "type").read[TypeInfo])(GenomicFeature.apply _)

  implicit val featureReads: Reads[Feature] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "observed").read[String] and
    (JsPath \ "ageOfOnset").read[String])(Feature.apply _)

  implicit val disorderReads: Reads[Disorder] = (
    (JsPath \ "id").read[String]).map(Disorder.apply _)

  implicit val contactReads: Reads[Contact] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "institution").read[Option[String]] and
    (JsPath \ "href").read[String])(Contact.apply _)

  implicit val patientReads: Reads[Patient] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "label").read[Option[String]] and
    (JsPath \ "contact").read[Contact] and
    (JsPath \ "species").read[Option[String]] and
    (JsPath \ "sex").read[Option[String]] and
    (JsPath \ "ageOfOnset").read[Option[String]] and
    (JsPath \ "inheritanceMode").read[Option[String]] and
    (JsPath \ "disorders").read[Option[List[Disorder]]] and
    (JsPath \ "features").read[List[Feature]] and
    (JsPath \ "genomicFeatures").read[List[GenomicFeature]])(Patient.apply _)

  implicit val matchQueryReads: Reads[MatchQuery] = (
    (JsPath \ "patient").read[Patient]).map(MatchQuery.apply _)
}