package io.mikroservice.domain

import com.typesafe.scalalogging.StrictLogging
import io.mikroservice.domain.service.{ExtService, GithubService, SomeResult}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author plutecki
  */


class BareService(extService: ExtService,githubService: GithubService)(implicit ec: ExecutionContext) extends StrictLogging {


  def initialize(someId: SomeId): Future[Option[SomeResult]] = {
    extService.find(someId)
  }

  def getUserRepositories(user: String, `type`: Option[String] = None, sort: Option[String] = None, direction: Option[String] = None) = {
    githubService.getUserRepos(user,`type`,sort,direction)
  }

}

