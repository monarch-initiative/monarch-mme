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
      println(request.acceptedTypes)
      println(request.body)
      // TODO check accepted types
      val matchQuery = request.body.validate[MatchQuery]

      matchQuery.fold(
        errors => {
          BadRequest(Json.obj("status" -> "Bad Request", "message" -> JsError.toFlatJson(errors)))
        },
        matchQueryObj => {
          // TODO check feature or genomicFeatures
          val onlyIds = Seq.empty
          Ok(MmeRequester.fetch("noQueryId", onlyIds))
        })
  }

}
