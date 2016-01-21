package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json._
import akka.actor.Actor
import models.inandout._

object MmeRequester {
  def fetch(queryId: String, ids: Seq[String]): String = {
    val url = "http://monarch1-1.cslu.ohsu.edu/analyze/phenotypes.json?input_items=" + ids.mkString("+")

    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

    val holder: WSRequestHolder = WS.url(url)
    val futureResults: Future[Seq[JsValue]] = holder.get().map {
      response =>
        val body = response.body
        (Json.parse(body) \ "results").as[Seq[JsValue]]
    }

    val seqResults = Await.result(futureResults, 120 seconds)

    val results: List[models.inandout.Result] = seqResults.map(r => {

      val patientId = (r \ "j" \ "id").as[String]
      val patientLabel = (r \ "j" \ "label").as[String]
      val patientScore = (r \ "combinedScore").as[Double]

      val features = (r \ "matches").as[Seq[JsValue]].map(m => {
        Feature((m \ "b" \ "id").as[String], Some("yes"), None, Some((m \ "b" \ "label").as[String]))
      }).toList

      val contact = Contact("Monarch Initiative", None, "mailto:info@monarchinitiative.org")
      val patient = Patient(patientId, Some(patientLabel), contact = contact, features = Some(features))

      models.inandout.Result(PatientScore(patientScore), patient)
    }).toList

    val mmeResponse = MatchResult(results)

    Json.stringify(Json.toJson(mmeResponse))
  }
}