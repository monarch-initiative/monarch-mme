package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json._
import akka.actor.Props
import play.api.libs.concurrent.Akka

object Application extends Controller {

  val concurrentMap = new scala.collection.concurrent.TrieMap[String, String]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def mmeMatch() = Action(parse.json) {
    implicit request =>
      val json = request.body
      val id = json \ "id"
      val oberservedFeatures = {
        val features = json \ "features"
        val idAndObservation = {
          val ids = (features \\ "id").map(_.as[String])
          val observations = (features \\ "observed").map(_.as[String])
          ids zip observations
        }
        idAndObservation.filter(_ match {
          case (id, observation) => observation == "yes"
        })
      }
      val onlyIds = oberservedFeatures.map(_._1).toSeq

      println(onlyIds)
      val queryId = java.util.UUID.randomUUID.toString
      concurrentMap.put(queryId, "")
      Akka.system.actorOf(Props[Worker]) ! MmeMessage(queryId, onlyIds, concurrentMap)

      Ok(Json.obj("queryId" -> queryId))
  }

  def matchResults() = Action(parse.json) {
    implicit request =>
      val json = request.body
      val queryId = (json \ "queryId").as[String]
      val data = concurrentMap.get(queryId)
      Ok(data.getOrElse(""))
  }

}
