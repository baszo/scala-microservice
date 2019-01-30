package io.mikroservice.domain

import com.typesafe.scalalogging.StrictLogging
import io.mikroservice.domain.service.{ExtService, SomeResult}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author plutecki
  */


class BareService(extService: ExtService)(implicit ec: ExecutionContext) extends StrictLogging {


  def initialize(someId: SomeId): Future[Option[SomeResult]] = {
    extService.find(someId)
  }

}

