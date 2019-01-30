package io.mikroservice.api

/**
  * @author majaschaefer
  */

object Requests {

  final case class CreateRequest(name: String)

  final case class GetUserRepositories(
                                        user: String,
                                        `type`: Option[String],
                                        sort: Option[String] ,
                                        direction: Option[String]
                                      )

}
