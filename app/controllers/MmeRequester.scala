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
    val url = "http://tartini.crbs.ucsd.edu/analyze/phenotypes.json/?input_items=" + ids.mkString("+")

    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

    val holder: WSRequestHolder = WS.url(url)
    val futureResults: Future[Seq[JsValue]] = holder.get().map {
      response =>
        val body = response.body
        (Json.parse(body) \ "results").as[Seq[JsValue]]
    }

    val seqResults = Await.result(futureResults, 120 seconds)

    val features: List[Feature] = seqResults.map(r => {

//      val id = (r \ "j" \ "id").as[String]
//      val label = (r \ "j" \ "label").as[String] 
      
        (r \\ "b").map(x => {
          Feature((x \ "id").as[String], Some("yes"), Some((x \ "label").as[String]))
        
      }).toList
    }).toList.flatten

    //    val idAndLabel: Seq[(String, String)] = seqMatches.map(m => {
    //      val ids = (m \\ "id").map(_.as[String])
    //      val labels = (m \\ "label").map(_.as[String])
    //      ids zip labels
    //    }).flatten
    //
    //    val mmeResults = idAndLabel.map(_ match {
    //      case (id, label) => MmeResult(id, label)
    //    })

    val contact = Contact("batman", None, "http://batman.org")
    val patient = Patient("Superman007", contact = contact, features = Some(features))
    val patientScore = PatientScore(5)
    val result = models.inandout.Result(patientScore, patient)

    val mmeResponse = MatchResult(List(result))

    Json.stringify(Json.toJson(mmeResponse))
  }
}