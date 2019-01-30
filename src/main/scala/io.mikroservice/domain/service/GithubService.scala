package io.mikroservice.domain.service

import akka.http.scaladsl.model.HttpResponse
import io.mikroservice.infrastructure.v3.{GithubRepository}

import scala.concurrent.Future

/**
  * @author plutecki
  */
trait GithubService {


  def getUserRepos(user: String, `type`: Option[String], sort: Option[String], direction: Option[String]): Future[Either[Seq[GithubRepository], HttpResponse]]
}
