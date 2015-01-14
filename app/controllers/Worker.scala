package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json._
import akka.actor.Actor

class Worker extends Actor {

  def receive = {
    case MmeMessage(queryId, ids, map) => {
      map.replace(queryId, fetch(ids))
    }
    case _ => println("Unknown message received")
  }

  def fetch(ids: Seq[String]): String = {
    val url = "http://tartini.crbs.ucsd.edu/analyze/phenotypes.json/?input_items=" + ids.mkString("+")

    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

    val holder: WSRequestHolder = WS.url(url)
    val futureMatches: Future[Seq[JsValue]] = holder.get().map {
      response =>
        val body = response.body
        (Json.parse(body) \\ "j")
    }

    val seqMatches = Await.result(futureMatches, 120 seconds)
    val idAndLabel: Seq[(String, String)] = seqMatches.map(m => {
      val ids = (m \\ "id").map(_.as[String])
      val labels = (m \\ "label").map(_.as[String])
      ids zip labels
    }).flatten

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

    val mmeResults = idAndLabel.map(_ match {
      case (id, label) => MmeResult(id, label)
    })

    val mmeResponse = MmeResponse("FakeQueryId", mmeResults)

    Json.stringify(Json.toJson(mmeResponse))
  }

}

case class MmeMessage(queryId: String, ids: Seq[String], concurrentMap: scala.collection.concurrent.Map[String, String])

case class MmeResponse(queryId: String, mmeResults: Seq[MmeResult])
case class MmeResult(id: String, label: String)