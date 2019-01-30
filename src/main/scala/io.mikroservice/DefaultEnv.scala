package io.mikroservice

import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import io.codeheroes.akka.http.lb.{Endpoint, EndpointUp}

/**
  * @author plutecki
  */
class DefaultEnv extends Env {

  private val endpointSource = Source.single(EndpointUp(Endpoint("localhost", 8080)))
  private val config = ConfigFactory.load("default.conf")
  private val services = ServicesConfig(endpointSource)

  override def toApplication: Application = new Application(config, services)

  override def bindHandler: () => Unit = () => ()

}
