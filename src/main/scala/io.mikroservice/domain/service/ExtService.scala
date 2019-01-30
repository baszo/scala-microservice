package io.mikroservice.domain.service

import io.mikroservice.domain.SomeId

import scala.concurrent.Future

/**
  * @author plutecki
  */

final case class SomeResult(data: String)

trait ExtService {

  def find(id: SomeId): Future[Option[SomeResult]]
}
