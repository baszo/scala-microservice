package io.mikroservice.domain.service

import akka.http.scaladsl.model.HttpResponse
import io.mikroservice.infrastructure.v3.{GithubRepository, GithubRequest}

import scala.concurrent.Future

/**
  * @author plutecki
  */
trait GithubService {


  def getUserRepos(user: String, `type`: Option[String], sort: Option[String], direction: Option[String]): Future[Either[Seq[GithubRepository], HttpResponse]]

  def get[T: Manifest](request: GithubRequest): Future[Either[Option[T],HttpResponse]]
}
