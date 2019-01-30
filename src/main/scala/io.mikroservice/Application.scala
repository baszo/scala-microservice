package io.mikroservice

import java.net.{InetSocketAddress, URI}

import akka.NotUsed
import akka.actor.{ActorSystem, Scheduler}
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.http.scaladsl.{ClientTransport, Http}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import io.codeheroes.akka.http.lb.EndpointEvent
import io.mikroservice.api.BareRoutes
import io.mikroservice.domain.BareService
import io.mikroservice.infrastructure.RESTExtService

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/**
  * @author plutecki
  */

case class ServicesConfig(
                           extService: Source[EndpointEvent, NotUsed]
                         )

class Application(config: Config, servicesConfig: ServicesConfig) extends StrictLogging {

  private implicit val system: ActorSystem = ActorSystem("ServiceActorSystem", config)
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  private implicit val scheduler: Scheduler = system.scheduler
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val apiBindHost = config.getString("application.bind-host")
  private val apiBindPort = config.getInt("application.bind-port")

  private implicit var proxySettings: ConnectionPoolSettings = ConnectionPoolSettings(system)
  if (config.hasPath("application.outgoing-proxy")) {
      val uri = URI.create("my://"+config.getString("application.outgoing-proxy"))
      logger.info(s"Using proxy ${uri.getHost}:${uri.getPort} for my service")
      proxySettings = ConnectionPoolSettings(system).withTransport(ClientTransport.httpsProxy(InetSocketAddress.createUnresolved(uri.getHost, uri.getPort)))
  } else logger.info(s"Not using proxy for my service")

  private val extService = new RESTExtService(servicesConfig.extService)


  private val bareService = new BareService(extService)

  val bareRoutes = new BareRoutes(bareService).routing

  def bind(callback: () => Unit) =
    Http().bindAndHandle(bareRoutes, apiBindHost, apiBindPort).onComplete {
      case Success(_) =>
        logger.info(s"API started at $apiBindHost:$apiBindPort")
        callback()
      case Failure(ex) => logger.error(s"Cannot bind API to $apiBindHost:$apiBindPort", ex)
    }

}
