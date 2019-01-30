package io.mikroservice.mock

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import io.mikroservice.domain.service.SomeResult
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class SomeServiceMock(host: String, port: Int) extends Json4sSupport {

  private implicit val serialization: Serialization.type = Serialization
  private implicit val formats: DefaultFormats.type = DefaultFormats
  private implicit val system: ActorSystem = ActorSystem()
  private implicit val mat: ActorMaterializer = ActorMaterializer()

  val someResponse = SomeResult("324971")

  private val routes =
    (path("some" / Segment) & get) {
      case _ => complete(OK, someResponse)
    }


  def start(): Unit = Await.result(Http().bindAndHandle(routes, host, port), 5 seconds)

}
