package io.mikroservice.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.settings.RoutingSettings
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import io.mikroservice.domain.{BareService, SomeId, ValidationException}
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.ExecutionContext

/**
  * @author plutecki
  */
class BareRoutes(bearService: BareService)(implicit ec: ExecutionContext, system: ActorSystem) extends Json4sSupport {

  private implicit val formats: Formats = DefaultFormats ++ DomainFormats.all
  private implicit val serialization: Serialization.type = Serialization
  private val routingSettings = RoutingSettings(system.settings.config)
  private val exceptionHandler = ExceptionHandler {
    case ValidationException(errors) ⇒ ctx ⇒ {
      ctx.log.warning("Error during processing of request {} with errors {}.", ctx.request, errors)
      ctx.complete(NotAcceptable, errors)
    }
  }.seal(routingSettings)

  val routing: Route =
    handleExceptions(exceptionHandler) {
      path("create") {
        post {
          entity(as[Requests.CreateRequest]) { requests =>
            val result = bearService.initialize(SomeId(requests.name))
            onSuccess(result)(r => complete(Accepted, r))
          }
        }
      } ~ path("refund") {
        post {
          complete(NotImplemented)
        }
      } ~ (path("status") & get) (complete(OK))
    }
}
