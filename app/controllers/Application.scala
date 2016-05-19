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
import com.fasterxml.jackson.core.JsonParseException

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(Version.version))
  }

  def mmeMatch() = Action.async(BodyParsers.parse.raw) {
    implicit request =>

      implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext

      val compatible = Version.checkAcceptedVersion(request.acceptedTypes.map(_.toString).toList)

      val response = if (compatible) {
        try {
          val rawBody = new String(request.body.asBytes(Integer.MAX_VALUE).get)

          val bodyJson = Json.parse(rawBody)
          val matchQuery = bodyJson.validate[MatchQuery]

          matchQuery.fold(
            errors => {
              Future(BadRequest(Json.obj("status" -> "Bad Request", "message" -> JsError.toFlatJson(errors))))
            },
            matchQueryObj => {
              if (matchQueryObj.patient.features.isDefined || matchQueryObj.patient.genomicFeatures.isDefined) {
                val featuresOpt = matchQueryObj.patient.features
                if (featuresOpt.isDefined) {
                  if (!featuresOpt.get.isEmpty) {
                    val onlyIds = featuresOpt.get.map(_.id)
                    MmeRequester.fetch("noQueryId", onlyIds).map(Ok(_))
                  } else {
                    Future(BadRequest("features can't be empty."))
                  }
                } else {
                  Future(NotImplemented("genomicFeatures not implemented on this server."))
                }
              } else {
                Future(BadRequest("genomicFeatures and features are both undefined."))
              }
            })
        } catch {
          case e: JsonParseException => Future(BadRequest(s"Not a valid JSON."))
        }

      } else {
        Future(NotAcceptable(s"Can't find version in header or incompatible version provided."))
      }
      response.map(_.withHeaders("Content-Type" -> Version.version))
  }

}
