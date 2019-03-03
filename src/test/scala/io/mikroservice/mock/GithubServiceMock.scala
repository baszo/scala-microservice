package io.mikroservice.mock

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import io.mikroservice.domain.service.SomeResult
import io.mikroservice.infrastructure.v3.GithubUser
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class GithubServiceMock(host: String, port: Int) extends Json4sSupport {

  private implicit val serialization: Serialization.type = Serialization
  private implicit val formats: DefaultFormats.type = DefaultFormats
  private implicit val system: ActorSystem = ActorSystem()
  private implicit val mat: ActorMaterializer = ActorMaterializer()

  val someResponse =
    """
      {
      	"login": "baszo",
      	"id": 2632307,
      	"node_id": "MDQ6VXNlcjI2MzIzMDc=",
      	"avatar_url": "https://avatars0.githubusercontent.com/u/2632307?v=4",
      	"gravatar_id": "",
      	"url": "https://api.github.com/users/baszo",
      	"html_url": "https://github.com/baszo",
      	"followers_url": "https://api.github.com/users/baszo/followers",
      	"following_url": "https://api.github.com/users/baszo/following{/other_user}",
      	"gists_url": "https://api.github.com/users/baszo/gists{/gist_id}",
      	"starred_url": "https://api.github.com/users/baszo/starred{/owner}{/repo}",
      	"subscriptions_url": "https://api.github.com/users/baszo/subscriptions",
      	"organizations_url": "https://api.github.com/users/baszo/orgs",
      	"repos_url": "https://api.github.com/users/baszo/repos",
      	"events_url": "https://api.github.com/users/baszo/events{/privacy}",
      	"received_events_url": "https://api.github.com/users/baszo/received_events",
      	"type": "User",
      	"site_admin": false,
      	"name": "Andrzej Plutecki",
      	"blog": "",
      	"public_repos": "6",
      	"public_gists": "0",
      	"followers": "0",
      	"following": "1",
      	"created_at": "2012-10-23T14:41:10Z",
      	"updated_at": "2018-04-15T12:00:02Z"
      }
    """.stripMargin

  private val routes =
    (path("user" / Segment) & get) {
      case _ => complete(OK, someResponse)
    }


  def start(): Unit = Await.result(Http().bindAndHandle(routes, host, port), 5 seconds)

}
