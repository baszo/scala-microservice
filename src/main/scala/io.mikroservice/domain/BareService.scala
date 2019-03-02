package io.mikroservice.domain

import akka.http.scaladsl.model.HttpResponse
import com.typesafe.scalalogging.StrictLogging
import io.mikroservice.domain.service.{ExtService, GithubService, SomeResult}
import io.mikroservice.infrastructure.v3.{GetSingleUser, GithubUser}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author plutecki
  */


class BareService(extService: ExtService, githubService: GithubService)(implicit ec: ExecutionContext) extends StrictLogging {


  def initialize(someId: SomeId): Future[Option[SomeResult]] = {
    extService.find(someId)
  }

  def getUserRepositories(user: String, `type`: Option[String] = None, sort: Option[String] = None, direction: Option[String] = None) = {
    githubService.getUserRepos(user, `type`, sort, direction)
  }

  def getSingleUser(username: String): Future[Either[GithubUser, HttpResponse]] = {
    githubService.get[GithubUser](GetSingleUser(username))
  }

}

