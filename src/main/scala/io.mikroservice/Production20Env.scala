package io.mikroservice

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import ch.qos.logback.classic.LoggerContext
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import io.codeheroes.akka.http.lb.{Endpoint, EndpointUp}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor

class Production20Env extends Env with StrictLogging {

  private val config = ConfigFactory.load("production2_0.conf")

  private implicit val tempSystem: ActorSystem = ActorSystem()
  private implicit val ec: ExecutionContextExecutor = tempSystem.dispatcher
  private implicit val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]


  private def endpointSource(host: String) = Source.single(EndpointUp(Endpoint(host, 8080)))

  private val services = ServicesConfig(endpointSource("ext-service"))

  override def toApplication: Application = new Application(config, services)

  override def bindHandler: () => Unit = () => ()

}
