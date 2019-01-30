package io.mikroservice.core

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import io.mikroservice.api.DomainFormats
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration.{FiniteDuration, _}
import scala.language.postfixOps

trait TestHelpers extends FlatSpec with ScalatestRouteTest with Json4sSupport {

  protected implicit val formats: Formats = DefaultFormats ++ DomainFormats.all
  protected implicit val serialization: Serialization.type = Serialization
  protected implicit val requestTimeout: RouteTestTimeout = RouteTestTimeout(5 seconds)

  def responseAsJson(implicit timeout: FiniteDuration = 1 second) =
    Await.result(responseEntity.toStrict(timeout).map(_.data.utf8String).map(parse(_)), timeout)

  def responseAsList(implicit timeout: FiniteDuration = 1 second) = responseAs[List[String]]

  object PutRequest {
    def apply(uri: String, body: AnyRef) = HttpRequest(
      method = HttpMethods.PUT,
      uri = uri,
      entity = HttpEntity(ContentTypes.`application/json`, write(body))
    )
  }

  object GetRequest {
    def apply(uri: String) = HttpRequest(method = HttpMethods.GET, uri = uri)
  }

  object PostRequest {
    def apply(uri: String, body: AnyRef) = HttpRequest(
      method = HttpMethods.POST,
      uri = uri,
      entity = HttpEntity(ContentTypes.`application/json`, write(body))
    )
  }

}
