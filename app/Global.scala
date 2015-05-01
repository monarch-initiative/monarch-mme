import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._
import scala.concurrent.ExecutionContext.Implicits.global
import controllers.Version

object Global extends GlobalSettings {

  def ContentType(action: EssentialAction): EssentialAction = EssentialAction { request =>
    action(request).map(_.withHeaders("Content-Type" -> Version.version))
  }

  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    super.onRouteRequest(request).map { handler =>
      handler match {
        case a: EssentialAction => ContentType(a)
        case other              => other
      }
    }
  }
}