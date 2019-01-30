package io.mikroservice.infrastructure

import akka.NotUsed
import akka.actor.{ActorSystem, Scheduler}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.pattern.CircuitBreaker
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.StrictLogging
import io.codeheroes.akka.http.lb.{EndpointEvent, LoadBalancer, LoadBalancerSettings}
import io.mikroservice.domain.SomeId
import io.mikroservice.domain.service.{ExtService, SomeResult}
import org.json4s.{DefaultFormats, native}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.control.NonFatal

/**
  * @author plutecki
  */

class RESTExtService(endpoints: Source[EndpointEvent, NotUsed])
                    (implicit system: ActorSystem, ec: ExecutionContext, mat: ActorMaterializer, scheduler: Scheduler, proxySettings: ConnectionPoolSettings)
  extends ExtService with StrictLogging {

  private val client = LoadBalancer.singleRequests(endpoints, LoadBalancerSettings.default)
  private val breaker = CircuitBreaker(system.scheduler, 3, 15 seconds, 15 seconds)
  private val serializer = native.Serialization
  private implicit val formats = DefaultFormats

  breaker
    .onOpen(logger.error("CircuitBreaker for RESTExtService opened."))
    .onHalfOpen(logger.warn("CircuitBreaker for RESTExtService half opened."))
    .onClose(logger.info("CircuitBreaker for RESTExtService closed."))

  override def find(id: SomeId): Future[Option[SomeResult]] = breaker.withCircuitBreaker(_find(id))

  private def _find(id: SomeId): Future[Option[SomeResult]] = {
    val request = HttpRequest(method = HttpMethods.GET, uri = s"/some/${id.value}")

    client
      .request(request)
      .flatMap(r => r.entity.toStrict(5 seconds).map(e => (r.status, e.getData().utf8String)))
      .map {
        case (OK, body) =>
          try {
            Some(serializer.read[SomeResult](body))
          } catch {
            case NonFatal(ex) =>
              throw new IllegalStateException(s"Cannot parse response $body for request $request", ex)
          }
        case (NotFound, _) => None
        case (other, body) =>
          throw new IllegalStateException(s"Unhandled status [$other] for request [$request] from bare service with response body [$body].")
      }
  }
}
