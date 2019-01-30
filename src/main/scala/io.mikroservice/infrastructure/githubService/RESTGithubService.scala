package io.mikroservice.infrastructure.githubService

import akka.actor.{ActorSystem, Scheduler}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.pattern.CircuitBreaker
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import io.mikroservice.domain.service.GithubService
import io.mikroservice.infrastructure.core.JsonParsing
import io.mikroservice.infrastructure.v3.{GetUserRepositories, GithubRepository}
import org.json4s.native.Serialization

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

/**
  * @author plutecki
  */

object GithubService {

  case class GetUserRepositories(userName: String)

}

class RESTGithubService (implicit system: ActorSystem, ec: ExecutionContext, mat: ActorMaterializer, scheduler: Scheduler, proxySettings: ConnectionPoolSettings)
  extends GithubService with StrictLogging with JsonParsing {

  private val client = Http()
  private val serialization = Serialization

  private val breaker = new CircuitBreaker(scheduler, maxFailures = 3, callTimeout = 60 seconds, resetTimeout = 10 seconds)
    .onOpen(logger.error("CircuitBreaker for RESTGithubServiceTransactionsService opened."))
    .onHalfOpen(logger.warn("CircuitBreaker for RESTGithubServiceTransactionsService half opened."))
    .onClose(logger.info("CircuitBreaker for RESTGithubServiceTransactionsService closed."))


  override def getUserRepos(user: String, `type`: Option[String], sort: Option[String], direction: Option[String]): Future[Either[Seq[GithubRepository], HttpResponse]] =
    breaker.withCircuitBreaker(_getUserRepos(GetUserRepositories(user,`type`,sort,direction)))

  private def _getUserRepos(get: GetUserRepositories): Future[Either[Seq[GithubRepository], HttpResponse]] = {

    val url = Uri.from(scheme = "https",host = "api.github.com",path = get.getUrlPath,queryString = get.getQueryParams)


    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = url,
    )

    client
      .singleRequest(request, settings = proxySettings)
      .map(response => (response.status, response))
      .flatMap {
        case (OK, entity) =>
          fromJson[Seq[GithubRepository]](entity).map(Left(_))
        case (Created, entity) =>
          fromJson[Seq[GithubRepository]](entity).map(Left(_))
        case (other, entity) =>
          throw new IllegalStateException(s"Unhandled status $other for request $request with response $entity")
      }

  }
}