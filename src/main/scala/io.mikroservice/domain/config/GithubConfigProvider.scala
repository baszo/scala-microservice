package io.mikroservice.domain.config

import com.typesafe.config.Config

class GithubConfigProvider(config: Config) {
  private val productionUrl = config.getString("github.productionUrl")
  private val testUrl = config.getString("github.testUrl")
  private val port = if (!config.getIsNull("github.port")) Some(config.getInt("github.port")) else None
  private lazy val result = GithubConfig(productionUrl, port, testUrl)

  def getConfig: GithubConfig = result
}

case class GithubConfig(productionUrl: String, port: Option[Int], testUrl: String)
