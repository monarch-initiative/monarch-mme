import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import akka.actor.Props
import play.api.libs.concurrent.Akka
import models.inandout._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

package object controllers {

  implicit val typeInfoWrites: Writes[TypeInfo] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "label").writeNullable[String])(unlift(TypeInfo.unapply))

  implicit val variantWrites: Writes[Variant] = (
    (JsPath \ "assembly").write[String] and
    (JsPath \ "referenceName").write[String] and
    (JsPath \ "start").write[Int] and
    (JsPath \ "end").writeNullable[Int] and
    (JsPath \ "referenceBases").writeNullable[String] and
    (JsPath \ "alternateBases").writeNullable[String])(unlift(Variant.unapply))

  implicit val geneWrites = new Writes[Gene] {
    def writes(gene: Gene) = Json.obj(
      "id" -> gene.id)
  }

  implicit val genomicFeatureWrites: Writes[GenomicFeature] = (
    (JsPath \ "gene").write[Gene] and
    (JsPath \ "variant").writeNullable[Variant] and
    (JsPath \ "zygosity").writeNullable[Int] and
    (JsPath \ "type").write[TypeInfo])(unlift(GenomicFeature.unapply))

  implicit val featureWrites: Writes[Feature] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "observed").writeNullable[String] and
    (JsPath \ "ageOfOnset").writeNullable[String])(unlift(Feature.unapply))

  implicit val disorderWrites = new Writes[Disorder] {
    def writes(disorder: Disorder) = Json.obj(
      "id" -> disorder.id)
  }

  implicit val contactWrites: Writes[Contact] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "institution").writeNullable[String] and
    (JsPath \ "href").write[String])(unlift(Contact.unapply))

  implicit val patientWrites: Writes[Patient] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "label").writeNullable[String] and
    (JsPath \ "contact").write[Contact] and
    (JsPath \ "species").writeNullable[String] and
    (JsPath \ "sex").writeNullable[String] and
    (JsPath \ "ageOfOnset").writeNullable[String] and
    (JsPath \ "inheritanceMode").writeNullable[String] and
    (JsPath \ "disorders").writeNullable[List[Disorder]] and
    (JsPath \ "features").writeNullable[List[Feature]] and
    (JsPath \ "genomicFeatures").writeNullable[List[GenomicFeature]])(unlift(Patient.unapply))

  implicit val scoreWrites = new Writes[PatientScore] {
    def writes(patientScore: PatientScore) = Json.obj(
      "patient" -> patientScore.patient)
  }

  implicit val resultWrites = new Writes[models.inandout.Result] {
    def writes(result: models.inandout.Result) = Json.obj(
      "score" -> result.score,
      "patient" -> result.patient)
  }

  implicit val mmeResponseWrites = new Writes[MatchResult] {
    def writes(mmeResponse: MatchResult) = Json.obj(
      "results" -> mmeResponse.results)
  }

  implicit val typeInfoReads: Reads[TypeInfo] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "label").readNullable[String])(TypeInfo.apply _)

  implicit val variantReads: Reads[Variant] = (
    (JsPath \ "assembly").read[String] and
    (JsPath \ "referenceName").read[String] and
    (JsPath \ "start").read[Int] and
    (JsPath \ "end").readNullable[Int] and
    (JsPath \ "referenceBases").readNullable[String] and
    (JsPath \ "alternateBases").readNullable[String])(Variant.apply _)

  implicit val geneReads: Reads[Gene] = (
    (JsPath \ "id").read[String]).map(Gene.apply _)

  implicit val genomicFeatureReads: Reads[GenomicFeature] = (
    (JsPath \ "gene").read[Gene] and
    (JsPath \ "variant").readNullable[Variant] and
    (JsPath \ "zygosity").readNullable[Int] and
    (JsPath \ "type").read[TypeInfo])(GenomicFeature.apply _)

  implicit val featureReads: Reads[Feature] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "observed").readNullable[String] and
    (JsPath \ "ageOfOnset").readNullable[String])(Feature.apply _)

  implicit val disorderReads: Reads[Disorder] = (
    (JsPath \ "id").read[String]).map(Disorder.apply _)

  implicit val contactReads: Reads[Contact] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "institution").readNullable[String] and
    (JsPath \ "href").read[String])(Contact.apply _)

  implicit val patientReads: Reads[Patient] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "label").readNullable[String] and
    (JsPath \ "contact").read[Contact] and
    (JsPath \ "species").readNullable[String] and
    (JsPath \ "sex").readNullable[String] and
    (JsPath \ "ageOfOnset").readNullable[String] and
    (JsPath \ "inheritanceMode").readNullable[String] and
    (JsPath \ "disorders").readNullable[List[Disorder]] and
    (JsPath \ "features").readNullable[List[Feature]] and
    (JsPath \ "genomicFeatures").readNullable[List[GenomicFeature]])(Patient.apply _)

  implicit val matchQueryReads: Reads[MatchQuery] = (
    (JsPath \ "patient").read[Patient]).map(MatchQuery.apply _)
}