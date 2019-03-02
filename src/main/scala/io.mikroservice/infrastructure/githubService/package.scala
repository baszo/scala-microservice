package io.mikroservice.infrastructure

import java.net.URLEncoder

package object v3 {

  trait GithubRequest {

    val params: Map[String, Option[String]] = Map.empty;

    def getUrlPath: String

    def getQueryParams = Option(params.flatMap(pair => pair._2.map(p => pair._1 + "=" + URLEncoder.encode(p, "UTF-8"))).mkString("&"))
  }

  final case class GetUserRepositories(
                                        user: String,
                                        `type`: Option[String],
                                        sort: Option[String],
                                        direction: Option[String]
                                      ) extends GithubRequest {
    override val params = Map("type" -> `type`, "sort" -> sort, "direction" -> direction)

    def getUrlPath: String = s"/users/$user/repos"
  }

  final case class GetSingleUser(
                                  username: String
                                ) extends GithubRequest {
    override def getUrlPath: String = s"/users/$username"
  }


  trait GithubResponse

  final case class GithubRepository(
                                     id: String,
                                     node_id: String,
                                     name: String,
                                     full_name: String,
                                     `private`: Boolean,
                                     html_url: String,
                                     description: String,
                                     fork: Boolean,
                                     url: String
                                   ) extends GithubResponse

  final case class GithubUser(
                               login: Option[String],
                               id: Int,
                               node_id: Option[String],
                               avatar_url: Option[String],
                               gravatar_id: Option[String],
                               url: Option[String],
                               html_url: Option[String],
                               followers_url: Option[String],
                               following_url: Option[String],
                               gists_url: Option[String],
                               starred_url: Option[String],
                               subscriptions_url: Option[String],
                               organizations_url: Option[String],
                               repos_url: Option[String],
                               events_url: Option[String],
                               received_events_url: Option[String],
                               `type`: Option[String],
                               site_admin: Option[Boolean],
                               name: Option[String],
                               company: Option[String],
                               blog: Option[String],
                               location: Option[String],
                               email: Option[String],
                               hireable: Option[Boolean],
                               bio: Option[String],
                               public_repos: Option[String],
                               public_gists: Option[String],
                               followers: Option[String],
                               following: Option[String],
                               created_at: Option[String],
                               updated_at: Option[String]) extends GithubResponse

}
