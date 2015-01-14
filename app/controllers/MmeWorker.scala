package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.json._
import akka.actor.Actor

class MmeWorker extends Actor {

  def receive = {
    case MmeWorkerMessage(queryId, ids, map) => {
      map.replace(queryId, MmeRequester.fetch(queryId, ids))
    }
    case _ => println("Unknown message received")
  }

}

case class MmeWorkerMessage(queryId: String, ids: Seq[String], concurrentMap: scala.collection.concurrent.Map[String, String])