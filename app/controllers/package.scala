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

  implicit val typeInfoWrites = new Writes[TypeInfo] {
    def writes(typeInfo: TypeInfo) = Json.obj(
      "id" -> typeInfo.id,
      "label" -> typeInfo.label)
  }
  implicit val variantWrites = new Writes[Variant] {
    def writes(variant: Variant) = Json.obj(
      "assembly" -> variant.assembly,
      "referenceName" -> variant.referenceName,
      "start" -> variant.start,
      "end" -> variant.end,
      "referenceBases" -> variant.referenceBases,
      "alternateBases" -> variant.alternateBases)
  }
  implicit val geneWrites = new Writes[Gene] {
    def writes(gene: Gene) = Json.obj(
      "id" -> gene.id)
  }

  implicit val genomicFeatureWrites = new Writes[GenomicFeature] {
    def writes(genomicFeature: GenomicFeature) = Json.obj(
      "gene" -> genomicFeature.gene,
      "variant" -> genomicFeature.variant,
      "zygosity" -> genomicFeature.zygosity,
      "type" -> genomicFeature.typeInfo)
  }

  implicit val featureWrites = new Writes[Feature] {
    def writes(feature: Feature) = Json.obj(
      "id" -> feature.id,
      "observed" -> feature.observed,
      "ageOfOnset" -> feature.ageOfOnset)
  }

  implicit val disorderWrites = new Writes[Disorder] {
    def writes(disorder: Disorder) = Json.obj(
      "id" -> disorder.id)
  }

  implicit val contactWrites = new Writes[Contact] {
    def writes(contact: Contact) = Json.obj(
      "name" -> contact.name,
      "institution" -> contact.institution,
      "href" -> contact.href)
  }

  implicit val patientWrites = new Writes[Patient] {
    def writes(patient: Patient) = Json.obj(
      "id" -> patient.id,
      "label" -> patient.label,
      "contact" -> patient.contact,
      "species" -> patient.species,
      "sex" -> patient.sex,
      "ageOfOnset" -> patient.ageOfOnset,
      "inheritanceMode" -> patient.inheritanceMode,
      "disorders" -> patient.disorders,
      "features" -> patient.features,
      "genomicFeatures" -> patient.genomicFeatures)
  }

  implicit val scoreWrites = new Writes[PatientScore] {
    def writes(patientScore: PatientScore) = Json.obj(
      "patient" -> patientScore.patient)
  }

  implicit val resultWrites = new Writes[models.inandout.Result] {
    def writes(result: models.inandout.Result) = Json.obj(
      "score" -> result.score,
      "patient" -> result.patient)
  }

  implicit val mmeResponseWrites = new Writes[MmeResponse] {
    def writes(mmeResponse: MmeResponse) = Json.obj(
      "results" -> mmeResponse.results)
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
    (JsPath \ "features").read[Option[List[Feature]]] and
    (JsPath \ "genomicFeatures").read[Option[List[GenomicFeature]]])(Patient.apply _)

  implicit val matchQueryReads: Reads[MatchQuery] = (
    (JsPath \ "patient").read[Patient]).map(MatchQuery.apply _)
}