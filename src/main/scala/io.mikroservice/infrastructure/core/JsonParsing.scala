package io.mikroservice.infrastructure.core

import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

/**
  * @author plutecki
  */
trait JsonParsing {

  implicit val formats = DefaultFormats

  def fromJson[T: Manifest](response: HttpResponse)(implicit mat: ActorMaterializer, ec: ExecutionContext): Future[T] =
    response.entity.toStrict(5 seconds)
      .map(_.getData().decodeString("UTF-8"))
      .map(body => try {
        read[T](body)
      } catch {
        case ex: Throwable => throw new IllegalStateException(s"Cannot parse response $body", ex)
      })

}
