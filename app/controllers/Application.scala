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
    Ok(views.html.index(Version.version))
  }

  def mmeMatch() = Action(BodyParsers.parse.json) {
    implicit request =>
      val compatible = Version.checkAcceptedVersion(request.acceptedTypes.map(_.toString).toList)

      if (compatible) {

        // TODO check X-Auth-Token
        val matchQuery = request.body.validate[MatchQuery]

        matchQuery.fold(
          errors => {
            BadRequest(Json.obj("status" -> "Bad Request", "message" -> JsError.toFlatJson(errors)))
          },
          matchQueryObj => {
            if (matchQueryObj.patient.features.isDefined && matchQueryObj.patient.genomicFeatures.isDefined) {
              val featuresOpt = matchQueryObj.patient.features
              if (featuresOpt.isDefined) {
                val onlyIds = featuresOpt.get.map(_.id)
                Ok(MmeRequester.fetch("noQueryId", onlyIds)).withHeaders("Content-Type" -> Version.version)
              } else {
                Ok("").withHeaders("Content-Type" -> Version.version) // TODO
              }
            }else{
              BadRequest("genomicFeatures and features are both empty.").withHeaders("Content-Type" -> Version.version)
            }
          })

      } else {
        NotAcceptable(s"Can't find version in header or incompatible version provided").withHeaders("Content-Type" -> Version.version)
      }
  }

}
