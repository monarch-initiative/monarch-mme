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

  implicit val mmeResultWrites = new Writes[MmeResult] {
    def writes(mmeResult: MmeResult) = Json.obj(
      "id" -> mmeResult.id,
      "label" -> mmeResult.label)
  }

  implicit val mmeResponseWrites = new Writes[MmeResponse] {
    def writes(mmeResponse: MmeResponse) = Json.obj(
      "queryId" -> mmeResponse.queryId,
      "results" -> mmeResponse.mmeResults)
  }
  
  implicit val featureReads: Reads[Feature] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "observed").read[String])(Feature.apply _)

  implicit val matchQueryReads: Reads[MatchQuery] = (
    (JsPath \ "responseType").read[String] and
    (JsPath \ "features").read[List[Feature]])(MatchQuery.apply _)
}