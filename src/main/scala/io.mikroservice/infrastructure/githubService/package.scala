package io.mikroservice.infrastructure

import java.net.URLEncoder

package object v3 {

  final case class GetUserRepositories(
                                       user: String,
                                       `type`: Option[String],
                                       sort: Option[String],
                                       direction: Option[String]
                                     ) {
    var params = Map("type" -> `type`,"sort" -> sort,"direction"->direction)

    def getUrlPath: String = s"/users/$user/repos"
    def getQueryParams: Option[String] = Option(params.flatMap(pair =>  pair._2.map(p =>pair._1 + "=" + URLEncoder.encode(p, "UTF-8"))).mkString("&"))
  }

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
                                     )

}
