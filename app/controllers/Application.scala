package controllers

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

object Application extends Controller {

  val concurrentMap = new scala.collection.concurrent.TrieMap[String, String]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def mmeMatch() = Action(BodyParsers.parse.json) {
    implicit request =>
      val matchQuery = request.body.validate[MatchQuery]

      matchQuery.fold(
        errors => {
          BadRequest(Json.obj("status" -> "Bad Request", "message" -> JsError.toFlatJson(errors)))
        },
        matchQueryObj => {
          val onlyIds = matchQueryObj.features.map(_.id)

          if (matchQueryObj.responseType == "inline") {
            Ok(MmeRequester.fetch("noQueryId", onlyIds))
          } else {
            val queryId = java.util.UUID.randomUUID.toString
            concurrentMap.put(queryId, "No data yet")
            Akka.system.actorOf(Props[MmeWorker]) ! MmeWorkerMessage(queryId, onlyIds, concurrentMap)
            Ok(Json.obj("queryId" -> queryId))
          }
        })
  }

  def matchResults() = Action(parse.json) {
    implicit request =>
      val json = request.body
      val queryId = (json \ "queryId").as[String]
      val data = concurrentMap.get(queryId)
      Ok(data.getOrElse("Invalid queryId"))
  }

}
