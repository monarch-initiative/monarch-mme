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

  def index = Action {
    Ok(views.html.index())
  }

  def mmeMatch() = Action(BodyParsers.parse.json) {
    implicit request =>
      println(request.acceptedTypes.map(_.toString))
      println(request.body)
      // TODO check Accept
      // TODO check X-Auth-Token
      val matchQuery = request.body.validate[MatchQuery]

      matchQuery.fold(
        errors => {
          BadRequest(Json.obj("status" -> "Bad Request", "message" -> JsError.toFlatJson(errors)))
        },
        matchQueryObj => {
          // TODO check feature or genomicFeatures
          val featuresOpt = matchQueryObj.patient.features
          if (featuresOpt.isDefined) {
            val onlyIds = featuresOpt.get.map(_.id)
            Ok(MmeRequester.fetch("noQueryId", onlyIds))
          } else {
            Ok("") // TODO
          }
        })
  }

}
