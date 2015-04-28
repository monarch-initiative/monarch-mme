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
//    val url = "http://tartini.crbs.ucsd.edu/analyze/phenotypes.json/?input_items=" + ids.mkString("+")
//
//    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
//
//    val holder: WSRequestHolder = WS.url(url)
//    val futureResults: Future[Seq[JsValue]] = holder.get().map {
//      response =>
//        val body = response.body
//        (Json.parse(body) \ "results").as[Seq[JsValue]]
//    }
//
//    val seqResults = Await.result(futureResults, 120 seconds)
//    
//    val mmeResults: List[MmeResult] = seqResults.map(r => {
//      val id = (r \ "j" \ "id").as[String]
//      val label = (r \"j" \ "label").as[String]
//      val features = {
//        (r \\ "b").map(x => {
//          ResponseFeature((x \ "id").as[String], (x \ "label").as[String])
//        })
//      }.toList
//      MmeResult(id, label, features)
//    }).toList
//    
////    val idAndLabel: Seq[(String, String)] = seqMatches.map(m => {
////      val ids = (m \\ "id").map(_.as[String])
////      val labels = (m \\ "label").map(_.as[String])
////      ids zip labels
////    }).flatten
////
////    val mmeResults = idAndLabel.map(_ match {
////      case (id, label) => MmeResult(id, label)
////    })
//
//    val mmeResponse = MmeResponse(queryId, mmeResults)
//
//    Json.stringify(Json.toJson(mmeResponse))
    ""
  }
}