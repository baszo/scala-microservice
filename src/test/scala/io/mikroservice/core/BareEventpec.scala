package io.mikroservice.core

import akka.http.scaladsl.server
import akka.stream.scaladsl.Source
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import io.codeheroes.akka.http.lb.{Endpoint, EndpointUp}
import io.mikroservice.{Application, ServicesConfig}
import io.mikroservice.mock.SomeServiceMock
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers}

import scala.util.Random

class BareEventpec extends TestHelpers with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {


  private val someServiceMockPort = 16000 + Random.nextInt(1000)

  private val appInfoServiceMock = new SomeServiceMock("localhost", someServiceMockPort)
  protected var endpoint: server.Route = _

  override protected def beforeAll(): Unit = {

    val config = ConfigFactory.load("default.conf")
      .withValue("akka.persistence.journal.leveldb.dir", ConfigValueFactory.fromAnyRef(s"/tmp/leveldb-${System.currentTimeMillis()}"))

    appInfoServiceMock.start()


    val servicesConfig = ServicesConfig(endpointSource(someServiceMockPort))

    endpoint = new Application(config, servicesConfig).bareRoutes
  }

  private def endpointSource(port: Int) = Source.single(EndpointUp(Endpoint("localhost", port)))

}
